package com.geii.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity  { ;
    int mWidth,mHeight;
    Bitmap bitmap;
    public float eyesDistance = 80;
    public int x,y;
    Uri imageUri;
    Camera camera;

// Build the camera
    camera = new Camera.Builder()
            .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
            .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new BitmapView(this));
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mWidth= this.getResources().getDisplayMetrics().widthPixels;
        mHeight= this.getResources().getDisplayMetrics().heightPixels;
        x = mWidth/2;
        y = mHeight/2;
    }
    private ArrayList<Bitmap> getFrames(String path) {
        try {
            ArrayList<Bitmap> bArray = new ArrayList<Bitmap>();
            bArray.clear();
            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
            mRetriever.setDataSource("/sdcard/myvideo.mp4");

            for (int i = 0; i < 30; i++) {
                bArray.add(mRetriever.getFrameAtTime(1000*i,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
            }
            return bArray;
        } catch (Exception e) { return null; }
    }
    boolean isBitmap = false;

    //called after camera intent finished
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }


    }

    @SuppressLint({ "DrawAllocation", "DrawAllocation" })
    private class BitmapView extends View{
        private FaceDetector myFaceDetect;
        private FaceDetector.Face[] myFace;
        private int imageWidth, imageHeight;
        private int numberOfFaceDetected;
        private final Context context;
        int iDotPosX = 3,iDotPosY = 3;
        Uri image;
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    String path = String.valueOf(Environment.getExternalStorageDirectory()) + "/your_name_folder";
                    try {
                        File ruta_sd = new File(path);
                        File folder = new File(ruta_sd.getAbsolutePath(), "/your_name_folder");
                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdir();
                        }
                        if (success) {
                            Toast.makeText(MainActivity.this, "Carpeta Creada...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Log.e("Carpetas", "Error al crear Carpeta a tarjeta SD");
                    }

                    Intent i = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();//agregue este
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    File file = new File(Environment.getExternalStorageDirectory(), "/your_name_folder/a" + "/photo_" + timeStamp + ".png");
                    Uri imageUri = Uri.fromFile(file);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
            }
            return true;
        }


        public BitmapView(Context context) {
            super(context);
            this.context = context;

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint myPaint = new Paint();
            myPaint.setColor(Color.GREEN);
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setStrokeWidth(7);

            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);

            Paint paint2 = new Paint();
            paint2.setColor(Color.GREEN);
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setStrokeWidth(5);
            if(isBitmap){
                imageWidth = bitmap.getWidth();
                imageHeight = bitmap.getHeight();
                myFace = new FaceDetector.Face[5];
                myFaceDetect = new FaceDetector(imageWidth, imageHeight, 5);
                numberOfFaceDetected = myFaceDetect.findFaces(bitmap, myFace);
                Rect dstRectForRender = new Rect(0,0,mWidth,mHeight);
                canvas.drawBitmap(bitmap,  null, dstRectForRender, null);
                for (int count = 0; count < numberOfFaceDetected; count++) {
                    FaceDetector.Face face = myFace[count];
                    PointF myMidPoint = new PointF();
                    face.getMidPoint(myMidPoint);
                    eyesDistance = face.eyesDistance();
//                    for(int i = 0; i < 7; i++ ){
//                        if(i == iDotPosY){
//                            canvas.drawCircle((mWidth/2)+(3*50), (mHeight-400-3*32)+(i*32), 15, paint);
//                        }else {
//                            canvas.drawCircle((mWidth/2)+(3*50), (mHeight-400-3*32)+(i*32), 10, paint);
//                        }
//                    }
//                    for(int i = 0; i < 7; i++ ){
//                        if(i == iDotPosX){
//                            canvas.drawCircle(mWidth/2+(i*32)-(3*50), mHeight-400, 15, paint);
//                        }else {
//                            canvas.drawCircle(mWidth/2+(i*32)-(3*50), mHeight-400, 10, paint);
//                        }
//                    }
                    canvas.drawRect((int) (myMidPoint.x - eyesDistance * 2), (int) (myMidPoint.y - eyesDistance * 2), (int) (myMidPoint.x + eyesDistance * 2), (int) (myMidPoint.y + eyesDistance * 2), myPaint);
                }
            }
        }
    }
}
