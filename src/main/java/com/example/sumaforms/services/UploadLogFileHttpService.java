package com.example.sumaforms.services;

import android.app.IntentService;
import android.content.Intent;

import com.example.sumaforms.preferences.Const;
import com.example.sumaforms.application.DusmileApplication;
import com.example.sumaforms.common.UploadImage;
import com.example.sumaforms.utils.IOUtils;

import java.io.File;

/**
 * Created by suma on 23/03/17.
 */

public class UploadLogFileHttpService extends IntentService {

    private static String TAG = UploadLogFileHttpService.class.getSimpleName();
    public UploadLogFileHttpService() {
        super(UploadLogFileHttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if(IOUtils.isInternetPresent(DusmileApplication.getAppContext())) {
                File logFile = IOUtils.getLogFile();
                boolean isImageUploaded = UploadImage.uploadMultipartFile(logFile, new Const().REQUEST_AUTO_UPLOAD_LOGS, DusmileApplication.getAppContext(), null,null,false,null, null, null, null);
            }
        }
    }
}
