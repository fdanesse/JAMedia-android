package com.fdanesse.jamedia.Archivos;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.fdanesse.jamedia.PlayerActivity;
import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.io.File;
import java.util.ArrayList;


public class FileChooserActivity extends AppCompatActivity {

    private String currentpath;
    private ArrayList<String> tracks;

    private Toolbar myactionbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        myactionbar = (Toolbar) findViewById(R.id.file_chooser_toolbar);
        setSupportActionBar(myactionbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        currentpath = extras.getString("currentpath",
                Environment.getExternalStorageDirectory().getAbsolutePath()); //"/mnt/sdcard/Musica/"

        recyclerView = (RecyclerView) findViewById(R.id.file_chooser_reciclerview);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        tracks = new ArrayList<String>();

        load_path(currentpath);

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        this.setVolumeControlStream(audioManager.STREAM_MUSIC);

        Button boton = (Button) myactionbar.findViewById(R.id.anterior);
        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    File file = new File(currentpath);
                    if (file.getParentFile() != null) {
                        File parentdir = file.getParentFile();
                        load_path(parentdir.getPath());
                    }
                }
                return true;
            }
        });

        boton = (Button) myactionbar.findViewById(R.id.play);
        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ArrayList<ListItem> listItems = FileManager.get_ListItems(tracks);
                    Intent intent = new Intent(FileChooserActivity.this, PlayerActivity.class);
                    intent.putExtra("tracks", listItems);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    private void check_button_anterior(){
        File file = new File(currentpath);
        Button boton = (Button) myactionbar.findViewById(R.id.anterior);
        if (file.getParentFile() != null) {
            if (file.getParentFile().canRead() && ! boton.isEnabled()) {
                Utils.setActiveView(boton);
                boton.setEnabled(true);
            } else if (! file.getParentFile().canRead() && boton.isEnabled()) {
                Utils.setInactiveView(boton);
                boton.setEnabled(false);
            }
        }
        else {
            Utils.setInactiveView(boton);
            boton.setEnabled(false);
        }
    }

    private void check_button_play(){
        Button boton = (Button) myactionbar.findViewById(R.id.play);
        if (tracks.size() > 0 && ! boton.isEnabled()){
            Utils.setActiveView(boton);
            boton.setEnabled(true);
        }
        else if (tracks.size() < 1 && boton.isEnabled()){
            Utils.setInactiveView(boton);
            boton.setEnabled(false);
        }
    }

    public void check_track_in_selected(String filepath, View view){
        if (! tracks.contains(filepath)){
            Utils.setActiveView(view);
            tracks.add(filepath);
        }
        else if (tracks.contains(filepath)){
            Utils.setInactiveView(view);
            tracks.remove(filepath);
        }
        check_button_play();
        //FIXME: LA vista del recyclerview no es coherente
    }

    public void load_path(String dirpath) {
        currentpath = dirpath;
        tracks.clear();
        ArrayList<ItemFileChooser> lista = FileManager.readDirPath(currentpath);
        FileChooserItemListAdapter listAdapter = new FileChooserItemListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
        check_button_anterior();
        check_button_play();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(FileChooserActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}