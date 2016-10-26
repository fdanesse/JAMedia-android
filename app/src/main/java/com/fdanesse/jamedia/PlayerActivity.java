package com.fdanesse.jamedia;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.fdanesse.jamedia.JamediaPlayer.FragmentVideoPlayer;
import com.fdanesse.jamedia.JamediaPlayer.Notebook;
import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.PlayerList.FragmentPlayerList;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;

import java.util.ArrayList;

// Formatos: https://developer.android.com/guide/appendix/media-formats.html
// Custom MediaController: http://stackoverflow.com/questions/12482203/how-to-create-custom-ui-for-android-mediacontroller/14323144#14323144

/**
 * MediaPLayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
 * WifiLock wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
 * wifiLock.acquire();
 * wifiLock.release();
 */

public class PlayerActivity extends FragmentActivity {

    private static int idcurrenttrack = 0;
    private static ArrayList<ListItem> tracks;

    private Toolbar toolbar;
    private static View anterior;
    private static View siguiente;
    private static View play;
    private static View list;

    private TabLayout tabLayout;
    private static ViewPager viewPager;

    private FragmentVideoPlayer fragmentVideoPlayer;
    private FragmentPlayerList fragmentPlayerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        toolbar = (Toolbar) findViewById(R.id.JAMediaToolbar);
        tabLayout = (TabLayout) findViewById(R.id.lenguetas);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        anterior = (View) findViewById(R.id.anterior);
        siguiente = (View) findViewById(R.id.siguiente);
        play = (View) findViewById(R.id.play);
        list = (View) findViewById(R.id.list);

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        this.setVolumeControlStream(audioManager.STREAM_MUSIC);

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragmentVideoPlayer = new FragmentVideoPlayer();
        fragmentPlayerList = new FragmentPlayerList();
        fragments.add(fragmentPlayerList);
        fragments.add(fragmentVideoPlayer);

        viewPager.setAdapter(new Notebook(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Lista");
        tabLayout.getTabAt(1).setText("Player");

        init();
        connect_buttons_actions();
    }

    private void init(){
        Bundle extras = getIntent().getExtras();
        tracks = (ArrayList<ListItem>) extras.getSerializable("tracks");
        fragmentPlayerList.setArguments(extras);
    }

    public static void playtrack(int index){
        idcurrenttrack = index;
        ListItem item = tracks.get(index);
        viewPager.setCurrentItem(1);
        FragmentVideoPlayer.load_and_play(Uri.parse(item.getUrl()));
    }

    public static void stop(){
        FragmentVideoPlayer.stop();
    }

    public static void set_status(Boolean playing){
        //FIXME: agregar actualizacion segun playing
        check_buttons();
    }

    private void connect_buttons_actions(){
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_track();
            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous_track();
            }
        });
    }

    private static void check_buttons(){
        int x = tracks.size();
        if (idcurrenttrack < x-1){
            if (!siguiente.isEnabled()){
                Utils.setActiveView(siguiente);
                siguiente.setEnabled(true);
            }
        }
        else{
            if (siguiente.isEnabled()){
                Utils.setInactiveView(siguiente);
                siguiente.setEnabled(false);
            }
        }

        if (idcurrenttrack > 0){
            if (!anterior.isEnabled()){
                Utils.setActiveView(anterior);
                anterior.setEnabled(true);
            }
        }
        else{
            if (anterior.isEnabled()){
                Utils.setInactiveView(anterior);
                anterior.setEnabled(false);
            }
        }
    }

    public static void next_track(){
        int x = tracks.size();
        if (idcurrenttrack < x-1){
            idcurrenttrack += 1;
        }
        else{
            idcurrenttrack = 0;
        }
        playtrack(idcurrenttrack);
    }

    public void previous_track(){
        int x = tracks.size();
        if (idcurrenttrack > 0){
            idcurrenttrack -= 1;
        }
        else{
            idcurrenttrack = x - 1;
        }
        playtrack(idcurrenttrack);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                    startActivity(intent);
                    //finish();
                    return true;
                }
                break;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
