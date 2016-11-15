package com.fdanesse.jamedia;

import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.fdanesse.jamedia.JamediaPlayer.FragmentVideoPlayer;
import com.fdanesse.jamedia.JamediaPlayer.Notebook;
import com.fdanesse.jamedia.PlayerList.FragmentPlayerList;
import com.fdanesse.jamedia.PlayerList.ItemListAdapter;
import com.fdanesse.jamedia.PlayerList.ListItem;

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

    private static int idcurrenttrack = -1;
    private static ArrayList<ListItem> tracks;

    private Toolbar toolbar;
    private static Button anterior;
    private static Button siguiente;
    private static Button play;
    private static Button creditos;

    private static int img_pausa = R.drawable.pausa;
    private static int img_play = R.drawable.play;
    private static int img_stop = R.drawable.stop;

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
        fragments.add(fragmentPlayerList);
        fragments.add(fragmentVideoPlayer);

        viewPager.setAdapter(new Notebook(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        init();
        connect_buttons_actions();
    }

    private void init(){
        Bundle extras = getIntent().getExtras();
        tracks = (ArrayList<ListItem>) extras.getSerializable("tracks");
        fragmentPlayerList.setArguments(extras);
    }

    public static void playtrack(int index){
        /**
         * Cuando se se clickea un item en la lista.
         */

        idcurrenttrack = index;
        ListItem item = tracks.get(idcurrenttrack);
        Uri url = Uri.parse(item.getUrl());

        viewPager.setCurrentItem(1);
        FragmentVideoPlayer.load_and_play(url);
        Utils.setActiveView(play);
        play.setEnabled(true);

        ArrayList<ItemListAdapter.ItemListViewHolder> items = FragmentPlayerList.listAdapter.getHolders();
        for (ItemListAdapter.ItemListViewHolder i : items) {
            Uri turl = Uri.parse(i.getText_view_url().getText().toString());
            if (turl.compareTo(url) == 0){
                Utils.setActiveView(i.itemView);
            }
            else{
                Utils.setInactiveView(i.itemView);
            }
            }
        }

    public static void set_status(Boolean playing, Boolean canpause){
        check_buttons();
        if (playing){
            if (canpause){
                play.setBackgroundResource(img_pausa);}
            else{
                play.setBackgroundResource(img_stop);}
            }
        else{
            play.setBackgroundResource(img_play);
        }
    }

    private void connect_buttons_actions(){
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next_track();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentVideoPlayer.pause_play();
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
        if (idcurrenttrack < x - 1){
            idcurrenttrack += 1;
        }
        else{
            idcurrenttrack = 0;
        }
        playtrack((int)idcurrenttrack);
    }

    public void previous_track(){
        int x = tracks.size();
        if (idcurrenttrack > 0){
            idcurrenttrack -= 1;
        }
        else{
            idcurrenttrack = x - 1;
        }
        playtrack((int)idcurrenttrack);
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
