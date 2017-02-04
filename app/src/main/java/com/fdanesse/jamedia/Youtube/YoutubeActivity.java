package com.fdanesse.jamedia.Youtube;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fdanesse.jamedia.MainActivity;
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
import java.util.Iterator;
import java.util.List;

public class YoutubeActivity extends AppCompatActivity {

    private EditText busquedas;
    private Button boton_buscar;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        busquedas = (EditText) findViewById(R.id.busquedas);
        boton_buscar = (Button) findViewById(R.id.boton_buscar);
        result = (TextView) findViewById(R.id.result);

        boton_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute(busquedas.getText().toString());
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




    public class MyTask extends AsyncTask<String, Integer, String> {

        private static final String apiKey = "";
        private static final long NUMBER_OF_VIDEOS_RETURNED = 1;
        private YouTube youtube;

        String TAG = getClass().getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String queryTerm = strings[0];
            String ret = "";

            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("JAMedia").build();

            try {
                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setKey(apiKey);
                search.setQ(queryTerm);
                search.setType("video");

                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();

                if (searchResultList != null) {
                    Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();

                    while (iteratorSearchResults.hasNext()) {
                        SearchResult singleVideo = iteratorSearchResults.next(); //GenericJson

                        ResourceId rId = singleVideo.getId();
                        if (rId.getKind().equals("youtube#video")) {
                            Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                            ret += "Video id: " + rId.getVideoId();
                            ret += "Title: " + singleVideo.getSnippet().getTitle();
                            ret += "Thumbnail: " + thumbnail.getUrl();
                        }

                        //Log.i("*****", ret);
                    }

                }
            }
            catch (Exception e){
                return e.toString();
            }

            return ret;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG + " onPostExecute", " " + s);
            super.onPostExecute(s);
        }
    }
}
