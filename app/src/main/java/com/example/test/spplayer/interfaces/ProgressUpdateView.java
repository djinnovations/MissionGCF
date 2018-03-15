package com.example.test.spplayer.interfaces;

/**
 * Created by User on 15-03-2018.
 */

public interface ProgressUpdateView {

    void startProgress();

    void stopProgress();

    void setErrMsgIndefinite(String msg);

    void setErrMsg(String msg);

    void setErrMsg(String msg, boolean lengthLong);

    void setErrMsg(Object json);

    void setWarningMsg(String msg);

    void setInfoMsg(String msg);

    void serverCallEnd(Object response);
}
