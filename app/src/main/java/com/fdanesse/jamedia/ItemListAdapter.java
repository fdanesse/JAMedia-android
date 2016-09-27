package com.fdanesse.jamedia;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by flavio on 27/09/16.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>{

    private ArrayList<ListItem> lista = new ArrayList<ListItem>();

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ItemListViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagen_view;
        private TextView text_view_nombre;
        private TextView text_view_url;

        public ItemListViewHolder(View itemView) {
            super(itemView);

            imagen_view = (ImageView) itemView.findViewById(R.id.imagen);
            text_view_nombre = (TextView) itemView.findViewById(R.id.nombre);
            text_view_url = (TextView) itemView.findViewById(R.id.url);
        }
    }
}
