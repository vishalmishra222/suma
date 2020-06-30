package com.app.dusmile.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.R;
import com.app.dusmile.activity.ChangePasswordActivity;
import com.app.dusmile.activity.JobDetailsActivity;
import com.app.dusmile.activity.JobsActivity;
import com.app.dusmile.activity.ReportActivity;
import com.app.dusmile.activity.ReportDetailsActivity;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.gps.GPSTracker;
//import com.app.dusmile.model.Images;
import com.app.dusmile.model.SaveSubmitJobResponseModel;
import com.app.dusmile.model.TabsDataResponse;

import com.app.dusmile.pdfupload.UploadImage;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.recording.RecordingMainActivity;
import com.app.dusmile.utils.BitmapCompression;
import com.app.dusmile.utils.IOUtils;
import com.example.sumaforms2.BtnClickListener;
import com.example.sumaforms2.ImageOperationListener;
import com.example.sumaforms2.JobDetailsResponse;
import com.example.sumaforms2.SubProcessFieldDataResponse;
import com.example.sumaforms2.UI;
import com.google.gson.Gson;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;


import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;


import cn.pedant.SweetAlert.SweetAlertDialog;
import permission.auron.com.marshmallowpermissionhelper.FragmentManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JobDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JobDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobDetailsFragment extends FragmentManagePermission {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final int CHOOSE_FILE_REQUESTCODE = 101;
    public SubProcessFieldDataResponse subProcessFieldDataResponse;
    // TODO: Rename and change types of parameters
    private String jobId;
    private String nbfcName;
    private String jobType;
    static String imagetype, jobID;
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;
    private LinearLayout llParentLayout;
    private String type;
    List<TabsDataResponse.TemplateMaster> templateMasterList = new ArrayList<>();
    private String selectedForm;
    private int selectedPosition;
    private Context mContext;
    private static final String[] INITIAL_PERMS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final String[] INITIAL_PERMS_CAMERA = {
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int INITIAL_REQUEST_CAMERA = 1339;
    private static final int REQUEST_CAMERA = 0;
    private LinearLayout llGallleryView;
    HashMap<String, ArrayList<Bitmap>> hashMapImages = new HashMap<>();
    //  ArrayList<LoginResponse.Image> img = new ArrayList<>();
    private static File UploadPdfFile;
    private static String JobId;
    private static String uploadPdfUrl;
    private int imageCount = 0;
    private Uri imageUri;
    private String Tag = "JobDetailsFragment";
    private static HashMap<String, String> editTextSparseArray = new HashMap<>();

    private static HashMap<String, String> textViewSparseArray = new HashMap<>();
    private static HashMap<String, String> templateTextViewSparseArray = new HashMap<>();
    private static ArrayList<LinearLayout> arrayListParentLayout = new ArrayList<>();
    private static HashMap<String, EditText> allEdittextsToValidate = new HashMap<>();
    private JobDetailsResponse jobDetailsResponse;
    private ProgressDialog loadDataProgressBar;
    private ProgressDialog getJobDetailsProgressBar;

    public JobDetailsFragment() {
        // Required empty public constr
        // uctor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JobDetailsFragment newInstance(String param1, String param2, String param3) {
        JobDetailsFragment fragment = new JobDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jobId = JobDetailsActivity.job_id;
        nbfcName = JobDetailsActivity.nbfcName;
        jobType = JobDetailsActivity.subCategoryName;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_job_details, container, false);
        llParentLayout = (LinearLayout) v.findViewById(R.id.llParentLayout);
        jobId = JobDetailsActivity.job_id;
        nbfcName = JobDetailsActivity.nbfcName;
        jobType = JobDetailsActivity.subCategoryName;
        mContext = this.getActivity();
        if (AppConstant.formPosition == 0) {
            editTextSparseArray = new HashMap<>();
            textViewSparseArray = new HashMap<>();
            templateTextViewSparseArray = new HashMap<>();
            allEdittextsToValidate = new HashMap<>();
            arrayListParentLayout = new ArrayList<>();
        }
        hashMapImages = new HashMap<>();
        String response = "";
        if (JobDetailsActivity.isAccept == true) {
           IOUtils.startLoading(getActivity(), "Loading form data...");
            response = JobDetailsActivity.response;
            try {
                createUI(response);

                //show personal information and other loan information form in one activity
                // - formPosition for person information is 3. Increment formPostion and render next form in same activity
                if (AppConstant.formPosition == 3) {
                    AppConstant.formPosition++;
                    createUI(response);
                }

            } catch (Exception e) {

            }
            IOUtils.stopLoading();
        } else {
            getJobDeails(jobId, nbfcName, "Assigned");
        }

        return v;
    }


    public void getJobDeails(String jobId, String nbfcName, String type) {
        IOUtils.startLoading(getActivity(), "Retrieving Job Details...");

        //{"criteria":{"job_id":"10037","NBFCName":"HeroFincorp"},"type":"Assigned"}
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("job_id", jobId);
            jsonObject1.put("NBFCName", nbfcName);
            jsonObject.put("type", type);
            jsonObject.put("criteria", jsonObject1);

        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        IOUtils.appendLog(Tag + " API " + new Const().REQUEST_JOB_DETAILS + "\nREQUEST" + jsonObject.toString());
        new HttpVolleyRequest(getActivity(), jsonObject, new Const().REQUEST_JOB_DETAILS, listenerJobDetails);
    }

    MyListener listenerJobDetails = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {

            if (obj != null) {
                String response = obj.toString();
                Log.d(Const.TAG, response);
                try {
                    createUI(response);
                    //show personal information and other loan information form in one activity
                    // - formPosition for person information is 3. Increment formPostion and render next form in same activity
                    if (AppConstant.formPosition == 3) {
                        AppConstant.formPosition++;
                        createUI(response);
                    }

                } catch (Exception e) {

                }
            }
            IOUtils.stopLoading();
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    Toast.makeText(mContext, "Unable to connect", Toast.LENGTH_LONG).show();
                    if (volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
                    }
                } else {
                    Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createUI(String response) {

        Gson gson = new Gson();
        jobDetailsResponse = gson.fromJson(response.trim(), JobDetailsResponse.class);
        TabsDataResponse tabsDataResponse = gson.fromJson(jobDetailsResponse.getTabData().trim(), TabsDataResponse.class);
        final String imagesData = UserPreference.getImagesData(mContext);
      /*  Type type = new TypeToken<ArrayList<LoginResponse.Image>>() {
        }.getType();
        img = gson.fromJson(imagesData, type);*/


        //if form is other loan information then do not add tabs
        // because personal information form and other loan information form are in same activity
        if (AppConstant.formPosition != 4) {
            UI.createUIForHeader(getActivity(), llParentLayout, tabsDataResponse.getTemplateMaster().get(0).getTabs().get(0).getName(), "tabs");
        }
        subProcessFieldDataResponse = gson.fromJson(jobDetailsResponse.getSubProcessFieldsData().trim(), SubProcessFieldDataResponse.class);
        //add form headers from subprocesses
        for (int i = 0; i < tabsDataResponse.getTemplateMaster().get(0).getTabs().get(0).getSubProcesses().size(); i++) {

            //check for subProcessname and form headers are equal to add respective data only
            for (int j = 0; j < subProcessFieldDataResponse.getTemplateFields().size(); j++) {

                if (tabsDataResponse.getTemplateMaster().get(0).getTabs().get(0).getSubProcesses().get(i).equalsIgnoreCase(subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessName())) {

                    //create form header(name)
                    //show form data according to activity i == AppConstant.formPosition is condition to check form is for same activity
                    if (i == AppConstant.formPosition) {
                        UI.createUIForHeader(getActivity(), llParentLayout, tabsDataResponse.getTemplateMaster().get(0).getTabs().get(0).getSubProcesses().get(i), "form");

                    }
                    for (int k = 0; k < subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessFields().size(); k++) {
                        if (i == 0 && i == AppConstant.formPosition) {

                            //UI for form information(form information does not contains editText)
                            UI.createHorizontalLayoutAndFields(getActivity(), llParentLayout, subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessFields().get(k), subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessFields().get(k).getKey(), jobDetailsResponse.getDetails().getProcessDataJSON().getApplicant());
                            llParentLayout.setTag(subProcessFieldDataResponse.getTemplateFields().get(j).getControllerName());
                        } else if (k < subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessFields().size() && i == AppConstant.formPosition) {

                            //dropdown default values are not getting from POJO so parse it manually
                            String value = getDefaultValueForDropDown(jobDetailsResponse, j, k);

                            //UI for remaining
                            UI.createHorizontalLayoutAndFields(getActivity(), llParentLayout, subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessFields().get(k), jobDetailsResponse.getDetails().getProcessDataJSON().getApplicant(), value);
                            llParentLayout.setTag(subProcessFieldDataResponse.getTemplateFields().get(j).getControllerName());
                            //If applicant is already has loandetails table create table with existing data dynamically
                            if (k == subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessFields().size() - 1) {
                                if (subProcessFieldDataResponse.getTemplateFields().get(j).getIsTableExist()) {
                                    // UI.crateTableLayoutAndAddButtons(getActivity(), llParentLayout, subProcessFieldDataResponse.getTemplateFields().get(j), jobDetailsResponse.getDetails().getProcessDataJSON().getApplicant().getLoanDetailsTable(), subProcessFieldDataResponse.getTemplateFields().get(j).getSubProcessFields());
                                    AppConstant.tableName = subProcessFieldDataResponse.getTemplateFields().get(j).getTableID();
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        //if form is personal loan information then do not add buttons and layout to arraylist
        // because personal information form and other loan information form are in same activity
        if (AppConstant.formPosition != 3) {
            arrayListParentLayout.add(llParentLayout);
            UI.createButtonsLayout(getActivity(), llParentLayout, btnClickListener);
        }
        UI ui = new UI(getActivity());
    }

    private String getDefaultValueForDropDown(JobDetailsResponse jobDetailsResponse, int j, int k) {
        String value = "";
        try {
            JSONObject jsonObject = new JSONObject(jobDetailsResponse.getSubProcessFieldsData().trim());
            JSONArray templateFieldData = jsonObject.getJSONArray("TemplateFields");
            JSONObject jsonArray = templateFieldData.getJSONObject(j);
            JSONArray subProcessFields = jsonArray.getJSONArray("subProcessFields");
            JSONObject jsonObjectField = subProcessFields.getJSONObject(k);
            value = jsonObjectField.get("value").toString();
        } catch (JSONException e) {
            e.printStackTrace();
            IOUtils.stopLoading();
        }
        return value;
    }


    BtnClickListener btnClickListener = new BtnClickListener() {
        @Override
        public void uploadImageListener() {
            openUplaodImagePopup(getActivity());
        }

        @Override
        public void saveListerners() {
            try {
                UI ui = new UI(getActivity());
                templateTextViewSparseArray.putAll(ui.putAllKeysToSave(subProcessFieldDataResponse.getTemplateFields(), jobDetailsResponse.getDetails().getProcessDataJSON().getApplicant()));
                for (int i = 0; i < arrayListParentLayout.size(); i++) {
                    editTextSparseArray.putAll(ui.findAllEdittexts(arrayListParentLayout.get(i)));
                    textViewSparseArray.putAll(ui.findAllTextViews(arrayListParentLayout.get(i), subProcessFieldDataResponse.getTemplateFields()));
                    templateTextViewSparseArray.putAll(ui.findAllTemplateTextViews(arrayListParentLayout.get(i)));
                    allEdittextsToValidate.putAll(ui.findAllEdittextsToValidate(llParentLayout));
                }
                if (validatePattern(allEdittextsToValidate)) {
                    try {
                        JSONObject jsonObject = createSaveSubmitJobsJson(editTextSparseArray, textViewSparseArray, templateTextViewSparseArray, false, subProcessFieldDataResponse.getTemplateFields());
                        Log.d("jsonObject", jsonObject.toString());
                        saveSubmitJobDeails(jsonObject, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void submitListener() {

            try {
                UI ui = new UI(getActivity());
                //templateTextViewSparseArray.putAll(ui.putAllKeysToSave(subProcessFieldDataResponse.getTemplateFields()));
                for (int i = 0; i < arrayListParentLayout.size(); i++) {
                    editTextSparseArray.putAll(ui.findAllEdittexts(arrayListParentLayout.get(i)));
                    textViewSparseArray.putAll(ui.findAllTextViews(arrayListParentLayout.get(i), subProcessFieldDataResponse.getTemplateFields()));
                    templateTextViewSparseArray.putAll(ui.findAllTemplateTextViews(arrayListParentLayout.get(i)));
                    allEdittextsToValidate.putAll(ui.findAllEdittextsToValidate(llParentLayout));
                }
                if (isMadatoryFieldsEntered(editTextSparseArray) && validatePattern(allEdittextsToValidate)) {
                    String latitude = UserPreference.readString(mContext, UserPreference.LATITUDE, "");
                    String longitude = UserPreference.readString(mContext, UserPreference.LONGITUDE, "");
                    if (!latitude.equalsIgnoreCase("") && !longitude.equalsIgnoreCase("")) {
                        try {
                            JSONObject jsonObject = createSaveSubmitJobsJson(editTextSparseArray, textViewSparseArray, templateTextViewSparseArray, true, subProcessFieldDataResponse.getTemplateFields());
                            saveSubmitJobDeails(jsonObject, true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(mContext, "Please Click on Save Location Button & Retry", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void audioListener() {
            Intent intent = new Intent(getActivity(), RecordingMainActivity.class);
            startActivity(intent);
        }

        @Override
        public void cancelListener() {

            switch (AppConstant.subCategoryName) {

                case "Logout":
                    IOUtils.showLogoutMessage(getActivity(), getResources().getString(R.string.logout_app), getActivity());
                    break;

                case "Available Jobs":
                    AppConstant.isAvilableJobs = true;
                    AppConstant.isAssinedJobs = false;
                    AppConstant.isCompletedJobs = false;
                    startActivity(new Intent(getActivity(), JobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    getActivity().finish();
                    break;
                case "Assigned Jobs":
                    AppConstant.isAvilableJobs = false;
                    AppConstant.isAssinedJobs = true;
                    AppConstant.isCompletedJobs = false;
                    startActivity(new Intent(getActivity(), JobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    getActivity().finish();
                    break;
                case "View Completed Jobs":
                    AppConstant.isAvilableJobs = false;
                    AppConstant.isAssinedJobs = false;
                    AppConstant.isCompletedJobs = true;
                    startActivity(new Intent(getActivity(), JobsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    getActivity().finish();
                    break;
                case "Change Password":
                    startActivity(new Intent(getActivity(), ChangePasswordActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    getActivity().finish();
                    break;
                case "Status Report":
                    startActivity(new Intent(getActivity(), ReportActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    getActivity().finish();
                    break;
                case "Jobs Submitted To Supervisor": //for status report
                    startActivity(new Intent(getActivity(), ReportDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    getActivity().finish();
                    break;
            }
        }

        @Override
        public void clickListener(String val) {

        }

        @Override
        public void buttonListener(int pos) {

        }

        @Override
        public void viewListener(int pos) {

        }

        @Override
        public void sendGeoLocationListener() {
            try {
                checkPermission();
                boolean isGranted = isPermissionsGranted(getActivity(), new String[]{PermissionUtils.Manifest_ACCESS_FINE_LOCATION});
                if (isGranted) {
                    UserPreference userPreference = new UserPreference();
                    GPSTracker gpsTracker = new GPSTracker(mContext);
                    if (gpsTracker.canGetLocation()) {
                        String latitude = String.valueOf(gpsTracker.getLatitude());
                        String longitude = String.valueOf(gpsTracker.getLongitude());
                        userPreference.writeString(mContext, UserPreference.LATITUDE, latitude);
                        userPreference.writeString(mContext, UserPreference.LONGITUDE, longitude);
                        Toast.makeText(mContext, "Your location saved Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        showGPSSettingsAlert();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void holdListener(String date, String reason, String jobID, String nbfcName) {

        }


        @Override
        public void showHoldPopupListener(int pos) {

        }


    };

    //validate pattern
    private boolean validatePattern(HashMap<String, EditText> allEdittextsToValidate) {
        boolean isValid = true;
        if (allEdittextsToValidate.size() > 0) {
            try {
                List<SubProcessFieldDataResponse.TemplateField> templateFieldList = subProcessFieldDataResponse.getTemplateFields();
                for (int i = 0; i < templateFieldList.size(); i++) {
                    for (int j = 0; j < templateFieldList.get(i).getSubProcessFields().size(); j++) {
                        SubProcessFieldDataResponse.SubProcessField subProcessField = templateFieldList.get(i).getSubProcessFields().get(j);
                        if (subProcessField.getValidationPattern() != null && !subProcessField.getValidationPattern().isEmpty()) {
                            if (allEdittextsToValidate.containsKey(subProcessField.getKey())) {
                                EditText editText = allEdittextsToValidate.get(subProcessField.getKey());
                                if (subProcessField.getIsreadonly() != null && !subProcessField.getIsreadonly()) {
                                    Pattern sPattern
                                            = Pattern.compile(subProcessField.getValidationPattern());
                                    String text = editText.getText().toString().trim();
                                    if (!text.isEmpty() && !sPattern.matcher(text).matches()) {
                                        Toast.makeText(getActivity(), subProcessField.getValidationMessage() + " in " + subProcessField.getFieldName(), Toast.LENGTH_SHORT).show();
                                        isValid = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }


    private boolean isMadatoryFieldsEntered(HashMap<String, String> editTextSparseArray) {
        boolean result = true;
        try {
            for (Map.Entry<String, String> mandatoryCheck : editTextSparseArray.entrySet()) {
                if (mandatoryCheck.getKey().contains("/Mandatory")) {
                    Log.i("MYDATA" + mandatoryCheck.getKey(), " " + mandatoryCheck.getValue());
                    String key[] = mandatoryCheck.getKey().split("/");
                    if (mandatoryCheck.getValue().equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), key[0] + " " + "Can not be empty", Toast.LENGTH_SHORT).show();
                        result = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    private void openUplaodImagePopup(final Context mContext) {
        try {


            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_upload_image);
            dialog.setCancelable(false);
            ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
            final EditText dropdownSelectImage = (EditText) dialog.findViewById(R.id.dropdownSelectImage);
            Button btnBrowseImage = (Button) dialog.findViewById(R.id.btnBrowseImage);
            llGallleryView = (LinearLayout) dialog.findViewById(R.id.llGallleryView);
            Button btnUpload = (Button) dialog.findViewById(R.id.btnUpload);
            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

            final ArrayList<String> value_arr = new ArrayList<>();

            imageCount = 0;

          /*  for (int i = 0; i < img.size(); i++) {
                UI.createViewPager(mContext, llGallleryView, i, img.get(i).getImageName());
                value_arr.add(img.get(i).getImageName());

            }*/

            // preview already selected images. get images from sdcard location and show
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + jobId);
            if (directory.isDirectory()) {
                int i = 0;
                for (File c : directory.listFiles()) {
                    String fileName = c.getName();
                    String[] fileName_arr = fileName.split("_");
                    String selectedForm = fileName_arr[0];
                    int selectedPosition = Integer.parseInt(fileName_arr[1]);
                    String lastPos = fileName_arr[2].substring(0, fileName_arr[2].lastIndexOf("."));
                    int addPosition = Integer.parseInt(lastPos);
                    Bitmap decoded = BitmapFactory.decodeFile(c.getPath());
                    UI.setImagesToGallaryView(getActivity(), llGallleryView, selectedPosition, selectedForm, decoded, addPosition, mCallBack);
                }
            }

            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            });
            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hashMapImages.size() > 0) {
                        if (dialog != null) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                                copyDirectoryFromTemp();
                                Toast.makeText(mContext, "Images saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // new doCreatePDFWork().execute();
                    } else {
                        Toast.makeText(mContext, "Please select at least one image", Toast.LENGTH_SHORT).show();
                    }
                }

            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String tempFileUri = BitmapCompression.getDirectory("temp_" + jobId);
                        File source = new File(tempFileUri);

                        if (source.isDirectory()) {
                            String[] children = source.list();
                            for (int i = 0; i < children.length; i++) {
                                new File(source, children[i]).delete();
                            }
                            source.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });
            dropdownSelectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFormSelectionPopup(mContext, dropdownSelectImage, value_arr);
                }
            });
            btnBrowseImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!dropdownSelectImage.getText().toString().equalsIgnoreCase("Select")) {
                        openMenu();

                    } else {
                        Toast.makeText(getActivity(), "Select from dropdown", Toast.LENGTH_LONG).show();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyDirectoryFromTemp() {
        String tempFileUri = BitmapCompression.getDirectory("temp_" + jobId);
        String uploadFileUri = BitmapCompression.getDirectory(jobId);
        File source = new File(tempFileUri);
        File dest = new File(uploadFileUri);
        try {
            FileUtils.copyDirectory(source, dest);
            if (source.isDirectory()) {
                String[] children = source.list();
                for (int i = 0; i < children.length; i++) {
                    new File(source, children[i]).delete();
                }
                source.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMenu() {
        final CharSequence[] items = {getResources().getString(R.string.capture_photo), getResources().getString(R.string.browse_photo)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.uploadPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= Build.VERSION_CODES.M) {
                        if (!(canAccessCamera() && canAccessFiles())) {
                            requestPermissions(INITIAL_PERMS_CAMERA, INITIAL_REQUEST_CAMERA);
                        } else {
                            cameraIntent();
                        }
                    } else {
                        cameraIntent();
                    }
                } else if (item == 1) {
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= Build.VERSION_CODES.M) {
                        if (!canAccessFiles()) {
                            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                        } else {

                            browseFiles("image/*");
                        }
                    } else {
                        browseFiles("image/*");
                    }
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean canAccessCamera() {
        return (hasPermissionCamera(android.Manifest.permission.CAMERA));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasPermissionCamera(String perm) {
        return (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm));
    }

    private boolean canAccessFiles() {
        return (hasPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean hasPermission(String perm, String perm2) {
        return (PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm) && PackageManager.PERMISSION_GRANTED == getActivity().checkSelfPermission(perm2));
    }

    private void openFormSelectionPopup(Context mContext, final EditText dropdownSelectImage, final ArrayList<String> value_arr) {

        try {
            final Dialog dialogImages = new Dialog(mContext);
            dialogImages.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogImages.setContentView(R.layout.popup_dropdown);
            ListView dropdownList = (ListView) dialogImages.findViewById(R.id.dropdownList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.list_dropdown_items, value_arr);
            adapter.notifyDataSetChanged();
            dropdownList.setAdapter(adapter);
            dropdownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedForm = value_arr.get(position);
                    selectedPosition = position;
                    dropdownSelectImage.setText(value_arr.get(position));
                    dialogImages.dismiss();
                }
            });
            dialogImages.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void browseFiles(String minmeType) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(minmeType);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // special intent for Samsung file manager
            Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            // if you want any file type, you can skip next line
            sIntent.putExtra("CONTENT_TYPE", minmeType);
            sIntent.addCategory(Intent.CATEGORY_DEFAULT);

            Intent chooserIntent;
            if (getActivity().getPackageManager().resolveActivity(sIntent, 0) != null) {
                // it is device with samsung file manager
                chooserIntent = Intent.createChooser(sIntent, "Open file");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
            } else {
                chooserIntent = Intent.createChooser(intent, "Open file");
            }

            try {
                startActivityForResult(chooserIntent, CHOOSE_FILE_REQUESTCODE);
            } catch (ActivityNotFoundException ex) {

                Toast.makeText(getActivity(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {

            return;
        }
        switch (requestCode) {
            case CHOOSE_FILE_REQUESTCODE:
                imageUri = data.getData();
                convertAndSaveBitmap();
                break;
            case REQUEST_CAMERA:
                convertAndSaveBitmap();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void convertAndSaveBitmap() {
        try {
            imageCount++;
            Bitmap decoded = BitmapCompression.bitmapCompressor(getActivity(), imageUri);

            UI.createCropImageDialog(mContext, decoded, mCallBack);

        } catch (Exception e) {
            Log.e("Gallery", "Error while creating temp file", e);
        }
    }


    private void setCropImageToGallery(Bitmap bitmap) {

        try {

            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + jobId);
            if (directory.isDirectory()) {
                int i = 0;
                for (File c : directory.listFiles()) {
                    String fileName = c.getName();
                    String[] fileName_arr = fileName.split("_");
                    String selectedForm = fileName_arr[0];
                    int selectedPosition = Integer.parseInt(fileName_arr[1]);
                    String lastPos = fileName_arr[2].substring(0, fileName_arr[2].lastIndexOf("."));
                    int addPosition = Integer.parseInt(lastPos);
                    Bitmap decoded = BitmapFactory.decodeFile(c.getPath());

                    if (hashMapImages.containsKey(selectedForm)) {
                        ArrayList<Bitmap> arrlBmp = hashMapImages.get(selectedForm);
                        arrlBmp.add(decoded);
                        hashMapImages.put(selectedForm, arrlBmp);
                    } else {
                        ArrayList<Bitmap> arrayListbmp = new ArrayList<>();
                        arrayListbmp.add(decoded);
                        hashMapImages.put(selectedForm, arrayListbmp);
                    }
                }
            }

            int addPosition = 0;
            if (hashMapImages.containsKey(selectedForm)) {
                addPosition = hashMapImages.get(selectedForm).size() + 1;
            } else {
                addPosition = 1;
            }
            FileOutputStream out = null;

            String new_filename = selectedForm + "_" + selectedPosition + "_" + addPosition + ".jpg";
            String current_filename = BitmapCompression.getFilename(new_filename, "temp_" + jobId);
            out = new FileOutputStream(current_filename);
//          write the compressed bitmap at the destination specified by filename.
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out);

            Bitmap decoded = BitmapCompression.bitmapCompressor(getActivity(), current_filename);

            if (hashMapImages.containsKey(selectedForm)) {
                ArrayList<Bitmap> arrlBmp = hashMapImages.get(selectedForm);
                arrlBmp.add(decoded);
                hashMapImages.put(selectedForm, arrlBmp);
            } else {
                ArrayList<Bitmap> arrayListbmp = new ArrayList<>();
                arrayListbmp.add(decoded);
                hashMapImages.put(selectedForm, arrayListbmp);
            }

            UI.setImagesToGallaryView(getActivity(), llGallleryView, selectedPosition, selectedForm, decoded, addPosition, mCallBack);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ImageOperationListener mCallBack = new ImageOperationListener() {
        @Override
        public void imageDelete(String formName, int formPosition, int imagePosition) {
            try {
                int position = imagePosition - 1;
                File sdCard = Environment.getExternalStorageDirectory();
                File file = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + jobId + "/" + formName + "_" + formPosition + "_" + imagePosition + ".jpg");
                if (file.exists()) {
                    file.delete();
                }

                if (hashMapImages.containsKey(formName)) {
                    ArrayList<Bitmap> arrlBmp = hashMapImages.remove(formName);
                    arrlBmp.remove(position);
                    if (arrlBmp.size() > 0) {
                        hashMapImages.put(formName, arrlBmp);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void cropImageListener(Bitmap bitmap) {
            setCropImageToGallery(bitmap);
        }
    };


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {

                case INITIAL_REQUEST:
                    if (canAccessFiles()) {
                        browseFiles("image/*");
                    } else {
                        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
                    }
                    break;
                case INITIAL_REQUEST_CAMERA:
                    if (canAccessCamera() && canAccessFiles()) {
                        cameraIntent();
                    } else {
                        requestPermissions(INITIAL_PERMS_CAMERA, INITIAL_REQUEST_CAMERA);
                    }
                    break;
            }
        } else {
            Log.e("Permission", "Denied");

        }
    }

    private void cameraIntent() {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                imageUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                imageUri = getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * 'http://developer.android.com/training/basics/fragments/communicating.html'
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void saveSubmitJobDeails(JSONObject jsonObject, boolean isSubmit) {
        IOUtils.startLoading(getActivity(), "Please wait...");
        /*if(isSubmit) {
            GPSTracker gpsTracker = new GPSTracker(mContext);
            if (gpsTracker.canGetLocation()) {
                String latitude = String.valueOf(gpsTracker.getLatitude());
                String longitude = String.valueOf(gpsTracker.getLongitude());
                String query = null;
                try {
                    query = URLEncoder.encode("latlong="+latitude+","+longitude+"&jobID="+jobId+"&client=Sumasoft", "utf-8");
                    new HttpVolleyRequest(getActivity(), jsonObject, Const.REQUEST_SAVE_SUBMIT_JOB_DETAILS+"?"+query, listenerSaveSubmitJobDetails);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    IOUtils.stopLoading();
                }

            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                IOUtils.stopLoading();
                showSettingsAlert();
            }

        }
        else
        {*/

        new HttpVolleyRequest(getActivity(), jsonObject, new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS, listenerSaveSubmitJobDetails);
        // }
    }

    MyListener listenerSaveSubmitJobDetails = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    Log.d(Const.TAG, response);
                    Gson gson = new Gson();
                    JSONObject responseJson = new JSONObject(response);
                    if (responseJson.length() > 0) {
                        SaveSubmitJobResponseModel saveSubmitJobResponseModel = gson.fromJson(response, SaveSubmitJobResponseModel.class);
                        if (saveSubmitJobResponseModel.getSuccess() == true) {
                            if (saveSubmitJobResponseModel.getRedirect() == true && jsonObject.toString().contains("\"isSubmit\":true")) {
                                UserPreference.writeString(mContext, UserPreference.LATITUDE, "");
                                UserPreference.writeString(mContext, UserPreference.LONGITUDE, "");
                                IOUtils.showSubmitMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getStatusMsg(), getActivity());

                                //create and upload pdf
                                new doCreatePDFWork().execute();

                            } else if (saveSubmitJobResponseModel.getRedirect() == true && jsonObject.toString().contains("\"isSubmit\":false")) {
                                IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getStatusMsg(), getActivity());
                            }

                        } else {
                            if (jsonObject.toString().contains("\"isSubmit\":true")) {
                                IOUtils.showWarningMessage(mContext, saveSubmitJobResponseModel.getStatusMsg());
                            } else if (jsonObject.toString().contains("\"isSubmit\":false")) {
                                IOUtils.showWarningMessage(mContext, saveSubmitJobResponseModel.getStatusMsg());
                            }

                        }
                    } else {
                        Toast.makeText(mContext, "Unexpected Response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    Toast.makeText(mContext, "Unable to connect", Toast.LENGTH_LONG).show();
                    if (volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
                    }
                } else {
                    Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Server Erorr !!", Toast.LENGTH_LONG).show();
            }
        }
    };

    private JSONObject createSaveSubmitJobsJson(HashMap<String, String> editTextSparseArray, HashMap<String, String> textViewSparseArray, HashMap<String, String> templateTextViewSparseArray, boolean isSubmit, List<SubProcessFieldDataResponse.TemplateField> templateFields) throws JSONException {

        JSONObject jsonObject = null;
        try {
            List<JSONObject> loanDetailsTablesList = new ArrayList<>();
            jsonObject = new JSONObject();
            JSONObject applicantJsonObject = new JSONObject();
            JSONObject jobJsonObject = new JSONObject();
            JSONObject jobCriteriaJsonObject = new JSONObject();
            JSONObject locationJsonObject = new JSONObject();

            if (UserPreference.readString(mContext, UserPreference.LATITUDE, "") != null && UserPreference.readString(mContext, UserPreference.LONGITUDE, "") != null) {
                locationJsonObject.put("latitude", UserPreference.readString(mContext, UserPreference.LATITUDE, ""));
                locationJsonObject.put("longitude", UserPreference.readString(mContext, UserPreference.LONGITUDE, ""));
                jsonObject.put("FI_Location", locationJsonObject);
            }
            jobCriteriaJsonObject.put("job_id", jobId);
            jobCriteriaJsonObject.put("NBFCName", nbfcName);
            jobCriteriaJsonObject.put("process_queue_id", "102");
            String Key = "";
            for (Map.Entry<String, String> entry : editTextSparseArray.entrySet()) {
                if (entry.getKey().contains("/Mandatory")) {
                    Key = entry.getKey().split("/")[0];
                    applicantJsonObject.put(Key, entry.getValue());

                } else {
                    Key = entry.getKey();
                    applicantJsonObject.put(Key, entry.getValue());

                }
            }


            for (int i = 1; i <= UI.addCount; i++) {
                JSONObject loanjsonObject = new JSONObject();
                for (HashMap.Entry<String, String> textViewData : textViewSparseArray.entrySet()) {

                    if (textViewData.getKey().contains("_" + i)) {
                        loanjsonObject.put(textViewData.getKey().split("_")[0], textViewData.getValue());

                    }/*else {
                    if (!applicantJsonObject.has(textViewData.getKey())) {
                        applicantJsonObject.put(textViewData.getKey(), textViewData.getValue());
                    }
                }*/
                }
                if (loanjsonObject.length() > 0) {
                    loanDetailsTablesList.add(loanjsonObject);
                }
            }
            for (HashMap.Entry<String, String> templateTextViewData : templateTextViewSparseArray.entrySet()) {
                String key = templateTextViewData.getKey();
                if (templateTextViewData.getKey().contains("/Mandatory")) {
                    key = templateTextViewData.getKey().split("/")[0];
                } else {
                    key = templateTextViewData.getKey();
                }
                if (!applicantJsonObject.has(key)) {
                    if (key.contains("_")) {
                        if (!IOUtils.containsDigit(key)) {
                            applicantJsonObject.put(key, templateTextViewData.getValue());
                        }
                    } else {
                        applicantJsonObject.put(key, templateTextViewData.getValue());
                    }

                }
            }
            String controllerName = arrayListParentLayout.get(0).getTag().toString();
            jobJsonObject.put(controllerName, applicantJsonObject);
            Log.i("Data", controllerName);

            if (AppConstant.tableName != null) {
                applicantJsonObject.put(AppConstant.tableName, new JSONArray(loanDetailsTablesList));
            }
            jsonObject.put("isSubmit", isSubmit);
            jsonObject.put("jobData", jobJsonObject);
            jsonObject.put("jobCriteria", jobCriteriaJsonObject);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public int createPdf() {
        int imageUploaded = 0;   //pdf not created = 0, uploaded successfully = 1, not uploaded = 2
        try {

            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + jobId);
            if (directory.isDirectory()) {
                int i = 0;
                for (File c : directory.listFiles()) {
                    String fileName = c.getName();
                    String[] fileName_arr = fileName.split("_");
                    String selectedForm = fileName_arr[0];
                    int selectedPosition = Integer.parseInt(fileName_arr[1]);
                    String lastPos = fileName_arr[2].substring(0, fileName_arr[2].lastIndexOf("."));
                    int addPosition = Integer.parseInt(lastPos);
                    Bitmap decoded = BitmapFactory.decodeFile(c.getPath());

                    if (hashMapImages.containsKey(selectedForm)) {
                        ArrayList<Bitmap> arrlBmp = hashMapImages.get(selectedForm);
                        arrlBmp.add(decoded);
                        hashMapImages.put(selectedForm, arrlBmp);
                    } else {
                        ArrayList<Bitmap> arrayListbmp = new ArrayList<>();
                        arrayListbmp.add(decoded);
                        hashMapImages.put(selectedForm, arrayListbmp);
                    }
                }
            }


            if (hashMapImages.size() > 0) {
                PDFBoxResourceLoader.init(getActivity().getApplicationContext());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                final Calendar myCalendar = Calendar.getInstance();
                PDDocument document = new PDDocument();
                // Create a new font object selecting one of the PDF base fonts
                PDType0Font font = null;
                try {
                    font = PDType0Font.load(document, getActivity().getAssets().open("LiberationSans-Regular.ttf"));
                } catch (IOException e) {
                    e.printStackTrace();
                    IOUtils.stopLoading();
                }

                try {


                   /* for (int k = 0; k < img.size(); k++) {
                        if (hashMapImages.containsKey(img.get(k).getImageName())) {
                            ArrayList<Bitmap> arrayListBitmap = hashMapImages.get(img.get(k).getImageName());
                            if (arrayListBitmap != null) {

                                for (int j = 0; j < arrayListBitmap.size(); j++) {
                                    PDPage page = new PDPage();
                                    document.addPage(page);
                                    // Define a content stream for adding to the PDF
                                    PDPageContentStream contentStream = new PDPageContentStream(document, page, false, true);
                                    contentStream.beginText();
                                    contentStream.setNonStrokingColor(15, 38, 192);
                                    contentStream.setFont(font, 12);
                                    contentStream.newLineAtOffset(100, 700);
                                    int imgIndex = j + 1;
                                    contentStream.showText(img.get(k).getImageName() + "(" + imgIndex + ")");
                                    contentStream.endText();
                                    int y = 0;
                                    if (arrayListBitmap.get(j).getHeight() > 650) {
                                        y = arrayListBitmap.get(j).getHeight() - 650;
                                    } else {
                                        y = 650 - arrayListBitmap.get(j).getHeight();
                                    }
                                    PDImageXObject fImage = LosslessFactory.createFromImage(document, arrayListBitmap.get(j));
                                    contentStream.drawImage(fImage, 20, y);
                                    // Make sure that the content stream is closed:
                                    contentStream.close();

                                }
                            }
                        }
                    }*/


                    //create directory if not exist
                    if (!directory.isDirectory()) {
                        directory.mkdirs();
                    }
                    File newFile = new File(directory, "Dusmile_" + jobId + ".pdf");
                    try {
                        newFile.createNewFile();
                    } catch (Exception e) {
                        e.printStackTrace();
                        IOUtils.stopLoading();
                    }
                    String path = newFile.getPath();
                    document.save(path);
                    document.close();
                    //uploadPDF(path,img.get(0).getImageName().concat(sdf.format(myCalendar.getTime())),jobId);
                    if (hashMapImages.size() > 0) {
                        List<String> imageList = new ArrayList<>();
                        for (Map.Entry<String, ArrayList<Bitmap>> imageListMap : hashMapImages.entrySet()) {
                            imageList.add(imageListMap.getKey());
                        }
                        String imageType = android.text.TextUtils.join(",", imageList);
                        imageType = imageType.replaceAll(" ", "");
                        String query = "typeOfFile=" + imageType + "&" + "folderName=" + jobId;
                        String url = new Const().REQUEST_UPLOAD_PDF + "?" + "&" + query;
                        uploadPdfUrl = url;
                        jobID = jobId;
                        UploadPdfFile = newFile;
                        imagetype = imageType;
                        if (UploadImage.uploadMultipartFile(newFile, url, mContext, jobId, nbfcName, false, jobType, null, getActivity(), getString(R.string.app_name))) {
                            imageUploaded = 1;
                        } else {
                            imageUploaded = 2;
                        }
                    }
                    Log.d("is uploaded", imageUploaded + "");

                } catch (IOException e) {
                    e.printStackTrace();
                    IOUtils.stopLoading();
                }
            }
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        return imageUploaded;
    }


    public class doCreatePDFWork extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            IOUtils.startLoading(getActivity(), "Uploading document...");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int imageUploaded = 0;
            try {
                imageUploaded = createPdf();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUploaded;
        }

        @Override
        protected void onPostExecute(Integer imageUploaded) {
            super.onPostExecute(imageUploaded);
            IOUtils.stopLoading();
            if (imageUploaded == 1) {
                IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), "Pdf Uploaded Successfully", getActivity());
            } else if (imageUploaded == 2) {
                showRetryUploadMessage(mContext, "PDF Upload Failed", UploadPdfFile, uploadPdfUrl, getActivity(), jobID);
            }

        }
    }


    public class doUploadRetryPDFWork extends AsyncTask<String, Void, Boolean> {
        private File file;
        private String url;
        private Context mContext;
        private Activity activity;
        private String jobId;

        public doUploadRetryPDFWork(File file, String url, Context mContext, Activity activity, String jobId) {
            this.file = file;
            this.url = url;
            this.mContext = mContext;
            this.activity = activity;
            this.jobId = jobId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            IOUtils.startLoading(getActivity(), "Uploading document...");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean imageUploaded = false;
            try {

                imageUploaded = UploadImage.uploadMultipartFile(file, url, mContext, jobId, nbfcName, false, jobType, null, getActivity(), getString(R.string.app_name));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUploaded;
        }

        @Override
        protected void onPostExecute(Boolean isImageUploaded) {
            super.onPostExecute(isImageUploaded);
            IOUtils.stopLoading();
            if (isImageUploaded) {
                IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), "Pdf Uploaded Successfully", getActivity());
            } else {
                showRetryUploadMessage(mContext, "PDF Upload Failed", file, url, activity, jobId);
            }

        }
    }


    //Check Premission for marshmallow
    public void checkPermission() {
        //String permissionAsk[] = {PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,PermissionUtils.Manifest_READ_CALL_LOG,PermissionUtils.Manifest_WRITE_CALL_LOG,PermissionUtils.Manifest_READ_PHONE_STATE};
        String permissionAsk[] = {PermissionUtils.Manifest_ACCESS_FINE_LOCATION};
        askCompactPermissions(permissionAsk, new PermissionResult() {

            public void permissionGranted() {
            }


            public void permissionDenied() {

            }

            @Override
            public void permissionForeverDenied() {
                {
                    String permissionAsk[] = {PermissionUtils.Manifest_ACCESS_FINE_LOCATION};
                    boolean isGranted = isPermissionsGranted(getActivity(), permissionAsk);
                    if (!isGranted) {
                        // showDialog();
                    } else {
                        System.out.println("IS GRANTED -- " + isGranted);
                    }
                }
            }
        });
    }

    public void showGPSSettingsAlert() {
        if (!((Activity) mContext).isFinishing()) {
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(mContext);
            alertDialog.setCancelable(false);
            // Setting Dialog Title
            alertDialog.setTitle(getString(R.string.app_name));

            // Setting Dialog Message
            alertDialog
                    .setMessage("Not able access your location. GPS need to be ON.");

            // On pressing Settings button
            alertDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 1);
                            //((Activity) mContext).finish();
                            dialog.cancel();
                        }
                    });

           /* // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });*/

            // Showing Alert Message
            alertDialog.show();
        }
    }


    public void showRetryUploadMessage(final Context mContext, String message, final File file, final String url, final Activity activity, final String jobId) {
        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(message)
                .setConfirmText("Retry")
                .setCancelText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        new doUploadRetryPDFWork(file, url, mContext, activity, jobId).execute();

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
}