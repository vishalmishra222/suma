package com.app.dusmile.common;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.error.VolleyError;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsStatusDB;
import com.app.dusmile.database.CategoryDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.fragment.PendingPdfUploadFragment;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.example.sumaforms2.AssignedJobs;
import com.example.sumaforms2.AssignedJobsDB;
import com.example.sumaforms2.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by suma on 02/05/17.
 */

public class UpdateJobStatus {
    private static String Tag = "UpdateJobStatus";
    private static String jobID;
    public static ArrayList<String> jobIdList = new ArrayList<>();
    private static DBHelper dbHelper;
    private static Context mContext;
    private static String successMsg;
    private static FragmentActivity mActivity;
    private static String strApp;

    public static void updateJobStatus(JSONObject jsonObject, String jobId, Context context, String message, FragmentActivity activity, String string) {
        jobID = jobId;
        mContext = context;
        successMsg = message;
        mActivity = activity;
        strApp = string;
        IOUtils.startUpdateStatusLoading(DusmileApplication.getAppContext(), "Please wait...");
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_UPDATE_JOB_STATUS + " JOB ID " + jobID);
        new HttpVolleyRequest(DusmileApplication.getAppContext(), new Const().REQUEST_UPDATE_JOB_STATUS + "/" + jobID, listenerUpdateJobStatus);
    }

    static MyListener listenerUpdateJobStatus = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {
            try {
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject responseJsonObject = new JSONObject(response);
                    if (responseJsonObject.length() > 0) {
                        Log.i("JOB STATUS UPDATEDT", response.toString());
                        Log.d(Const.TAG, response);
                        String success = responseJsonObject.getString("success");
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_UPDATE_JOB_STATUS + " JOB ID " + jobID + " STATUS " + success);
                        if (success.equalsIgnoreCase("true")) {
                            File sdCard = Environment.getExternalStorageDirectory();
                            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobID);
                            if (directory != null && directory.isDirectory()) {
                                try {
                                    String[] children = directory.list();
                                    for (int i = 0; i < children.length; i++) {
                                        new File(directory, children[i]).delete();
                                    }
                                    directory.delete();
                                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_UPDATE_JOB_STATUS + " JOB ID " + jobID + " Folder deleted");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            File directory1 = new File(sdCard.getAbsolutePath() + "/Dusmile/Audio/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobID);
                            if (directory1.isDirectory()) {
                                String[] children = directory1.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(directory1, children[i]).delete();
                                }
                                directory1.delete();
                            }
                            DBHelper dbHelper = new DBHelper(DusmileApplication.getAppContext());
                            AssignedJobsDB.removeJobByJobId(dbHelper, jobID);
                            AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobID);
                            jobIdList.add(jobID);
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_UPDATE_JOB_STATUS + " JOB ID " + jobID + " Assigned Job entry deleted");
                            if (AppConstant.isPendingPdfVisible) {
                                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_UPDATE_JOB_STATUS + " JOB ID " + jobID + " Pending pdf list refreshed");
                                new PendingPdfUploadFragment().onJobSubmittedRefresh();
                            }
                            IOUtils.showSubmitMessage(mContext, strApp, successMsg, mActivity);
                            IOUtils.showSuccessMessage(mContext, strApp, "Pdf Uploaded Successfully",mActivity);
                        } else {
                            AssignedJobsDB.removeJobByJobId(dbHelper, jobID);
                            AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobID);
                            UpdateJobStatus.jobIdList.add(jobID);
                            File sdCard = Environment.getExternalStorageDirectory();
                            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobID);
                            if (directory.isDirectory()) {
                                String[] children = directory.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(directory, children[i]).delete();
                                }
                                directory.delete();
                            }
                            File directory1 = new File(sdCard.getAbsolutePath() + "/Dusmile/Audio/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobID);
                            if (directory1.isDirectory()) {

                                String[] children = directory1.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(directory1, children[i]).delete();
                                }
                                directory1.delete();
                            }
                            IOUtils.showSuccessMessage(mContext, strApp, responseJsonObject.getString("message"), mActivity);
                           // MyDynamicToast.successMessage(mContext, responseJsonObject.getString("message"));
                        }
                    }
                    int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(mContext));
                    int categoryId = CategoryDB.getCategoryIdDependsOnLoginJsonID(dbHelper, String.valueOf(loginJsonTemplateId));
                    SubCategoryDB.deleteMenusFromDB(dbHelper, String.valueOf(categoryId), "true");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            IOUtils.stopUpdateStatusLoading();
            IOUtils.stopLoading();
        }

        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            IOUtils.stopUpdateStatusLoading();
            IOUtils.stopLoading();
            if (!IOUtils.isInternetPresent(mContext)) {
                IOUtils.stopLoading();
                IOUtils.showOfflineSuccessMessage(mContext, strApp, "Your device is offline. Pdf is created and saved locally, will upload to server once internet available", mActivity);
            }
        }
    };


    public static JSONObject createUpdateJobStatusJson(String jobId, String nbfc, String status, String jobType) {
        JSONObject jsonObject = new JSONObject();
        try {
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Creating updateJobStatus Json");
            dbHelper = DBHelper.getInstance(DusmileApplication.getAppContext());
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
            if (!TextUtils.isEmpty(assignedJobs.getApplicant_json())) ;
            {
                try {
                    JSONObject applicationJsonObject = new JSONObject(assignedJobs.getApplicant_json());
                    String applicationFormNo = applicationJsonObject.getString("applicationFormNo");
                    jobType = applicationJsonObject.getString("JobType");
                    jsonObject.put("applicationFormNo", applicationFormNo);
                    jsonObject.put("templateName", jobType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            JSONObject criteriaJsonObject = new JSONObject();
            JSONObject jobDataJsonObject = new JSONObject();
            criteriaJsonObject.put("job_id", jobId);
            criteriaJsonObject.put("NBFCName", nbfc);
            criteriaJsonObject.put("templateName", jobType);
            criteriaJsonObject.put("process_queue_id", "102");
            jsonObject.put("jobcriteria", criteriaJsonObject);
            jobDataJsonObject.put("status", status);
            jsonObject.put("jobdata", jobDataJsonObject);
            jsonObject.put("collectionName", nbfc);
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " UpdateJobStatus Json created");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
