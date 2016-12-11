package com.fdanesse.jamedia.JamediaPlayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.MediaController;



import com.fdanesse.jamedia.R;


public class FragmentVideoPlayer extends Fragment{

    public SurfaceView surfaceView;
    public SurfaceHolder surfaceHolder;
    //private static MediaController mediaController;
    private PlayerActivity playerActivity;


    public FragmentVideoPlayer() {}

    public void set_parent(PlayerActivity playerActivity){
        this.playerActivity = playerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_video_player, container, false);
        surfaceView = (SurfaceView) layout.findViewById(R.id.videoView);

        surfaceHolder = surfaceView.getHolder();
        //surfaceHolder.setFixedSize(800, 480);
        //surfaceHolder.setSizeFromLayout();
        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        /*
        mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        */
        return layout;
    }
}
