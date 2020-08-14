package com.app.dusmile.interfaces;

import android.graphics.Bitmap;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by suma on 03/03/17.
 */

public interface PDFUploadListener {
    public void uploadPdf(File file);

    public void uploadPDF(Map.Entry<File, List<String>> fileListEntry);
}
