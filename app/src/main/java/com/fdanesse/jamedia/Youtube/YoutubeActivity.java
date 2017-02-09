package com.fdanesse.jamedia.Youtube;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class YoutubeActivity extends AppCompatActivity {

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

    private FragmentYoutubePlayer fragmentYoutubePlayer;
    private FragmentYoutubeList fragmentPlayerList;

    private SearchView busquedas;
    private WifiManager.WifiLock wifiLock;

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

        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        this.setVolumeControlStream(audioManager.STREAM_MUSIC);

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragmentYoutubePlayer = new FragmentYoutubePlayer();
        fragmentPlayerList = new FragmentYoutubeList();
        fragmentPlayerList.set_parent(this);
        fragments.add(fragmentPlayerList);
        fragments.add(fragmentYoutubePlayer);

        viewPager.setAdapter(new Notebook2(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        try {
            IntentFilter filter = new IntentFilter(fragmentYoutubePlayer.END_TRACK);
            registerReceiver(end_track, filter);
            filter = new IntentFilter(fragmentYoutubePlayer.PLAY);
            registerReceiver(playing_track, filter);
            filter = new IntentFilter(fragmentYoutubePlayer.PAUSE);
            registerReceiver(paused_track, filter);
            filter = new IntentFilter(fragmentYoutubePlayer.STOP);
            registerReceiver(stoped_track, filter);
        }
        catch (Exception e){}

        connect_buttons_actions();

        network_changed();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);
        //FIXME: Arreglar para 3g
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
        wifiLock.acquire();

        viewPager.setCurrentItem(0);
        viewPager.setEnabled(false);

        busquedas = (SearchView) findViewById(R.id.busquedas);

        busquedas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = busquedas.getQuery().toString();
                busquedas.setQuery("", false);
                new MyTask().execute(text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        /*
        FIXME: AdMob
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        */

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    busquedas.setVisibility(View.VISIBLE);
                }
                else {
                    busquedas.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // NETWORK
    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        /*
        http://www.mysamplecode.com/2013/04/android-automatically-detect-internet-connection.html
        http://stackoverflow.com/questions/38271365/connection-changed-broadcast-doesnt-work-when-mobile-data-is-enabled-in-marshma
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {
            isNetworkAvailable(context);
        }
        private boolean isNetworkAvailable(Context context) {
            network_changed();
            return true;
        }
    };

    private boolean network_check(){
        /**
         * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html?hl=es
         */
        ConnectivityManager cm = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null){
            //FIXME: Arreglar para 3g
            return (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting());
        }
        return false;
    }

    private void network_changed(){
        boolean i = network_check();
        if (i == false){
            Snackbar.make(viewPager, "No tienes conexión a internet", Snackbar.LENGTH_INDEFINITE).show();
        }
        else{
            //Snackbar.make(viewPager, "Conectando a internet...", Snackbar.LENGTH_LONG).show();
        }
    }
    //NETWORK

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(end_track);
        unregisterReceiver(stoped_track);
        unregisterReceiver(playing_track);
        unregisterReceiver(paused_track);
        try {
            unregisterReceiver(networkStateReceiver);
            wifiLock.release();}
        catch (Exception e){}
        fragmentPlayerList.listAdapter.releaseLoaders();
    }

    // Reproductor pide Cambio de pista
    private BroadcastReceiver end_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fragmentPlayerList.getListAdapter().next();
        }
    };

    // Reproductor está play
    private BroadcastReceiver playing_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            play.setImageResource(img_pausa);
            Utils.setActiveView(play, "default");
            play.setEnabled(true);
            check_buttons();
            //resize();
            viewPager.setCurrentItem(1);
            viewPager.setEnabled(true);
        }
    };

    // Reproductor está stop
    private BroadcastReceiver stoped_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            play.setImageResource(img_play);
        }
    };

    // Reproductor está pausa
    private BroadcastReceiver paused_track = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            play.setImageResource(img_play);
        }
    };

    private void check_buttons() {
        if (fragmentPlayerList.getListAdapter().getItemCount() > 1){
            Utils.setActiveView(siguiente, "default");
            siguiente.setEnabled(true);
            Utils.setActiveView(anterior, "default");
            anterior.setEnabled(true);
        }
        else{
            Utils.setInactiveView(siguiente, "default");
            siguiente.setEnabled(false);
            Utils.setInactiveView(anterior, "default");
            anterior.setEnabled(false);
        }
    }

    private void connect_buttons_actions(){
        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentPlayerList.getListAdapter().next();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentYoutubePlayer.pause_play();
            }
        });

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentPlayerList.getListAdapter().previous();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YoutubeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
        viewPager.setCurrentItem(1);
        viewPager.setEnabled(true);
        YoutubeListItem item = fragmentPlayerList.getListAdapter().getLista().get(index);
        fragmentYoutubePlayer.load(item.getId());
    }


    // AsyncTask Search
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

            /*
            Ver ademas: http://stackoverflow.com/questions/33969507/how-to-retrieve-details-of-single-video-from-youtube-using-videoid-through-data/39478514#39478514
            http://stackoverflow.com/questions/20462815/youtube-data-api-v3-java-search-with-pagination-issue
            var results = YouTube.Search.list('id,snippet', {
                q: 'dogs',
                maxResults: 25,
                videoDuration: 'long',
                type: 'video'
              });
             */

            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("JAMedia").build();

            try {
                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setKey(Keys.apikey);
                search.setQ(queryTerm);
                search.setType("video");
                search.setVideoSyndicated("any");
                search.setVideoEmbeddable("any");
                search.setOrder("viewCount");
                search.setSafeSearch("strict");
                //search.setVideoDimension("2d");
                //search.setVideoDuration("medium");
                //search.setVideoLicense("creativeCommon");
                //search.setVideoType("any");

                //search.setVideoDefinition("any");
                //search.setQ(queryTerm + ", official channel");
                /*
                    (shakira+(oficial|official))
                    "shakira"+"official"-"cover"-"parody"-"parodia"
                */

                //search.setVideoCategoryId("10");
                /* https://gist.github.com/dgp/1b24bf2961521bd75d6c
                    2 - Autos & Vehicles
                    1 -  Film & Animation
                    10 - Music
                    15 - Pets & Animals
                    17 - Sports
                    18 - Short Movies
                    19 - Travel & Events
                    20 - Gaming
                    21 - Videoblogging
                    22 - People & Blogs
                    23 - Comedy
                    24 - Entertainment
                    25 - News & Politics
                    26 - Howto & Style
                    27 - Education
                    28 - Science & Technology
                    29 - Nonprofits & Activism
                    30 - Movies
                    31 - Anime/Animation
                    32 - Action/Adventure
                    33 - Classics
                    34 - Comedy
                    35 - Documentary
                    36 - Drama
                    37 - Family
                    38 - Foreign
                    39 - Horror
                    40 - Sci-Fi/Fantasy
                    41 - Thriller
                    42 - Shorts
                    43 - Shows
                    44 - Trailers
                */
                            /*
                videoCaption
                topicId  https://developers.google.com/youtube/v3/guides/searching_by_topic?hl=es-419
                relevanceLanguage
                relatedToVideoId
                regionCode
                publishedBefore
                publishedAfter
                pageToken
                onBehalfOfContentOwner
                locationRadius
                location
                forMine
                forDeveloper
                forContentOwner
                eventType
                channelType
                channelId
                 */

                //search.getHttpContent();
                //search.getJsonContent();
                //search.getUnknownKeys();

                //search.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
                search.setFields("items(id/videoId,snippet/title,snippet/thumbnails/default/url)");
                //etag,items(etag,id(kind,videoId),snippet(description,thumbnails/default,title)),nextPageToken,pageInfo,prevPageToken,tokenPagination
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

            ArrayList<YoutubeListItem> lista = new ArrayList<YoutubeListItem>();

            List<SearchResult> searchResultList = s.getItems();
            if (searchResultList != null) {
                Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();

                while (iteratorSearchResults.hasNext()) {
                    SearchResult singleVideo = iteratorSearchResults.next(); //GenericJson
                    ResourceId rId = singleVideo.getId();
                    //Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                    String id = rId.getVideoId();
                    String url = "https://www.youtube.com/watch?v=" + id;
                    String title = singleVideo.getSnippet().getTitle();
                    lista.add(new YoutubeListItem(title, id, url));
                }
            }

            fragmentPlayerList.load_list(lista);
            super.onPostExecute(s);
        }
    }
}

