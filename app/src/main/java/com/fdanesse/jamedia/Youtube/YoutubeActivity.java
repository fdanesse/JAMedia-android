package com.fdanesse.jamedia.Youtube;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.R;

public class YoutubeActivity extends AppCompatActivity {

    private EditText busquedas;
    private Button boton_buscar;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        busquedas = (EditText) findViewById(R.id.busquedas);
        boton_buscar = (Button) findViewById(R.id.boton_buscar);
        result = (TextView) findViewById(R.id.result);

        boton_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(busquedas, "OK", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(YoutubeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
