package com.example.backgroundremover;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

public interface BackgroundListner {
    public void onSuccess(Bitmap bitmap);
    public void onFailure(Exception error);
}
