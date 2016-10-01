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

        llenar_lista();

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

    private void llenar_lista(){
        lista.add(new ListItem(R.drawable.jamedia, "Oceano FM (Montevideo - Uruguay", "http://radio4.oceanofm.com:8010/"));
        lista.add(new ListItem(R.drawable.jamedia, "ABCLounge", "http://streaming208.radionomy.com:80/ABC-Lounge"));
        lista.add(new ListItem(R.drawable.jamedia, "ADANA RADYO YILDIZ", "http://sunucu2.radyolarburada.com:2006"));
        lista.add(new ListItem(R.drawable.jamedia, "AMLO (Venezuela)", "http://stream.radioamlo.info:8010"));
        lista.add(new ListItem(R.drawable.jamedia, "ANTENA ZAGREB", "http://173.192.137.34:8050/"));
        lista.add(new ListItem(R.drawable.jamedia, "CW 41 (San José - Uruguay)", "http://server-uk1.radioseninternetuy.com:8148"));
    }
}
