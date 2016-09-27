package com.fdanesse.jamedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class RadioActivity extends AppCompatActivity {

    private ArrayList<ListItem> lista = new ArrayList<ListItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);

        lista.add(new ListItem(R.drawable.jamedia, "AAA", "xxx"));
    }
}
