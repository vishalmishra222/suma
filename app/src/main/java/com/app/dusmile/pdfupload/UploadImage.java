package com.app.dusmile.pdfupload;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.model.DocumentUploadResponseModel;
import com.example.sumaforms2.DBHelper;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by suma on 06/03/17.
 */

public class UploadImage {

    public static boolean uploadMultipartFile(final File file, final String requestURL, final Context context, String JobId, String nbfcName, boolean isRecording, String jobType, String message, FragmentActivity activity, String string) {
        Gson gson = new Gson();
        boolean imageUploaded = false;
        DBHelper dbHelper = DBHelper.getInstance(context);
        String charset = "UTF-8";
        //long filesize = file.length();
        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset, context);
            Log.i("Request URL", requestURL);
            // multipart.addHeaderField("Authorization","Bearer "+ UserPreference.getUserRecord(context).getToken());
            multipart.addFilePart("file", file);

            List<String> response = multipart.finish(requestURL);
            Log.i("RESPONSE", response.get(0));
            try {
                if (response != null) {
                    if (JobId != null) {
                        for (String line : response) {
                            DocumentUploadResponseModel documentUploadResponseModel = gson.fromJson(line, DocumentUploadResponseModel.class);
                            if (documentUploadResponseModel.getSuccess() == true) {
                                imageUploaded = true;
                                try {
                                    if (!isRecording) {
                                     /*   int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(context));
                                        int categoryId = CategoryDB.getCategoryIdDependsOnLoginJsonID(dbHelper, String.valueOf(loginJsonTemplateId));
                                        SubCategoryDB.deleteMenusFromDB(dbHelper, String.valueOf(categoryId), "true");*/
                                        JSONObject updateJobJsonObject = UpdateJobStatus.createUpdateJobStatusJson(JobId, nbfcName, "FI Done", jobType);
                                        UpdateJobStatus.updateJobStatus(updateJobJsonObject, JobId, context, message, activity, string);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return imageUploaded;
    }
}
