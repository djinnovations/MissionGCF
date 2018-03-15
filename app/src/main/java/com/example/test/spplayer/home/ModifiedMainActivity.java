package com.example.test.spplayer.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.test.spplayer.R;
import com.example.test.spplayer.activities.BaseActivity;
import com.example.test.spplayer.adapters.TrackAdapter;
import com.example.test.spplayer.fragments.MainFragment;
import com.example.test.spplayer.fragments.MediaCtrlFragment;
import com.example.test.spplayer.models.Track;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by User on 14-03-2018.
 */

public class ModifiedMainActivity extends BaseActivity {

    private static final String TAG = "ModifiedMainActivity";
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.container)
    View container;
    @BindView(R.id.flController)
    View flController;

    private MainFragment mainFragment;
    private MediaCtrlFragment mediaCtrlFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_main_modified);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .replace(container.getId(), mainFragment = new MainFragment()).commit();
        getSupportFragmentManager().beginTransaction()
                .replace(flController.getId(), mediaCtrlFragment = new MediaCtrlFragment()).commit();
    }

    public void onTrackSelection(Track track){
        if (track == null)
            return;
        if (mediaCtrlFragment != null){
            if(mediaCtrlFragment.isAdded()){
                mediaCtrlFragment.updateCtrl(track);
            }
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    protected View getViewForLayoutAccess() {
        return container;
    }

    @Override
    public Activity getSelf() {
        return this;
    }


    @Override
    protected void serverCallEnd(int id, Call call, Response response) {
        Log.d(TAG, "url queried-" + TAG + ": " + call.request().url().toString());
        Log.d(TAG, "response-" + TAG + ": " + response.body());
        stopProgress();
        if (id == RECENT_TRACKS_CALL) {
            //if (response != null) {
                if (response.isSuccessful()) {
                    if (mainFragment != null) {
                        if (mainFragment.isAdded()) {
                            try {
                                List<Track> tracks = (List<Track>) response.body();
                                for (Track data : tracks) {
                                    data.setViewType(TrackAdapter.DEFAULT);
                                }
                                onTrackSelection(tracks.get(0));
                                mainFragment.changeData(tracks);
                            } catch (Exception e) {
                                e.printStackTrace();
                                setErrMsg("Something went wrong");
                            }
                        }
                    }
                    return;
                }
                setErrMsg("Error code " + response.code());
            //}
        } else super.serverCallEnd(id, call, response);
    }

    @Override
    protected void onGarbageCollection() {

    }

    @Override
    public void serverCallEnd(Object response) {
        if (mainFragment != null) {
            if (mainFragment.isAdded()) {
                try {
                    List<Track> tracks = (List<Track>) response;
                    for (Track data : tracks) {
                        data.setViewType(TrackAdapter.DEFAULT);
                    }
                    onTrackSelection(tracks.get(0));
                    mainFragment.changeData(tracks);
                } catch (Exception e) {
                    e.printStackTrace();
                    setErrMsg("Something went wrong");
                }
            }
        }
    }
}
