package com.app.dusmile.interfaces;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Date;

/**
 * Created by sumasoft on 03/02/17.
 */

public interface BtnClickListener {
    void uploadImageListener();
    void saveListerners();
    void submitListener();
    void audioListener();
    void cancelListener();
    void clickListener(String val);
    void buttonListener(int pos);
    void viewListener(int pos);
    void sendGeoLocationListener();
    void holdListener(String date, String reason, String jobID, String nbfcName);
    void showHoldPopupListener(int pos);
}
