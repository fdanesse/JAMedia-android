package com.fdanesse.jamedia.JamediaPlayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.SurfaceHolder;

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

    private static Point videosize;
    private final IBinder iBinder = new LocalBinder();
    private static boolean hasvideo = false;

    public static final String END_TRACK = "END_TRACK";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String STOP = "STOP";
    /*
    public static final String BUFFER = "BUFFER";
    public static final String ERROR = "ERROR";
    */

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
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /*
        Intent broadcastIntent = new Intent(BUFFER);
        broadcastIntent.putExtra("buffer", percent);
        sendBroadcast(broadcastIntent);
        */
    }

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

        /*
        Intent broadcastIntent = new Intent(ERROR);
        broadcastIntent.putExtra("what", what);
        broadcastIntent.putExtra("extra", extra);
        sendBroadcast(broadcastIntent);
        */
        stopMedia();
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {return false;}

    @Override
    public void onPrepared(MediaPlayer mp) {
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {}
    //< ********* Interfaz de MediaPlayer *********

    public void setDisplay(SurfaceHolder surfaceHolder) {
        mediaPlayer.setDisplay(surfaceHolder);
    }

    public Point getVideosize() {
        return videosize;
    }

    public boolean get_hasvideo(){
        return hasvideo;
    }

    public Point getDuration_Position(){
        Point point = new Point();
        point.x = 0;
        point.y = 0;
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()) {
                point.x = mediaPlayer.getDuration();
                point.y = mediaPlayer.getCurrentPosition();
            }
        }
        return point;
    }

    public void set_pos(int pos){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(pos);
            }
        }
    }

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
        catch (IOException e) {}

        videosize = new Point();
        videosize.x = 0; videosize.y = 0;
        hasvideo = false;
        mediaPlayer.prepareAsync();
    }

    private void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            hasvideo = false;
            mediaPlayer.seekTo(0);
            mediaPlayer.start();

            videosize.x = 0;
            videosize.y = 0;
            MediaPlayer.TrackInfo[] i = mediaPlayer.getTrackInfo();
            for (MediaPlayer.TrackInfo inf : i){
                if (inf.getTrackType() == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_VIDEO){
                    videosize.x = mediaPlayer.getVideoWidth();
                    videosize.y = mediaPlayer.getVideoHeight();
                    hasvideo = true;
                }
            }

            send_play();
        }
    }

    private void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            Intent broadcastIntent = new Intent(STOP);
            sendBroadcast(broadcastIntent);
        }
    }

    public void pause_play() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Intent broadcastIntent = new Intent(PAUSE);
            sendBroadcast(broadcastIntent);
        }
        else{
            mediaPlayer.start();
            send_play();
        }
    }

    private void send_play(){
        Intent broadcastIntent = new Intent(PLAY);
        sendBroadcast(broadcastIntent);
    }
}
