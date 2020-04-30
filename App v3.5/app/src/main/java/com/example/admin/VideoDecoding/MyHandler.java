package com.example.admin.VideoDecoding;


import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.admin.pilotage.MainActivityPilotage;

import java.lang.ref.WeakReference;

public class MyHandler extends Handler {
    private final WeakReference<MainActivityPilotage> mActivity;

    public MyHandler(MainActivityPilotage activity) {
        this.mActivity = new WeakReference<MainActivityPilotage>(activity);
        Log.e("Handler","CREATED");
    }

    @Override
    public void handleMessage(Message msg) {
        MainActivityPilotage activity = mActivity.get();
        if (activity != null) {
            try {

                activity.btm = (Bitmap) msg.obj;
                activity.needtoupdate =true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

