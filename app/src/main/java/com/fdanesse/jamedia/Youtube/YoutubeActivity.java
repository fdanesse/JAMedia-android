package com.fdanesse.jamedia.Youtube;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fdanesse.jamedia.JamediaPlayer.Notebook;
import com.fdanesse.jamedia.JamediaPlayer.PlayerActivity;
import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class YoutubeActivity extends AppCompatActivity{

    private AppBarLayout appbar;
    private Toolbar toolbar;
    private ImageButton anterior;
    private ImageButton siguiente;
    private ImageButton play;
    private ImageButton creditos;  // FIXME: terminar
    private ImageButton cancel;

    private int img_pausa = R.drawable.pausa;
    private int img_play = R.drawable.play;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    //private FragmentVideoPlayer fragmentVideoPlayer;
    private FragmentYoutubePlayerList fragmentPlayerList;



    private static final String apiKey = "";
    //private static String video = "";

    private SearchView busquedas;
    private YouTubePlayerView youtube_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        appbar = (AppBarLayout) findViewById(R.id.appbar);
        toolbar = (Toolbar) findViewById(R.id.player_toolbar);
        tabLayout = (TabLayout) findViewById(R.id.lenguetas);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        creditos = (ImageButton) findViewById(R.id.creditos);
        anterior = (ImageButton) findViewById(R.id.anterior);
        play = (ImageButton) findViewById(R.id.play);
        siguiente = (ImageButton) findViewById(R.id.siguiente);
        cancel = (ImageButton) findViewById(R.id.cancel);

        img_pausa = R.drawable.pausa;
        img_play = R.drawable.play;

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        //fragmentVideoPlayer = new FragmentVideoPlayer();
        fragmentPlayerList = new FragmentYoutubePlayerList();
        fragmentPlayerList.set_parent(this);
        fragments.add(fragmentPlayerList);
        //fragments.add(fragmentVideoPlayer);


        viewPager.setAdapter(new Notebook(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        //connect_buttons_actions();

        viewPager.setCurrentItem(0);
        viewPager.setEnabled(false);




        busquedas = (SearchView) findViewById(R.id.busquedas);
        //youtube_view = (YouTubePlayerView) findViewById(R.id.youtube_view);

        busquedas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Snackbar.make(busquedas, busquedas.getQuery(), Snackbar.LENGTH_INDEFINITE).show();
                new MyTask().execute(busquedas.getQuery().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:{
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent intent = new Intent(YoutubeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


    public void playtrack(int index){
        ListItem item = fragmentPlayerList.getListAdapter().getLista().get(index);
        /*
        Intent broadcastIntent = new Intent(NEW_TRACK);
        broadcastIntent.putExtra("media", item.getUrl());
        sendBroadcast(broadcastIntent);
        */
        Snackbar.make(busquedas, item.getUrl(), Snackbar.LENGTH_INDEFINITE).show();
        //youtube_view.initialize(apiKey, this);
    }
    /*
    private void load(String video_url){
        video = video_url;
        Snackbar.make(busquedas, video.toString(), Snackbar.LENGTH_INDEFINITE).show();
        youtube_view.initialize(apiKey, this);
    }
    */


    /*
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(video);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, 1).show();
        } else {
            String error = String.format("ERROR Inicialización", errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(apiKey, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youtube_view;
    }
    */


    /*
    Busquedas
     */
    public class MyTask extends AsyncTask<String, Integer, SearchListResponse> {

        private static final long NUMBER_OF_VIDEOS_RETURNED = 50;
        private YouTube youtube;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SearchListResponse doInBackground(String... strings) {
            String queryTerm = strings[0];
            SearchListResponse searchResponse;

            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("JAMedia").build();

            try {
                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setKey(apiKey);
                search.setQ(queryTerm);
                search.setType("video");

                //search.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
                search.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

                searchResponse = search.execute();
            }
            catch (Exception e){
                Log.i("*****", e.toString());
                return new SearchListResponse();
            }

            return searchResponse;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(SearchListResponse s) {

            ArrayList<ListItem> lista = new ArrayList<ListItem>();

            List<SearchResult> searchResultList = s.getItems();
            if (searchResultList != null) {
                Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();

                while (iteratorSearchResults.hasNext()) {
                    SearchResult singleVideo = iteratorSearchResults.next(); //GenericJson
                    ResourceId rId = singleVideo.getId();
                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                    String url = rId.getVideoId();

                    String title = singleVideo.getSnippet().getTitle();
                    lista.add(new ListItem(R.drawable.video, title, url));
                }
            }

            //load(lista.get(0).getUrl());

            fragmentPlayerList.load_list(lista);

            super.onPostExecute(s);
        }
    }
}
