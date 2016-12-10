package com.fdanesse.jamedia.JamediaPlayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.io.IOException;

//https://developer.android.com/guide/components/bound-services.html
//https://www.sitepoint.com/a-step-by-step-guide-to-building-an-android-audio-player-app/
//https://www.sitepoint.com/managing-multiple-sound-sources-in-android-with-audio-focus/
//https://developer.android.com/reference/android/app/Notification.MediaStyle.html


public class JAMediaPLayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {

    private static MediaPlayer mediaPlayer = null;
    private static String mediaFile;
    private final IBinder iBinder = new LocalBinder();

    public static final String END_TRACK = "END_TRACK";

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class LocalBinder extends Binder {
        public JAMediaPLayerService getService() {
            return JAMediaPLayerService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        unregisterReceiver(playNewTrack);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Registrar escucha de señal desde PlayerActivity
        IntentFilter filter = new IntentFilter(PlayerActivity.NEW_TRACK);
        registerReceiver(playNewTrack, filter);
    }

    //> ********* SEÑALES *********
    private BroadcastReceiver playNewTrack = new BroadcastReceiver() {
        /*
        Cuando se recibe la señal Broadcast_PLAY_NEW_AUDIO desde PlayerActivity
        */
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            mediaFile = extras.getString("media", "");
            initMediaPlayer();
        }
    };
    //< ********* SEÑALES *********

    //> ********* Interfaz de MediaPlayer *********
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {}

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMedia();
        Intent broadcastIntent = new Intent(END_TRACK);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what){
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:{
                break;
            }
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:{
                break;
            }
        }

        switch (extra){
            case MediaPlayer.MEDIA_ERROR_IO:{
                break;
            }
            case MediaPlayer.MEDIA_ERROR_MALFORMED:{
                break;
            }
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:{
                break;
            }
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:{
                break;
            }
        }

        Log.i("**** ERROR: ", " " + what + " " + extra);
        //stop();
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
    }
    //< ********* Interfaz de MediaPlayer *********

    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(1.0f, 1.0f);
        }

        stopMedia();
        mediaPlayer.reset();

        if (mediaFile == null || mediaFile == "") {return;}

        try {
            mediaPlayer.setDataSource(mediaFile);
            }
        catch (IOException e) {
            e.printStackTrace();
            //stopSelf();
            }

        mediaPlayer.prepareAsync();
    }

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
            //playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), jaMediaPLayerService.mPlayer.canPause());
            //playerActivity.set_status(mediaPlayer.isPlaying(), true);
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            //playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), jaMediaPLayerService.mPlayer.canPause());
            //playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), true);
        }
    }

    /*
    private void pauseMedia() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }
    */
}
