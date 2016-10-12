package com.fdanesse.jamedia.Archivos;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.fdanesse.jamedia.JamediaPlayer.JAMediaPlayer;
import com.fdanesse.jamedia.R;

import java.util.ArrayList;


public class FileChooserActivity extends AppCompatActivity {

    private String currentpath;
    private ArrayList<ItemFileChooser> lista;
    private FileChooserItemListAdapter listAdapter;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        Toolbar myactionbar = (Toolbar) findViewById(R.id.file_chooser_toolbar);
        setSupportActionBar(myactionbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        currentpath = extras.getString("currentpath", "/mnt/sdcard/Musica/");

        recyclerView = (RecyclerView) findViewById(R.id.file_chooser_reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        lista = FileManager.readDirPath(currentpath);

        listAdapter = new FileChooserItemListAdapter(lista, this, this);
        recyclerView.setAdapter(listAdapter);
    }

    public void add_track_in_selected(String filename, String filepath){
        Snackbar.make((View) findViewById(R.id.filename),
                "add: " + filename, Snackbar.LENGTH_LONG).show();
    }

    public void remove_track_in_selected(String filename, String filepath){
        Snackbar.make((View) findViewById(R.id.filename),
                "remove: " + filename, Snackbar.LENGTH_LONG).show();
    }

    public void load_path(String dirpath) {
        lista.clear();
        currentpath = dirpath;
        lista = FileManager.readDirPath(currentpath);
        listAdapter = new FileChooserItemListAdapter(lista, this, this);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            /*
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    //Intent intent = new Intent(this, ListActivity.class);
                    //intent.putExtra("tracks", tracks);
                    //startActivity(intent);
                    //finish();
                }
                return true;
            }*/
            case KeyEvent.KEYCODE_VOLUME_UP: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.up_vol(this);
                }
                Snackbar.make((View) findViewById(R.id.filename),
                        "Volumen: " + JAMediaPlayer.get_vol(this), Snackbar.LENGTH_SHORT).show();
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.down_vol(this);
                }
                Snackbar.make((View) findViewById(R.id.filename),
                        "Volumen: " + JAMediaPlayer.get_vol(this), Snackbar.LENGTH_SHORT).show();
                return true;
            }
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}