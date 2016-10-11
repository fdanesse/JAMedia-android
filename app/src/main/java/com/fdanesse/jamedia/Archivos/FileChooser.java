package com.fdanesse.jamedia.Archivos;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

import com.fdanesse.jamedia.R;

import java.util.ArrayList;


public class FileChooser extends AppCompatActivity {

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

    public void load_path(String dirpath) {
        lista.clear();
        currentpath = dirpath;
        lista = FileManager.readDirPath(currentpath);
        listAdapter = new FileChooserItemListAdapter(lista, this, this);
        recyclerView.setAdapter(listAdapter);
    }
}