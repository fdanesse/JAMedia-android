package com.fdanesse.jamedia.JamediaPlayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.fdanesse.jamedia.R;


public class FragmentVideoPlayer extends Fragment{

    public SurfaceView surfaceView;
    public SurfaceHolder surfaceHolder;

    public FragmentVideoPlayer() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_video_player, container, false);
        surfaceView = (SurfaceView) layout.findViewById(R.id.videoView);
        surfaceHolder = surfaceView.getHolder();
        return layout;
    }
}
