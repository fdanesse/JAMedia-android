package com.fdanesse.jamedia.JamediaPlayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup.LayoutParams;
import android.widget.SeekBar;

import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.PlayerList.FragmentPlayerList;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;


public class PlayerActivity extends FragmentActivity{

    private AppBarLayout appbar;
    private Toolbar toolbar;
    private Button anterior;
    private Button siguiente;
    private Button play;
    private Button creditos;  // FIXME: terminar

    private int img_pausa = R.drawable.pausa;
    private int img_play = R.drawable.play;
    private int img_stop = R.drawable.stop;

    private SeekBar seekBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FragmentVideoPlayer fragmentVideoPlayer;
    private FragmentPlayerList fragmentPlayerList;

    private JAMediaPLayerService jaMediaPLayerService;
    private boolean serviceBound = false;

    private Point displaysize = new Point();

    //http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    private Handler mHandler = new Handler();

    // SEÑALES
    public static final String NEW_TRACK = "NEW_TRACK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.player_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.lenguetas);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        anterior = (Button) findViewById(R.id.anterior);
        siguiente = (Button) findViewById(R.id.siguiente);
        play = (Button) findViewById(R.id.play);
        creditos = (Button) findViewById(R.id.creditos);

        img_pausa = R.drawable.pausa;
        img_play = R.drawable.play;

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        this.setVolumeControlStream(audioManager.STREAM_MUSIC);

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragmentVideoPlayer = new FragmentVideoPlayer();
        fragmentPlayerList = new FragmentPlayerList();
        fragmentPlayerList.set_parent(this);
        fragments.add(fragmentPlayerList);
        fragments.add(fragmentVideoPlayer);

        viewPager.setAdapter(new Notebook(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        connect_buttons_actions();

        Bundle extras = getIntent().getExtras();
        fragmentPlayerList.setArguments(extras);

        Intent intent = new Intent(getApplicationContext(), JAMediaPLayerService.class);
        getApplicationContext().startService(intent);
        bindService(intent, mConnection, getApplicationContext().BIND_AUTO_CREATE);

        try {
            // Registro de señales del server
            IntentFilter filter = new IntentFilter(JAMediaPLayerService.END_TRACK);
            registerReceiver(end_track, filter);
            filter = new IntentFilter(JAMediaPLayerService.PLAY);
            registerReceiver(playing_track, filter);
            filter = new IntentFilter(JAMediaPLayerService.STOP);
            registerReceiver(stoped_track, filter);
        }
        catch(Exception e){}

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                jaMediaPLayerService.set_pos(seekBar.getProgress());
                updateProgressBar();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        display.getRealSize(displaysize);
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            Point point = jaMediaPLayerService.getDuration_Position();
            seekBar.setMax(point.x);
            seekBar.setProgress(point.y);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        try{
            if (hasFocus) {
                jaMediaPLayerService.setDisplay(fragmentVideoPlayer.surfaceHolder);
                resize();
            }
            else {
                jaMediaPLayerService.setDisplay(null);
            }
        }
        catch(Exception e){}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            try {
                unbindService(mConnection);
            }
            catch(Exception e){}
            try {
                jaMediaPLayerService.stopSelf();
            }
            catch (Exception e){}
        }
        unregisterReceiver(end_track);
        unregisterReceiver(stoped_track);
        unregisterReceiver(playing_track);
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /* Cuando cambia la orientación de la pantalla */

        SurfaceView v = (SurfaceView) findViewById(R.id.videoView);
        LayoutParams params = v.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = LayoutParams.WRAP_CONTENT;
            params.height = LayoutParams.MATCH_PARENT;
            v.setLayoutParams(params);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.WRAP_CONTENT;
            v.setLayoutParams(params);
        }
        resize();
    }

    //> ********** Conexion y Desconexion del servidor **********
    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            JAMediaPLayerService.LocalBinder binder = (JAMediaPLayerService.LocalBinder) service;
            jaMediaPLayerService = binder.getService();
            serviceBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Snackbar.make(viewPager, "JAMediaPlayerService OFF", Snackbar.LENGTH_LONG).show();
            serviceBound = false;
        }
    };
    //< ********** Conexion y Desconexion del servidor **********

    public void playtrack(int index){
        ListItem item = fragmentPlayerList.getListAdapter().getLista().get(index);
        viewPager.setCurrentItem(1);
        Intent broadcastIntent = new Intent(NEW_TRACK);
        broadcastIntent.putExtra("media", item.getUrl());
        sendBroadcast(broadcastIntent);
        }

    // Reproductor pide Cambio de pista
    private BroadcastReceiver end_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mHandler.removeCallbacks(mUpdateTimeTask);
            fragmentPlayerList.getListAdapter().next();
        }
    };

    // Reproductor está play
    private BroadcastReceiver playing_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            resize();
            play.setBackgroundResource(img_pausa);
            Utils.setActiveView(play);
            play.setEnabled(true);
            check_buttons();
            jaMediaPLayerService.setDisplay(fragmentVideoPlayer.surfaceHolder);
            updateProgressBar();
        }
    };

    // Reproductor está stop
    private BroadcastReceiver stoped_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            play.setBackgroundResource(img_play);
            //FIXME: Detener handler?
        }
    };

    private void resize(){
        // VIDEO
        Point l = jaMediaPLayerService.getVideosize();
        float x = (float) l.x;
        float y = (float) l.y;

        // FACTOR de ESCALA
        float factor = Math.min(displaysize.x / x, (displaysize.y - appbar.getHeight()) / y);
        int width = (int) (x * factor);
        int height = (int) (y * factor);

        // VIDEOVIEW
        SurfaceView v = (SurfaceView) findViewById(R.id.videoView);
        LayoutParams params = v.getLayoutParams();

        // ESCALAR
        params.width = width;
        params.height = height;
        v.setLayoutParams(params);
    }

    private void connect_buttons_actions(){
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentPlayerList.getListAdapter().next();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jaMediaPLayerService.pause_play();
            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentPlayerList.getListAdapter().previous();
            }
        });
    }

    private void check_buttons() {
        if (fragmentPlayerList.getListAdapter().getItemCount() > 1){
            Utils.setActiveView(siguiente);
            siguiente.setEnabled(true);
            Utils.setActiveView(anterior);
            anterior.setEnabled(true);
        }
        else{
            Utils.setInactiveView(siguiente);
            siguiente.setEnabled(false);
            Utils.setInactiveView(anterior);
            anterior.setEnabled(false);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    if (serviceBound){
                        try{
                            jaMediaPLayerService.stopSelf();
                        }
                        catch(Exception e){}
                    }
                    Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}