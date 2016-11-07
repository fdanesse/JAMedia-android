package com.fdanesse.jamedia.JamediaPlayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fdanesse.jamedia.PlayerActivity;
import com.fdanesse.jamedia.R;


public class FragmentVideoPlayer extends Fragment {

    public static VideoView videoView;
    private static MediaController mediaController;

    public FragmentVideoPlayer() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_video_player, container, false);
        videoView = (VideoView) layout.findViewById(R.id.videoView);
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        listen();
        return layout;
    }

    private void listen(){
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.seekTo(0);
                videoView.start();
                PlayerActivity.set_status(videoView.isPlaying(), videoView.canPause());
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                PlayerActivity.next_track();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
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

    public static void pause_play(){
        if (videoView.isPlaying()){
            if (videoView.canPause()) {
                videoView.pause();
            }
            else{
                stop();
            }
        }
        else{
            if (videoView.canPause()) {
                videoView.resume();
            }
            else{
                videoView.seekTo(0);
                videoView.start();
            }
        }
        PlayerActivity.set_status(videoView.isPlaying(), videoView.canPause());
    }

    public static void stop(){
        if (videoView.isPlaying()){
            videoView.stopPlayback();
        }
        PlayerActivity.set_status(videoView.isPlaying(), videoView.canPause());
    }

    public static void load_and_play(Uri trackurl){
        stop();
        videoView.setVideoURI(trackurl);
    }
}
