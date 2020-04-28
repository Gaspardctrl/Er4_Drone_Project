package com.example.admin.VideoDecoding;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.example.admin.pilotage.MainActivityPilotage;
import com.twilight.h264.decoder.AVFrame;
import com.twilight.h264.decoder.AVPacket;
import com.twilight.h264.decoder.H264Decoder;
import com.twilight.h264.decoder.MpegEncContext;
import com.twilight.h264.util.Frame;
import com.twilight.h264.util.FrameUtils;


/*              ------------------- This is not an original creation -----------------------
 *  Our work is a fork from the base made by https://github.com/AutonomyLab/ardrone_autonomy/ on GitHub
 *  Modified by Gaspard MISERY and Kelian SERMET to be receive bitmaps frames without using parrot SDK
 */


public class ARDrone20VideoDataDecoder extends VideoDataDecoder {
    /**PAVE**/
    int[] signature = new int[4];
    int version;
    int video_codec;
    int header_size;
    int payload_size;
    int encoded_stream_width;
    int encoded_stream_height;
    public int display_width;
    public int display_height;
    int frame_number;
    int timestamp;
    int total_chunks;
    int chunk_index;
    int frame_type;

    boolean bRun =true;

    /****/
    Logger  log = Logger.getLogger("DECODER");

    public static final int INBUF_SIZE = 255535;

    H264Decoder codec;
    MpegEncContext c = null;

    int len;
    int[] got_picture = new int[1];

    AVFrame picture;

    byte[] inbuf = new byte[INBUF_SIZE + MpegEncContext.FF_INPUT_BUFFER_PADDING_SIZE];
    int[] inbuf_int = new int[INBUF_SIZE + MpegEncContext.FF_INPUT_BUFFER_PADDING_SIZE];
    private int[] buffer = null;

    InputStream fin;
    MainActivityPilotage MainDrone;

    AVPacket avpkt;

    int dataPointer;


    public ARDrone20VideoDataDecoder(MainActivityPilotage main, InputStream in) {
        this.MainDrone = main;
        this.fin = in;

        avpkt = new AVPacket();
        avpkt.av_init_packet();

        Arrays.fill(inbuf, INBUF_SIZE, MpegEncContext.FF_INPUT_BUFFER_PADDING_SIZE + INBUF_SIZE, (byte)0);

        codec = new H264Decoder();
        if (codec == null) {
            System.out.println("codec not found\n");
            System.exit(1);
        }

        c = MpegEncContext.avcodec_alloc_context();
        picture= AVFrame.avcodec_alloc_frame();

        if((codec.capabilities & H264Decoder.CODEC_CAP_TRUNCATED)!=0)
            c.flags |= MpegEncContext.CODEC_FLAG_TRUNCATED; /* we do not send complete frames */

        if (c.avcodec_open(codec) < 0) {
            System.out.println("could not open codec\n");
            System.exit(1);
        }
        Thread.currentThread().setName("ArDrone20VideoDataDecoder");
    }
    Bitmap bitmapResized;
    @Override
    public Void doInBackground(Void... params) {
                    do {
                        do {
                            try {
                                signature[0] = fin.read();
                                signature[1] = fin.read();
                                signature[2] = fin.read();
                                signature[3] = fin.read();
                                while (!(signature[0] == 'P' && signature[1] == 'a' && signature[2] == 'V' && signature[3] == 'E')) {
                                    signature[0] = signature[1];
                                    signature[1] = signature[2];
                                    signature[2] = signature[3];
                                    signature[2] = fin.read();
                                }
                                version = fin.read();
                                video_codec = fin.read(); //codec == 4 -> H264 (MPEG4 AVC)
                                header_size = fin.read() | fin.read() << 8;
                                payload_size = fin.read() | fin.read() << 8 | fin.read() << 16 | fin.read() << 24;
                                encoded_stream_width = fin.read() | fin.read() << 8;
                                encoded_stream_height = fin.read() | fin.read() << 8;
                                display_width = fin.read() | fin.read() << 8;
                                if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                                    Log.e("LOOPER UI","ARDrone20vodepDataDecoder is in main looper");
                                }
                                display_height = fin.read() | fin.read() << 8;
                                frame_number = fin.read() + fin.read() << 8 | fin.read() << 16 | fin.read() << 24;
                                timestamp = fin.read() + fin.read() << 8 | fin.read() << 16 | fin.read() << 24;
                                total_chunks = fin.read();
                                chunk_index = fin.read();
                                frame_type = fin.read();
                                for (int i = 0; i < header_size - 31; i++) {
                                    int iDebug = fin.read();
                                }
                            } catch (Exception ex) {
                                log.log(Level.FINEST, "Error in decoder initialization", ex);
                            }
                        } while (frame_type == 0);

                        try {

                            int[] cacheRead = new int[4];
                            // 4 first bytes always indicate NAL header
                            inbuf_int[0] = inbuf_int[1] = inbuf_int[2] = 0x00;
                            inbuf_int[3] = 0x00;
                            inbuf_int[4] = 0x01;
                            dataPointer = 5;
                            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                                Log.e("LOOPER UI","ARDrone20vodepDataDecoder is in main looper");
                            }
                            cacheRead[0] = fin.read();
                            cacheRead[1] = fin.read();
                            cacheRead[2] = fin.read();
                            cacheRead[3] = fin.read();
                            for (int i = 0; i < payload_size - 4; i++) {
                                inbuf_int[dataPointer++] = fin.read();
                                //Log.e("H264","skip");
                            }
                            avpkt.size = dataPointer;
                            // Log.e("h264","Sended");
                            avpkt.data_base = inbuf_int;
                            avpkt.data_offset = 0;

                            try {
                                bitmapResized = null;
                                while (avpkt.size > 0) {
                                    if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                                        Log.e("LOOPER UI","ARDrone20vodepDataDecoder is in main looper");
                                    }
                                    len = c.avcodec_decode_video2(picture, got_picture, avpkt);
                                    if (len < 0) {
                                        //
                                        //System.out.println("Error while decoding frame " + len);
                                        // Discard current packet and proceed to next packet
                                        break;
                                    } // if
                                    if (got_picture[0] != 0) {
                                        picture = c.priv_data.displayPicture;
                                        Log.e("H264", "WORKEDDDD");
                                        int bufferSize = picture.imageWidth * picture.imageHeight;
                                        if (buffer == null || bufferSize != buffer.length) {
                                            buffer = new int[bufferSize];
                                        }
                                        FrameUtils.YUV2RGB(picture, buffer);
                                        Bitmap bitmap = Bitmap.createBitmap( buffer, picture.imageWidth,picture.imageHeight, Bitmap.Config.RGB_565);

                                        /*
                                        * Partie détection de visages :
                                        * Placée ici pour limiter les passages d'images (bitmap) volumineuses
                                        */

                                        float eyesDistance;
                                        Canvas canvas;
                                        Paint myPaint, paint, paint2;
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

//                                        DisplayMetrics metrics = new DisplayMetrics();
                                        //getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                                        int screenHeight = metrics.heightPixels;
//                                        int screenWidth = metrics.widthPixels;

                                        FaceDetector myFaceDetect;
                                        FaceDetector.Face[] myFace;
                                        int imageWidth, imageHeight;
                                        int numberOfFaceDetected;
                                        //int iDotPosX = 3,iDotPosY = 3;
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

                                        //DrewCanvas = mFaceDetection.GetDrawedCanvas(bitmap,screenWidth,screenHeight);
                                        //if(bitmap != null) {
                                        //mPreview.invalidate();
                                        bitmapResized = Bitmap.createScaledBitmap(bitmap,
                                                (int) (bitmap.getWidth() * 0.5), (int) (bitmap.getHeight() * 0.5), false);

                                        //mPreview.setImageBitmap(mutable_);
                                        publishProgress(bitmapResized);

                                        //notifyDroneWithDecodedFrame(0, 0, picture.imageWidth, picture.imageHeight, buffer, 0, picture.imageWidth);
                                   }
                                    avpkt.size -= len;
                                    avpkt.data_offset += len;
                                }
                            } catch (Exception ie) {
                                // Any exception, we should try to proceed reading next packet!
                                log.log(Level.FINEST, "Error decodeing frame", ie);
                            } // try
                        Thread.sleep(1000);
                        } catch (Exception ex) {
                            log.log(Level.FINEST, "Error in decoder initialization", ex);
                        }
                    }while(bRun);
        return null;
    }


    @Override
    protected void onProgressUpdate(Bitmap... bitmapResized) {
        super.onProgressUpdate(bitmapResized);
        Message message;
        message=Message.obtain();
        message.obj=bitmapResized[0];
        MainDrone.handler.sendMessage(message);
    }

//    @Override
//    public void finish() {
//        bRun =false;
//        c.avcodec_close();
//    }


//    @Override
//    public void notifyDroneWithDecodedFrame(int startX, int startY, int width, int height, int[] rgbArray, int offset, int scansize) {
//
//       MainDrone.ShowBitmap(startX, startY, width, height, rgbArray, offset, scansize);
//
//    }

}
