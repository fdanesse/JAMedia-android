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
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        lista = new ArrayList<ListItem>();
        recyclerView = (RecyclerView) findViewById(R.id.reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)",
                "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)",
                "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)",
                "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)",
                "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)",
                "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)",
                "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)",
                "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)",
                "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)",
                "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)",
                "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)",
                "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)",
                "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)",
                "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "z)",
                "http://bulofm.com:19000/"));

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
