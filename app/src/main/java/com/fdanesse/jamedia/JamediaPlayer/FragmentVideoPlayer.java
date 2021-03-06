package com.fdanesse.jamedia.JamediaPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.fdanesse.jamedia.R;


public class FragmentVideoPlayer extends Fragment{

    public SurfaceView surfaceView;
    public SurfaceHolder surfaceHolder;

    public static final String TOUCH = "TOUCH";
    public static final String FULLSCREEN = "FULLSCREEN";

    public FragmentVideoPlayer() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_video_player, container, false);
        surfaceView = (SurfaceView) layout.findViewById(R.id.videoView);
        surfaceHolder = surfaceView.getHolder();

        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent broadcastIntent = new Intent(TOUCH);
                getContext().sendBroadcast(broadcastIntent);
                return false;
            }
        });

        FloatingActionButton fullscreen = (FloatingActionButton) layout.findViewById(R.id.fullscreen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent(FULLSCREEN);
                getContext().sendBroadcast(broadcastIntent);
            }
        });

        return layout;
    }
}
