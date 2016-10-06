package com.fdanesse.jamedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.fdanesse.jamedia.PlayerList.ListActivity;


public class MainActivity extends AppCompatActivity {

    private Button radio;
    private Button television;
    private Button archivos;

    private NetworkChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radio = (Button) findViewById(R.id.radio);
        television = (Button) findViewById(R.id.television);
        archivos = (Button) findViewById(R.id.archivos);

        radio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        television.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                return true;
            }
        });

        archivos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                return true;
            }
        });

        network_changed();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }

    private void button_clicked(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                final Animation animAlpha = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.anim_alpha1);
                view.startAnimation(animAlpha);
                view.setAlpha(0.5f);
                break;
            }
            case MotionEvent.ACTION_UP:{
                final Animation animAlpha = AnimationUtils.loadAnimation(
                        getApplicationContext(), R.anim.anim_alpha2);
                view.startAnimation(animAlpha);
                view.setAlpha(1.0f);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private boolean network_check(){
        /**
         * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html?hl=es
         */
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null){
            return (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

    private void network_changed(){
        boolean i = network_check();
        radio.setEnabled(i);
        television.setEnabled(i);

        if (i == false){
            final Animation animAlpha = AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.anim_alpha1);
            radio.startAnimation(animAlpha);
            radio.setAlpha(0.5f);
            television.startAnimation(animAlpha);
            television.setAlpha(0.5f);
            Snackbar.make(radio, "No tienes conexión a internet", Snackbar.LENGTH_INDEFINITE).show();
        }
        else{
            final Animation animAlpha = AnimationUtils.loadAnimation(
                    getApplicationContext(), R.anim.anim_alpha2);
            radio.startAnimation(animAlpha);
            radio.setAlpha(1.0f);
            television.startAnimation(animAlpha);
            television.setAlpha(1.0f);
            Snackbar.make(radio, "Conectando a internet...", Snackbar.LENGTH_LONG).show();
        }
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        /**
         * http://www.mysamplecode.com/2013/04/android-automatically-detect-internet-connection.html
         */

        @Override
        public void onReceive(final Context context, final Intent intent) {
            //Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(Context context) {
            network_changed();
            return true;
        }

    } //NetworkChangeReceiver
} //MainActivity