package com.app.dusmile.interfaces;

import android.graphics.Bitmap;

/**
 * Created by sumasoft on 14/02/17.
 */

public interface ImageOperationListener {
    public void imageDelete(String formName, int formPosition, int imagePosition);
    public void cropImageListener(Bitmap bitmap);
}
