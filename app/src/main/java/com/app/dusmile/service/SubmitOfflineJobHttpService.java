package com.app.dusmile.service;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.error.VolleyError;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.database.AssignedJobsStatusDB;
import com.app.dusmile.model.SaveSubmitJobResponseModel;
import com.app.dusmile.pdfupload.UploadImage;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.example.sumaforms2.AssignedJobs;
import com.example.sumaforms2.AssignedJobsDB;
import com.example.sumaforms2.DBHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubmitOfflineJobHttpService extends IntentService {
    private static String Tag = "SubmitOfflineJobHttpService";
    public SubmitOfflineJobHttpService() {
        super(SubmitOfflineJobHttpService.class.getSimpleName());
    }
    private DBHelper dbHelper;
    private File file=null;
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            dbHelper = DBHelper.getInstance(DusmileApplication.getAppContext());
           // getSubmittedJobsAndSendToServer();
            //uploadPDFOneAfterOther(DusmileApplication.getAppContext());
        }
    }

  /*  private void uploadPDFOneAfterOther(Context context) {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf");


            if(directory.isDirectory()){
                for(File file : directory.listFiles())
                {
                    if(file.isDirectory())
                    {
                        List<String> imageList = new ArrayList<>();
                        File pdfFile = null;
                        String jobId ="";
                        for(File infileFile : file.listFiles())
                        {
                            String fileName = infileFile.getName();
                            if(fileName.toLowerCase().contains(".pdf".toLowerCase()))
                            {
                                pdfFile = infileFile;
                                if (pdfFile.getName().contains("_")) {
                                    jobId = pdfFile.getName().split("_")[1].replaceAll(".pdf","").trim();
                                }
                            }
                            else
                            {
                                imageList.add(fileName.split("_")[0]);
                            }

                        }
                        if(pdfFile!=null && imageList.size() > 0  && !TextUtils.isEmpty(jobId))
                        {
                            String imageType = android.text.TextUtils.join(",", imageList);
                            String query = "selectedImageType=" + imageType + "&" + "tempCaseID=" + jobId + "&" + "extension=pdf";
                            String url = Const.REQUEST_UPLOAD_PDF + "?" + query;
                            boolean isImageUploaded = UploadImage.uploadMultipartFile(pdfFile, url, context, jobId);
                        }
                    }

                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }*/

    private void getSubmittedJobsAndSendToServer()
    {
      List<AssignedJobs> getSubmittedJobs = AssignedJobsDB.getAllAssignedSubmittedJobs(dbHelper,"true");
      if(getSubmittedJobs.size()>0)
      {
           for(int i=0;i<getSubmittedJobs.size();i++)
           {
               try {
                   JSONObject jsonObject = new JSONObject(getSubmittedJobs.get(i).getSubmit_json());
                   JSONObject applicantJsonObject = new JSONObject(getSubmittedJobs.get(i).getApplicant_json());
                   Log.i("OFFLINE JSON", jsonObject.toString());
                   String availability  = applicantJsonObject.getString("Availability");
                   if(!TextUtils.isEmpty(availability))
                   {
                       if(!TextUtils.isEmpty(getSubmittedJobs.get(i).getAssigned_jobId()))
                       {
                           if(availability.equalsIgnoreCase("Out Of GEO limit") || !(TextUtils.isEmpty(getSubmittedJobs.get(i).getLatLong())))
                           {
                               submitJobDeails(jsonObject, getSubmittedJobs.get(i).getAssigned_jobId());
                           }
                       }
                   }

               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
      }
    }


    public void submitJobDeails(JSONObject jsonObject, String jobId) {
        new HttpVolleyRequest(DusmileApplication.getAppContext(), jsonObject, new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS+"/"+jobId, listenerSubmitJobDetails);
    }

    MyListener listenerSubmitJobDetails = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {
            try {
                if (obj != null) {
                    String response = obj.toString();
                    Gson gson = new Gson();
                    JSONObject responseJson = new JSONObject(response);
                    if (responseJson.length() > 0) {
                        SaveSubmitJobResponseModel saveSubmitJobResponseModel = gson.fromJson(response, SaveSubmitJobResponseModel.class);
                        if (saveSubmitJobResponseModel.getSuccess() == true && saveSubmitJobResponseModel.getRedirect() == true && jsonObject.toString().contains("\"isSubmit\":true")) {
                            String jobId = saveSubmitJobResponseModel.getJOBID();
                            String nbfcName = saveSubmitJobResponseModel.getNBFCNAME();
                            String jobType = saveSubmitJobResponseModel.getJobTYPE();
                            //create and upload pdf
                            Set<String> imageList = new HashSet<>();
                            imageList = getListOfImages(jobId);
                            String imageType = android.text.TextUtils.join(",", imageList);
                            if(imageList.size()>0)
                            {
                                imageType = imageType.replaceAll(" ","");
                                imageType = imageType.replaceAll(" ","");
                                String query = "typeOfFile=" + imageType + "&" + "folderName=" + jobId;
                                String url = new Const().REQUEST_UPLOAD_PDF + "?" + "&" + query;
                                if(file!=null) {
                                    new doUploadPDFWork(file,url,DusmileApplication.getAppContext(),jobId,nbfcName,jobType).execute();
                                }
                            }
                            else
                            {   try {
                               /* JSONObject updateJobStatusJsonObject = UpdateJobStatus.createUpdateJobStatusJson(jobId, nbfcName, "Completed");
                                UpdateJobStatus.updateJobStatus(updateJobStatusJsonObject,jobId);
                                Log.i("UPdating status","iuhdsihsd");*/
                                AssignedJobsDB.removeJobByJobId(dbHelper,jobId);
                                AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper,jobId);
                                UpdateJobStatus.jobIdList.add(jobId);
                                IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" API "+ new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS+"/"+jobId + "Images are not present. Jobs info deleted from Assigned Job table");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            }
                        } else if (saveSubmitJobResponseModel.getSuccess() == false && saveSubmitJobResponseModel.getRedirect() == false && !TextUtils.isEmpty(saveSubmitJobResponseModel.getStatusMsg())) {
                               String jobId = saveSubmitJobResponseModel.getJOBID();
                               AssignedJobsDB.removeJobByJobId(dbHelper,jobId);
                               AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper,jobId);
                               UpdateJobStatus.jobIdList.add(jobId);
                               File sdCard = Environment.getExternalStorageDirectory();
                               File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/"+ UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername()+"/" + jobId);
                               if (directory.isDirectory()) {
                                String[] children = directory.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(directory, children[i]).delete();
                                }
                                directory.delete();
                                   IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" API "+ new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS+"/"+jobId + "Job is on hold. Deleted job info from table and from folder");

                               }
                        }

                    }

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
               // IOUtils.stopLoading();
                if (volleyError != null) {

                } else {
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    public class doUploadPDFWork extends AsyncTask<Void,Void,Boolean> {

        private File file;
        private String url;
        private Context mContext;
        private String jobId;
        private String nbfcName;
        private String jobType;
        public doUploadPDFWork(File file, String url, Context mContext, String jobId,String nbfcName , String jobType) {
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Uploading pending pdf file JobID = " + jobId);
            this.file = file;
            this.url = url;
            this.mContext = mContext;
            this.jobId = jobId;
            this.nbfcName = nbfcName;
            this.jobType = jobType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isImageUploaded = false;
            try {
                isImageUploaded = UploadImage.uploadMultipartFile(file, url, mContext, jobId,nbfcName,false,jobType, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isImageUploaded;
        }

        @Override
        protected void onPostExecute(Boolean isImageUploaded) {
            super.onPostExecute(isImageUploaded);
        }
    }

    private Set<String> getListOfImages(String jobId)
    {
        Set<String> imageList = new HashSet<>();
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/"+ UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername()+"/" + jobId);
        if (directory.isDirectory()) {

            for(File infileFile : directory.listFiles())
            {
                String fileName = infileFile.getName();
                if(fileName.toLowerCase().contains(".pdf".toLowerCase())) {
                    file = infileFile;
                }
                else
                {
                    imageList.add(fileName.split("_")[0]);
                }
            }
        }
        return imageList;
    }

}
