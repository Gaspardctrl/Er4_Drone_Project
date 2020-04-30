package com.example.admin.VideoDecoding;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.admin.pilotage.DroneManager;

import java.util.logging.Logger;

public abstract class VideoDataDecoder extends AsyncTask<Void, Bitmap, Void> {

    //private ARDrone drone;
    private Logger log = Logger.getLogger(this.getClass().getName());
    private ARDroneDataReader  datareader;
    private boolean            pauseFlag;
    private boolean bTest;
    public VideoDataDecoder(){
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        // update the UI (this is executed on UI thread)
        super.onPostExecute(aVoid);
    }
}
