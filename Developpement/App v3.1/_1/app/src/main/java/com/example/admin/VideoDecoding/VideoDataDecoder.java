package com.example.admin.VideoDecoding;


import com.example.admin.pilotage.DroneManager;

public abstract class VideoDataDecoder extends DataDecoder {

    //private ARDrone drone;

    public VideoDataDecoder(){
    }

    public void notifyDroneWithDecodedFrame(int startX, int startY, int width, int height, int[] rgbArray, int offset, int scansize) {
        //drone.videoFrameReceived(startX, startY, width, height, rgbArray, offset, scansize);
    }
}
