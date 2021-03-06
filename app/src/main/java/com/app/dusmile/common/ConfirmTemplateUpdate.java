package com.app.dusmile.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.model.ChangeTemplateUpdateStatus;
import com.app.dusmile.model.JobDetailsResponse;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by suma on 27/03/17.
 */

public class ConfirmTemplateUpdate {
    private static String Tag = "ConfirmTemplateUpdate";
    private static Gson gson;
    public static void sendTemplateUpdateStatusToserver(Context context, String updatedTemplateList, String templetName, ProgressDialog updateTemplateProgressBar)
    {

        //IOUtils.startLoading(context, "Updating template... Please wait.....");
        IOUtils.appendLog(Tag+": API "+ new Const().REQUEST_CONFIRM_TEMPLATE_UPDATE );
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray temp = new JSONArray();
            temp.put(templetName);
            jsonObject.put("updatedTemplate", temp);
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CONFIRM_TEMPLATE_UPDATE + "\nREQUEST" + jsonObject.toString());
        new HttpVolleyRequest(context, jsonObject, new Const().REQUEST_CONFIRM_TEMPLATE_UPDATE+"/"+updatedTemplateList, listenerUpdateTemplateStatus);
        Log.i(Tag,new Const().REQUEST_CONFIRM_TEMPLATE_UPDATE);
    }



    static MyListener listenerUpdateTemplateStatus = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {
            if (obj != null) {
                String updateTemplateResponse = obj.toString();
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CONFIRM_TEMPLATE_UPDATE + "\nREQUEST" + obj.toString());
                try {
                    gson = new Gson();
                    ChangeTemplateUpdateStatus changeTemplateUpdateStatus = gson.fromJson(updateTemplateResponse,ChangeTemplateUpdateStatus.class);
                    if(changeTemplateUpdateStatus.getSuccess()==true)
                    {
                        MyDynamicToast.successMessage(DusmileApplication.getAppContext(),changeTemplateUpdateStatus.getDetails());
                    }
                    else
                    {

                    }
                } catch (Exception e) {
                  e.printStackTrace();
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void failure(VolleyError volleyError) {
            try {
                if (volleyError != null) {
                    IOUtils.appendLog(Tag+": API "+new Const().REQUEST_CONFIRM_TEMPLATE_UPDATE  +"\nRESPONSE Failed "+volleyError.getMessage().toString());
                    MyDynamicToast.warningMessage(DusmileApplication.getAppContext(), "Unable to connect");
                } else {
                    MyDynamicToast.errorMessage(DusmileApplication.getAppContext(),"Server Error !!");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                MyDynamicToast.errorMessage(DusmileApplication.getAppContext(),"Server Error !!");
            }

        }
    };
}
