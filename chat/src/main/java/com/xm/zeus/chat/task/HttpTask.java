package com.xm.zeus.chat.task;


import android.os.Handler;

/**
 *
 */
public class HttpTask extends Task {

    public Handler handler = new Handler();

    public HttpTask(String strTaskName) {
        super(strTaskName);
    }

    public void onSuccess() {
    }


    public void onError(Exception e) {
    }


}
