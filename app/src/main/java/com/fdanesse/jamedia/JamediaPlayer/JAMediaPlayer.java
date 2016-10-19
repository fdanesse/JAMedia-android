package com.fdanesse.jamedia.JamediaPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
//import android.widget.MediaController;
import android.widget.VideoView;


/**
 * Created by flavio on 02/10/16.
 */
public final class JAMediaPlayer extends VideoView {

    //private static MediaController mediaController;

    public JAMediaPlayer(Context context) {
        super(context);

        //mediaController = new MediaController(context);
        //mediaController.setAnchorView(this);
        //mediaController.setMediaPlayer(this);
        //videoView.setMediaController(mediaController);
        //videoView.requestFocus();

        listen();
    }

    private void listen(){
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i("**** onPrepared: ", "OK");
                seekTo(0);
                start();
                /*
                Snackbar.make(videoView, trackurl, Snackbar.LENGTH_INDEFINITE).show();
                check_buttons();
                videoView.seekTo(0);
                videoView.start();
                */
                //FIXME: playing? (actualizar botones play y stop)
                /*
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                        mediaController.setAnchorView(videoView);
                    }
                });
                */
            }
        });

        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //next_track();
                Log.i("**** Next Tack: ", "OK");
            }
        });

        setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                switch (i){
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:{
                        break;
                    }
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:{
                        break;
                    }
                }

                switch (i1){
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

                Log.i("**** ERROR: ", " " + i + " " + i1);
                stop();
                return true;
            }
        });
    }

    public void stop(){
        stopPlayback();
    }

    public void load_and_play(String trackurl){
        if (isPlaying()){
            stop();
        }
        setVideoPath(trackurl);
    }
}

