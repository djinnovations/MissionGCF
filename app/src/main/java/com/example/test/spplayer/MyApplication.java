package com.example.test.spplayer;

import android.app.Application;
import android.os.Handler;

/**
 * Created by User on 14-03-2018.
 */

public class MyApplication extends Application {

    private static MyApplication ourInstance;
    public static MyApplication getInstance(){
        return ourInstance;
    }

    private Handler uiHandler;
    public Handler getUiHandler() {
        return uiHandler;
    }


    //If there is need of interface, this is the one interface to be used all over app
    public interface MenuSelectionListener {
        void onMenuSelected(Object data);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        uiHandler = new Handler();
    }
}
