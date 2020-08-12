package com.app.dusmile.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.DBModel.AssignedJobsStatus;
import com.app.dusmile.R;
import com.app.dusmile.adapter.PendingPdfListAdapter;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.AssignedJobsStatusDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.interfaces.PDFUploadListener;
import com.app.dusmile.model.SaveSubmitJobResponseModel;
import com.app.dusmile.pdfupload.UploadImage;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.utils.RecyclerItemClickListener;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by suma on 03/03/17.
 */

public class PendingPdfUploadFragment extends Fragment {
    private static Context mContext;
    private static RecyclerView recyclerView;
    private static EditText searchEditText;
    private static Button cancelButton;
    private static TextView tv_msg;
    private static LinearLayout parent_layout;
    private static RecyclerView.LayoutManager mLayoutManager;
    private PendingPdfListAdapter pendingPdfListAdapter;
    private HashMap<File, List<String>> filterPdfListMap;
    HashMap<String, ArrayList<Bitmap>> hashMapImages = new HashMap<>();
    HashMap<String, ArrayList<String>> hashMapFilenames = new HashMap<>();
    private static Toast toast;
    private static View toastRoot;
    String jobId, nbfcName, jobtype;
    private File fileToUpload = null;
    private String requestUrl = null;
    private String Tag = "PendingPdfUploadFragment";
    private DBHelper dbHelper;
    public static int PendingCount;
    private HashMap<File, List<String>> pendingPdfListMap = new HashMap<>();

    public PendingPdfUploadFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_pdfupload, container, false);
        this.findViews(rootView);
        Activity activity = this.getActivity();
        mContext = activity;
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        new createPendingPdfFileList().execute();
        this.searchPendingJobUploadListener();
        this.onCancelButtonClicked();
        return rootView;
    }

 /*   @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, 2);
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        StatusReportFragment statusReportFragment = new StatusReportFragment();
        if (statusReportFragment != null && statusReportFragment.isAdded()) {
            FragmentTransaction ft = statusReportFragment.getFragmentManager().beginTransaction();
            ft.remove(statusReportFragment);
            ft.commit();
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void findViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.pdf_recycler_view);
        searchEditText = (EditText) view.findViewById(R.id.searchbox);
        cancelButton = (Button) view.findViewById(R.id.searchBoxCancel);
    }


    public void setAdapter(HashMap<File, List<String>> pendingPdfHashMap, PDFUploadListener pdfUploadListener) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Adding pending pdf file list in adapter");
        pendingPdfListAdapter = new PendingPdfListAdapter(mContext, pendingPdfHashMap, pdfUploadListener);
        pendingPdfListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(pendingPdfListAdapter);
        PendingCount = recyclerView.getAdapter().getItemCount();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

    }

    private void searchPendingJobUploadListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                try {

                    query = query.toString().toLowerCase();

                    filterPdfListMap = new HashMap<File, List<String>>();
                    if (query.length() > 0) {
                        cancelButton.setVisibility(View.VISIBLE);
                        String code = "";
                        for (Map.Entry<File, List<String>> hm : pendingPdfListMap.entrySet()) {
                            try {
                                code = hm.getKey().getName().toString();
                                if (code.toLowerCase().contains(query.toString().toLowerCase())) {
                                    filterPdfListMap.put(hm.getKey(), hm.getValue());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (filterPdfListMap != null) {
                            try {
                                if (filterPdfListMap.size() == 0) {
                                    showAToast("No Matching Records Found");
                                }
                                pendingPdfListAdapter.refresh(filterPdfListMap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        try {
                            cancelButton.setVisibility(View.GONE);
                            if (pendingPdfListMap != null) {
                                if (pendingPdfListMap.size() == 0) {
                                    showAToast("No Data Available");
                                }
                                pendingPdfListAdapter.refresh(pendingPdfListMap);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    private void onCancelButtonClicked() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchEditText.setText("");
                    cancelButton.setVisibility(View.GONE);
                    if (pendingPdfListMap != null) {
                        try {


                            if (pendingPdfListMap.size() == 0) {
                                showAToast("No Data Available");
                            }

                            pendingPdfListAdapter.refresh(pendingPdfListMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAToast(String message) {

        try {
            if (toast != null) {
                toast.cancel();
            }
           /* toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
            TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
            toastMessage.setTextColor(Color.WHITE);
            toastMessage.setBackgroundColor(Color.RED);
            toast.show();*/
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastRoot = inflater.inflate(R.layout.my_toast, null);
            ImageView iv_left_icon = (ImageView) toastRoot.findViewById(R.id.iv_left_icon);
            tv_msg = (TextView) toastRoot.findViewById(R.id.tv_msg);
            parent_layout = (LinearLayout) toastRoot.findViewById(R.id.parent_layout);
            iv_left_icon.setVisibility(View.VISIBLE);
            iv_left_icon.setImageResource(android.R.drawable.ic_dialog_alert);
            tv_msg.setText(message);
            tv_msg.setTextColor(Color.WHITE);
            tv_msg.setTextSize(16);

//        parent_layout.setBackgroundDrawable(createToastBackground(context, parent_layout));
            parent_layout.setBackgroundResource(R.drawable.error_msg_back);

            toast = new Toast(mContext);
            toast.setView(toastRoot);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMapOfPendingPdf() {
        pendingPdfListMap.clear();
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " get hashmap of pending pdf files list");
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername());
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    List<String> imageList = new ArrayList<>();
                    File pdfFile = null;
                    for (File infileFile : file.listFiles()) {
                        String fileName = infileFile.getName();
                        if (fileName.toLowerCase().contains(".pdf".toLowerCase())) {
                            pdfFile = infileFile;
                        } else {
                            imageList.add(fileName.split("_")[0]);
                        }

                    }
                    if (pdfFile != null) {
                        pendingPdfListMap.put(pdfFile, imageList);
                    }
                }

            }
        }
    }

    public class createPendingPdfFileList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  IOUtils.startLoading(getActivity(), "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                getMapOfPendingPdf();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           // IOUtils.stopLoading();
            setAdapter(pendingPdfListMap, pdfUploadListener);
            if (pendingPdfListMap.size() == 0) {
                MyDynamicToast.informationMessage(mContext, "No Data Available");
            }

        }
    }


    PDFUploadListener pdfUploadListener = new PDFUploadListener() {
        @Override
        public void uploadPdf(File file) {

        }

        @Override
        public void uploadPDF(Map.Entry<File, List<String>> fileListEntry) {
            try {
                if (IOUtils.isInternetPresent(mContext)) {
                    if (fileListEntry.getKey().getName().contains("_")) {
                        if (!TextUtils.isEmpty(fileListEntry.getKey().getName().split("_")[1]) && !TextUtils.isEmpty(fileListEntry.getKey().getName().split("_")[2])) {
                            jobId = fileListEntry.getKey().getName().split("_")[2].replaceAll(".pdf", "").trim();
                            nbfcName = fileListEntry.getKey().getName().split("_")[1].replaceAll(".pdf", "").trim();
                        }
                        AssignedJobs assignedJobs = AssignedJobsDB.getSingleSubmittedAssignedJob(dbHelper, jobId, "false");
                        jobtype = assignedJobs.getJob_type();

                        HashSet<String> formSet = new HashSet<>();
                        for (String formName : fileListEntry.getValue()) {
                            formSet.add(formName);
                        }

                        hashMapImages = getAlreadyExistingImagesHashmap(jobId);
                        List<String> imageList = new ArrayList<>();
                        for (Map.Entry<String, ArrayList<Bitmap>> imageListMap : hashMapImages.entrySet()) {
                            imageList.add(imageListMap.getKey());
                        }
                        String imageType = android.text.TextUtils.join(",", imageList);
                        imageType = imageType.replaceAll("Dusmile,", "");
                        imageType = imageType.replace(" ", "");
                        String fileName = "FIPhotos_" + jobId;
                        String query = "typeOfFile=" + fileName + "&" + "folderName=" + jobId;
                        String url = new Const().REQUEST_UPLOAD_PDF + "?" + "&" + query;
                        fileToUpload = fileListEntry.getKey();
                        requestUrl = url;
                        if (assignedJobs.getAssigned_jobId()!=null && assignedJobs.getIs_submit().equals("false") && assignedJobs.getSubmit_json()!=null){
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(assignedJobs.getSubmit_json().toString());
                                submitJobDeails(jsonObject, jobId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else{
                            new doUploadPDFWork(fileListEntry.getKey(), url, getActivity(), jobId, nbfcName, jobtype).execute();
                        }
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "No Internet Available");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public class doUploadPDFWork extends AsyncTask<Void, Void, Boolean> {

        private File file;
        private String url;
        private Context mContext;
        private String jobId;
        private String nbfcName;
        private String jobType;

        public doUploadPDFWork(File file, String url, Context mContext, String jobId, String nbfcName, String jobType) {
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
            IOUtils.startLoading(getActivity(), "Uploading document...");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isImageUploaded = false;
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File RecordingDirectory = new File(sdCard.getAbsolutePath() + "/Dusmile/Audio/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
                File RecordingFile = new File(RecordingDirectory, jobId + ".mp3");

                if (RecordingFile.exists()) {
                    String fileType = "FIRecording_" + jobId;
                    String Rquery = "typeOfFile=" + fileType + "&" + "folderName=" + jobId;
                    String Rurl = new Const().REQUEST_UPLOAD_PDF + "?" + "&" + Rquery;
                    if (IOUtils.isInternetPresent(mContext)) {
                        if (UploadImage.uploadMultipartFile(RecordingFile, Rurl, mContext, jobId, nbfcName, true, jobType, null, getActivity(), getString(R.string.app_name))) {
                            // imageUploaded = 1;
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Recording Uploaded Successfully");
                        } else {
                            //imageUploaded = 2;
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Recording Upload Failed");
                        }
                    } else {
                        //imageUploaded = 3;
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Recording created OFFLINE");
                    }
                }
                isImageUploaded = UploadImage.uploadMultipartFile(file, url, mContext, jobId, nbfcName, false, jobType, "PDF uploaded Successfully", getActivity(), getString(R.string.app_name));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isImageUploaded;
        }

        @Override
        protected void onPostExecute(Boolean isImageUploaded) {
            super.onPostExecute(isImageUploaded);
            //IOUtils.stopLoading();
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
            if (isImageUploaded) {
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Uploading pending pdf file JobID = " + jobId + " :Success");
                if (pendingPdfListMap.containsKey(file)) {
                    pendingPdfListMap.remove(file);
                }
                onJobSubmittedRefresh();
               // IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), "PDF uploaded Successfully");
            } else {
                IOUtils.stopLoading();
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Uploading pending pdf file JobID = " + jobId + " :Failed");
                showRetryUploadMessage(mContext, "PDF upload Failed", file, url, jobId, nbfcName, jobType);
            }

        }
    }


    public void showRetryUploadMessage(final Context mContext, String message, final File file, final String url, final String jobId, final String nbfcName, final String jobType) {
        try {
            if (mContext != null) {
                new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(message)
                        .setConfirmText("Retry")
                        .setCancelText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                new doUploadPDFWork(file, url, mContext, jobId, nbfcName, jobType).execute();

                            }
                        })
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatusOfJob() {
        //get record of this form id from db if exist then update time else insert new entry of this form
        //getcurrent timestamp
        try {
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " Updating Job status");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            Date currentDate = new Date();
            String currentTimeStamp = sdf.format(currentDate);
            AssignedJobsStatus assignedJobsStatus = AssignedJobsStatusDB.getAllAssignedJobsStatusByJsonTemplateIdAnndJobId(dbHelper, jobId);
            if (assignedJobsStatus.getID() == null) {
                assignedJobsStatus = new AssignedJobsStatus();
                assignedJobsStatus.setForm_data_update_time(currentTimeStamp);
                assignedJobsStatus.setJob_id(jobId);
                // assignedJobsStatus.setJson_template_id(jsonTemplateId);
                AssignedJobsStatusDB.addAssignedJobsStatusEntry(assignedJobsStatus, dbHelper);
            } else {
                assignedJobsStatus.setForm_data_update_time(currentTimeStamp);
                AssignedJobsStatusDB.updateAssignedJobsStatus(assignedJobsStatus, dbHelper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void submitJobDeails(JSONObject jsonObject, String jobId) {
        IOUtils.startLoading(getActivity(), "Please wait...");
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId);
        new HttpVolleyRequest(DusmileApplication.getAppContext(), jsonObject, new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId, listenerSubmitJobDetails);
    }

    MyListener listenerSubmitJobDetails = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {
            try {
             //   IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    Gson gson = new Gson();
                    JSONObject responseJson = new JSONObject(response);
                    if (responseJson.length() > 0) {
                        SaveSubmitJobResponseModel saveSubmitJobResponseModel = gson.fromJson(response, SaveSubmitJobResponseModel.class);
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId + "STATUS " + saveSubmitJobResponseModel.getSuccess());
                        Boolean success = saveSubmitJobResponseModel.getSuccess();
                        if (success == true) {
                           // MyDynamicToast.successMessage(mContext, saveSubmitJobResponseModel.getMessage());
                            String jobID = saveSubmitJobResponseModel.getJOBID();
                            String nbfcname = saveSubmitJobResponseModel.getNBFCNAME();
                           /* AssignedJobsDB.removeJobByJobId(dbHelper, jobId);
                            AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobId);*/
                            if (hashMapImages.size() > 0) {
                                new doUploadPDFWork(fileToUpload, requestUrl, getActivity(), jobId, nbfcName, jobtype).execute();
                            } else {
                                try {
                                    AssignedJobsDB.removeJobByJobId(dbHelper, jobId);
                                    AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobId);
                                    UpdateJobStatus.jobIdList.add(jobId);
                                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId + "Images are not present. Jobs info deleted from Assigned Job table");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                           // MyDynamicToast.successMessage(mContext, saveSubmitJobResponseModel.getMessage());
                        } else if (saveSubmitJobResponseModel.getStatusMsg() != null && saveSubmitJobResponseModel.getSuccess() == false && saveSubmitJobResponseModel.getRedirect() == false && !TextUtils.isEmpty(saveSubmitJobResponseModel.getStatusMsg())) {
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId + "Job is on Hold. Deleting entries");
                            AssignedJobsDB.removeJobByJobId(dbHelper, jobId);
                            AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobId);
                            File sdCard = Environment.getExternalStorageDirectory();
                            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobId);
                            if (directory.isDirectory()) {

                                String[] children = directory.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(directory, children[i]).delete();
                                }
                                directory.delete();
                            }
                            if (pendingPdfListMap.containsKey(fileToUpload)) {
                                pendingPdfListMap.remove(fileToUpload);
                            }
                           /* pendingPdfListAdapter.notifyDataSetChanged();
                            recyclerView.getAdapter().notifyDataSetChanged();*/
                            onJobSubmittedRefresh();
                        }else {
                            AssignedJobsDB.removeJobByJobId(dbHelper, jobId);
                            AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobId);
                            UpdateJobStatus.jobIdList.add(jobId);
                            File sdCard = Environment.getExternalStorageDirectory();
                            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobId);
                            if (directory.isDirectory()) {
                                String[] children = directory.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(directory, children[i]).delete();
                                }
                                directory.delete();
                            }
                            File directory1 = new File(sdCard.getAbsolutePath() + "/Dusmile/Audio/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobId);
                            if (directory1.isDirectory()) {

                                String[] children = directory1.list();
                                for (int i = 0; i < children.length; i++) {
                                    new File(directory1, children[i]).delete();
                                }
                                directory1.delete();
                            }
                            IOUtils.stopUpdateStatusLoading();
                            IOUtils.stopLoading();

                            IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getMessage(), getActivity());
                            IOUtils.showSubmitMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getMessage(), getActivity());
                           // MyDynamicToast.errorMessage(mContext, saveSubmitJobResponseModel.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                IOUtils.stopLoading();
                IOUtils.stopUpdateStatusLoading();
                e.printStackTrace();
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        AppConstant.isPendingPdfVisible = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConstant.isPendingPdfVisible = false;
    }


    public void onJobSubmittedRefresh() {
        new createPendingPdfFileList().execute();
    }

    private HashMap<String, ArrayList<Bitmap>> getAlreadyExistingImagesHashmap(String jobId) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Get already existing images from folder");
        int imageUploaded = 0;   //pdf not created = 0, uploaded successfully = 1, not uploaded = 2
        try {
            hashMapImages.clear();
            hashMapFilenames.clear();
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
            if (directory.isDirectory()) {
                int i = 0;
                for (File c : directory.listFiles()) {
                    String fileName = c.getName();
                    String[] fileName_arr = fileName.split("_");
                    String selectedForm = fileName_arr[0];
                    // int selectedPosition = Integer.parseInt(fileName_arr[1]);
                    //  String lastPos = fileName_arr[2].substring(0, fileName_arr[2].lastIndexOf("."));
                    // int addPosition = Integer.parseInt(lastPos);
                    Bitmap decoded = BitmapFactory.decodeFile(c.getPath());

                    if (hashMapImages.containsKey(selectedForm)) {
                        ArrayList<Bitmap> arrlBmp = hashMapImages.get(selectedForm);
                        ArrayList<String> fileNameList = hashMapFilenames.get(selectedForm);
                        arrlBmp.add(decoded);
                        fileNameList.add(fileName.split("\\.")[0]);
                        hashMapImages.put(selectedForm, arrlBmp);
                        hashMapFilenames.put(selectedForm, fileNameList);
                    } else {
                        ArrayList<Bitmap> arrayListbmp = new ArrayList<>();
                        ArrayList<String> fileNameList = new ArrayList<>();
                        arrayListbmp.add(decoded);
                        fileNameList.add(fileName.split("\\.")[0]);
                        hashMapImages.put(selectedForm, arrayListbmp);
                        hashMapFilenames.put(selectedForm, fileNameList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Exception occured while getting already existing images from folder " + e.getMessage());
        }
        return hashMapImages;
    }
}
