package com.example.test.spplayer.uiutils;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.test.spplayer.MyApplication;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

/**
 * Created by DJphy on 18-12-2016.
 */

public class UiRandomUtils {

    private static UiRandomUtils ourInstance;
    public static UiRandomUtils getInstance(){
        if (ourInstance == null)
            ourInstance = new UiRandomUtils();
        return ourInstance;
    }

    public void startAnim(View view, int animResID){
        try {
            Animation anim = AnimationUtils.loadAnimation(MyApplication.getInstance(), animResID);
            view.startAnimation(anim);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    public void addSnapper(RecyclerView recyclerView, int gravity){
        SnapHelper snapHelper = new GravitySnapHelper(gravity);
        snapHelper.attachToRecyclerView(recyclerView);
    }

    /******************Customizing google login button*******************/
    /*public void setGooglePlusButtonText(String txt, SignInButton button) {
        for (int i = 0; i < button.getChildCount(); i++) {
            View v = button.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTypeface(Typeface.SERIF, Typeface.BOLD);
                tv.setText(txt);
                tv.setGravity(Gravity.CENTER);
                return;
            }
        }
    }*/
}
