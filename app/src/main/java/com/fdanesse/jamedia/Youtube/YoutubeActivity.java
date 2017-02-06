package com.fdanesse.jamedia.Youtube;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;

import com.fdanesse.jamedia.MainActivity;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;
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

    private static final String apiKey = "";

    private SearchView busquedas;


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
        fragmentYoutubePlayer = new FragmentYoutubePlayer();
        fragmentPlayerList = new FragmentYoutubeList();
        fragmentPlayerList.set_parent(this);
        fragments.add(fragmentPlayerList);
        fragments.add(fragmentYoutubePlayer);

        viewPager.setAdapter(new Notebook2(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        Bundle extra = new Bundle();
        extra.putString("apiKey", apiKey);
        fragmentYoutubePlayer.setArguments(extra);

        //connect_buttons_actions();

        viewPager.setCurrentItem(0);
        //viewPager.setEnabled(false);

        busquedas = (SearchView) findViewById(R.id.busquedas);

        busquedas.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
        ListItem item = fragmentPlayerList.getListAdapter().getLista().get(index);
        fragmentYoutubePlayer.load(item.getUrl());
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

            fragmentPlayerList.load_list(lista);
            super.onPostExecute(s);
        }
    }
}
