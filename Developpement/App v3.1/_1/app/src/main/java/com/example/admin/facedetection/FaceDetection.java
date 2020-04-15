package com.example.admin.facedetection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;




/*
HOW TO USE :
@SuppressLint({ "DrawAllocation", "DrawAllocation" })
    private class BitmapView extends View{
    *
        invalidate(); to actualise

        public BitmapView(Context context) {
            super(context);
            this.context = context;
        }
        @Override
        protected void onDraw(Canvas canvas) {
             super.onDraw(canvas);
             canvas = FaceDetection.GetDrawedCanvas(bitmap);
        }
    }
*/
public class FaceDetection {
    public float eyesDistance;
    Canvas canvas;
    Paint myPaint, paint, paint2;
    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(new BitmapView(this));
    public FaceDetection(){
        canvas = new Canvas();
        myPaint = new Paint();
        myPaint.setColor(Color.GREEN);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(7);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint2 = new Paint();
        paint2.setColor(Color.GREEN);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(5);
    }
//    private ArrayList<Bitmap> getFrames() {
//        try {
//            ArrayList<Bitmap> bArray = new ArrayList<Bitmap>();
//            bArray.clear();
//            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
//            mRetriever.setDataSource("/sdcard/video/video.mp4", new HashMap<String, String>());
//            Log.d("Gaspard", "Works");
//
//            for (int i = 0; i < 30; i++) {
//                bArray.add(mRetriever.getFrameAtTime(1000*i, MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
//            }
//            return bArray;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Gaspard", "Didn't Works "+e);
//            return null;
//        }
//    }
//    private void onClick() {
//        new Thread(new Runnable() {
//            private int ICount = 0;
//            private ArrayList<Bitmap> Frames;
//            public void run() {
//                Frames = getFrames();
//                int Size = Frames.size();
//                while(ICount <= Size) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    ICount++;
//                    isBitmap = true;
//                    bitmap = Frames.get(ICount);
//                    bitmapView.postInvalidate();
//                }
//                isBitmap = false;
//            }
//        }).start();
//    }


    private FaceDetector myFaceDetect;
    private FaceDetector.Face[] myFace;
    private int imageWidth, imageHeight;
    private int numberOfFaceDetected;
    int iDotPosX = 3,iDotPosY = 3;


    public Canvas GetDrawedCanvas(Bitmap bitmap,int iwidthPixels, int iheightPixels) {
        imageWidth = bitmap.getWidth();
        imageHeight = bitmap.getHeight();
        float bitmapRatio = (float)imageWidth / (float) imageHeight;
        if (bitmapRatio > 1) {
            imageWidth = 1080;
            imageHeight = (int) (imageWidth / bitmapRatio);
        } else {
           imageHeight = 1920;
           imageWidth = (int) (imageHeight * bitmapRatio);
        }
        bitmap =  Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, true);
        myFace = new FaceDetector.Face[5];
        myFaceDetect = new FaceDetector(imageWidth, imageHeight, 5);
        numberOfFaceDetected = myFaceDetect.findFaces(bitmap, myFace);
        Rect dstRectForRender = new Rect(0,0,imageWidth,imageHeight);
        canvas.drawBitmap(bitmap,  null, dstRectForRender, null);
        for (int count = 0; count < numberOfFaceDetected; count++) {
            FaceDetector.Face face = myFace[count];
            PointF myMidPoint = new PointF();
            face.getMidPoint(myMidPoint);
            eyesDistance = face.eyesDistance();
            iDotPosX = (int)(1/ (imageWidth / myMidPoint.x) * 7);
            iDotPosY = (int)(1/ (imageHeight / myMidPoint.y) * 7);
            for(int i = 0; i < 7; i++ ){
                if(i == iDotPosY){
                    canvas.drawCircle((iwidthPixels/2)+(3*50), (iheightPixels-400-3*32)+(i*32), 15, paint);
                }else {
                    canvas.drawCircle((iwidthPixels/2)+(3*50), (iheightPixels-400-3*32)+(i*32), 10, paint);
                }
            }
            for(int i = 0; i < 7; i++ ){
                if(i == iDotPosX){
                    canvas.drawCircle(iwidthPixels/2+(i*32)-(3*50), iheightPixels-400, 15, paint);
                }else {
                    canvas.drawCircle(iwidthPixels/2+(i*32)-(3*50), iheightPixels-400, 10, paint);
                }
            }
            canvas.drawRect((int) (myMidPoint.x - eyesDistance * 2), (int) (myMidPoint.y - eyesDistance * 2), (int) (myMidPoint.x + eyesDistance * 2), (int) (myMidPoint.y + eyesDistance * 2), myPaint);
        }
        return canvas;
    }
}
