package com.app.dusmile.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.AssignedJobsStatus;
import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.DBModel.DynamicTableField;
import com.app.dusmile.DBModel.TemplateJson;
import com.app.dusmile.DBModel.UpdatedTemplet;
import com.app.dusmile.R;
import com.app.dusmile.activity.AllFormsActivity;
import com.app.dusmile.activity.UploadActivity;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.common.TemplateOperations;
import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsStatusDB;
import com.app.dusmile.database.CategoryDB;
import com.app.dusmile.database.ClientTemplateDB;
import com.app.dusmile.database.DynamicFieldTableDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.TemplateJsonDB;
import com.app.dusmile.gps.GPSTracker;
import com.app.dusmile.model.SaveSubmitJobResponseModel;
import com.app.dusmile.model.SubProcessFieldDataResponse1;
import com.app.dusmile.model.TabsDataResponse;
import com.app.dusmile.pdfupload.UploadImage;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.recording.RecordingMainActivity;
import com.app.dusmile.utils.BitmapCompression;
import com.app.dusmile.utils.IOUtils;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms2.AssignedJobs;
import com.example.sumaforms2.AssignedJobsDB;
import com.example.sumaforms2.BtnClickListener;
import com.example.sumaforms2.DBHelper;
import com.example.sumaforms2.DatabaseUI;
import com.example.sumaforms2.ImageOperationListener;
import com.example.sumaforms2.JobDetailResponse;
import com.example.sumaforms2.JobDetailsResponse;
import com.example.sumaforms2.SubProcessFieldDataResponse;
import com.example.sumaforms2.UI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.pdmodel.PDPage;
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream;
import com.tom_roush.pdfbox.pdmodel.font.PDType0Font;
import com.tom_roush.pdfbox.pdmodel.graphics.image.JPEGFactory;
import com.tom_roush.pdfbox.pdmodel.graphics.image.PDImageXObject;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import permission.auron.com.marshmallowpermissionhelper.FragmentManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

import static android.app.Activity.RESULT_OK;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

//import com.app.dusmile.model.Images;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllFormsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllFormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFormsFragment extends FragmentManagePermission {

    GoogleMap mGoogleMap;
    MapView mapView;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final int CHOOSE_FILE_REQUESTCODE = 101;
    private String Tag = "AllFormsFragment";
    public SubProcessFieldDataResponse1 subProcessFieldDataResponse;
    // TODO: Rename and change types of parameters
    public static String jobId;
    private String nbfcName, cityName, pinCode, mobileNo, applicationFormNo, tATime, address, jobStartTime, jobType;
    static String imagetype, jobID;
    private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;
    private LinearLayout llParentLayout;
    private String type;
    List<TabsDataResponse.TemplateMaster> templateMasterList = new ArrayList<>();
    private String selectedForm;
    private int selectedPosition;
    private Context mContext;
    private JSONObject jsonObject;
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
    // ArrayList<LoginResponse.Image> img = new ArrayList<>();
    private static File UploadPdfFile;
    private static String JobId;
    private static String uploadPdfUrl;
    private int imageCount = 0;
    private Uri imageUri;
    private static HashMap<String, String> editTextSparseArray = new HashMap<>();
    private static HashMap<String, String> textViewSparseArray = new HashMap<>();
    private static HashMap<String, String> templateTextViewSparseArray = new HashMap<>();
    private static HashMap<String, RadioButton> radioSparseArray = new HashMap<>();
    private static HashMap<String, CheckBox> checkboxSparseArray = new HashMap<>();
    HashMap<String, ArrayList<String>> hashMapFilenames = new HashMap<>();
    private JobDetailResponse jobDetailsResponse;
    private String applicant_Json;
    public int formPosition;
    public String formName;
    private Gson gson;
    private DBHelper dbHelper;
    private String templateName;
    private String creationTime;
    private AwesomeValidation mAwesomeValidation;
    private String applicantJson;
    private String allFieldsKeys;
    private String tableExist;
    private String tableName;
    private String tableHeader;
    private String tableKeys;
    private String tableMandatoryFields;
    private SaveSubmitJobResponseModel saveSubmitJobResponseModel;
    private String clearSubProcessFields;
    private String controllerName;
    private String allMandatoryKeys;
    private String client_template_id;
    private String jsonTemplateId;
    Dialog gpsDialog;

    public AllFormsFragment() {
        // Required empty public constr
        // uctor
    }


    // TODO: Rename and change types and number of parameters
    public static AllFormsFragment newInstance(int formPosition, String formName) {
        AllFormsFragment fragment = new AllFormsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", formPosition);
        bundle.putString("formName", formName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        IOUtils.startLoadingPleaseWait(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        IOUtils.stopLoading();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.formPosition = getArguments().getInt("position");
        this.formName = getArguments().getString("formName");
        jobId = AllFormsActivity.job_id;
        nbfcName = AllFormsActivity.nbfcName;
        jobType = AllFormsActivity.jobType;
        templateName = AllFormsActivity.templateName;
        type = AllFormsActivity.subCategoryName;
        creationTime = AllFormsActivity.creationTime;
        cityName = AllFormsActivity.cityName;
        pinCode = AllFormsActivity.pinCode;
        mobileNo = AllFormsActivity.mobileNo;
        address = AllFormsActivity.address;
        tATime = AllFormsActivity.totalAssignedTime;
        jobStartTime = AllFormsActivity.jobStartTime;
        applicationFormNo = AllFormsActivity.applicationFormNo;
        jobStartTime = AllFormsActivity.jobStartTime;
        setHasOptionsMenu(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IOUtils.startLoading(getActivity(), "Retrieving Job Details...");
        View v = inflater.inflate(R.layout.fragment_job_details, container, false);
        llParentLayout = (LinearLayout) v.findViewById(R.id.llParentLayout);
        mContext = this.getActivity();
        jobId = AllFormsActivity.job_id;
        nbfcName = AllFormsActivity.nbfcName;
        templateName = AllFormsActivity.templateName;
        type = AllFormsActivity.subCategoryName;
        // creationTime = AllFormsActivity.creationTime;
        if (!TextUtils.isEmpty(jobId) && !TextUtils.isEmpty(nbfcName)) {
            getActivity().setTitle(nbfcName + " (" + jobId + ")");
        }
        gson = new Gson();
        dbHelper = DBHelper.getInstance(mContext);

        textViewSparseArray = new HashMap<>();
        editTextSparseArray = new HashMap<>();
        templateTextViewSparseArray = new HashMap<>();
        radioSparseArray = new HashMap<>();
        checkboxSparseArray = new HashMap<>();
        hashMapImages = new HashMap<>();
        String response = "";
        if (AllFormsActivity.isAccept == true) {
            IOUtils.startLoading(getActivity(), "Loading form data...");
            response = AllFormsActivity.response;
            try {
                handleResponseToCheckVersion(response);
            } catch (Exception e) {

            }
            IOUtils.stopLoading();
        } else {
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
            String client_template_id = assignedJobs.getClient_template_id();
            if (!TextUtils.isEmpty(client_template_id)) {
                getTemplateFromDb(UserPreference.getLanguage(mContext), null, "", "");
            } else {
                IOUtils.stopLoading();
                getJobDetails(jobId, nbfcName, "Assigned");
            }
        }

        return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                showJobInfo(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getJobDetails(String jobId, String nbfcName, String type) {
        IOUtils.startLoading(getActivity(), "Retrieving Job Details...");

        try {
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_JOB_DETAILS1 + "/" + jobId);
            new HttpVolleyRequest(getActivity(), new Const().REQUEST_JOB_DETAILS1 + "/" + jobId, listenerJobDetails);
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }

    }

    MyListener listenerJobDetails = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {
            if (obj != null) {
                String response = obj.toString();
                try {
                    handleResponseToCheckVersion(response);
                } catch (Exception e) {

                }
            }
            IOUtils.stopLoading();
        }

        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {


        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    IOUtils.appendLog(Tag + ":" + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_JOB_DETAILS1 + "/" + jobId + "\nRESPONSE " + volleyError.getMessage().toString());
                    // Toast.makeText(mContext, "Unable to connect", Toast.LENGTH_LONG).show();
                    MyDynamicToast.warningMessage(mContext, "Unable to connect");
                    if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }

        }
    };


    public void getUpdatedTemplate(String templateVersionId) {
        IOUtils.startLoading(getActivity(), "Updating template... Please wait.....");
        IOUtils.appendLog(Tag + ": API " + new Const().REQUEST_GET_UPDATED_TEMPLATE + "/" + templateVersionId);
        new HttpVolleyRequest(getActivity(), new Const().REQUEST_GET_UPDATED_TEMPLATE + "/" + templateVersionId, listenerGetUpdatedTemplate);
        Log.i(Tag, new Const().REQUEST_GET_UPDATED_TEMPLATE + "/" + templateVersionId);
    }

    MyListener listenerGetUpdatedTemplate = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {
            if (obj != null) {
                String updateTemplateResponse = obj.toString();
                try {
                    gson = new Gson();
                    UpdatedTemplet updatedTemplet = gson.fromJson(updateTemplateResponse, UpdatedTemplet.class);
                    TemplateOperations.saveUpdatedTemplateInDatabase(updatedTemplet, mContext);
                    String app_language = UserPreference.getLanguage(mContext);
                    getTemplateFromDb(app_language, jobDetailsResponse, "false", applicant_Json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            IOUtils.stopLoading();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    IOUtils.appendLog(Tag + ": API " + new Const().REQUEST_GET_UPDATED_TEMPLATE + "/" + jobType + "\nRESPONSE " + volleyError.getMessage().toString());
                    MyDynamicToast.warningMessage(mContext, "Unable to connect");
                    if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }

        }
    };

    private void handleResponseToCheckVersion(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (response.contains("data")) {
                JSONObject details = jsonObject.getJSONObject("data");
                applicant_Json = details.getString("Applicant");
            }
            jobDetailsResponse = gson.fromJson(response.trim(), JobDetailResponse.class);
            IOUtils.appendLog(Tag + ": API " + new Const().REQUEST_JOB_DETAILS1 + "\nRESPONSE " + jobDetailsResponse.getStatus());
            String response_version_no = jobDetailsResponse.getLatestVersion();
            String app_language = UserPreference.getLanguage(mContext);
            ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateAccVerionNo(dbHelper, templateName, nbfcName, app_language, response_version_no);
            String clientTemplateVersion = clientTemplate.getVersion();
            if (clientTemplate.getID() != null) {
                //do not update template use this template to render form
                getTemplateFromDb(app_language, jobDetailsResponse, clientTemplate.getIs_deprecated(), applicant_Json);

                IOUtils.stopLoading();
            } else {
                getUpdatedTemplate(jobType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getTemplateFromDb(String app_language, JobDetailResponse jobDetailsResponse, String isDeprecatedFlag, String applicant_Json) {
        IOUtils.appendLog(Tag + ": Getting Template from DB");
        AssignedJobs assignedJob = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
        if (AllFormsActivity.isAccept == true || assignedJob.getAssigned_jobId() == null) {
            ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateByTemplateNameClientLanguage(dbHelper, templateName, nbfcName, app_language, isDeprecatedFlag);
            //insert entry into assigned jobs table to maintain assigned jobs
            insertIntoAssignedJobs(clientTemplate.getID(), jobDetailsResponse, applicant_Json);
        }
        //set current job to in progress
        AssignedJobsDB.updateAllJobsStatus(dbHelper, "false");
        AssignedJobsDB.updateJobsStatusByJobId(dbHelper, "true", jobId);

        //get client_tmeplate id of assigned Job
        AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
        client_template_id = assignedJobs.getClient_template_id();
        applicantJson = assignedJobs.getApplicant_json();
        //get template according to forms

        TemplateJson templateJson = TemplateJsonDB.getSingleTemplateJson(dbHelper, client_template_id, formName);
        String tableExist = templateJson.getIs_table_exists();
        String template_json_id = templateJson.getID();
        DynamicTableField dynamicTableField = new DynamicTableField();
        if (!TextUtils.isEmpty(tableExist)) {
            if (tableExist.equalsIgnoreCase("true")) {
                dynamicTableField = DynamicFieldTableDB.getSingleDynamicFieldTable(dbHelper, template_json_id);
            }
        }
        createUIFromDbTemplate(templateJson, dynamicTableField, mContext);
    }


    private void createUIFromDbTemplate(TemplateJson templateJson, DynamicTableField dynamicTableField, Context mContext) {
        IOUtils.appendLog(Tag + ": Rendering form from DB");
        controllerName = templateJson.getController_name();
        jsonTemplateId = templateJson.getID();
        String fieldJson = templateJson.getField_json();
        tableExist = templateJson.getIs_table_exists();
        allFieldsKeys = templateJson.getOther_field_keys();
        allMandatoryKeys = templateJson.getMandatory_field_keys();
        String formName = templateJson.getForm_name();
        DBHelper dbHelper = DBHelper.getInstance(mContext);
        try {
            UpdatedTemplet.SubProcessFieldsDatum templateField = gson.fromJson(fieldJson, UpdatedTemplet.SubProcessFieldsDatum.class);
            JobDetailsResponse.Applicant applicant = gson.fromJson(applicantJson, JobDetailsResponse.Applicant.class);
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
            //ArrayList list = new ArrayList();
            //list.add(templateField);
            List<SubProcessFieldDataResponse.SubProcessField> subProcessFields = templateField.getSubProcessFields();
            mAwesomeValidation = new AwesomeValidation(BASIC);
            DatabaseUI databaseUI = new DatabaseUI(getActivity(), mAwesomeValidation, getActivity(), getChildFragmentManager(), jobId);
            /*ClientTemplate  clientTemplate = ClientTemplateDB.getSingleClientTemplate(dbHelper,templateJson.getClient_template_id());
            if(clientTemplate.getID()!=null) {
                AssignedJobs assignedJobs =  AssignedJobsDB.getSingleAssignedJobs(dbHelper,templateJson.getClient_template_id());
                if(assignedJobs.getAssigned_jobId()!=null) {
                    databaseUI.createUIForHeader(getActivity(), llParentLayout, clientTemplate.getClient_name()+ " "+"("+assignedJobs.getAssigned_jobId()+")", "Job");
                }
            }*/
            databaseUI.createUIForHeader(getActivity(), llParentLayout, formName, "form", applicant.getCustomerName(), "");
            for (int subProcessFieldsCnt = 0; subProcessFieldsCnt < subProcessFields.size(); subProcessFieldsCnt++) {
                if (subProcessFields.get(subProcessFieldsCnt).getFieldName().equalsIgnoreCase("label")) {
                    databaseUI.createHorizontalLayoutAndFields(getActivity(), llParentLayout, subProcessFields.get(subProcessFieldsCnt), subProcessFields.get(subProcessFieldsCnt).getKey(), applicant, assignedJobs.getApplicant_json(), formPosition);
                } else {
                    databaseUI.createHorizontalLayoutAndFields(getActivity(), llParentLayout, subProcessFields.get(subProcessFieldsCnt), applicant, assignedJobs.getApplicant_json(), formPosition);
                }
                if (subProcessFieldsCnt == subProcessFields.size() - 1) {
                    if (tableExist.equalsIgnoreCase("true")) {
                        tableName = dynamicTableField.getTable_name();
                        tableHeader = dynamicTableField.getTable_headers_array();
                        if (tableHeader.contains("[")) {
                            tableHeader = tableHeader.replace("[", "");
                        }
                        if (tableHeader.contains("]")) {
                            tableHeader = tableHeader.replace("]", "");
                        }
                        tableKeys = dynamicTableField.getTable_keys_array();
                        if (tableKeys.contains("[")) {
                            tableKeys = tableKeys.replace("[", "");
                        }
                        if (tableKeys.contains("]")) {
                            tableKeys = tableKeys.replace("]", "");
                        }
                        tableMandatoryFields = String.valueOf(dynamicTableField.getMandatory_table_fields());
                        if (tableMandatoryFields.contains("[")) {
                            tableMandatoryFields = tableMandatoryFields.replace("[", "");
                        }
                        if (tableMandatoryFields.contains("]")) {
                            tableMandatoryFields = tableMandatoryFields.replace("]", "");
                        }
                        clearSubProcessFields = dynamicTableField.getClear_subprocess_fields();
                        if (clearSubProcessFields.contains("[")) {
                            clearSubProcessFields = clearSubProcessFields.replace("[", "");
                        }
                        if (clearSubProcessFields.contains("]")) {
                            clearSubProcessFields = clearSubProcessFields.replace("]", "");
                        }

                        List<String> tableHeaderList = new ArrayList<String>(Arrays.asList(tableHeader.split(",")));
                        List<String> tableKeysList = new ArrayList<String>(Arrays.asList(tableKeys.split(",")));
                        List<String> tableMandatoryFieldsList = new ArrayList<String>(Arrays.asList(tableMandatoryFields.split(",")));
                        List<String> clearSubProcessFieldsList = new ArrayList<String>(Arrays.asList(clearSubProcessFields.split(",")));
                        if (tableName.equalsIgnoreCase("VehicleDetailsTable")) {
                            // databaseUI.crateTableLayoutAndAddButtonsVehicle(getActivity(), llParentLayout, tableName, tableHeaderList, tableKeysList, tableMandatoryFieldsList, clearSubProcessFieldsList, applicant.getVehicleDetailsTable());
                        } else if (tableName.equalsIgnoreCase("AssetsTable")) {
                            // databaseUI.crateTableLayoutAndAddButtonsAssets(getActivity(), llParentLayout, tableName, tableHeaderList, tableKeysList, tableMandatoryFieldsList, clearSubProcessFieldsList, applicant.getAssetsTable());

                        } else {
                            //databaseUI.crateTableLayoutAndAddButtonsLoan(getActivity(), llParentLayout, tableName, tableHeaderList, tableKeysList, tableMandatoryFieldsList, clearSubProcessFieldsList, applicant.getLoanDetailsTable());

                        }
                    }
                }
            }
            databaseUI.createButtonsLayout(getActivity(), llParentLayout, btnClickListener, formPosition, null);

        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.appendLog(Tag + ": Exception occured " + e.getMessage());
        }

    }

    private void insertIntoAssignedJobs(String client_template_id, JobDetailResponse jobDetailsResponse, String applicantJson) {

        IOUtils.appendLog(Tag + ": Inserting entries into assigned job");

        //do not add entry if its already present in db
        AssignedJobs assignedJobsExisting = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
        if (assignedJobsExisting.getID() == null) {
            JobDetailResponse.Data details = jobDetailsResponse.getData();
            //JobDetailsResponse.ProcessDataJSON processDataJSON = details.getProcessDataJSON();
            JobDetailsResponse.Applicant applicant = details.getApplicant();
            //String applicantJson = gson.toJson(applicant);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Date oldDate = null;
//             String endTime = null;
//            try {
//            oldDate = sdf.parse(creationTime);
//            Date newDate = DateUtils.addHours(oldDate, 3);
//            endTime = sdf.format(newDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            GPSTracker gpsTracker = new GPSTracker(mContext);
            String latitude = String.valueOf(gpsTracker.getLatitude());
            String longitude = String.valueOf(gpsTracker.getLongitude());
            AssignedJobs assignedJobs = new AssignedJobs();

            assignedJobs.setClient_template_id(client_template_id);
            assignedJobs.setAssigned_jobId(jobId);
            assignedJobs.setOriginal_applicant_json(applicantJson);
            assignedJobs.setApplicant_json(applicantJson);
            assignedJobs.setFIlat(latitude);
            assignedJobs.setFILon(longitude);
            //assignedJobs.setJob_end_time(endTime);
            assignedJobs.setIS_IN_PROGRESS("true");
            assignedJobs.setIs_submit("false");
            assignedJobs.setJob_type(jobType);
            AssignedJobsDB.addAssignedJobsEntry(assignedJobs, dbHelper);
        }
    }

    BtnClickListener btnClickListener = new BtnClickListener() {
        @Override
        public void uploadImageListener() {
            // openUplaodImagePopup(getActivity());
            Intent i = new Intent(mContext, UploadActivity.class);
            startActivity(i);
        }

        @Override
        public void saveListerners() {
            GPSTracker gpsTracker = new GPSTracker(mContext);
            if (gpsTracker.canGetLocation()) {
                boolean validate = false;
                if (mAwesomeValidation != null) {
                    validate = mAwesomeValidation.validate();
                }
                try {
                    String allFieldsKeysArray[] = allFieldsKeys.split(",");
                    DatabaseUI ui = new DatabaseUI(getActivity(), mAwesomeValidation, getActivity(), getChildFragmentManager(), jobId);
                    JobDetailsResponse.Applicant applicant = gson.fromJson(applicantJson, JobDetailsResponse.Applicant.class);
                    DBHelper dbHelper = DBHelper.getInstance(mContext);
                    AssignedJobs assignedJobsExisting = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
                    String applicant_json = assignedJobsExisting.getApplicant_json();
                    templateTextViewSparseArray.clear();
                    textViewSparseArray.clear();
                    editTextSparseArray.clear();
                    radioSparseArray.clear();
                    checkboxSparseArray.clear();
                    templateTextViewSparseArray.putAll(ui.putAllKeysToSave(allFieldsKeysArray, applicant, applicant_json));
                    textViewSparseArray.putAll(ui.findAllTextViews(llParentLayout, tableExist, tableKeys));
                    editTextSparseArray = ui.findAllEdittexts(llParentLayout);
                    templateTextViewSparseArray = ui.findAllTemplateTextViews(llParentLayout);
                    radioSparseArray = ui.findCustomValidateRadioButtons(llParentLayout);
                    checkboxSparseArray = ui.findAllCheckBox(llParentLayout);
                    if (validate) {
                        try {
                            JSONObject jsonObjectApplicant = createApplicant();
                            Log.d("jsonObject", jsonObjectApplicant.toString());
                            AssignedJobsDB.updateApplicant(dbHelper, jobId, jsonObjectApplicant.toString(), "false");
                            updateStatusOfJob();
                            //Toast.makeText(mContext,"Successfully saved !!!",Toast.LENGTH_LONG).show();
                            MyDynamicToast.successMessage(mContext, "Successfully saved !!!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "Please Switch On Location!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void submitListener() {
            try {
                boolean validate = false;
                if (mAwesomeValidation != null) {
                    validate = mAwesomeValidation.validate();
                }
                String allFieldsKeysArray[] = allFieldsKeys.split(",");
                DatabaseUI ui = new DatabaseUI(getActivity(), mAwesomeValidation, getActivity(), getChildFragmentManager(), jobId);
                //delete old data in case user has selected
                AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
                if (AppConstant.isRedRadioButtonClicked) {
                    IOUtils.deleteImagesFolder(jobId);
                    if (!TextUtils.isEmpty(assignedJobs.getOriginal_applicant_json())) {
                        AssignedJobsDB.updateApplicant(dbHelper, jobId, assignedJobs.getOriginal_applicant_json(), "false");
                        updateStatusOfJob();
                        assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
                    }
                }
                applicantJson = assignedJobs.getApplicant_json();
                HashMap<String, ArrayList<Bitmap>> hashMapImages = new HashMap<>();
                hashMapImages = getAlreadyExistingImagesHashmap(jobId);
                radioSparseArray.clear();
                checkboxSparseArray.clear();
                templateTextViewSparseArray.clear();
                textViewSparseArray.clear();
                editTextSparseArray.clear();
                JobDetailsResponse.Applicant applicant = gson.fromJson(applicantJson, JobDetailsResponse.Applicant.class);
                templateTextViewSparseArray.putAll(ui.putAllKeysToSave(allFieldsKeysArray, applicant, applicantJson));
                textViewSparseArray.putAll(ui.findAllTextViews(llParentLayout, tableExist, tableHeader));
                editTextSparseArray = ui.findAllEdittexts(llParentLayout);
                templateTextViewSparseArray = ui.findAllTemplateTextViews(llParentLayout);
                radioSparseArray = ui.findCustomValidateRadioButtons(llParentLayout);
                checkboxSparseArray = ui.findAllCheckBox(llParentLayout);
                if (validate) {

                    try {
                        String latlong = assignedJobs.getLatLong();
                        JSONObject jsonObjectApplicant = createApplicant();
                       /* if (AppConstant.isRedRadioButtonClicked) {
                            jsonObjectApplicant.put("FI_Recommendation", "Negative");
                        }*/
                        String availability = "Out Of GEO limit";
                        if (jsonObjectApplicant.has("Availability")) {
                            availability = jsonObjectApplicant.getString("Availability");
                        }
                        if (!TextUtils.isEmpty(latlong) && availability.equalsIgnoreCase("Out Of GEO limit")) {
                            Log.d("jsonObject", jsonObjectApplicant.toString());
                            //String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                            String recordingDirectory = sdcardPath + "/Dusmile/Audio/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId;
                            File dir = new File(recordingDirectory);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            //File file = new File(dir,filename);
                            String filePath = recordingDirectory + "/" + jobId + ".mp3";
                            boolean check = new File(filePath).exists();
                            if (hashMapImages.size() != 0) {
                                if (check == true) {
                                    if (AppConstant.isRedRadioButtonClicked || validateFromApplicant(jsonObjectApplicant)) {
                                        jsonObject = createSaveSubmitJobsJson(jsonObjectApplicant, true);
                             /*           AssignedJobsDB.updateApplicant(dbHelper, jobId, jsonObjectApplicant.toString(), "true");
                                        updateStatusOfJob();*/
                                        AssignedJobsDB.updateSubmitJson(dbHelper, jobId, jsonObject.toString(), "false");
                                        new CreatePdf().execute();
                                    }
                                } else {
                                    MyDynamicToast.informationMessage(mContext, "Please Save your Recording & Retry");
                                }
                            } else {
                                MyDynamicToast.informationMessage(mContext, "Please Save Atleast one photo");
                            }

                        } else {
                            //Toast.makeText(mContext, "Please Click on Save Location Button & Retry", Toast.LENGTH_SHORT).show();
                            MyDynamicToast.informationMessage(mContext, "Please Save your location & Retry");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void cancelListener() {
            getActivity().finish();
        }

        @Override
        public void audioListener() {
            Intent intent = new Intent(getActivity(), RecordingMainActivity.class);
            intent.putExtra("recording", jobId);
            startActivity(intent);
            //   Intent intent1 = new Intent(getActivity(), AMRAudioRecorder.class);

           /* Toast.makeText(mContext, "This is my Toast message!",
                    Toast.LENGTH_LONG).show();*/
         /*   FragmentManager fragment = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragment.beginTransaction();
            RecordingMainActivity recording = new RecordingMainActivity();
            fragmentTransaction.replace(R.id.llParentLayout, recording);
            fragmentTransaction.commit();*/
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
                        // Toast.makeText(mContext, "Your location saved Successfully", Toast.LENGTH_SHORT).show();
                        MyDynamicToast.successMessage(mContext, "Your location saved Successfully");
                    } else {
                        showGPSSettingsAlert();
                    }
                }
                IOUtils.appendLog(Tag + ": Saved user location successfully");
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

    private boolean validateFromApplicant(JSONObject jsonObjectApplicant) {
        boolean isMaditory = true;
        ArrayList<TemplateJson> allMandatoryFieldsArrayList = TemplateJsonDB.getAllTemplateJsonBtClientTemplateId(dbHelper, client_template_id);
        for (int i = 0; i < allMandatoryFieldsArrayList.size(); i++) {
            TemplateJson templateJson = allMandatoryFieldsArrayList.get(i);
            String formName = templateJson.getForm_name();
            String allMandatoryFields = templateJson.getMandatory_field_keys();
            if (!allMandatoryFields.isEmpty()) {
                String allMandatoryFieldsArray[] = allMandatoryFields.split(",");
                for (int j = 0; j < allMandatoryFieldsArray.length; j++) {
                    try {
                        if (jsonObjectApplicant.has(allMandatoryFieldsArray[j])) {
                            String value = jsonObjectApplicant.getString(allMandatoryFieldsArray[j]);
                            if (value == null || value.isEmpty() || value.equalsIgnoreCase("")) {
                                isMaditory = false;
                                //Toast.makeText(mContext, allMandatoryFieldsArray[j] + " is Mandatory in "+formName, Toast.LENGTH_LONG).show();
                                MyDynamicToast.errorMessage(mContext, allMandatoryFieldsArray[j] + " is Mandatory in " + formName);
                                break;
                            }
                        }
                        /*else
                        {
                            isMaditory = false;
                            MyDynamicToast.errorMessage(mContext, allMandatoryFieldsArray[j] + " is Mandatory in " + formName);
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
        return isMaditory;
    }

    private JSONObject createApplicant() {
        JSONObject applicantJsonObject = null;
        List<JSONObject> loanDetailsTablesList = new ArrayList<>();
        try {
            applicantJsonObject = new JSONObject(applicantJson);
            for (Map.Entry<String, String> entry : editTextSparseArray.entrySet()) {
                String Key;
                if (entry.getKey().contains("/Mandatory")) {
                    Key = entry.getKey().split("/")[0];
                    applicantJsonObject.put(Key, entry.getValue());

                } else {
                    Key = entry.getKey();
                    applicantJsonObject.put(Key, entry.getValue());

                }
            }

            for (Map.Entry<String, RadioButton> entry : radioSparseArray.entrySet()) {
                String Key;
                if (entry.getKey().contains("/Mandatory")) {
                    Key = entry.getKey().split("/")[0];
                    applicantJsonObject.put(Key, entry.getValue().getText().toString());

                } else {
                    Key = entry.getKey();
                    applicantJsonObject.put(Key, entry.getValue().getText().toString());

                }
            }

            for (Map.Entry<String, CheckBox> entry : checkboxSparseArray.entrySet()) {
                String Key;
                try {

                    if (entry.getKey().contains("/true")) {
                        Key = entry.getKey().split("/")[0];
                        applicantJsonObject.put(Key, entry.getKey().split("/")[1]);

                    } else {
                        Key = entry.getKey();
                        applicantJsonObject.put(Key, entry.getKey().split("/")[1]);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (HashMap.Entry<String, String> templateTextViewData : templateTextViewSparseArray.entrySet()) {
                String key = templateTextViewData.getKey();
                if (key.contains("/Mandatory")) {
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
            if (tableExist.equalsIgnoreCase("true")) {
                for (int i = 1; i <= DatabaseUI.addCount; i++) {
                    JSONObject loanjsonObject = new JSONObject();
                    for (HashMap.Entry<String, String> textViewData : textViewSparseArray.entrySet()) {

                        if (textViewData.getKey().contains("_" + i)) {
                            loanjsonObject.put(textViewData.getKey().split("_")[0], textViewData.getValue());

                        }
                    }
                    if (loanjsonObject.length() > 0) {
                        loanDetailsTablesList.add(loanjsonObject);
                    }
                }
                applicantJsonObject.put(tableName, new JSONArray(loanDetailsTablesList));
            }

            GPSTracker gpsTracker = new GPSTracker(mContext);
            String latitude = String.valueOf(gpsTracker.getLatitude());
            String longitude = String.valueOf(gpsTracker.getLongitude());
            JSONObject locationJsonObject = new JSONObject();
            //JSONObject FILocation = new JSONObject();
            try {
                locationJsonObject.put("latitude", latitude);
                locationJsonObject.put("longitude", longitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            applicantJsonObject.put("FIlocation", locationJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return applicantJsonObject;
    }


    private void openUplaodImagePopup(final Context mContext) {
        try {

            IOUtils.appendLog(Tag + ": Open upload image popup");
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

         /*   for (int i = 0; i < img.size(); i++) {
                UI.createViewPager(mContext, llGallleryView, i, img.get(i).getImageName());
                value_arr.add(img.get(i).getImageName());

            }
*/
            // preview already selected images. get images from sdcard location and show
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobId);
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
        IOUtils.appendLog(Tag + ": Copy images from temp directory to actual directory");
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
        IOUtils.appendLog(Tag + ": Show image upload options");
        final CharSequence[] items = {getResources().getString(R.string.capture_photo), getResources().getString(R.string.browse_photo)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.uploadPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    int currentapiVersion = Build.VERSION.SDK_INT;
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
                    int currentapiVersion = Build.VERSION.SDK_INT;
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
        IOUtils.appendLog(Tag + ": Browse images from gallery ");
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
        IOUtils.appendLog(Tag + ": convert image uri to bitmap");
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
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
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
        String userId = UserPreference.readString(mContext, UserPreference.USER_ID, "");
        try {
            jsonObject.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId);
        new HttpVolleyRequest(getActivity(), jsonObject, new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId, listenerSaveSubmitJobDetails);
    }

    MyListener listenerSaveSubmitJobDetails = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {
            try {
                //IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    Log.d(Const.TAG, response);
                    Gson gson = new Gson();
                    JSONObject responseJson = new JSONObject(response);
                    if (responseJson.length() > 0) {
                        saveSubmitJobResponseModel = gson.fromJson(response, SaveSubmitJobResponseModel.class);
                        Boolean success = saveSubmitJobResponseModel.getSuccess();
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId + "STATUS " + saveSubmitJobResponseModel.getSuccess());
                        if (success == true) {
                            UserPreference.writeString(mContext, UserPreference.LATITUDE, "");
                            UserPreference.writeString(mContext, UserPreference.LONGITUDE, "");
                            //delete menus from local DB
                            int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(mContext));
                            int categoryId = CategoryDB.getCategoryIdDependsOnLoginJsonID(dbHelper, String.valueOf(loginJsonTemplateId));
                            if (hashMapImages.size() > 0) {
                                new doUploadPDF().execute();
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

                            //IOUtils.showSubmitMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getMessage(), getActivity());
                            // remove job from assign job db and assignJob status db

                        } else if (saveSubmitJobResponseModel.getStatusMsg() != null && saveSubmitJobResponseModel.getSuccess() == false && saveSubmitJobResponseModel.getRedirect() == false && !TextUtils.isEmpty(saveSubmitJobResponseModel.getStatusMsg())) {
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
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_SAVE_SUBMIT_JOB_DETAILS + "/" + jobId + "Job is on hold. Deleted job info from table and from folder");
                            IOUtils.showSubmitErrorMessage(getActivity(), getString(R.string.app_name), saveSubmitJobResponseModel.getStatusMsg(), getActivity());
                            IOUtils.showSubmitMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getMessage(), getActivity());
                            IOUtils.stopLoading();
                            IOUtils.stopUpdateStatusLoading();
                        } else {
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
                            IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getMessage(),getActivity());
                            IOUtils.showSubmitMessage(mContext, getString(R.string.app_name), saveSubmitJobResponseModel.getMessage(), getActivity());
                            //MyDynamicToast.errorMessage(mContext, saveSubmitJobResponseModel.getMessage());
                        }
                    } else {
                        MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                if (!IOUtils.isInternetPresent(mContext)) {
                    IOUtils.stopLoading();
                    IOUtils.stopUpdateStatusLoading();
                    IOUtils.showOfflineSuccessMessage(mContext, getString(R.string.app_name), "Your device is offline. Pdf is created and saved locally, will upload to server once internet available", getActivity());
                }
                IOUtils.stopUpdateStatusLoading();
                IOUtils.stopLoading();
                //new CreatePdf().execute();
                if (volleyError != null) {
                    MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                    if (volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }
        }
    };

    private JSONObject createSaveSubmitJobsJson(JSONObject applicantJsonObject, boolean isSubmit) throws JSONException {

        JSONObject jsonObject = null;
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Creating applicant Json");
        try {

            jsonObject = new JSONObject();
            HashMap<String, ArrayList<Bitmap>> imagesHash = getAlreadyExistingImages();
            List<String> imageList = new ArrayList<>();
            for (Map.Entry<String, ArrayList<Bitmap>> imageListMap : imagesHash.entrySet()) {
                imageList.add(imageListMap.getKey());
            }
            if (imageList.size() > 0) {
                // jsonObject.put("selectedImages", new JSONArray(imageList));
                jsonObject.put("hasImagesAttached", true);
            } else {
                jsonObject.put("hasImagesAttached", false);
            }
            JSONObject jobJsonObject = new JSONObject();
            JSONObject jobCriteriaJsonObject = new JSONObject();
            JSONObject locationJsonObject = new JSONObject();
            //JSONObject FiLocationJsonObject = new JSONObject();
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
            if (assignedJobs.getLatLong() != null && !TextUtils.isEmpty(assignedJobs.getLatLong())) {
                try {

                    locationJsonObject.put("latitude", assignedJobs.getLatLong().split(",")[1]);
                    locationJsonObject.put("longitude", assignedJobs.getLatLong().split(",")[2]);
                    // sourceFiLocationJsonObject.put(assignedJobs.getLatLong().split(",")[0], locationJsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // fiLocationJsonObject.put("FIlocation",locationJsonObject);

            //jobCriteriaJsonObject.put("job_id", jobId);
            jobCriteriaJsonObject.put("NBFCName", nbfcName);
            jobCriteriaJsonObject.put("process_queue_id", "102");
            String Key = "";
            // applicantJsonObject.put(fiLocationJsonObject);
            applicantJsonObject.put("sourceFILocations", locationJsonObject);
            jobJsonObject.put(controllerName, applicantJsonObject);
            Log.i("Data", controllerName);
            jsonObject.put("process_queue_id", "102");
            jsonObject.put("job_id", jobId);
            // jsonObject.put("FIlocation",locationJsonObject);
            //jsonObject.put("isSubmit", isSubmit);
            jsonObject.put("actionType", "SB");
            jsonObject.put("data", jobJsonObject);
            // jsonObject.put("jobCriteria", jobCriteriaJsonObject);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


/*    public int createPdf() {
        int imageUploaded = 0;   //pdf not created = 0, uploaded successfully = 1, not uploaded = 2
        try {
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Creating PDF");
            hashMapImages.clear();
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);

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

                PDPage page = null;
                PDPageContentStream contentStream = null;
                List<Bitmap> bitmapsList = new ArrayList<>();
                List<String> bitmapNameList = new ArrayList<>();
                for (Map.Entry<String, ArrayList<Bitmap>> imagesMap : hashMapImages.entrySet()) {
                    ArrayList<Bitmap> arrayListBitmap = imagesMap.getValue();
                    for (int k = 0; k < arrayListBitmap.size(); k++) {
                        bitmapsList.add(arrayListBitmap.get(k));
                        bitmapNameList.add(imagesMap.getKey() + " (" + (k + 1) + ")");
                    }
                }

                if (bitmapsList != null) {
                    for (int j = 0; j < bitmapsList.size(); j++) {
                        if (j % 12 == 0 || j == 0) {
                            try {
                                page = new PDPage();
                                document.addPage(page);
                                contentStream = new PDPageContentStream(document, page);
                                drawTable(page, contentStream, page.getMediaBox().getHeight(), 20, j, bitmapsList, font, bitmapNameList, document);
                                contentStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //create directory if not exist
                if (!directory.isDirectory()) {
                    directory.mkdirs();
                }
                File newFile = new File(directory, "Dusmile_" + nbfcName + "_" + jobId + ".pdf");
                try {
                    newFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    IOUtils.stopLoading();
                }
                String path = newFile.getPath();
                document.save(path);
                document.close();
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF created");
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
                File RecordingDirectory = new File(sdCard.getAbsolutePath() + "/Dusmile/Audio/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
                File RecordingFile = new File(RecordingDirectory, jobId + ".mp3");
                if (RecordingFile.exists()) {
                    if (IOUtils.isInternetPresent(mContext)) {
                        if (UploadImage.uploadMultipartFile(RecordingFile, url, mContext, jobId, nbfcName, true, jobType)) {
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
                if (IOUtils.isInternetPresent(mContext)) {
                    if (UploadImage.uploadMultipartFile(newFile, url, mContext, jobId, nbfcName, false, jobType)) {
                        imageUploaded = 1;
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF Uploaded Successfully");
                    } else {
                        imageUploaded = 2;
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF Upload Failed");
                    }
                } else {
                    imageUploaded = 3;
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF created OFFLINE");
                }
            }
            Log.d("is uploaded", imageUploaded + "");

        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.stopLoading();
        }
        return imageUploaded;
    }*/


/*    public class doCreatePDFWork extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            IOUtils.startLoading(getActivity(), "Creating / Uploading document...");
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
                if (mContext != null && getActivity() != null) {
                    IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), "Pdf Uploaded Successfully");
                }
            } else if (imageUploaded == 2) {
                if (mContext != null && getActivity() != null) {
                    showRetryUploadMessage(mContext, "PDF Upload Failed", UploadPdfFile, uploadPdfUrl, getActivity(), jobID);
                }
            } else if (imageUploaded == 3) {
                if (mContext != null && getActivity() != null) {
                    IOUtils.showOfflineSuccessMessage(mContext, getString(R.string.app_name), "Your device is offline. Pdf is created and saved locally, will upload to server once internet available", getActivity());
                }
            } else {
                if (mContext != null && getActivity() != null) {
                    IOUtils.showWarningMessage(mContext, getString(R.string.app_name), "Something is wrong. If images pdf created, will be sync with server", getActivity());
                }
            }

        }
    }*/

    public class doUploadPDF extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            IOUtils.startLoading(getActivity(), "Creating / Uploading document...");
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int imageUploaded = 0;
            try {
                imageUploaded = uploadPdfPending();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUploaded;
        }

        @Override
        protected void onPostExecute(Integer imageUploaded) {
            super.onPostExecute(imageUploaded);
            // IOUtils.stopLoading();
            if (!IOUtils.isInternetPresent(mContext)) {
                IOUtils.stopLoading();
                IOUtils.stopUpdateStatusLoading();
                IOUtils.showOfflineSuccessMessage(mContext, getString(R.string.app_name), "Your device is offline. Pdf is created and saved locally, will upload to server once internet available", getActivity());
            }
            if (imageUploaded == 1) {
                if (mContext != null && getActivity() != null) {
                    // IOUtils.showSuccessMessage(mContext, getString(R.string.app_name), "Pdf Uploaded Successfully");
                }
            } else if (imageUploaded == 2) {
                if (mContext != null && getActivity() != null) {
                    showRetryUploadMessage(mContext, "PDF Upload Failed", UploadPdfFile, uploadPdfUrl, getActivity(), jobID);
                }
            } else if (imageUploaded == 3) {
                if (mContext != null && getActivity() != null) {
                    IOUtils.showOfflineSuccessMessage(mContext, getString(R.string.app_name), "Your device is offline. Pdf is created and saved locally, will upload to server once internet available", getActivity());
                }
            } else {
                if (mContext != null && getActivity() != null) {
                    IOUtils.showWarningMessage(mContext, getString(R.string.app_name), "Something is wrong. If images pdf created, will be sync with server", getActivity());
                }
            }
        }
    }

    public int uploadPdfPending() {
        int imageUploaded = 0;
        try {
            nbfcName = new Const().DATABASE_NAME;
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
            File newFile = new File(directory, "Dusmile_" + nbfcName + "_" + jobId + ".pdf");
            List<String> imageList = new ArrayList<>();
            for (Map.Entry<String, ArrayList<Bitmap>> imageListMap : hashMapImages.entrySet()) {
                imageList.add(imageListMap.getKey());
            }
            String imageType = android.text.TextUtils.join(",", imageList);
            imageType = imageType.replaceAll(" ", "");
            imageType = imageType.replaceAll(" ", "");
            String fileName = "FIPhotos_" + jobId;
            String query = "typeOfFile=" + fileName + "&" + "folderName=" + jobId;
            String url = new Const().REQUEST_UPLOAD_PDF + "?" + "&" + query;
            uploadPdfUrl = url;
            jobID = jobId;
            UploadPdfFile = newFile;
            imagetype = imageType;
            File RecordingDirectory = new File(sdCard.getAbsolutePath() + "/Dusmile/Audio/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
            File RecordingFile = new File(RecordingDirectory, jobId + ".mp3");
            if (RecordingFile.exists()) {
                String fileType = "FIRecording_" + jobId;
                String Rquery = "typeOfFile=" + fileType + "&" + "folderName=" + jobId;
                String Rurl = new Const().REQUEST_UPLOAD_PDF + "?" + "&" + Rquery;
                if (IOUtils.isInternetPresent(mContext)) {
                    if (UploadImage.uploadMultipartFile(RecordingFile, Rurl, mContext, jobId, nbfcName, true, jobType, saveSubmitJobResponseModel.getMessage(), getActivity(), getString(R.string.app_name))) {
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
            if (IOUtils.isInternetPresent(mContext)) {
                if (UploadImage.uploadMultipartFile(newFile, url, mContext, jobId, nbfcName, false, jobType, saveSubmitJobResponseModel.getMessage(), getActivity(), getString(R.string.app_name))) {
                    imageUploaded = 1;
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF Uploaded Successfully");
                } else {
                    imageUploaded = 2;
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF Upload Failed");
                }
            } else {
                imageUploaded = 3;
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF created OFFLINE");
            }
            Log.d("is uploaded", imageUploaded + "");

        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.stopLoading();
        }
        return imageUploaded;
    }

    public int createPdfPending() {
        int imageUploaded = 0;   //pdf not created = 0, uploaded successfully = 1, not uploaded = 2
        try {
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " Creating PDF");
            hashMapImages.clear();
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
            if (directory.isDirectory()) {
                int i = 0;
                for (File c : directory.listFiles()) {
                    String fileName = c.getName();
                    String[] fileName_arr = fileName.split("_");
                    String selectedForm = fileName_arr[0];
                    //  int selectedPosition = Integer.parseInt(fileName_arr[1]);
                    //    String lastPos = fileName_arr[2].substring(0, fileName_arr[2].lastIndexOf("."));
                    //   int addPosition = Integer.parseInt(lastPos);
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

                PDPage page = null;
                PDPageContentStream contentStream = null;
                List<Bitmap> bitmapsList = new ArrayList<>();
                List<String> bitmapNameList = new ArrayList<>();
                for (Map.Entry<String, ArrayList<Bitmap>> imagesMap : hashMapImages.entrySet()) {
                    ArrayList<Bitmap> arrayListBitmap = imagesMap.getValue();
                    for (int k = 0; k < arrayListBitmap.size(); k++) {
                        bitmapsList.add(arrayListBitmap.get(k));
                        bitmapNameList.add(imagesMap.getKey() + " (" + (k + 1) + ")");
                    }
                }

                if (bitmapsList != null) {
                    for (int j = 0; j < bitmapsList.size(); j++) {
                        if (j % 12 == 0 || j == 0) {
                            try {
                                page = new PDPage();
                                document.addPage(page);
                                contentStream = new PDPageContentStream(document, page);
                                drawTable(page, contentStream, page.getMediaBox().getHeight(), 20, j, bitmapsList, font, bitmapNameList, document);
                                contentStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //create directory if not exist
                if (!directory.isDirectory()) {
                    directory.mkdirs();
                }
                File newFile = new File(directory, "Dusmile_" + nbfcName + "_" + jobId + ".pdf");
                try {
                    newFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    IOUtils.stopLoading();
                }
                String path = newFile.getPath();
                document.save(path);
                document.close();
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF created");
                // imagetype = imageType;
               /* String query = "nbfcName=" + nbfcName + "&" + "selectedImageType=" + jobType + "&" + "tempCaseID=" + jobId;
               //  String url = new Const().REQUEST_UPLOAD_PDF + "?" + query;
                // uploadPdfUrl = url;
                jobID = jobId;
                UploadPdfFile = newFile;
                imagetype = imageType;
                File RecordingDirectory = new File(sdCard.getAbsolutePath() + "/Dusmile/Audio/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
                File RecordingFile = new File(RecordingDirectory, jobId + ".mp3");
                if (RecordingFile.exists()) {
                    //imageUploaded = 3;
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Recording created OFFLINE");
                }
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "PDF created OFFLINE");*/
            }
            //imageUploaded = 3;
            Log.d("is in pending", imageUploaded + "");

        } catch (Exception e) {
            e.printStackTrace();
            IOUtils.stopLoading();
        }
        return imageUploaded;
    }

    public class CreatePdf extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            IOUtils.startUpdateStatusLoading(getActivity(), "Creating / Uploading document...");
            IOUtils.startLoading(getActivity(), "Please wait...");
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int imageUploaded = 0;
            try {
                imageUploaded = createPdfPending();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageUploaded;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (IOUtils.isInternetPresent(mContext)) {
                JSONObject jsonObjectApplicant = createApplicant();
                AssignedJobsDB.updateApplicant(dbHelper, jobId, jsonObjectApplicant.toString(), "true");
                updateStatusOfJob();
                AssignedJobsDB.updateSubmitJson(dbHelper, jobId, jsonObject.toString(), "true");
                saveSubmitJobDeails(jsonObject, true);
            } else {
                IOUtils.stopLoading();
                IOUtils.stopUpdateStatusLoading();
                IOUtils.showOfflineSuccessMessage(mContext, getString(R.string.app_name), "Your device is offline. Pdf is created and saved locally, will upload to server once internet available", getActivity());
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
                imageUploaded = UploadImage.uploadMultipartFile(file, url, mContext, jobId, nbfcName, false, jobType, saveSubmitJobResponseModel.getMessage(), getActivity(), getString(R.string.app_name));
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

            // Showing Alert Message
            alertDialog.show();
        }
    }


    public void showRetryUploadMessage(final Context mContext, String message, final File file, final String url, final Activity activity, final String jobId) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveData() {
        boolean validate = false;
        if (mAwesomeValidation != null) {
            validate = mAwesomeValidation.validate();
        }
        try {
            String allFieldsKeysArray[] = allFieldsKeys.split(",");
            DatabaseUI ui = new DatabaseUI(getActivity(), mAwesomeValidation, getActivity(), getChildFragmentManager(), jobId);
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
            applicantJson = assignedJobs.getApplicant_json();
            JobDetailsResponse.Applicant applicant = gson.fromJson(applicantJson, JobDetailsResponse.Applicant.class);
            templateTextViewSparseArray.putAll(ui.putAllKeysToSave(allFieldsKeysArray, applicant, applicantJson));
            textViewSparseArray.putAll(ui.findAllTextViews(llParentLayout, tableExist, tableKeys));
            editTextSparseArray = ui.findAllEdittexts(llParentLayout);
            templateTextViewSparseArray = ui.findAllTemplateTextViews(llParentLayout);
            radioSparseArray = ui.findCustomValidateRadioButtons(llParentLayout);
            if (validate) {
                try {
                    JSONObject jsonObjectApplicant = createApplicant();
                    Log.d("jsonObject", jsonObjectApplicant.toString());
                    AssignedJobsDB.updateApplicant(dbHelper, jobId, jsonObjectApplicant.toString(), "false");
                    updateStatusOfJob();
                    //Toast.makeText(mContext,"Successfully saved !!!",Toast.LENGTH_LONG).show();
                    // MyDynamicToast.successMessage(mContext, "Successfully saved !!!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private HashMap<String, ArrayList<Bitmap>> getAlreadyExistingImages() {
        try {
            hashMapImages.clear();
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId);
            if (directory.isDirectory()) {
                for (File c : directory.listFiles()) {
                    String fileName = c.getName();
                    String[] fileName_arr = fileName.split("_");
                    String selectedForm = fileName_arr[0];
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMapImages;
    }


    public void drawTable(PDPage page, PDPageContentStream contentStream,
                          float y, float margin,
                          int count, List<Bitmap> bitmaps, PDType0Font font, List<String> bitmapNameList, PDDocument document) throws IOException {
        try {

            final int rows = 3;
            final int cols = 4;
            final float rowHeight = (page.getMediaBox().getHeight() / 3);
            final float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
            final float tableHeight = rowHeight * rows;
            final float colWidth = tableWidth / (float) cols;
            final float cellMargin = 3f;

            //now add the text
            float textx = margin + cellMargin;
            float texty = y - (page.getMediaBox().getHeight() / 3) + 10;
            for (int l = 0; l < rows; l++) {
                //contentStream.drawLine(margin, textx, margin + tableWidth, texty);
                for (int m = 0; m < cols; m++) {
                    if (bitmaps.size() > count) {
                        int width = Float.valueOf(page.getMediaBox().getWidth() / 4).intValue();
                        int height = Float.valueOf(page.getMediaBox().getHeight() / 4).intValue();
                        contentStream.drawLine(textx, page.getMediaBox().getHeight() - 20, textx + width, page.getMediaBox().getHeight() - 20);
                        contentStream.beginText();
                        contentStream.setNonStrokingColor(15, 38, 192);
                        contentStream.setFont(font, 12);
                        contentStream.newLineAtOffset(textx, texty - 10 + rowHeight - 50);
                        contentStream.drawString(bitmapNameList.get(count));
                        contentStream.endText();
                        Bitmap bitmap = getResizedBitmap(bitmaps.get(count), width, height);
                        PDImageXObject fImage = JPEGFactory.createFromImage(document, bitmap);
                        contentStream.drawImage(fImage, textx, texty + 5, width - 20, height - 20);
                        contentStream.drawLine(textx, texty - 10, textx + width, texty - 10);
                        textx += colWidth;
                        count++;
                    }
                }
                // contentStream.drawLine(10,texty,page.getMediaBox().getWidth()-10,texty);
                texty -= rowHeight;
                textx = margin + cellMargin;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    public void showJobInfo(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_job_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textViewJobId = (TextView) dialog.findViewById(R.id.jobIDValue);
        // final TextView textViewNBFCName = (TextView) dialog.findViewById(R.id.nbfcNameValue);
        final TextView textViewTemplateName = (TextView) dialog.findViewById(R.id.templateNameValue);
        // final TextView textViewCreationTimeValue = (TextView) dialog.findViewById(R.id.creationTimeValue);
        final TextView textViewPincode = (TextView) dialog.findViewById(R.id.pinCodeValue);
        final TextView textViewCityName = (TextView) dialog.findViewById(R.id.cityNameValue);
        final TextView textViewStartTimeValue = (TextView) dialog.findViewById(R.id.jobStartTimeValue);
        // final TextView textViewApplicationFormNo = (TextView) dialog.findViewById(R.id.applicationFormNoValue);
        final TextView textViewAddress = (TextView) dialog.findViewById(R.id.addressValue);
        final TextView textViewMobile = (TextView) dialog.findViewById(R.id.mobileNoValue);
        final TextView textViewTAT = (TextView) dialog.findViewById(R.id.tATimeValue);
        final Button locationMapButton = (Button) dialog.findViewById(R.id.locationValue);
        textViewJobId.setText(jobId);
        // textViewNBFCName.setText(nbfcName);
        textViewTemplateName.setText(templateName);
        // textViewCreationTimeValue.setText(creationTime);
        textViewCityName.setText(cityName);
        textViewPincode.setText(pinCode);
        textViewStartTimeValue.setText(jobStartTime);
        textViewAddress.setText(address);
        textViewMobile.setText(mobileNo);
        textViewTAT.setText(tATime);
        locationMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + address));
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // textViewApplicationFormNo.setText(applicationFormNo);
        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();
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
