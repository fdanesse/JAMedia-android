package com.fdanesse.jamedia.Archivos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
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

    private String currentpath;                         // Directorio actual
    private ArrayList<String> tracks;                   // Archivos reproducibles seleccionados
    private ArrayList<ItemFileChooser> lista;           // Archivos y Directorios en directorio actual
    private Toolbar myactionbar;
    private RecyclerView recyclerView;
    private FileChooserItemListAdapter listAdapter;

    private SharedPreferences.Editor conf;              // Guardamos el último directorio leido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        myactionbar = (Toolbar) findViewById(R.id.file_chooser_toolbar);
        setSupportActionBar(myactionbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = (RecyclerView) findViewById(R.id.file_chooser_reciclerview);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        tracks = new ArrayList<String>();
        lista = new ArrayList<ItemFileChooser>();

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        this.setVolumeControlStream(audioManager.STREAM_MUSIC);

        SharedPreferences pref = getSharedPreferences("config", MODE_PRIVATE);
        conf = pref.edit();
        currentpath = pref.getString("currentpath",
                Environment.getExternalStorageDirectory().getAbsolutePath());

        buttons_listen_clicks();
        load_path(currentpath);
    }

    private void buttons_listen_clicks(){

        // Leer Directorio padre al actual
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

        // Reproducir los archivos seleccionados
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

        // Seleccionar todos los archivos reproducibles
        boton = (Button) myactionbar.findViewById(R.id.todos);
        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    tracks.clear();

                    for (ItemFileChooser fc : lista){
                        if (fc.getTipo() == "Archivo"){
                            tracks.add(fc.getFilepath());
                        }
                    }

                    ArrayList<FileChooserItemListAdapter.ItemListViewHolder> items = listAdapter.getHolders();
                    for (FileChooserItemListAdapter.ItemListViewHolder i : items){
                        if (tracks.contains(i.get_filepath())){
                            Utils.setActiveView(i.itemView);
                        }
                    }

                    check_button_play();
                    check_button_todos();
                }
                return true;
            }
        });
    }

    private void check_button_anterior(){
        /**
         * Si se puede leer el nivel superior en el filesystem, se activa el boton "atras"
         */
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
        /**
         * Si hay al menos un archivo seleccionado, se activa el boton play
         */
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

    private void check_button_todos() {
        /**
         * Si al menos un archivo no está seleccionado,se activa el boton todos
         */
        Boolean f = false;

        if (!lista.isEmpty()){
            for (ItemFileChooser ifc : lista) {
                if (ifc.getTipo() == "Archivo" && !tracks.contains(ifc.getFilepath())) {
                    f = true;
                    break;
                }
            }
        }

        Button boton = (Button) myactionbar.findViewById(R.id.todos);
        if (f){
            Utils.setActiveView(boton);
            boton.setEnabled(true);
        }else{
            Utils.setInactiveView(boton);
            boton.setEnabled(false);
        }
    }

    public void onClickFileItem(String filepath, View view){
        /**
         * Cuando se hace click en un item de la lista
         */
        if (! tracks.contains(filepath)){
            Utils.setActiveView(view);
            tracks.add(filepath);
        }
        else if (tracks.contains(filepath)){
            Utils.setInactiveView(view);
            tracks.remove(filepath);
        }
        check_button_play();
        check_button_todos();
    }

    public Boolean check_path(String path){
        return tracks.contains(path);
    }

    public void load_path(String dirpath) {
        currentpath = dirpath;
        conf.putString("currentpath", currentpath);
        conf.commit();

        tracks.clear();
        lista = FileManager.readDirPath(currentpath);
        listAdapter = new FileChooserItemListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);

        check_button_anterior();
        check_button_play();
        check_button_todos();
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