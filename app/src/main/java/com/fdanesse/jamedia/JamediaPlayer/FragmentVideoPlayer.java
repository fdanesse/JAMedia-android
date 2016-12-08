package com.fdanesse.jamedia.JamediaPlayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fdanesse.jamedia.R;

import java.io.IOException;


public class FragmentVideoPlayer extends Fragment {

    public static VideoView videoView;
    //private static MediaController mediaController;
    private PlayerActivity playerActivity;

    public JAMediaPLayerService jaMediaPLayerService;
    public boolean serviceBound = false;


    public FragmentVideoPlayer() {
    }

    public void set_parent(PlayerActivity playerActivity){
        this.playerActivity = playerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_video_player, container, false);

        videoView = (VideoView) layout.findViewById(R.id.videoView);
        /*
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        */
        return layout;
    }

    //Binding this Client to the AudioPlayer Service
    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            JAMediaPLayerService.LocalBinder binder = (JAMediaPLayerService.LocalBinder) service;
            jaMediaPLayerService = binder.getService();
            serviceBound = true;
            //listen();
            Snackbar.make(videoView, "JAMediaPlayerService ON", Snackbar.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Snackbar.make(videoView, "JAMediaPlayerService OFF", Snackbar.LENGTH_LONG).show();
            serviceBound = false;
        }
    };

    public void load_and_play(Uri trackurl){
        if (!serviceBound) {
            Intent intent = new Intent(getContext(), JAMediaPLayerService.class);
            intent.putExtra("media", trackurl.toString());
            getContext().startService(intent);
            playerActivity.bindService(intent, mConnection, getContext().BIND_AUTO_CREATE);
        }
        else {
            //Service is active
            //Send media with BroadcastReceiver
        }
    }





    /*
    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (serviceBound) {
            playerActivity.unbindService(mConnection);
            serviceBound = false;
        }
    }

    private void listen() {
        jaMediaPLayerService.mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                //playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), jaMediaPLayerService.mPlayer.canPause());
                playerActivity.set_status(mediaPlayer.isPlaying(), true);
            }
        });

        jaMediaPLayerService.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playerActivity.next_track();
            }
        });

        jaMediaPLayerService.mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
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
    */

    /*
    public void pause_play(){
        //FIXME: Nunca pausa y/o resume
        /
        if (jaMediaPLayerService.mPlayer.isPlaying()){
            if (jaMediaPLayerService.mPlayer.canPause()) {
                jaMediaPLayerService.mPlayer.pause();
            }
            else{
                stop();
            }
        }
        else{
            if (jaMediaPLayerService.mPlayer.canPause()) {
                jaMediaPLayerService.mPlayer.resume();
            }
            else{
                jaMediaPLayerService.mPlayer.seekTo(0);
                jaMediaPLayerService.mPlayer.start();
            }
        }
        /
        if (jaMediaPLayerService.mPlayer.isPlaying()) {
            jaMediaPLayerService.mPlayer.pause();
        }
        else{
            jaMediaPLayerService.mPlayer.pause();jaMediaPLayerService.mPlayer.seekTo(0);
            jaMediaPLayerService.mPlayer.start();
        }
        //playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), jaMediaPLayerService.mPlayer.canPause());
        playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), true);
    }
    */

    /*
    public void stop(){
        if (serviceBound) {
            if (jaMediaPLayerService.mPlayer.isPlaying()) {
                jaMediaPLayerService.mPlayer.stop();
                jaMediaPLayerService.mPlayer.reset();
            }
            //playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), jaMediaPLayerService.mPlayer.canPause());
            playerActivity.set_status(jaMediaPLayerService.mPlayer.isPlaying(), true);
        }
    }
    */

}
