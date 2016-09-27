package com.fdanesse.jamedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class RadioActivity extends AppCompatActivity {

    private ArrayList<ListItem> lista = new ArrayList<ListItem>();
    private RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reciclerview);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        lista.add(new ListItem(R.drawable.jamedia, "AAA", "xxx"));
        lista.add(new ListItem(R.drawable.jamedia, "BBB", "xxx"));

        ItemListAdapter listAdapter = new ItemListAdapter(lista);
        recyclerView.setAdapter(listAdapter);

    }
}
