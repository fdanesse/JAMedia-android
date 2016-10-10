package com.fdanesse.jamedia.Archivos;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;

/**
 * Created by flavio on 09/10/16.
 */
public class FileChooserItemListAdapter extends RecyclerView.Adapter<FileChooserItemListAdapter.ItemListViewHolder>{

    private ArrayList<ItemFileChooser> lista;
    private Activity activity;

    public FileChooserItemListAdapter(ArrayList<ItemFileChooser> lista, Activity activity){
        this.lista = lista;
        this.activity = activity;
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_item_file_chooser, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setActiveView(view);
                /*
                TextView textview1 = (TextView) view.findViewById(R.id.nombre);
                TextView textview2 = (TextView) view.findViewById(R.id.url);
                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra("name", textview1.getText().toString());
                intent.putExtra("url", textview2.getText().toString());
                intent.putExtra("tracks", lista);
                activity.startActivity(intent);
                activity.finish();
                */
            }
        });

        return new ItemListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {
        ItemFileChooser listItem = lista.get(position);
        holder.imagen_view.setImageResource(listItem.getImagen());
        holder.text_view_filename.setText(listItem.getFilename());
        holder.text_view_filepath.setText(listItem.getFilepath());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ItemListViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagen_view;
        private TextView text_view_filename;
        private TextView text_view_filepath;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            imagen_view = (ImageView) itemView.findViewById(R.id.imagen);
            text_view_filename = (TextView) itemView.findViewById(R.id.filename);
            text_view_filepath = (TextView) itemView.findViewById(R.id.filepath);
        }
    }
}