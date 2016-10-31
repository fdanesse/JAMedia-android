package com.fdanesse.jamedia.PlayerList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdanesse.jamedia.R;
//import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;

/**
 * Created by flavio on 27/09/16.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>{

    private ArrayList<ListItem> lista;
    private FragmentPlayerList fragmentPlayerList;
    private ArrayList<ItemListViewHolder> holders = null;

    public ItemListAdapter(ArrayList<ListItem> lista, FragmentPlayerList fragmentPlayerList){
        this.lista = lista;
        this.fragmentPlayerList = fragmentPlayerList;
        this.holders = new ArrayList<ItemListViewHolder>();
    }

    public ArrayList<ItemListViewHolder> getHolders() {
        return holders;
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
    }

    @Override
    public int getItemCount() {return lista.size();}

    protected void playtrack(int index, View view){
        fragmentPlayerList.playtrack(index, view);
    }

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
                    int index = getAdapterPosition();
                    playtrack(index, view);
                }
            });
        }
    }
}