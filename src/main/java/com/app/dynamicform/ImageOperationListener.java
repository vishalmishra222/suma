package com.app.dynamicform;

import android.graphics.Bitmap;

public interface ImageOperationListener {
    public void imageDelete(String formName,int formPosition,int imagePosition);
    public void cropImageListener(Bitmap bitmap);
}
