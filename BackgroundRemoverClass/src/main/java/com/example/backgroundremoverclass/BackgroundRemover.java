package com.example.backgroundremoverclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.segmentation.Segmentation;
import com.google.mlkit.vision.segmentation.SegmentationMask;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions;


import java.nio.ByteBuffer;
import java.util.List;

public class BackgroundRemover {
    BackgroundListner listner;
    Segmenter segmenter;
    private  Context context;
    public BackgroundRemover(Context context,BackgroundListner listner) {
        SelfieSegmenterOptions options =
                new SelfieSegmenterOptions.Builder()
                        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
                        .build();
       segmenter  = Segmentation.getClient(options);
        this.listner = listner;
        this.context = context;

    }


    public void process(Bitmap imageBitmap,int color){
        final Bitmap copyBitmap =  imageBitmap.copy(Bitmap.Config.ARGB_8888, true);
        InputImage input = InputImage.fromBitmap(imageBitmap, 0);
        segmenter.process(input).addOnSuccessListener(new OnSuccessListener<SegmentationMask>() {
            @Override
            public void onSuccess(SegmentationMask segmentationMask) {
                ByteBuffer buffer = segmentationMask.getBuffer();
                int width = segmentationMask.getWidth();
                int height = segmentationMask.getHeight();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap result=removeBackgroundFromImage(copyBitmap,buffer,height,width,color);
                        listner.onSuccess(result);
                    }
                }).start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listner.onFailure(e);
            }
        });

    }
    private Bitmap removeBackgroundFromImage(Bitmap image, ByteBuffer buffer, int height, int width, int color) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float bgConfidence = (float) ((1.0 - buffer.getFloat()) * 255);
                if (bgConfidence >= 100) {
                    image.setPixel(x, y, color);
                }
            }
        }
        buffer.rewind();
        return image;
    }
    public interface BackgroundListner {
        public void onSuccess(Bitmap bitmap);
        public void onFailure(Exception error);
    }
}
