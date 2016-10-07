package com.fdanesse.jamedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        set_touch_listeners();
        network_changed();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        setActiveView(archivos);
    }

    private void set_touch_listeners(){
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
                //if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                //    showFileChooser();
                //}
                return true;
            }
        });
    }

    private void setActiveView(View view){
        final Animation animAlpha = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.anim_alpha2);
        view.startAnimation(animAlpha);
        view.setAlpha(1.0f);
    }

    private void setInactiveView(View view){
        final Animation animAlpha = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.anim_alpha1);
        view.startAnimation(animAlpha);
        view.setAlpha(0.5f);
    }

    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("***", "File Uri: " + uri.toString());
                    Snackbar.make(radio, "OK " + uri.toString(), Snackbar.LENGTH_INDEFINITE).show();
                    // Get the path
                    //String path = FileUtils.getPath(this, uri);
                    //Log.d("***", "File Path: " + path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                else{
                    Snackbar.make(radio, "No OK", Snackbar.LENGTH_INDEFINITE).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    */

    //private void showFileChooser() {
    //    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    //    intent.setType("*/audio|*/video");
    //    intent.addCategory(Intent.CATEGORY_OPENABLE);
    //    try {
    //        startActivityForResult(Intent.createChooser(intent, ""), 0);
    //    } catch (android.content.ActivityNotFoundException ex) {}
    //}

    private void button_clicked(View view, MotionEvent motionEvent){
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                setInactiveView(view);
                break;
            }
            case MotionEvent.ACTION_UP:{
                setActiveView(view);
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
            setInactiveView(radio);
            setInactiveView(television);
            Snackbar.make(radio, "No tienes conexión a internet", Snackbar.LENGTH_INDEFINITE).show();
        }
        else{
            setActiveView(radio);
            setActiveView(television);
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