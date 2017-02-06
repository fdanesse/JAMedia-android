package com.fdanesse.jamedia.Youtube;

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
    private static String apiKey;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_youtube_player, container, false);

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        Bundle bundle = getArguments();
        apiKey = bundle.getString("apiKey", "");

        return rootView;
    }

    public void load(String v){
        final String videoid = v;
        if(youTubePlayer != null){
            youTubePlayer.release();
        }

        youTubePlayerFragment.initialize(apiKey, new OnInitializedListener() {

            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    Log.i("*****", "PLAY: " + videoid);
                    youTubePlayer = player;
                    //youTubePlayer.setFullscreen(true);
                    youTubePlayer.loadVideo(videoid);
                    youTubePlayer.play();
                    Log.i("*****", "PLAY OK");
                }
            }

            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
                if (errorReason.isUserRecoverableError()) {
                    //errorReason.getErrorDialog(this, 1).show();
                    Log.i("*****", "ERROR is User Recoverable Error");
                } else {
                    String error = String.format("ERROR Inicialización", errorReason.toString());
                    Log.i("*****", "ERROR: " + error);
                }
            }
        });
    }
}
