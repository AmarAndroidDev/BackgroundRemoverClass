package com.example.backgroundremoverclass;

import android.graphics.Bitmap;

public interface BackgroundListner {
    public void onSuccess(Bitmap bitmap);
    public void onFailure(Exception error);
}
