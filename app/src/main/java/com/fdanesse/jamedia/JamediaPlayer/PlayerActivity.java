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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.view.ViewGroup.LayoutParams;
import android.widget.SeekBar;

import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.PlayerList.FragmentPlayerList;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;

/*
FIXME: AdMob
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
*/


public class PlayerActivity extends AppCompatActivity {

    private AppBarLayout appbar;
    private Toolbar toolbar;
    private ImageButton anterior;
    private ImageButton siguiente;
    private ImageButton play;
    private ImageButton creditos;  // FIXME: terminar
    private ImageButton cancel;

    private int img_pausa = R.drawable.pausa;
    private int img_play = R.drawable.play;

    private SeekBar seekBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FragmentVideoPlayer fragmentVideoPlayer;
    private FragmentPlayerList fragmentPlayerList;

    private JAMediaPLayerService jaMediaPLayerService;
    private boolean serviceBound = false;

    private Point displaysize = new Point();
    private boolean fullscreen = false;

    //http://www.androidhive.info/2012/03/android-building-audio-player-tutorial/
    private Handler mHandler = new Handler();

    private WifiManager.WifiLock wifiLock;
    private boolean network = false;

    //Publicidad:
    /*
    FIXME: AdMob
    private AdView mAdView;
    */

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

        creditos = (ImageButton) findViewById(R.id.creditos);
        anterior = (ImageButton) findViewById(R.id.anterior);
        play = (ImageButton) findViewById(R.id.play);
        siguiente = (ImageButton) findViewById(R.id.siguiente);
        cancel = (ImageButton) findViewById(R.id.cancel);

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

        //Si se carga la radio, se necesita la red activa siempre
        network = extras.getBoolean("network", false);
        if (network){
            seekBar.setVisibility(View.GONE);
            network_changed();
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkStateReceiver, filter);
            //FIXME: Arreglar para 3g
            wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
            wifiLock.acquire();}
        else{seekBar.setEnabled(false);}

        Intent intent = new Intent(getApplicationContext(), JAMediaPLayerService.class);
        getApplicationContext().startService(intent);
        bindService(intent, mConnection, getApplicationContext().BIND_AUTO_CREATE);

        try {
            // Registro de señales de JAMediaPlayerService
            IntentFilter filter = new IntentFilter(JAMediaPLayerService.END_TRACK);
            registerReceiver(end_track, filter);
            filter = new IntentFilter(JAMediaPLayerService.PLAY);
            registerReceiver(playing_track, filter);
            filter = new IntentFilter(JAMediaPLayerService.PAUSE);
            registerReceiver(paused_track, filter);
            filter = new IntentFilter(JAMediaPLayerService.STOP);
            registerReceiver(stoped_track, filter);
            /*
            filter = new IntentFilter(JAMediaPLayerService.BUFFER);
            registerReceiver(buffer_update, filter);
            filter = new IntentFilter(JAMediaPLayerService.ERROR);
            registerReceiver(error_player, filter);
            */
        }
        catch(Exception e){}

        //Señales de FragmentVideoPlayer
        IntentFilter filter = new IntentFilter(FragmentVideoPlayer.TOUCH);
        registerReceiver(touch_in_fragment_video, filter);
        filter = new IntentFilter(FragmentVideoPlayer.FULLSCREEN);
        registerReceiver(setfullscreen, filter);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                jaMediaPLayerService.set_pos(seekBar.getProgress());
                updateProgressBar();
            }
        });

        Display display = getWindowManager().getDefaultDisplay();
        display.getRealSize(displaysize);

        viewPager.setCurrentItem(0);
        viewPager.setEnabled(false);

        /*
        FIXME: AdMob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */
    }

    // NETWORK
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        /*
        http://www.mysamplecode.com/2013/04/android-automatically-detect-internet-connection.html
        http://stackoverflow.com/questions/38271365/connection-changed-broadcast-doesnt-work-when-mobile-data-is-enabled-in-marshma
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {
            isNetworkAvailable(context);
        }
        private boolean isNetworkAvailable(Context context) {
            network_changed();
            return true;
        }
    };

    private boolean network_check(){
        /**
         * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html?hl=es
         */
        ConnectivityManager cm = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null){
            //FIXME: Arreglar para 3g
            return (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

    private void network_changed(){
        boolean i = network_check();
        //radio.setEnabled(i);
        if (i == false){
            Snackbar.make(viewPager, "No tienes conexión a internet", Snackbar.LENGTH_INDEFINITE).show();
        }
        else{
            //Snackbar.make(viewPager, "Conectando a internet...", Snackbar.LENGTH_LONG).show();
        }
    }
    //NETWORK

    public void updateProgressBar() {
        if (!network) {mHandler.postDelayed(mUpdateTimeTask, 1000);}
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
        try{resize();}
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
        unregisterReceiver(paused_track);
        /*
        unregisterReceiver(buffer_update);
        unregisterReceiver(error_player);
        */
        unregisterReceiver(touch_in_fragment_video);
        unregisterReceiver(setfullscreen);

        try {
            unregisterReceiver(networkStateReceiver);
            wifiLock.release();}
        catch (Exception e){}

        try{mHandler.removeCallbacks(mUpdateTimeTask);}
        catch (Exception e) {}
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
        Intent broadcastIntent = new Intent(NEW_TRACK);
        broadcastIntent.putExtra("media", item.getUrl());
        sendBroadcast(broadcastIntent);
        }

    // Reproductor pide Cambio de pista
    private BroadcastReceiver end_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fragmentPlayerList.getListAdapter().next();
            if (!network){
                try{mHandler.removeCallbacks(mUpdateTimeTask);}
                catch (Exception e) {}
                seekBar.setEnabled(false);
            }
        }
    };

    // Reproductor está play
    private BroadcastReceiver playing_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            play.setImageResource(img_pausa);
            Utils.setActiveView(play, "default");
            play.setEnabled(true);
            check_buttons();
            resize();
            if (!network) {
                updateProgressBar();
                seekBar.setEnabled(true);
            }
        }
    };

    // Reproductor está stop
    private BroadcastReceiver stoped_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            play.setImageResource(img_play);
            if (!network){
                try{mHandler.removeCallbacks(mUpdateTimeTask);}
                catch (Exception e) {}
                seekBar.setEnabled(false);
            }
        }
    };

    // Reproductor está pausa
    private BroadcastReceiver paused_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            play.setImageResource(img_play);
            if (!network) {
                try {mHandler.removeCallbacks(mUpdateTimeTask);}
                catch (Exception e) {}
                seekBar.setEnabled(false);
            }
        }
    };

    // Cargando buffer desde un streaming
    private BroadcastReceiver buffer_update = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /* FIXME: Solo son comentarios
            Bundle extras = intent.getExtras();
            Snackbar.make(toolbar, "Cargando: " + extras.getInt("buffer", 0) + " %",
                    Snackbar.LENGTH_SHORT).show();
            */
            //FIXME: Implementar
        }
    };

    // Error en el reproductor
    private BroadcastReceiver error_player = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /* FIXME: Solo son comentarios
            Bundle extras = intent.getExtras();
            Snackbar.make(toolbar, "Error: " + extras.get("what") + " " + extras.get("extra"),
                    Snackbar.LENGTH_LONG).show();
            */
        }
    };

    private void resize(){
        if (hasWindowFocus()){
            if (jaMediaPLayerService.get_hasvideo()){

                if (fullscreen){
                    toolbar.setVisibility(View.GONE);
                    seekBar.setVisibility(View.GONE);
                    appbar.setVisibility(View.GONE);
                    /* FIXME: AdMob mAdView.setVisibility(View.GONE);*/
                    getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                else{
                    getWindow().clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    toolbar.setVisibility(View.VISIBLE);
                    if (!network){seekBar.setVisibility(View.VISIBLE);}
                    appbar.setVisibility(View.VISIBLE);
                    /* FIXME: AdMob mAdView.setVisibility(View.VISIBLE);*/
                }

                int width = LayoutParams.MATCH_PARENT;
                int height = LayoutParams.MATCH_PARENT;

                Point l = jaMediaPLayerService.getVideosize();
                float x = (float) l.x;
                float y = (float) l.y;

                SurfaceView v = (SurfaceView) findViewById(R.id.videoView);
                LayoutParams params = v.getLayoutParams();

                if (!fullscreen){
                    float factor = Math.min(displaysize.x / x,
                            (displaysize.y - appbar.getHeight()) / y);
                    width = (int) (x * factor);
                    height = (int) (y * factor);
                }

                params.width = width;
                params.height = height;
                v.setLayoutParams(params);

                jaMediaPLayerService.setDisplay(fragmentVideoPlayer.surfaceHolder);
                viewPager.setCurrentItem(1);
                viewPager.setEnabled(true);
            }
            else{
                jaMediaPLayerService.setDisplay(null);
                viewPager.setCurrentItem(0);
                viewPager.setEnabled(false);
                getWindow().clearFlags(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                toolbar.setVisibility(View.VISIBLE);
                if (!network){seekBar.setVisibility(View.VISIBLE);}
                appbar.setVisibility(View.VISIBLE);
                /* FIXME: AdMob mAdView.setVisibility(View.VISIBLE);*/
            }
        }
        else{
            jaMediaPLayerService.setDisplay(null);
        }
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void check_buttons() {
        if (fragmentPlayerList.getListAdapter().getItemCount() > 1){
            Utils.setActiveView(siguiente, "default");
            siguiente.setEnabled(true);
            Utils.setActiveView(anterior, "default");
            anterior.setEnabled(true);
        }
        else{
            Utils.setInactiveView(siguiente, "default");
            siguiente.setEnabled(false);
            Utils.setInactiveView(anterior, "default");
            anterior.setEnabled(false);
        }
    }

    // Tocar zona de reproduccion de video
    private BroadcastReceiver touch_in_fragment_video = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            jaMediaPLayerService.pause_play();
        }
    };

    private BroadcastReceiver setfullscreen = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (fullscreen){
                fullscreen = false;
                resize();}
            else{
                fullscreen = true;
                resize();}
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
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