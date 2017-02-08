package com.fdanesse.jamedia.Youtube;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fdanesse.jamedia.R;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

/**
 * Created by flavio on 27/09/16.
 */
public class YoutubeItemListAdapter extends RecyclerView.Adapter<YoutubeItemListAdapter.ItemListViewHolder>{

    private ArrayList<YoutubeListItem> lista;
    private FragmentYoutubeList fragmentPlayerList;
    private ArrayList<ItemListViewHolder> holders = null;

    private int trackselected = -1;                         //Pista en reproducción
    public String trackpath = "";                           //Pista en reproducción


    public YoutubeItemListAdapter(ArrayList<YoutubeListItem> lista, FragmentYoutubeList fragmentPlayerList){
        this.lista = lista;
        this.fragmentPlayerList = fragmentPlayerList;
        this.holders = new ArrayList<ItemListViewHolder>();
    }

    public ArrayList<YoutubeListItem> getLista() {
        return lista;
    }

    public ArrayList<ItemListViewHolder> getHolders() {
        return holders;
    }

    public void previous(){
        int x = getItemCount();
        if (trackselected > 0){
            trackselected -= 1;
        }
        else{
            trackselected = x - 1;
        }
        YoutubeListItem i = lista.get(trackselected);
        trackpath = i.getId();
        fragmentPlayerList.playtrack(trackselected);
    }

    public void next(){
        int x = getItemCount();
        if (trackselected < x - 1){
            trackselected += 1;
        }
        else{
            trackselected = 0;
        }
        YoutubeListItem i = lista.get(trackselected);
        trackpath = i.getId();
        fragmentPlayerList.playtrack(trackselected);
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_youtube_list_item, parent, false);
        ItemListViewHolder view = new ItemListViewHolder(v);
        holders.add(view);
        return view;
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {

        YoutubeListItem listItem = lista.get(position);

        holder.imagen_view.setTag(listItem.getId());
        holder.text_view_nombre.setText(listItem.getNombre());
        holder.text_view_id.setText(listItem.getId());
        holder.text_view_url.setText(listItem.getUrl());

        final String id = listItem.getId();
        final YoutubeListItem li = listItem;

        /* FIXME: No funciona con un recyclerview
        holder.imagen_view.initialize(Keys.apikey, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    final YouTubeThumbnailLoader ytl = youTubeThumbnailLoader;
                    ytl.setVideo(id);
                    ytl.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            ytl.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView,
                                                     YouTubeThumbnailLoader.ErrorReason errorReason) {
                        }
                    });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult youTubeInitializationResult) {
            }
        });
        */

        if (position == trackselected){
            holder.itemView.setAlpha(1.0f);
        }
        else{
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {return lista.size();}


    public class ItemListViewHolder extends RecyclerView.ViewHolder {

        private YouTubeThumbnailView imagen_view;
        private TextView text_view_nombre;
        private TextView text_view_id;
        private TextView text_view_url;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            imagen_view = (YouTubeThumbnailView) itemView.findViewById(R.id.imagen);
            text_view_nombre = (TextView) itemView.findViewById(R.id.nombre);
            text_view_id = (TextView) itemView.findViewById(R.id.id);
            text_view_url = (TextView) itemView.findViewById(R.id.video_url);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trackselected = getAdapterPosition();
                    trackpath = getText_view_url().getText().toString();
                    try {
                        fragmentPlayerList.playtrack(trackselected);
                    }
                    catch (Exception e){}
                }
            });
        }

        public TextView getText_view_url() {
            return text_view_id;
        }
    }
}