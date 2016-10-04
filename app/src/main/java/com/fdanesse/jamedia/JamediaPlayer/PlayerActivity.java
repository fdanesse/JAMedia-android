package com.fdanesse.jamedia.JamediaPlayer;

import android.content.Intent;
import android.media.AudioManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.fdanesse.jamedia.PlayerList.ListActivity;
import com.fdanesse.jamedia.R;


public class PlayerActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private int maxvol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Bundle extras = getIntent().getExtras();
        Snackbar.make((View) findViewById(R.id.imagen),
                "Reproducir: " + extras.getString("name", "") + extras.getString("url", ""),
                Snackbar.LENGTH_SHORT).show();

        audioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        maxvol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        JAMediaPlayer.play(extras.getString("url", ""));
        //JAMediaPlayer.setVolumen(0.5f, 0.5f);
    }

    /*
    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
            finish();
        }
        //return super.onKeyDown(keycode, event);
        return true;
    }
    */

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        //Log.i("*****", " " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(this, ListActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_UP: {
                if (action == KeyEvent.ACTION_DOWN) {
                    vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1;
                    if (vol > 0 && vol < maxvol) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol,
                                AudioManager.FLAG_PLAY_SOUND);
                    }
                }
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                if (action == KeyEvent.ACTION_DOWN) {
                    vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1;
                    if (vol > 0 && vol < maxvol) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol,
                                AudioManager.FLAG_PLAY_SOUND);
                    }
                }
                return true;
            }
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
