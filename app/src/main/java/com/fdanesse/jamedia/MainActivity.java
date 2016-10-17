package com.fdanesse.jamedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.fdanesse.jamedia.Archivos.FileChooserActivity;
import com.fdanesse.jamedia.Archivos.FileManager;
//import com.fdanesse.jamedia.JamediaPlayer.JAMediaPlayer;
import com.fdanesse.jamedia.PlayerList.ListActivity;
import com.fdanesse.jamedia.PlayerList.ListItem;

import java.util.ArrayList;


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

        set_touch_listeners();
        Utils.setActiveView(archivos);
        network_changed();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }

    private void set_touch_listeners(){
        radio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    ArrayList<ListItem> radios = FileManager.get_radios();
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putExtra("tracks", radios);
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
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    ArrayList<ListItem> tv = FileManager.get_tv();
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putExtra("tracks", tv);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        archivos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                button_clicked(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Intent intent = new Intent(MainActivity.this, FileChooserActivity.class);
                    intent.putExtra("currentpath",
                            Environment.getExternalStorageDirectory().getAbsolutePath()); //"/mnt/sdcard/Musica/"
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    private void button_clicked(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                Utils.setInactiveView(view);
                break;
            }
            case MotionEvent.ACTION_UP:{
                Utils.setActiveView(view);
                break;
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            /*
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(ListActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }*/
            /*
            case KeyEvent.KEYCODE_VOLUME_UP: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.up_vol(this);
                }
                Snackbar.make(radio, "Volumen: " + JAMediaPlayer.get_vol(this),
                        Snackbar.LENGTH_SHORT).show();
                return true;
            }
            case KeyEvent.KEYCODE_VOLUME_DOWN: {
                if (action == KeyEvent.ACTION_DOWN) {
                    JAMediaPlayer.down_vol(this);
                }
                Snackbar.make(radio, "Volumen: " + JAMediaPlayer.get_vol(this),
                        Snackbar.LENGTH_SHORT).show();
                return true;
            }
            */
            default:
                return super.dispatchKeyEvent(event);
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
            Utils.setInactiveView(radio);
            Utils.setInactiveView(television);
            Snackbar.make(radio, "No tienes conexión a internet", Snackbar.LENGTH_INDEFINITE).show();
        }
        else{
            Utils.setActiveView(radio);
            Utils.setActiveView(television);
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