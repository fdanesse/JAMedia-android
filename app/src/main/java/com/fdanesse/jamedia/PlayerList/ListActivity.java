package com.fdanesse.jamedia.PlayerList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.R;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {

    private ArrayList<ListItem> lista;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        //FIXME: Si es llamada desde el reproductor, se debe mostrar activada la pista activa.
        lista = (ArrayList<ListItem>) extras.getSerializable("tracks");
        recyclerView = (RecyclerView) findViewById(R.id.reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        ItemListAdapter listAdapter = new ItemListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
    }

    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //return super.onKeyDown(keycode, event);
        return true;
    }
}
