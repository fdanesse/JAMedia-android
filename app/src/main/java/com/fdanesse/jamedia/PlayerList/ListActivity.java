package com.fdanesse.jamedia.PlayerList;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.fdanesse.jamedia.JamediaPlayer.JAMediaPlayer;
import com.fdanesse.jamedia.JamediaPlayer.PlayerActivity;
import com.fdanesse.jamedia.R;

import java.util.ArrayList;

/**
 * FIXME: Cuando se esta reproduciendo una pista y cambia a la siguiente, se debe ver reflejado aca
 */

public class ListActivity extends AppCompatActivity {

    private ArrayList<ListItem> lista;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        lista = (ArrayList<ListItem>) extras.getSerializable("tracks");
        int idcurrenttrack = extras.getInt("idcurrenttrack", -1);
        recyclerView = (RecyclerView) findViewById(R.id.reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        ItemListAdapter listAdapter = new ItemListAdapter(lista, this, idcurrenttrack);
        recyclerView.setAdapter(listAdapter);
    }

    protected void playtrack(int index){
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("tracks", lista);
        intent.putExtra("idcurrenttrack", index);
        this.startActivity(intent);
        this.finish();
    }

    /*
    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
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
            /*
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(ListActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
            */
            case KeyEvent.KEYCODE_VOLUME_UP: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.up_vol(this);
                }
                Snackbar.make(recyclerView, "Volumen: " + JAMediaPlayer.get_vol(this),
                        Snackbar.LENGTH_SHORT).show();
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.down_vol(this);
                }
                Snackbar.make(recyclerView, "Volumen: " + JAMediaPlayer.get_vol(this),
                        Snackbar.LENGTH_SHORT).show();
                return true;
            }
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}
