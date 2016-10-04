package com.fdanesse.jamedia.JamediaPlayer;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

//http://www.tutorialspoint.com/android/android_mediaplayer.htm
//https://developer.android.com/reference/android/media/MediaPlayer.html
//http://www.programcreek.com/java-api-examples/index.php?source_dir=Soundroid-2.x-master/src/com/siahmsoft/soundroid/sdk7/services/CopyOfMediaPlayerService.java

/**
 * Created by flavio on 02/10/16.
 */
public final class JAMediaPlayer{

    private static final MediaPlayer mediaPlayer = new MediaPlayer();
    private static String URL = "";

    public static void play(String url){

        if (url.compareTo(URL) == 0 && mediaPlayer.isPlaying()){
            return;}
        else{
            URL = url;}

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setAudioStreamType(AudioTrack.MODE_STREAM);

        try {
            mediaPlayer.setDataSource(url);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        mediaPlayer.prepareAsync();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                //getTrackInfo()
            }
        });

        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                Log.i("**** INFO: ", " " + i + " " + i1);
                return false;
            }
        });

        /*
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("***** Buffering:", "" + percent);
            }
        });
        */

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.i("**** ERROR: ", " " + i + " " + i1);
                return false;
            }
        });

        /*
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });

        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {

            }
        });

        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {

            }
        });

        public void seekTo(int position){
            //if(mMediaPlayer.isPlaying()){
            mediaPlayer.seekTo(position);
            //}
        }

        public void setLooping(boolean looping) {
            mediaPlayer.setLooping(looping);
        }

        public void setVolume(float leftVolume, float rightVolume) {
            mediaPlayer.setVolume(leftVolume, rightVolume);
        }
        */
    }

    /*
    public static void setVolumen(float leftVolume, float rightVolume){
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }
    */
}
