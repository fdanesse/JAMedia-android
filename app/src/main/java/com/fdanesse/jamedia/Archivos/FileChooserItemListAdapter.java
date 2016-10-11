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

    private FileChooser filechooser;
    private ArrayList<ItemFileChooser> lista;
    private Activity activity;

    public FileChooserItemListAdapter(ArrayList<ItemFileChooser> lista, Activity activity, FileChooser filechooser){
        this.filechooser = filechooser;
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

                TextView textview1 = (TextView) view.findViewById(R.id.filename);
                TextView textview2 = (TextView) view.findViewById(R.id.filepath);
                TextView textview3 = (TextView) view.findViewById(R.id.selected);
                TextView textview4 = (TextView) view.findViewById(R.id.tipo);

                String filename = textview1.getText().toString();
                String filepath = textview2.getText().toString();
                String selected = textview3.getText().toString();
                String tipo = textview4.getText().toString();

                if (tipo.compareTo("Directorio") == 0){
                    Utils.setActiveView(view);
                    textview3.setText("true");
                    filechooser.load_path(filepath);
                }
                else if (tipo.compareTo("Archivo") == 0){
                    if (selected == "true"){
                        //FIXME: Se quita de la lista
                        Utils.setInactiveView(view);
                        textview3.setText("false");
                    }
                    else if (selected == "false"){
                        //FIXME: Se agrega a la lista
                        Utils.setActiveView(view);
                        textview3.setText("true");
                    }
                }
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
        holder.text_view_selected.setText(listItem.getSelected());
        holder.text_view_tipo.setText(listItem.getTipo());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ItemListViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagen_view;
        private TextView text_view_filename;
        private TextView text_view_filepath;
        private TextView text_view_selected;
        private TextView text_view_tipo;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            imagen_view = (ImageView) itemView.findViewById(R.id.imagen);
            text_view_filename = (TextView) itemView.findViewById(R.id.filename);
            text_view_filepath = (TextView) itemView.findViewById(R.id.filepath);
            text_view_selected = (TextView) itemView.findViewById(R.id.selected);
            text_view_tipo = (TextView) itemView.findViewById(R.id.tipo);
        }
    }
}