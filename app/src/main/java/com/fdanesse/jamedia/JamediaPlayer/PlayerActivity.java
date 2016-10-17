package com.fdanesse.jamedia.JamediaPlayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fdanesse.jamedia.PlayerList.ListActivity;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;


public class PlayerActivity extends AppCompatActivity {

    private Toolbar myactionbar;

    private static int idcurrenttrack;
    private static String trackurl;
    private static ArrayList<ListItem> tracks;

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        myactionbar = (Toolbar) findViewById(R.id.playertoolbar);
        setSupportActionBar(myactionbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();

        init();
        connect_buttons_actions();
        listen_videoView();
    }

    private void listen_videoView(){

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Snackbar.make(videoView, trackurl, Snackbar.LENGTH_INDEFINITE).show();
                check_buttons();
                videoView.seekTo(0);
                videoView.start();
                //FIXME: playing? (actualizar botones play y stop)
                /*
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaController.setAnchorView(videoView);
                    }
                });
                */
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                next_track();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                switch (i){
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:{
                        break;
                    }
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:{
                        break;
                    }
                }

                switch (i1){
                    case MediaPlayer.MEDIA_ERROR_IO:{
                        break;
                    }
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:{
                        break;
                    }
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:{
                        break;
                    }
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:{
                        break;
                    }
                }

                Log.i("**** ERROR: ", " " + i + " " + i1);
                videoView.stopPlayback();
                return true;
            }
        });
    }

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

    private void init(){
        Bundle extras = getIntent().getExtras();
        idcurrenttrack = extras.getInt("idcurrenttrack", 0);
        tracks = (ArrayList<ListItem>) extras.getSerializable("tracks");
        ListItem item = tracks.get(idcurrenttrack);
        trackurl = item.getUrl();
        videoView.setVideoPath(trackurl);
    }

    private void check_buttons(){
        int x = tracks.size();
        View anterior = (View) findViewById(R.id.anterior);
        View siguiente = (View) findViewById(R.id.siguiente);
        if (idcurrenttrack < x-1){
            //FIXME: hay mas pistas delante
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
            //FIXME: hay mas pistas detras
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
        trackurl = item.getUrl();
        videoView.setVideoPath(trackurl);
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
        trackurl = item.getUrl();
        videoView.setVideoPath(trackurl);
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
                }
                return true;
            }

            /*
            case KeyEvent.KEYCODE_VOLUME_UP: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.up_vol(this);
                }
                Snackbar.make((View) findViewById(R.id.imagen),
                        "Volumen: " + JAMediaPlayer.get_vol(this), Snackbar.LENGTH_SHORT).show();
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.down_vol(this);
                }
                Snackbar.make((View) findViewById(R.id.imagen),
                        "Volumen: " + JAMediaPlayer.get_vol(this), Snackbar.LENGTH_SHORT).show();
                return true;
            }
            */
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
