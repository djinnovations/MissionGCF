package com.example.test.spplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.test.spplayer.activities.BaseActivity;
import com.example.test.spplayer.home.ModifiedMainActivity;
import com.example.test.spplayer.adapters.TrackAdapter;
import com.example.test.spplayer.models.Track;

import java.util.List;

/**
 * Created by User on 14-03-2018.
 */

public class MainFragment extends SingleMenuFragment {

    private TrackAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() instanceof BaseActivity){
            ((BaseActivity) getActivity()).queryForRecentTracks("last_week");
        }
    }


    @Override
    public void onEventReceived(Object event) {
        //super.onEventReceived(event);
        if (event instanceof Track) {
            if (getActivity() instanceof ModifiedMainActivity) {
                ((ModifiedMainActivity) getActivity()).onTrackSelection((Track) event);
            }
        }
    }

    @Override
    public boolean isAddSnapper() {
        return false;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        adapter = new TrackAdapter();
        return adapter;
    }

    @Override
    public void changeData(List dataList) {
        try {
            if (adapter != null)
                adapter.changeData(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onGarbageCollection() {
        adapter = null;
    }

    @Override
    protected String getFragmentTitle() {
        return "";
    }
}
