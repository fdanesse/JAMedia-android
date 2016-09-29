package com.fdanesse.jamedia;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by flavio on 27/09/16.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>{

    private ArrayList<ListItem> lista;

    public ItemListAdapter(ArrayList<ListItem> lista){
        this.lista = lista;
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_view_item_list, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textview = (TextView) view.findViewById(R.id.nombre);
                //Toast.makeText(view.getContext(), textview.getText(), Toast.LENGTH_LONG).show();
                Snackbar.make(view, textview.getText(), Snackbar.LENGTH_SHORT).show();
            }
        });
        return new ItemListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {
        ListItem listItem = lista.get(position);
        holder.imagen_view.setImageResource(listItem.getImagen());
        holder.text_view_nombre.setText(listItem.getNombre());
        holder.text_view_url.setText(listItem.getUrl());
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