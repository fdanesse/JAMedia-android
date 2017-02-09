package com.fdanesse.jamedia.Youtube;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;


public class FragmentYoutubeList extends Fragment {

    private YoutubeActivity playerActivity;
    private RecyclerView recyclerView;
    public YoutubeItemListAdapter listAdapter;

    public FragmentYoutubeList(){}

    public void set_parent(YoutubeActivity playerActivity) {
        this.playerActivity = playerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_youtube_list, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        listAdapter = new YoutubeItemListAdapter(new ArrayList<YoutubeListItem>(), this);
        recyclerView.setAdapter(listAdapter);

        return layout;
    }

    public void load_list(ArrayList<YoutubeListItem> lista){
        listAdapter.releaseLoaders();
        listAdapter = new YoutubeItemListAdapter(lista, this);
        recyclerView.setAdapter(listAdapter);
    }

    public YoutubeItemListAdapter getListAdapter() {
        return listAdapter;
    }

    protected void playtrack(int index){

        ArrayList<YoutubeItemListAdapter.ItemListViewHolder> items = listAdapter.getHolders();

        for (YoutubeItemListAdapter.ItemListViewHolder i : items) {
            String trackpath = i.getText_view_url().getText().toString();
            if (trackpath == listAdapter.trackpath){
                Utils.setActiveView(i.itemView, "default");
            }
            else{
                if (i.itemView.getAlpha() == 1.0f){
                    Utils.setInactiveView(i.itemView, "default");
                }
            }
        }

        playerActivity.playtrack(index);
    }
}