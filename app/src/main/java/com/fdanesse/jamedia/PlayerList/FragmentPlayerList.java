package com.fdanesse.jamedia.PlayerList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fdanesse.jamedia.PlayerActivity;
import com.fdanesse.jamedia.R;
import com.fdanesse.jamedia.Utils;

import java.util.ArrayList;


public class FragmentPlayerList extends Fragment {

    private PlayerActivity playerActivity;
    private RecyclerView recyclerView;
    private ItemListAdapter listAdapter;

    public FragmentPlayerList(){}

    public void set_parent(PlayerActivity playerActivity) {
        this.playerActivity = playerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_player_list, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.reciclerview);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        Bundle bundle = getArguments();
        listAdapter = new ItemListAdapter(
                (ArrayList<ListItem>) bundle.getSerializable("tracks"), this);
        recyclerView.setAdapter(listAdapter);
        return layout;
    }

    public ItemListAdapter getListAdapter() {
        return listAdapter;
    }

    protected void playtrack(int index){

        ArrayList<ItemListAdapter.ItemListViewHolder> items = listAdapter.getHolders();

        for (ItemListAdapter.ItemListViewHolder i : items) {
            String trackpath = i.getText_view_url().getText().toString();
            if (trackpath == listAdapter.trackpath){
                Utils.setActiveView(i.itemView);
            }
            else{
                if (i.itemView.getAlpha() == 1.0f){
                    Utils.setInactiveView(i.itemView);
                }
            }
        }
        playerActivity.playtrack(index);
    }
}