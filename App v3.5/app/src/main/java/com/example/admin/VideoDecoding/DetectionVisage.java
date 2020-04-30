package com.example.admin.VideoDecoding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;

public class DetectionVisage{
    float eyesDistance;
    Canvas canvas;
    FaceDetector myFaceDetect;
    FaceDetector.Face[] myFace;
    int imageWidth, imageHeight;
    int numberOfFaceDetected;
    Paint myPaint, paint, paint2;
    public DetectionVisage() {
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
    Bitmap PrintBitmap(Bitmap unebitmap){
    //int iDotPosX = 3,iDotPosY = 3;
        Bitmap bitmap = unebitmap;
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
        //Rect dstRectForRender = new Rect(0,0,imageWidth,imageHeight);
        canvas.setBitmap(bitmap);
            for (int count = 0; count < numberOfFaceDetected; count++) {
            FaceDetector.Face face = myFace[count];
            PointF myMidPoint = new PointF();
            face.getMidPoint(myMidPoint);
            eyesDistance = face.eyesDistance();
    //                                            iDotPosX = (int)(1/ (imageWidth / myMidPoint.x) * 7);
    //                                            iDotPosY = (int)(1/ (imageHeight / myMidPoint.y) * 7);
    //                                            for(int i = 0; i < 7; i++ ){
    //                                                if(i == iDotPosY){
    //                                                    canvas.drawCircle((screenWidth/2)+(3*50), (screenHeight-400-3*32)+(i*32), 15, paint);
    //                                                }else {
    //                                                    canvas.drawCircle((screenWidth/2)+(3*50), (screenHeight-400-3*32)+(i*32), 10, paint);
    //                                                }
    //                                            }
    //                                            for(int i = 0; i < 7; i++ ){
    //                                                if(i == iDotPosX){
    //                                                    canvas.drawCircle(screenWidth/2+(i*32)-(3*50), screenHeight-400, 15, paint);
    //                                                }else {
    //                                                    canvas.drawCircle(screenWidth/2+(i*32)-(3*50), screenHeight-400, 10, paint);
    //                                                }
    //                                            }
            canvas.drawRect((int) (myMidPoint.x - eyesDistance * 2), (int) (myMidPoint.y - eyesDistance * 2), (int) (myMidPoint.x + eyesDistance * 2), (int) (myMidPoint.y + eyesDistance * 2), myPaint);
            }
            return bitmap;
        }
}
