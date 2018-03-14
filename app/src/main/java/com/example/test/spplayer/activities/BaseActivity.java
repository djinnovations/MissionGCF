package com.example.test.spplayer.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.example.test.spplayer.utils.ApiUtils;
import com.example.test.spplayer.interfaces.SCService;
import com.example.test.spplayer.uiutils.ColoredSnackbar;
import com.example.test.spplayer.utils.IDUtils;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 14-03-2018.
 */

public abstract class BaseActivity extends AppCoreActivity {

    public void setErrMsgIndefinite(String msg){
        ColoredSnackbar.alert(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_INDEFINITE)).show();
    }

    public void setErrMsg(String msg){
        ColoredSnackbar.alert(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_SHORT)).show();
    }

    public void setErrMsg(String msg, boolean lengthLong) {
        int time = Snackbar.LENGTH_SHORT;
        if (lengthLong)
            time = Snackbar.LENGTH_LONG;
        setErrMsg(msg);
    }

    public void setErrMsg(Object json) {
        String msg = "UNKNOWN_ERR";
        try {
            msg = new JSONObject(json.toString()).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setErrMsg(msg);
    }

    public void setWarningMsg(String msg){
        ColoredSnackbar.warning(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_SHORT)).show();
    }

    public void setInfoMsg(String msg){
        ColoredSnackbar.info(Snackbar.make(getViewForLayoutAccess(), msg, Snackbar.LENGTH_SHORT)).show();
    }


    protected static final int RECENT_TRACKS_CALL = IDUtils.generateViewId();

    public void queryForRecentTracks(String date){
        if (TextUtils.isEmpty(date))
            return;
        startProgress();
        delegateRetrofitCallback(RECENT_TRACKS_CALL, getRestCallService().getRecentTracks(date));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onGarbageCollection();
    }

    protected abstract void onGarbageCollection();

}
