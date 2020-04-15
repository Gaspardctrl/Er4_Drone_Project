package com.example.admin.VideoDecoding;

import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.twilight.h264.util.FrameUtils;


/*              ------------------- This is not an original creation -----------------------
 *  Our work is a fork from the base made by ... using ... on GitHub at :
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
    }
    @Override
    public void run() {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
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
                                while (avpkt.size > 0) {

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
                                        notifyDroneWithDecodedFrame(0, 0, picture.imageWidth, picture.imageHeight, buffer, 0, picture.imageWidth);
                                    }
                                    avpkt.size -= len;
                                    avpkt.data_offset += len;
                                }
                            } catch (Exception ie) {
                                // Any exception, we should try to proceed reading next packet!
                                log.log(Level.FINEST, "Error decodeing frame", ie);
                            } // try
                        //Thread.sleep(100);
                        } catch (Exception ex) {
                            log.log(Level.FINEST, "Error in decoder initialization", ex);
                        }
                    }while(bRun);
                }
            });
    }


    @Override
    public void finish() {
        bRun =false;
        c.avcodec_close();
    }


    @Override
    public void notifyDroneWithDecodedFrame(int startX, int startY, int width, int height, int[] rgbArray, int offset, int scansize) {

       MainDrone.ShowBitmap(startX, startY, width, height, rgbArray, offset, scansize);

    }

}
