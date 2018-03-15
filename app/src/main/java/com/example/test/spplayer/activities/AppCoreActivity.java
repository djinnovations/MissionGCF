package com.example.test.spplayer.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.example.test.spplayer.MyApplication;
import com.example.test.spplayer.daggerinjector.DaggerDeps;
import com.example.test.spplayer.daggerinjector.Deps;
import com.example.test.spplayer.interfaces.ProgressUpdateView;
import com.example.test.spplayer.interfaces.SCService;
import com.example.test.spplayer.network.NetworkModule;
import com.example.test.spplayer.uiutils.ColoredSnackbar;
import com.example.test.spplayer.utils.ApiUtils;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 14-03-2018.
 */

public abstract class AppCoreActivity extends AppCompatActivity implements ProgressUpdateView{

    private String TAG = "AppCoreActivity";

    protected ProgressBar progressBar;
    public abstract ProgressBar getProgressBar();
    protected abstract View getViewForLayoutAccess();
    public abstract Activity getSelf();

    private Deps deps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
    }

    public Deps getDeps() {
        return deps;
    }

    public void startProgress() {
        Runnable runnable = new Runnable() {
            public void run() {
                AppCoreActivity.this.progressBar = getProgressBar();
                if (progressBar != null)
                    progressBar.setVisibility(View.VISIBLE);
            }
        };
        MyApplication.getInstance().getUiHandler().post(runnable);
    }

    public void stopProgress() {
        Runnable runnable = new Runnable() {
            public void run() {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
            }
        };
        MyApplication.getInstance().getUiHandler().post(runnable);
    }

    protected SCService getRestCallService(){
        return ApiUtils.getService();
    }

    protected void delegateRetrofitCallback(final int id, Call queryObj){
        if (queryObj != null){
            queryObj.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    serverCallEnd(id, call, response);
                }

                @Override
                public void onFailure(Call call, Throwable throwable) {
                    ColoredSnackbar.alert(Snackbar.make(getViewForLayoutAccess(), "Network Error: " +  throwable.getMessage(), Snackbar.LENGTH_SHORT)).show();
                }
            });
        }
    }


    protected void serverCallEnd(int id, Call call, Response response){

    }

    protected void promptBeforeExit(){
        final Snackbar snackbar = Snackbar.make(getViewForLayoutAccess(), "Sure you want to exit?", Snackbar.LENGTH_LONG);
        snackbar.setAction("Yes", new View.OnClickListener() {
            public void onClick(View v) {
                snackbar.dismiss();
                onExitYesClick();
            }
        });
        ColoredSnackbar.info(snackbar).show();
    }

    protected void onExitYesClick(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }
}
