package com.fdanesse.jamedia.Youtube;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdanesse.jamedia.PlayerList.FragmentPlayerList;
import com.fdanesse.jamedia.PlayerList.ListItem;
import com.fdanesse.jamedia.R;

import java.util.ArrayList;

/**
 * Created by flavio on 27/09/16.
 */
public class YoutubeItemListAdapter extends RecyclerView.Adapter<YoutubeItemListAdapter.ItemListViewHolder>{

    private ArrayList<ListItem> lista;
    private FragmentYoutubePlayerList fragmentPlayerList;
    private ArrayList<ItemListViewHolder> holders = null;

    private int trackselected = -1;                         //Pista en reproducción
    public String trackpath = "";                           //Pista en reproducción


    public YoutubeItemListAdapter(ArrayList<ListItem> lista, FragmentYoutubePlayerList fragmentPlayerList){
        this.lista = lista;
        this.fragmentPlayerList = fragmentPlayerList;
        this.holders = new ArrayList<ItemListViewHolder>();
    }

    public int getTrackselected() {
        return trackselected;
    }

    public ArrayList<ListItem> getLista() {
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
        ListItem i = lista.get(trackselected);
        trackpath = i.getUrl();
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
        ListItem i = lista.get(trackselected);
        trackpath = i.getUrl();
        fragmentPlayerList.playtrack(trackselected);
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_list_item, parent, false);
        ItemListViewHolder view = new ItemListViewHolder(v);
        holders.add(view);
        return view;
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {
        ListItem listItem = lista.get(position);
        holder.imagen_view.setImageResource(listItem.getImagen());
        holder.text_view_nombre.setText(listItem.getNombre());
        holder.text_view_url.setText(listItem.getUrl());

        if (position == trackselected){
            holder.itemView.setAlpha(1.0f);
        }
        else{
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {return lista.size();}


    //Clase interna
    public class ItemListViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagen_view;
        private TextView text_view_nombre;
        private TextView text_view_url;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            imagen_view = (ImageView) itemView.findViewById(R.id.imagen);
            text_view_nombre = (TextView) itemView.findViewById(R.id.nombre);
            text_view_url = (TextView) itemView.findViewById(R.id.url);

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
            return text_view_url;
        }
    }
}