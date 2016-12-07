package com.fdanesse.jamedia.JamediaPlayer;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.VideoView;

import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.PlayerList.FragmentPlayerList;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;


public class PlayerActivity extends FragmentActivity {

    private Toolbar toolbar;
    private static Button anterior;
    private static Button siguiente;
    private static Button play;
    private static Button creditos;  // FIXME: terminar

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

        toolbar = (Toolbar) findViewById(R.id.player_toolbar);
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
        fragmentVideoPlayer.set_parent(this);
        fragmentPlayerList.set_parent(this);
        fragments.add(fragmentPlayerList);
        fragments.add(fragmentVideoPlayer);

        viewPager.setAdapter(new Notebook(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        connect_buttons_actions();

        /*sigue:
            onStart()
        */

        Bundle extras = getIntent().getExtras();
        fragmentPlayerList.setArguments(extras);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        VideoView v = (VideoView) findViewById(R.id.videoView);
        LayoutParams params = v.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.width = LayoutParams.WRAP_CONTENT;
            params.height = LayoutParams.MATCH_PARENT;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.WRAP_CONTENT;
        }

        v.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*persistir
        sigue:
            onResume()
            o
            onStop()
         */
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*no siempre ocurre
        sigue:
            onRestart()
            o
            onDestroy()
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*sigue:
            onResume()
            o
            onStop()
        */
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*sigue:
            onStart()
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*sigue:
            onPause()
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //no siempre ocurre
    }

    public void playtrack(int index){
        // Cuando se clickea un item en la lista.
        ListItem item = fragmentPlayerList.getListAdapter().getLista().get(index);
        Uri url = Uri.parse(item.getUrl());

        viewPager.setCurrentItem(1);
        fragmentVideoPlayer.load_and_play(url);
        Utils.setActiveView(play);
        play.setEnabled(true);
        }

    public void next_track(){
        fragmentPlayerList.getListAdapter().next();
    }

    public void set_status(Boolean playing, Boolean canpause){
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
                fragmentPlayerList.getListAdapter().next();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentVideoPlayer.pause_play();
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
        int x = fragmentPlayerList.getListAdapter().getItemCount();
        int trackselected = fragmentPlayerList.getListAdapter().getTrackselected();

        if (trackselected < x-1){
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

        if (trackselected > 0){
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
