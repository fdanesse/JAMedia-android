package com.fdanesse.jamedia.JamediaPlayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Build;
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
    private static String url = "";
    private static AudioManager audioManager;
    private static int maxvol;
    private static Activity activity;

    public static void up_vol(){
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + 1;
        if (vol > 0 && vol < maxvol) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol,
                    AudioManager.FLAG_PLAY_SOUND);
        }
    }
    public static void down_vol(){
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) - 1;
        if (vol > 0 && vol < maxvol) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol,
                    AudioManager.FLAG_PLAY_SOUND);
        }
    }

    public static void play(Activity act, String u){

        activity = act;

        if (audioManager == null){
            audioManager = (AudioManager) activity.getSystemService(activity.AUDIO_SERVICE);
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            maxvol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }

        if (u.compareTo(url) == 0 && mediaPlayer.isPlaying()){
            return;}
        else{
            url = u;}

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setAudioStreamType(AudioTrack.MODE_STREAM);

        try {
            mediaPlayer.setDataSource(url);
        } catch (IllegalArgumentException e1) {
            //e1.printStackTrace();
            Log.i("***** 1: ", e1.getMessage());
            return;
        } catch (SecurityException e1) {
            //e1.printStackTrace();
            Log.i("***** 2: ", e1.getMessage());
            return;
        } catch (IllegalStateException e1) {
            //e1.printStackTrace();
            Log.i("***** 3: ", e1.getMessage());
            return;
        } catch (IOException e1) {
            //e1.printStackTrace();
            Log.i("***** 4: ", e1.getMessage());
            return;
        }

        mediaPlayer.prepareAsync();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                Log.i("**** Duracion: ", " " + mediaPlayer.getDuration());
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
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
                mediaPlayer.stop();
                mediaPlayer.reset();
                url = "";
                return true;
            }
        });

        /*
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int codeinfo, int extra) {
                switch (codeinfo){
                    //case MediaPlayer.MEDIA_INFO_UNKNOWN:{
                    //    break;
                    //}
                    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:{
                        break;
                    }
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:{
                        break;
                    }
                    case MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT:{
                        break;
                    }
                    case MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING:{
                        break;
                    }
                    default:{Log.i("**** INFO: ", " " + codeinfo + " " + extra);}
                }
                return true;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //Log.i("***** Buffering:", "" + percent);
            }
        });

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
}
