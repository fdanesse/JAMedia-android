package com.fdanesse.jamedia.Archivos;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.fdanesse.jamedia.JamediaPlayer.JAMediaPlayer;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.io.File;
import java.util.ArrayList;


public class FileChooserActivity extends AppCompatActivity {

    private String currentpath;
    private ArrayList<String> tracks;

    private ArrayList<ItemFileChooser> lista;
    private FileChooserItemListAdapter listAdapter;

    private Toolbar myactionbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        myactionbar = (Toolbar) findViewById(R.id.file_chooser_toolbar);
        setSupportActionBar(myactionbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle extras = getIntent().getExtras();
        currentpath = extras.getString("currentpath", "/mnt/sdcard/Musica/");

        recyclerView = (RecyclerView) findViewById(R.id.file_chooser_reciclerview);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        tracks = new ArrayList<String>();
        lista = new ArrayList<ItemFileChooser>();

        load_path(currentpath);

        Button boton = (Button) myactionbar.findViewById(R.id.anterior);
        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    File file = new File(currentpath);
                    if (file.getParentFile() != null) {
                        File parentdir = file.getParentFile();
                        load_path(parentdir.getPath());
                        Snackbar.make(view, parentdir.getPath(), Snackbar.LENGTH_INDEFINITE).show();
                    }
                }
                return true;
            }
        });

        boton = (Button) myactionbar.findViewById(R.id.play);
        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //FIXME: Llamar al reproductor con la lista de path
                /*
                button_clicked(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    ArrayList<ListItem> radios = FileManager.get_radios();
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putExtra("tracks", radios);
                    startActivity(intent);
                    finish();
                }
                 */
                return true;
            }
        });
    }

    private void check_button_anterior(){
        File file = new File(currentpath);
        Button boton = (Button) myactionbar.findViewById(R.id.anterior);
        if (file.getParentFile() != null) {
            if (file.getParentFile().canRead()) {
                Utils.setActiveView(boton);
                boton.setEnabled(true);
            } else {
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
        if (tracks.size() > 0 && !boton.isEnabled()){
            Utils.setActiveView(boton);
            boton.setEnabled(true);
        }
        else if (tracks.size() < 1 && boton.isEnabled()){
            Utils.setInactiveView(boton);
            boton.setEnabled(false);
        }
    }

    public void add_track_in_selected(String filepath){
        if (!tracks.contains(filepath)){
            tracks.add(filepath);
            check_button_play();
        }
        //Snackbar.make((View) findViewById(R.id.filename),
        //        "add: " + tracks.size(), Snackbar.LENGTH_LONG).show();
    }

    public void remove_track_in_selected(String filepath){
        if (tracks.contains(filepath)){
            tracks.remove(filepath);
            check_button_play();
        }
        //Snackbar.make((View) findViewById(R.id.filename),
        //        "remove: " + tracks.size(), Snackbar.LENGTH_LONG).show();
    }

    public void load_path(String dirpath) {
        currentpath = dirpath;
        tracks.clear();
        lista.clear();
        lista = FileManager.readDirPath(currentpath);
        listAdapter = new FileChooserItemListAdapter(lista, this, this);
        recyclerView.setAdapter(listAdapter);
        check_button_anterior();
        check_button_play();
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