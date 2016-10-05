package com.fdanesse.jamedia.JamediaPlayer;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.fdanesse.jamedia.PlayerList.ListActivity;
import com.fdanesse.jamedia.R;


public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Bundle extras = getIntent().getExtras();
        Snackbar.make((View) findViewById(R.id.imagen),
                "Reproducir: " + extras.getString("name", "") + extras.getString("url", ""),
                Snackbar.LENGTH_SHORT).show();

        JAMediaPlayer.play(this, extras.getString("url", ""));
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
                    JAMediaPlayer.up_vol();
                }
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.down_vol();
                }
                return true;
            }
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
