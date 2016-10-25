package com.fdanesse.jamedia.JamediaPlayer;

import android.media.AudioManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

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

public class Main2Activity extends AppCompatActivity {

    private static int idcurrenttrack;
    private static Uri trackurl;
    private static ArrayList<ListItem> tracks;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = (Toolbar) findViewById(R.id.JAMediaToolbar);
        tabLayout = (TabLayout) findViewById(R.id.lenguetas);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        init();
        //connect_buttons_actions();
        //listen_videoView();

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        this.setVolumeControlStream(audioManager.STREAM_MUSIC);

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        BlankFragment f2 = new BlankFragment();
        fragments.add(f2);
        fragments.add(new Fragment());

        viewPager.setAdapter(new Notebook(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init(){
        Bundle extras = getIntent().getExtras();
        idcurrenttrack = extras.getInt("idcurrenttrack", 0);
        tracks = (ArrayList<ListItem>) extras.getSerializable("tracks");
        ListItem item = tracks.get(idcurrenttrack);
        trackurl = Uri.parse(item.getUrl());
        /*
        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(getApplicationContext());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.setVideoURI(trackurl);
        */
    }

    /*
    private void connect_buttons_actions(){
        View siguiente = (View) findViewById(R.id.siguiente);
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_track();
            }
        });

        View anterior = (View) findViewById(R.id.anterior);
        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous_track();
            }
        });
    }
    */

    /*
    private void check_buttons(){
        int x = tracks.size();
        View anterior = (View) findViewById(R.id.anterior);
        View siguiente = (View) findViewById(R.id.siguiente);
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

    public void next_track(){
        int x = tracks.size();
        if (idcurrenttrack < x-1){
            idcurrenttrack += 1;
        }
        else{
            idcurrenttrack = 0;
        }
        ListItem item = tracks.get(idcurrenttrack);
        trackurl = Uri.parse(item.getUrl());
        videoView.setVideoURI(trackurl);
    }

    public void previous_track(){
        int x = tracks.size();
        if (idcurrenttrack > 0){
            idcurrenttrack -= 1;
        }
        else{
            idcurrenttrack = x - 1;
        }
        ListItem item = tracks.get(idcurrenttrack);
        trackurl = Uri.parse(item.getUrl());
        videoView.setVideoURI(trackurl);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(PlayerActivity.this, ListActivity.class);
                    intent.putExtra("tracks", tracks);
                    intent.putExtra("idcurrenttrack", idcurrenttrack);
                    startActivity(intent);
                    finish();
                    return true;
                }
                break;
            }
        }
        return super.dispatchKeyEvent(event);
    }
    */
}
