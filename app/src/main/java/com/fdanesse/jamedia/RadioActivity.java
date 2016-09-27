package com.fdanesse.jamedia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import java.util.ArrayList;

public class RadioActivity extends AppCompatActivity {

    private ArrayList<ListItem> lista;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        lista = new ArrayList<ListItem>();
        recyclerView = (RecyclerView) findViewById(R.id.reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        lista.add(new ListItem(R.drawable.jamedia, "AAA", "xxx"));
        lista.add(new ListItem(R.drawable.jamedia, "BBB", "xxx"));

        ItemListAdapter listAdapter = new ItemListAdapter(lista);
        recyclerView.setAdapter(listAdapter);
    }

    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(RadioActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //return super.onKeyDown(keycode, event);
        return true;
    }
}
