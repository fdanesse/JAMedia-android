package com.fdanesse.jamedia.Youtube;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fdanesse.jamedia.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class FragmentYoutubePlayer extends Fragment {

    private YouTubePlayer youTubePlayer = null;
    private YouTubePlayerSupportFragment youTubePlayerFragment;

    public static final String END_TRACK = "END_TRACK";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String STOP = "STOP";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_youtube_player, container, false);

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        return rootView;
    }

    public void load(String v){
        final String videoid = v;
        if(youTubePlayer != null){
            youTubePlayer.setFullscreen(false);
            youTubePlayer.release();
            Log.i("*****", "Release");
        }

        Log.i("*****", "Load: " + videoid + " " + youTubePlayerFragment);

        youTubePlayerFragment.initialize(Keys.apikey, new OnInitializedListener() {

            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;
                    //youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    //youTubePlayer.setOnFullscreenListener((YoutubeActivity) getActivity());
                    ListenEvent();
                    youTubePlayer.loadVideo(videoid);
                    youTubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
                //youTubePlayer = null;
                if (errorReason.isUserRecoverableError()) {
                    Log.i("*****", "ERROR is User Recoverable Error");
                    errorReason.getErrorDialog(getActivity(), 1).show();
                }
                else {
                    String error = String.format("ERROR Inicialización", errorReason.toString());
                    Log.i("*****", "ERROR: " + error);
                    errorReason.getErrorDialog(getActivity(), 0).show();
                }
            }
        });

    }

    private void ListenEvent(){
        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                Intent broadcastIntent = new Intent(PLAY);
                getContext().sendBroadcast(broadcastIntent);
            }

            @Override
            public void onPaused() {
                Intent broadcastIntent = new Intent(PAUSE);
                getContext().sendBroadcast(broadcastIntent);
            }

            @Override
            public void onStopped() {
                Intent broadcastIntent = new Intent(STOP);
                getContext().sendBroadcast(broadcastIntent);
            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        });

        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                youTubePlayer.setFullscreen(false);
                Intent broadcastIntent = new Intent(END_TRACK);
                getContext().sendBroadcast(broadcastIntent);
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });
    }

    public void pause_play() {
        if (youTubePlayer.isPlaying()) {
            youTubePlayer.pause();
            Intent broadcastIntent = new Intent(PAUSE);
            getContext().sendBroadcast(broadcastIntent);
        }
        else{
            youTubePlayer.play();
            Intent broadcastIntent = new Intent(PLAY);
            getContext().sendBroadcast(broadcastIntent);
        }
    }
}
