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

    private VideoView videoView;
    private MediaController mediaController;

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
        //videoView.setVideoURI(trackurl);
        return layout;
    }
}
