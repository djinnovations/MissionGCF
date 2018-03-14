package com.example.test.spplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.test.spplayer.utils.IDUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by User on 15-03-2018.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private final String TAG = "MediaPlayerService";

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            return MediaPlayerService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUpMediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public static final int STATE_PLAYER_PREPARED = IDUtils.generateViewId();
    public static final int STATE_PLAY_COMPLETED = IDUtils.generateViewId();
    public static final int STATE_PLAYER_ERROR = IDUtils.generateViewId();

    public static class MediaProgressData {
        public int state;
        public String infoMsg;

        public MediaProgressData(int state, String infoMsg) {
            this.state = state;
            this.infoMsg = infoMsg;
        }
    }

    private MediaPlayer mMediaPlayer;

    public boolean isMediaPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    private void setUpMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    public void playMedia(String url) throws Exception {
        if (TextUtils.isEmpty(url) || mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(url);
        mMediaPlayer.prepareAsync();
        Log.d(TAG, " playAudio()");
    }

    public void resumeMedia() {
        if (mMediaPlayer != null) {
            try {
                Log.d(TAG, " resumeMedia()");
                if (!mMediaPlayer.isPlaying())
                    mMediaPlayer.start();
            } catch (Exception e) {
                Log.e(TAG, " Err- pauseMedia()");
                e.printStackTrace();
            }
        }
    }

    public void pauseMedia() {
        if (mMediaPlayer != null) {
            try {
                Log.d(TAG, " pauseMedia()");
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.pause();
                //mediaPlayer.release();
            } catch (Exception e) {
                Log.e(TAG, " Err- pauseMedia()");
                e.printStackTrace();
            }
        }
    }

    public void stopMedia() {
        if (mMediaPlayer != null) {
            try {
                Log.d(TAG, " stopMediaPlayer()");
                if (mMediaPlayer.isPlaying())
                    mMediaPlayer.stop();
                mMediaPlayer.reset();
            } catch (Exception e) {
                Log.e(TAG, " Err- stopMediaPlayer()");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (mMediaPlayer != null) {
                try {
                    if (mMediaPlayer.isPlaying())
                        mMediaPlayer.stop();
                    mMediaPlayer.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mMediaPlayer.release();
                mMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        String reason = "Reason: " + what + " & " + extra;
        Log.d(TAG, " onError() radio, " + reason);
        try {
            mediaPlayer.reset();
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new MediaProgressData(STATE_PLAYER_ERROR, reason));
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        //mPlayerControl.setImageResource(android.R.drawable.ic_media_play);
        EventBus.getDefault().post(new MediaProgressData(STATE_PLAY_COMPLETED, ""));
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        EventBus.getDefault().post(new MediaProgressData(STATE_PLAYER_PREPARED, ""));
    }
}
