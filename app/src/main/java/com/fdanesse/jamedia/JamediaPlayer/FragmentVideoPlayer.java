package com.fdanesse.jamedia.JamediaPlayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.fdanesse.jamedia.R;


public class FragmentVideoPlayer extends Fragment {

    public static VideoView videoView;
    //private static MediaController mediaController;
    private PlayerActivity playerActivity;


    public FragmentVideoPlayer() {}

    public void set_parent(PlayerActivity playerActivity){
        this.playerActivity = playerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
}
