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
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);

        Toolbar myactionbar = (Toolbar) findViewById(R.id.file_chooser_toolbar);
        setSupportActionBar(myactionbar);

        Bundle extras = getIntent().getExtras();
        currentpath = extras.getString("currentpath", "/mnt/sdcard/Musica/");

        lista = new ArrayList<ItemFileChooser>();
        recyclerView = (RecyclerView) findViewById(R.id.file_chooser_reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        lista.add(new ItemFileChooser(R.drawable.video, "AAA", "xxx"));
        lista.add(new ItemFileChooser(R.drawable.audio, "BBB", "xxx"));

        FileChooserItemListAdapter listAdapter = new FileChooserItemListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
    }
}
