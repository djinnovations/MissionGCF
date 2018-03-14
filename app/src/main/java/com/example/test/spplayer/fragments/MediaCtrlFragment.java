package com.example.test.spplayer.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.spplayer.MediaPlayerService;
import com.example.test.spplayer.MyApplication;
import com.example.test.spplayer.R;
import com.example.test.spplayer.activities.BaseActivity;
import com.example.test.spplayer.models.Track;
import com.example.test.spplayer.utils.Config;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by User on 15-03-2018.
 */

public class MediaCtrlFragment extends BaseFragment {

    private static final String TAG = "MediaCtrlFragment";
    @BindView(R.id.player_control)
    ImageView player_control;
    @BindView(R.id.player_progress)
    ProgressBar player_progress;
    @BindView(R.id.selected_track_title)
    TextView selected_track_title;
    @BindView(R.id.selected_track_image)
    ImageView selected_track_image;

    Intent mediaServiceIntent;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mediaServiceIntent = new Intent(getActivity(), MediaPlayerService.class);

        if (getActivity() != null)
            getActivity().startService(mediaServiceIntent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_ctrl_media;
    }

    @Override
    protected void onGarbageCollection() {
        mediaServiceIntent = null;
    }

    @Override
    protected String getFragmentTitle() {
        return "";
    }

    public void updateCtrl(Track selectedTrack) {
        if (!TextUtils.isEmpty(selectedTrack.getArtworkURL())) {
            Picasso.with(MyApplication.getInstance())
                    .load(selectedTrack.getArtworkURL())
                        /*.placeholder(R.drawable.vector_icon_profile_white_outline)*/
                    .into(selected_track_image);
        }
        if (!TextUtils.isEmpty(selectedTrack.getTitle())) {
            selected_track_title.setText(selectedTrack.getTitle());
        }
        player_control.setVisibility(View.INVISIBLE);
        player_progress.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(selectedTrack.getStreamURL()))
            return;
        try {
            mBoundService.playMedia(selectedTrack.getStreamURL() + "?client_id=" + Config.CLIENT_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean mShouldUnbind;

    private MediaPlayerService mBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((MediaPlayerService.LocalBinder) service).getService();

            Toast.makeText(MyApplication.getInstance(), "Service connected",
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
            Toast.makeText(MyApplication.getInstance(), "Service Dis-connected",
                    Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        doBindService();
    }

    @Override
    public void onPause() {
        super.onPause();
        doUnbindService();
    }

    void doBindService() {
        if (getActivity() == null)
            return;
        if (getActivity().bindService(mediaServiceIntent, mConnection, Context.BIND_ABOVE_CLIENT)) {
            mShouldUnbind = true;
        } else {
            Log.e(TAG, "Error: The requested service doesn't " +
                    "exist, or this client isn't allowed access to it.");
        }
    }

    void doUnbindService() {
        if (getActivity() == null)
            return;
        if (mShouldUnbind) {
            getActivity().unbindService(mConnection);
            mShouldUnbind = false;
        }
    }

    @Override
    public void onEventReceived(Object event) {
        //super.onEventReceived(event);
        if (event instanceof MediaPlayerService.MediaProgressData) {
            MediaPlayerService.MediaProgressData data = (MediaPlayerService.MediaProgressData) event;
            int state = data.state;
            if (state == MediaPlayerService.STATE_PLAYER_PREPARED) {
                player_progress.setVisibility(View.GONE);
                togglePlayPause();
            } else if (state == MediaPlayerService.STATE_PLAYER_ERROR) {
                if (getActivity() instanceof BaseActivity)
                    ((BaseActivity) getActivity()).setErrMsg(data.infoMsg);
            } else if (state == MediaPlayerService.STATE_PLAY_COMPLETED) {
                player_control.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    @OnClick(R.id.player_control)
    void onPlayClicked() {
        togglePlayPause();
    }

    private void togglePlayPause() {
        player_control.setVisibility(View.VISIBLE);
        if (mBoundService.isMediaPlaying()) {
            mBoundService.pauseMedia();
            player_control.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mBoundService.resumeMedia();
            player_control.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
}
