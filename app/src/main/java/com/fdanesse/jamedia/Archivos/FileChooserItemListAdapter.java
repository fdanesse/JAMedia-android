package com.fdanesse.jamedia.Archivos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdanesse.jamedia.R;

import java.util.ArrayList;

/**
 * Created by flavio on 09/10/16.
 */
public class FileChooserItemListAdapter extends RecyclerView.Adapter<FileChooserItemListAdapter.ItemListViewHolder>{

    private FileChooserActivity filechooser = null;
    private ArrayList<ItemFileChooser> lista = null;
    private ArrayList<ItemListViewHolder> holders = null;

    public FileChooserItemListAdapter(ArrayList<ItemFileChooser> lista, FileChooserActivity filechooser){
        this.filechooser = filechooser;
        this.lista = lista;
        this.holders = new ArrayList<ItemListViewHolder>();
    }

    public ArrayList<ItemListViewHolder> getHolders() {
        return holders;
    }

    @Override
    public void onViewAttachedToWindow(ItemListViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textview2 = (TextView) view.findViewById(R.id.filepath);
                TextView textview3 = (TextView) view.findViewById(R.id.tipo);

                String filepath = textview2.getText().toString();
                String tipo = textview3.getText().toString();

                if (tipo.compareTo("Directorio") == 0){
                    filechooser.load_path(filepath);
                }
                else if (tipo.compareTo("Archivo") == 0){
                    filechooser.onClickFileItem(filepath, view);
                }
            }
        });
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_file_item, parent, false);
        ItemListViewHolder view = new ItemListViewHolder(v);
        holders.add(view);
        return view;
    }

    @Override
    public void onBindViewHolder(ItemListViewHolder holder, int position) {
        ItemFileChooser listItem = lista.get(position);
        holder.imagen_view.setImageResource(listItem.getImagen());
        holder.text_view_filename.setText(listItem.getFilename());
        holder.text_view_filepath.setText(listItem.getFilepath());
        holder.text_view_tipo.setText(listItem.getTipo());
        if (filechooser.check_path(holder.get_filepath())){
            holder.itemView.setAlpha(0.7f);
            }
        else{
            holder.itemView.setAlpha(0.4f);}
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public static class ItemListViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagen_view;
        private TextView text_view_filename;
        private TextView text_view_filepath;
        private TextView text_view_tipo;

        public ItemListViewHolder(View itemView) {
            super(itemView);
            imagen_view = (ImageView) itemView.findViewById(R.id.imagen);
            text_view_filename = (TextView) itemView.findViewById(R.id.filename);
            text_view_filepath = (TextView) itemView.findViewById(R.id.filepath);
            text_view_tipo = (TextView) itemView.findViewById(R.id.tipo);
        }

        public String get_filepath() {
            return text_view_filepath.getText().toString();
        }
    }
}