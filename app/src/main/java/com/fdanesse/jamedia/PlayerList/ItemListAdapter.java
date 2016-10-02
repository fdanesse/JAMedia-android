package com.fdanesse.jamedia.PlayerList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdanesse.jamedia.JamediaPlayer.PlayerActivity;
import com.fdanesse.jamedia.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by flavio on 27/09/16.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>{

    private ArrayList<ListItem> lista;
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private Activity activity;

    public ItemListAdapter(ArrayList<ListItem> lista, Activity activity){
        this.lista = lista;
        this.activity = activity;
    }

    @Override
    public ItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_card_view_item_list, parent, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textview1 = (TextView) view.findViewById(R.id.nombre);
                TextView textview2 = (TextView) view.findViewById(R.id.url);
                //Snackbar.make(view, textview.getText(), Snackbar.LENGTH_SHORT).show();
                //play(textview.getText().toString());

                Intent intent = new Intent(activity, PlayerActivity.class);
                intent.putExtra("name", textview1.getText().toString());
                intent.putExtra("url", textview2.getText().toString());
                activity.startActivity(intent);
                activity.finish();
            }
        });

        return new ItemListViewHolder(v);
    }

    /*
    private void play(String url){

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(url);
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });

        mediaPlayer.prepareAsync();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
    }
    */

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