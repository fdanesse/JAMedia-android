package com.fdanesse.jamedia.JamediaPlayer;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.fdanesse.jamedia.PlayerList.ListActivity;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;

import java.util.ArrayList;


public class PlayerActivity extends AppCompatActivity {

    private static String trackName;
    private static String trackurl;
    private static ArrayList<ListItem> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Bundle extras = getIntent().getExtras();
        trackName = extras.getString("name", "");
        trackurl = extras.getString("url", "");
        tracks = (ArrayList<ListItem>) extras.getSerializable("tracks");

        Snackbar.make((View) findViewById(R.id.imagen),
                "Reproducir: " + trackName + "\n" + trackurl, Snackbar.LENGTH_INDEFINITE).show();

        JAMediaPlayer.play(this, trackurl);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(this, ListActivity.class);
                    intent.putExtra("tracks", tracks);
                    //FIXME: Agregar "current track"
                    startActivity(intent);
                    finish();
                }
                return true;
            }
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
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
