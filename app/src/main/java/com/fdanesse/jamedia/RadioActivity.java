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
        lista.add(new ListItem(R.drawable.jamedia, "89.3 KPCC - Southern California Public Radio (EEUU)", "http://kpcclive1.publicradio.org:80/"));
        lista.add(new ListItem(R.drawable.jamedia, "A90's", "http://streaming204.radionomy.com:80/A90-s"));
        lista.add(new ListItem(R.drawable.jamedia, "ABCLounge", "http://streaming208.radionomy.com:80/ABC-Lounge"));
        lista.add(new ListItem(R.drawable.jamedia, "ACZEN'HITS", "http://streaming208.radionomy.com:80/AC-ZEN-HITS"));
        lista.add(new ListItem(R.drawable.jamedia, "ADANA FM", "http://yayin1.yayindakiler.com:3182/"));
        lista.add(new ListItem(R.drawable.jamedia, "ADANA RADYO YILDIZ", "http://sunucu2.radyolarburada.com:2006"));
        lista.add(new ListItem(R.drawable.jamedia, "AMLO (Venezuela)", "http://stream.radioamlo.info:8010"));
        lista.add(new ListItem(R.drawable.jamedia, "ANTENA 5", "http://217.115.140.23:8000/antenna5.mp3"));
        lista.add(new ListItem(R.drawable.jamedia, "ANTENA ZAGREB", "http://173.192.137.34:8050/"));
        lista.add(new ListItem(R.drawable.jamedia, "Ado FM", "http://broadcast.infomaniak.net/start-adofm-low.mp3"));
        lista.add(new ListItem(R.drawable.jamedia, "AirCDHitsRadio", "http://streaming208.radionomy.com:80/AirCD-Hits-Radio"));
        lista.add(new ListItem(R.drawable.jamedia, "aliens-radio", "http://streaming203.radionomy.com:80/aliens-radio"));
        lista.add(new ListItem(R.drawable.jamedia, "AlloLaRadio", "http://streaming203.radionomy.com:80/Allo-La-Radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Alphatune974", "http://streaming203.radionomy.com:80/Alpha-tune-974"));
        lista.add(new ListItem(R.drawable.jamedia, "Amanecer 91.9 FM (Colonia - Uruguay)", "http://67.222.25.168:7028/"));
        lista.add(new ListItem(R.drawable.jamedia, "AnUrbanRadio", "http://streaming203.radionomy.com:80/An-Urban-Radio"));
        lista.add(new ListItem(R.drawable.jamedia, "Antiguos pero buenos", "http://streaming203.radionomy.com:80/Antiguos--pero-buenos"));
        lista.add(new ListItem(R.drawable.jamedia, "ArabMusicRadio", "http://streaming208.radionomy.com:80/ArabMusicRadio"));
        lista.add(new ListItem(R.drawable.jamedia, "AŞK FM", "http://yayin.asymedya.com:8020/"));
        lista.add(new ListItem(R.drawable.jamedia, "aufilduson80s", "http://streaming203.radionomy.com:80/aufilduson80s"));
        lista.add(new ListItem(R.drawable.jamedia, "BEATLES", "http://streaming206.radionomy.com:80/100-BEATLES"));
        lista.add(new ListItem(R.drawable.jamedia, "Babel 97.1 FM (Montevideo - Uruguay)", "http://radio.sodreuruguay.com:9170/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bogotá Beer Company Radio (Colombia)", "http://99.198.110.162:8052/"));
        lista.add(new ListItem(R.drawable.jamedia, "Bulo FM - Montevideo (Uruguay)", "http://bulofm.com:19000/"));
        lista.add(new ListItem(R.drawable.jamedia, "CW 39 La Voz De Paysandú 1320 AM (Paysandú - Uruguay)", "http://todoserver.com:8939/"));
        lista.add(new ListItem(R.drawable.jamedia, "CW 41 (San José - Uruguay)", "http://server-uk1.radioseninternetuy.com:8148"));
        lista.add(new ListItem(R.drawable.jamedia, "Cadena del Mar 106.5 (Maldonado - Uruguay)", "http://streamingraddios.com:9455/"));
        lista.add(new ListItem(R.drawable.jamedia, "Campana Bar (Colombia)", "http://listen.radionomy.com/campana-bar"));
        lista.add(new ListItem(R.drawable.jamedia, "Club4total80s", "http://streaming201.radionomy.com:80/1Club4total80s"));
    }
}
