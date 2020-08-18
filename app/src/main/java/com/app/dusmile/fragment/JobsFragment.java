package com.app.dusmile.fragment;

import android.app.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.app.dusmile.DBModel.AssignedJobs;
import com.app.dusmile.DBModel.ClientTemplate;
import com.app.dusmile.DBModel.OfflineAssignedJobs;
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.DBModel.UpdatedTemplet;
import com.app.dusmile.R;
import com.app.dusmile.activity.AllFormsActivity;
import com.app.dusmile.activity.DusmileBaseActivity;
import com.app.dusmile.adapter.GenericAdapter;
import com.app.dusmile.application.DusmileApplication;
import com.app.dusmile.common.ConfirmTemplateUpdate;
import com.app.dusmile.common.TemplateOperations;
import com.app.dusmile.common.UpdateJobStatus;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.constant.AppConstant;
import com.app.dusmile.database.AssignedJobsDB;
import com.app.dusmile.database.AssignedJobsStatusDB;
import com.app.dusmile.database.ClientTemplateDB;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.OfflineAssignedJobsDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.database.helper.DBHelper;
import com.app.dusmile.interfaces.BtnClickListener;
import com.app.dusmile.model.AssignedJobsResources;
import com.app.dusmile.model.AvailableJobsDataModel;
import com.app.dusmile.model.HoldJobResponseModel;
import com.app.dusmile.model.JobDetailsResponse;
import com.app.dusmile.model.LoginResponse;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.utils.RecyclerItemClickListener;
import com.desai.vatsal.mydynamictoast.MyCustomToast;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by suma on 30/01/17.
 */

public class JobsFragment extends Fragment {
    private static String URL = null;
    private static String AURL = null;
    private static String CURL = null;
    MyCustomToast myCustomToast;
    private View layout = null;
    private Context mContext;
    private List<SubCategory> subCategoryMenuList;
    private RecyclerView recyclerView;
    private Gson gson;
    private static TextView tv_msg;
    private static LinearLayout parent_layout;
    private View toastRoot;
    private RecyclerView.LayoutManager mLayoutManager;
    private GenericAdapter genericAdapter;
    private EditText searchEditText;
    private Button cancelButton;
    TextView txtFromDate, txtToDate;
    private LinearLayout dateFilter;
    private JSONArray reportHeadersArray;
    private JSONArray reportDataArray;
    private JSONArray reportHeadersUIArray;
    JSONArray filterJsonArray;
    JSONArray cardHeadersKeyArray;
    JSONObject resourceJsonObj;
    private Toast toast;
    private List<String> formNameList = new ArrayList<>();
    private List<List<String>> exportData = new ArrayList<>();
    List<AvailableJobsDataModel.ReportDatum> filteredList;
    private String job_id, cityName, pincode, applicationFormNo, jobStartTime, totalAssignedTime, mobileNo, address, coApplicantArray;
    String hasCoApplicant;
    private int mYear, mMonth, mDay;
    String latestVersion;
    private String checkAssignedResp;
    private JobDetailsResponse jobDetailsResponseGlobal;
    private DBHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String Tag = "JobsFragment";
    String holdReasonsList[] = {"Door Locked", "Person not at home", "Bike failed"};
    public static FragmentTransaction fragmentTransaction;

    public JobsFragment() {
        super();
    }

    boolean isAvailable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jobs_fragment, container, false);
        this.findViews(rootView);
        // startDate = getArguments().getString("startDate");
        // endDate = getArguments().getString("endDate");
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        Activity activity = this.getActivity();
        mContext = activity;

        dbHelper = DBHelper.getInstance(mContext);
        this.searchJobListener();
        this.onCancelButtonClicked();
        this.fromDateClick();
        this.fromToClick();
        //this.swipeRefreshListener();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.swipeRefreshListener();
        if (!AppConstant.isCallButtonClicked) {
            searchEditText.setText("");
            if (genericAdapter != null) {
                reportHeadersUIArray = new JSONArray();
                reportHeadersArray = new JSONArray();
                reportDataArray = new JSONArray();
                cardHeadersKeyArray = new JSONArray();
                genericAdapter.refresh(reportDataArray);
                // genericAdapter.notifyDataSetChanged();
            }
            assignUrl();
            if (IOUtils.isInternetPresent(mContext)) {
                UpdateJobStatus.jobIdList.clear();
                if (AppConstant.isAvilableJobs) {
                    getAvailableJobs(URL);
                    getActivity().setTitle(getString(R.string.avialble_jobs));
                } else if (AppConstant.isAssinedJobs) {
                    // getAvailableJobs(AURL);
                    String url = new Const().GET_REPORT_METADATA;
                    getReportMetaData(url);
                    getActivity().setTitle(getString(R.string.assigned_jobs));
                } else {
                    dateFilter.setVisibility(View.VISIBLE);
                    getCompletedJobs(CURL);
                    getActivity().setTitle(getString(R.string.completed_jobs));
                }
            } else {
                if (AppConstant.isAssinedJobs) {
                    getActivity().setTitle(getString(R.string.assigned_jobs));
                    showOfflineAssignedJobs();
                } else {
                    IOUtils.showErrorMessage(mContext, "No internet connection");
                    if (AppConstant.isAvilableJobs) {
                        getActivity().setTitle(getString(R.string.avialble_jobs));
                    } else if (AppConstant.isCompletedJobs) {
                        getActivity().setTitle(getString(R.string.completed_jobs));
                    }
                }
            }
        }
        AppConstant.isCallButtonClicked = false;
    }

    private void assignUrl() {
        subCategoryMenuList = new ArrayList<>();
        int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(mContext));
        subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnIsMenuFlag(dbHelper, String.valueOf(loginJsonTemplateId), "false");
        for (int i = 0; i <= subCategoryMenuList.size() - 1; i++) {
            String jobClicked = subCategoryMenuList.get(i).getSubcategory_name();
            String usrId = UserPreference.readString(mContext, UserPreference.USER_INT_ID, "");
            if (jobClicked.equalsIgnoreCase("Assigned Jobs")) {
                String act = subCategoryMenuList.get(i).getAction();
                String st = act.substring(1, act.length());
                String replaceString = st.replace("userId", usrId);
                AURL = new Const().URL + replaceString;
            }
            if (jobClicked.equalsIgnoreCase("Completed Jobs")) {
                String act = subCategoryMenuList.get(i).getAction();
                String st = act.substring(1, act.length());
                String replaceString = st.replace("serId", usrId);
                CURL = new Const().URL + replaceString;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void findViews(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.job_recycler_view);
        searchEditText = (EditText) view.findViewById(R.id.searchbox);
        cancelButton = (Button) view.findViewById(R.id.searchBoxCancel);
        txtFromDate = (TextView) view.findViewById(R.id.from_date);
        txtToDate = (TextView) view.findViewById(R.id.to_date);
        dateFilter = (LinearLayout) view.findViewById(R.id.dateFilter);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = sdf.format(new Date());
        txtFromDate.setText(currentTime);
        txtToDate.setText(currentTime);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }

    public void getAvailableJobs(String url) {
        IOUtils.startLoading(mContext, "Loading......");
        try {
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url);
            new HttpVolleyRequest(mContext, url, listenerJobs);
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
    }

    MyListener listenerJobs = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        reportHeadersArray = new JSONArray();
                        reportDataArray = new JSONArray();
                        reportHeadersUIArray = new JSONArray();
                        cardHeadersKeyArray = new JSONArray();
                        reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                        reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                        cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                        resourceJsonObj = jsonObject.getJSONObject("resources");
                        getJobDataApi(jsonObject,resourceJsonObj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    String url = getURL();
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String success = jsonObject.getString("success");
                    if (success.equals("false")) {
                        String message = jsonObject.getString("message");
                        MyDynamicToast.informationMessage(mContext, message);
                    } else {
                        MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + volleyError.getMessage());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
                            IOUtils.sendUserToLogin(mContext, getActivity());
                        }
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

    public void setAdapter(JSONArray reportHeadersArray, JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray) {
        genericAdapter = new GenericAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
        genericAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(genericAdapter);
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

    private void searchJobListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                try {

                    query = query.toString().toLowerCase();
                    filterJsonArray = new JSONArray();
                    if (query.length() > 0) {
                        cancelButton.setVisibility(View.VISIBLE);
                        for (int i = 0; i < reportDataArray.length(); i++) {
                            try {
                                if (reportDataArray.get(i).toString().toLowerCase().contains(query.toString().toLowerCase())) {
                                    filterJsonArray.put(reportDataArray.get(i));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (filterJsonArray != null) {
                            if (filterJsonArray.length() == 0) {
                                // exportBtn.setVisibility(View.GONE);
                                showAToast("No Matching Records Found");
                            } else {
                                //exportBtn.setVisibility(View.VISIBLE);
                            }
                            // reportDetailsAdapter.refresh(filterJsonArray);
                            genericAdapter = new GenericAdapter(mContext, reportHeadersArray, filterJsonArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
                            recyclerView.setAdapter(genericAdapter);

                        }

                    } else {
                        try {
                            cancelButton.setVisibility(View.GONE);
                            if (reportDataArray != null) {
                                if (reportDataArray.length() == 0) {
                                    // exportBtn.setVisibility(View.GONE);
                                    //showAToast("No Data Available");
                                } else {
                                    // exportBtn.setVisibility(View.VISIBLE);
                                }
                                genericAdapter = new GenericAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
                                recyclerView.setAdapter(genericAdapter);

                            }
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


    private void onCancelButtonClicked() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchEditText.setText("");
                    cancelButton.setVisibility(View.GONE);
                    if (reportDataArray != null) {
                        if (reportDataArray.length() == 0) {
                            //  exportBtn.setVisibility(View.GONE);
                            // showAToast("No Data Available");
                        } else {
                            // exportBtn.setVisibility(View.VISIBLE);
                        }
                        genericAdapter = new GenericAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray, btnClickListener);
                        recyclerView.setAdapter(genericAdapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void fromDateClick() {
        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txtFromDate) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                private String DayOfMonth;
                                private String MonthOfYear;

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    if (monthOfYear < 10) {
                                        MonthOfYear = "0" + (monthOfYear + 1);
                                    } else {
                                        MonthOfYear = String.valueOf(monthOfYear);
                                    }
                                    if (dayOfMonth < 10) {
                                        DayOfMonth = "0" + dayOfMonth;
                                    } else {
                                        DayOfMonth = String.valueOf(dayOfMonth);
                                    }
                                    txtFromDate.setText(year + "-" + MonthOfYear + "-" + DayOfMonth);
                                    searchEditText.setText("");
                                    if (genericAdapter != null) {
                                        reportHeadersUIArray = new JSONArray();
                                        reportHeadersArray = new JSONArray();
                                        reportDataArray = new JSONArray();
                                        cardHeadersKeyArray = new JSONArray();
                                        genericAdapter.refresh(reportDataArray);
                                        // genericAdapter.notifyDataSetChanged();
                                    }
                                    UpdateJobStatus.jobIdList.clear();
                                    getCompletedJobs(new Const().REQUEST_GET_COMPLETED_JOBS + "/" + UserPreference.readString(mContext, UserPreference.USER_INT_ID, ""));
                                    getActivity().setTitle(getString(R.string.completed_jobs));
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
    }

    private void fromToClick() {
        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txtToDate) {

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                private String DayOfMonth;
                                private String MonthOfYear;

                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    if (monthOfYear < 10) {
                                        MonthOfYear = "0" + (monthOfYear + 1);
                                    } else {
                                        MonthOfYear = String.valueOf(monthOfYear);
                                    }
                                    if (dayOfMonth < 10) {
                                        DayOfMonth = "0" + dayOfMonth;
                                    } else {
                                        DayOfMonth = String.valueOf(dayOfMonth);
                                    }
                                    txtToDate.setText(year + "-" + MonthOfYear + "-" + DayOfMonth);
                                    searchEditText.setText("");
                                    if (genericAdapter != null) {
                                        reportHeadersUIArray = new JSONArray();
                                        reportHeadersArray = new JSONArray();
                                        reportDataArray = new JSONArray();
                                        cardHeadersKeyArray = new JSONArray();
                                        genericAdapter.refresh(reportDataArray);
                                        // genericAdapter.notifyDataSetChanged();
                                    }
                                    UpdateJobStatus.jobIdList.clear();
                                    getCompletedJobs(new Const().REQUEST_GET_COMPLETED_JOBS + "/" + UserPreference.readString(mContext, UserPreference.USER_INT_ID, ""));
                                    getActivity().setTitle(getString(R.string.completed_jobs));
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });
    }

    private void onExportButtonClicked() {
       /*// exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showExportChoiceDialog();
            }
        });*/
    }

   /* private void showExportChoiceDialog()
    {
        final String a[] = {"CSV","EXCEL"};
        new LovelyChoiceDialog(getActivity())
                .setTopColorRes(R.color.clrLoginButton)
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.suma48)
                .setMessage(R.string.export_message)
                .setItems(a, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                       switch (item)
                       {
                           case "CSV":
                                new doExportWork().execute(item);
                               break;
                           case "EXCEL":
                               new doExportWork().execute(item);
                               break;
                           default:
                               new doExportWork().execute("CSV");
                               break;
                       }
                    }
                })
                .show();
    }
*/

  /*  public class doExportWork extends AsyncTask<String,Void,Boolean>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            IOUtils.startLoadingPleaseWait(mContext);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean success = false;
            try {

                if (params[0].equalsIgnoreCase("CSV")) {
                    success = exportReportListToCSV();
                } else {
                    success = exportReportListToExcel();
                }
            }
            catch (Exception e)
            {
                success = false;
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            IOUtils.stopLoading();
            if(result == true)
            {
                IOUtils.showSuccessMessage(mContext,getString(R.string.app_name),"File Exported Successfully. Please check location InternalStorage/DusMile");
            }
            else
            {
                IOUtils.showWarningMessage(mContext,"File Export Failed");
            }

        }
    }*/

   /* private boolean exportReportListToCSV()
    {
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exporting Reports to CSV ");
        CSVWriter writer = null;
        String fileName = "";
        boolean result = false;
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            final Calendar myCalendar = Calendar.getInstance();
            List<String> headers = new ArrayList<>();
            List<List<String>> Data = new ArrayList<>();
            for(String header : reportHeaders)
            {
                if(header.contains("Location")||header.contains("View")||header.contains("Perform Job")){}
                else {headers.add(header);
                }
            }
            if(filteredList!=null&& searchEditText.getText().toString().length()>0 && filteredList.size()>0)
            {
                exportData = filteredList;
            }
            else
            {
                exportData = reportData;
            }
            for(int i = 0 ;i<exportData.size();i++)
            {
                List<String> data = new ArrayList<>();
                for(int j=0;j<exportData.get(i).size();j++){
                    if(exportData.get(i).get(j).contains("<img src=")||exportData.get(i).get(j).contains("<a href=")){}

                    else {
                        data.add(exportData.get(i).get(j));
                    }
                }
                Data.add(data);
            }
            if(AppConstant.isAvilableJobs) {
               fileName = "AvailableJobsReportData";
            }
            else if(AppConstant.isAssinedJobs)
            {
                  fileName = "AssignedJobsReportData";
            }
            else
            {
                fileName = "CompletedJobsReportData";
            }
            fileName = fileName.concat(sdf.format(myCalendar.getTime()))+".csv".trim();
            File sdCard = Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/CSV");
            //create directory if not exist
            if(!directory.isDirectory()){
                directory.mkdirs();
            }

            File file = new File(directory, fileName);
            writer = new CSVWriter(new FileWriter(file), ',');
            String[] headersData = new String[headers.size()];
            headersData = headers.toArray(headersData);
            writer.writeNext(headersData);
            for(int i = 0 ; i <Data.size();i++)
            {
                List<String> dataList = Data.get(i);
                String[] Reportdata = new String[Data.get(i).size()];
                Reportdata = dataList.toArray(Reportdata);
                writer.writeNext(Reportdata);
            }
            writer.close();
            result = true;
            IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exported Reports to CSV");
        }
        catch (Exception e)
        {
            result = false;
        }
        return  result;
    }*/


   /* public boolean exportReportListToExcel(){
        boolean result = false;
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exporting Reports to Excel ");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            final Calendar myCalendar = Calendar.getInstance();
            String fileName = "";
            File directory = null;
            File sdCard = Environment.getExternalStorageDirectory();
            if(AppConstant.isAvilableJobs) {
                fileName = "AvailableJobsReportData";
            }
            else if(AppConstant.isAssinedJobs)
            {
                fileName = "AssignedJobsReportData";
            }
            else
            {
                fileName = "CompletedJobsReportData";
            }
            fileName = fileName.concat(sdf.format(myCalendar.getTime()))+".xls".trim();
            directory = new File(sdCard.getAbsolutePath() + "/Dusmile/EXCEL");

            int val = 0;
            //create directory if not exist
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }

            List<String> headers = new ArrayList<>();
            List<List<String>> Data = new ArrayList<>();

            headers.clear();
            for (String s : reportHeaders) {
                if (s.contains("Location") || s.contains("View") || s.contains("Perform Job")) {
                } else {
                    headers.add(s);
                }
            }

                if(filteredList!=null&& searchEditText.getText().toString().length()>0 && filteredList.size()>0)
                {
                    exportData = filteredList;
                }
               else
                {
                    exportData = reportData;
                }

            for (int i = 0; i < exportData.size(); i++) {
                List<String> data = new ArrayList<>();
                for (int j = 0; j < exportData.get(i).size(); j++) {
                    if (exportData.get(i).get(j).contains("<img src=") || exportData.get(i).get(j).contains("<a href=")) {
                    } else {
                        data.add(exportData.get(i).get(j));
                    }

                }
                Data.add(data);

            }


            //file path
            File file = new File(directory, fileName);
            Workbook workbook = new HSSFWorkbook();
            Sheet studentsSheet = workbook.createSheet("Report Data");
            int rowIndex = 0;
            Row row = studentsSheet.createRow(rowIndex);
            for (int i = 0; i < headers.size(); i++) {
                row.createCell(i).setCellValue(headers.get(i));
            }
            for (int i = 0; i < Data.size(); i++) {
                val++;
                Row row1 = studentsSheet.createRow(val);
                for (int j = 0; j < Data.get(i).size(); j++) {

                    row1.createCell(j).setCellValue(Data.get(i).get(j));
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                workbook.write(fos);
                fos.close();
                result = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result =false;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            }
        }
        catch (Exception e)
        {
           e.printStackTrace();
            result = false;
        }
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exported Reports to Excel");
       return  result;

    }*/


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String templateName, creationTime, nbfcName, productName;

    BtnClickListener btnClickListener = new BtnClickListener() {
        @Override
        public void uploadImageListener() {

        }

        @Override
        public void saveListerners() {

        }

        @Override
        public void submitListener() {

        }

        @Override
        public void cancelListener() {

        }

        @Override
        public void audioListener() {

        }

        @Override
        public void clickListener(String val) {

        }

        @Override
        public void buttonListener(int pos) {
            try {
                JSONObject jsonObject = null;
                if (searchEditText.getText().toString().length() > 0) {
                    jsonObject = filterJsonArray.getJSONObject(pos);
                } else {
                    jsonObject = reportDataArray.getJSONObject(pos);
                }
                job_id = jsonObject.getString("job_id");
                if (jsonObject.has("NBFCName")) {
                    nbfcName = jsonObject.getString("NBFCName");
                } else {
                    nbfcName = new Const().DATABASE_NAME;
                }
                templateName = jsonObject.getString("JobType");
                if (jsonObject.has("city_Name")) {
                    cityName = jsonObject.getString("city_Name");
                }
                if (jsonObject.has("Product")) {
                    productName = jsonObject.getString("Product");
                }
                if (jsonObject.has("Pincode")) {
                    pincode = jsonObject.getString("Pincode");
                } else {
                    pincode = "-";
                }
                if (jsonObject.has("job_creation_date_STR")) {
                    creationTime = jsonObject.getString("job_creation_date_STR");
                }
                if (jsonObject.has("ApplicantOrCoApplicant")) {
                    hasCoApplicant = jsonObject.getString("ApplicantOrCoApplicant");
                }
                if (IOUtils.isInternetPresent(mContext)) {
                    checkJobIsAssignedOrNot(job_id, "102", templateName, new Const().DATABASE_NAME);
                } else {
                    IOUtils.showErrorMessage(mContext, "No internet connection");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void viewListener(int pos) {
            try {
                JSONObject jsonObject = null;
                if (searchEditText.getText().toString().length() > 0) {
                    jsonObject = filterJsonArray.getJSONObject(pos);
                } else {
                    jsonObject = reportDataArray.getJSONObject(pos);
                }
                isAvailable = false;
                job_id = jsonObject.getString("job_id");
                //nbfcName = jsonObject.getString("NBFCName");
                if (jsonObject.has("city_Name")) {
                    cityName = jsonObject.getString("city_Name");
                }
                if (jsonObject.has("Pincode")) {
                    pincode = jsonObject.getString("Pincode");
                }
                if (jsonObject.has("Product")) {
                    productName = jsonObject.getString("Product");
                }
                templateName = jsonObject.getString("JobType");
                // creationTime = jsonObject.getString("RequestDateandTime");
                if (jsonObject.has("ApplicantOrCoApplicant")) {
                    hasCoApplicant = jsonObject.getString("ApplicantOrCoApplicant");
                }
                //applicationFormNo = jsonObject.getString("applicationFormNo");
                jobStartTime = jsonObject.getString("job_creation_date_STR");
                totalAssignedTime = jsonObject.getString("ASSIGNED_TAT");
                mobileNo = jsonObject.getString("Mobile");
                if (jsonObject.has("Address")) {
                    address = jsonObject.getString("Address");
                }
                if (jsonObject.has("CoApplicantSubprocesses")) {
                    coApplicantArray = jsonObject.getString("CoApplicantSubprocesses");
                }
                //coApplicantArray = coApplicantArray.replaceAll("^\\[|]$", "");

                String jobStatus = jsonObject.getString("status");
                AppConstant.formPosition = 0;
                AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, job_id);
                String clientTemplateId = assignedJobs.getClient_template_id();
                String isSubmittedJob = assignedJobs.getIs_submit();
                if (!TextUtils.isEmpty(isSubmittedJob) && isSubmittedJob.equalsIgnoreCase("true") && !(jobStatus.equalsIgnoreCase("Mobile Upload Pending"))) {
                    MyDynamicToast.informationMessage(mContext, "You can not perform already submitted Job");
                } else if (!TextUtils.isEmpty(jobStatus) && jobStatus.equalsIgnoreCase("On Hold")) {
                    MyDynamicToast.informationMessage(mContext, "Sorry you can't perform this job. This job is on hold.");
                } else if (UpdateJobStatus.jobIdList.contains(job_id)) {
                    MyDynamicToast.informationMessage(mContext, "You can not perform already completed Job");
                } else {
                    if (!TextUtils.isEmpty(clientTemplateId)) {
                        TemplateOperations.getFormsFromDBAndInsertIntoMenus(Integer.parseInt(clientTemplateId), mContext);
                        sendToAllFormActivity(isAvailable, "");
                    } else {
                        String type = templateName;
                        getUpdatedTemplate(type + "/" + productName);
                       /* switch (type) {
                            case "Residence":
                                getUpdatedTemplate("Residence");
                                break;
                            case "Office":
                                getUpdatedTemplate("Office");
                                break;
                            case "ResidenceAndOffice":
                                getUpdatedTemplate("ResidenceAndOffice");
                                break;
                        }*/
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void sendGeoLocationListener() {

        }

        @Override
        public void holdListener(String date, String reason, String jobID, String nbfcName) {
            if (IOUtils.isInternetPresent(mContext)) {
                holdJob(reason, jobID, nbfcName, date);
            } else {
                MyDynamicToast.errorMessage(mContext, "Please check your internet connection");
            }
        }

        @Override
        public void showHoldPopupListener(int pos) {

        }
    };

    public void getCompletedJobs(String url) {

        IOUtils.startLoading(mContext, "Loading......");

        try {
            String startD, endD;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentTime = sdf.format(new Date());
            String startDate = (String) txtFromDate.getText();
            String endDate = (String) txtToDate.getText();
            if (startDate != null) {
                startD = startDate;
            } else {
                startD = currentTime;
            }
            if (endDate != null) {
                endD = endDate;
            } else {
                endD = currentTime;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("startDate", startD);
            jsonObject.put("endDate", endD);
            jsonObject.put("FOSExecutiveID", UserPreference.getUserRecord(mContext).getUserID());
            // jsonObject.put("process_queue_id", "107");
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url);
            new HttpVolleyRequest(mContext, jsonObject, url, completedlistenerJobs);
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }

    }

    MyListener completedlistenerJobs = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String url = getURL();
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);

                        /*gson = new Gson();
                        AvailableJobsDataModel availableJobsModel = gson.fromJson(response, AvailableJobsDataModel.class);*/
                        boolean status = jsonObject.getBoolean("success");
                        if (status == true) {
                            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + String.valueOf(status));
                            reportHeadersArray = new JSONArray();
                            reportDataArray = new JSONArray();
                            reportHeadersUIArray = new JSONArray();
                            cardHeadersKeyArray = new JSONArray();
                            reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                            reportDataArray = jsonObject.getJSONArray("reportData");
                            reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                            cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                            if (AppConstant.isAssinedJobs) {
                                DBHelper dbHelper = DBHelper.getInstance(mContext);
                                OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                                OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                                offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                                OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                                UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());
                            }
                            if (reportDataArray != null) {
                                if (reportDataArray.length() > 0) {
                                    // exportBtn.setVisibility(View.VISIBLE);
                                    setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                                } else {
                                    // exportBtn.setVisibility(View.GONE);
                                    MyDynamicToast.informationMessage(mContext, "No Data Available");
                                }
                            }
                        } else {
                            String message = jsonObject.getString("message");
                            MyDynamicToast.informationMessage(mContext, message);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }
        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String url = getURL();
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);

                        /*gson = new Gson();
                        AvailableJobsDataModel availableJobsModel = gson.fromJson(response, AvailableJobsDataModel.class);*/
                        boolean status = jsonObject.getBoolean("success");
                        if (status == true) {
                            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + String.valueOf(status));
                            reportHeadersArray = new JSONArray();
                            reportDataArray = new JSONArray();
                            reportHeadersUIArray = new JSONArray();
                            cardHeadersKeyArray = new JSONArray();
                            reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                            reportDataArray = jsonObject.getJSONArray("reportData");
                            reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                            cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                            if (AppConstant.isAssinedJobs) {
                                DBHelper dbHelper = DBHelper.getInstance(mContext);
                                OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                                OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                                offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                                OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                                UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());
                            }
                            if (reportDataArray != null) {
                                if (reportDataArray.length() > 0) {
                                    // exportBtn.setVisibility(View.VISIBLE);
                                    setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                                } else {
                                    // exportBtn.setVisibility(View.GONE);
                                    MyDynamicToast.informationMessage(mContext, "No Data Available");
                                }
                            }
                        } else {
                            String message = jsonObject.getString("message");
                            MyDynamicToast.informationMessage(mContext, message);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    String url = getURL();
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String success = jsonObject.getString("success");
                    if (success.equals("false")) {
                        String message = jsonObject.getString("message");
                        MyDynamicToast.informationMessage(mContext, message);
                    } else {
                        MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + volleyError.getMessage());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
                            IOUtils.sendUserToLogin(mContext, getActivity());
                        }
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

    public void checkJobIsAssignedOrNot(String jobId, String processQId, String templateName, String nbfcName) {

        IOUtils.startLoading(mContext, "Please wait......");

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("process_queue_id", processQId);
            jsonObject.put("job_id", jobId);
            jsonObject.put("JobType", templateName);
            jsonObject.put("NBFCName", nbfcName);
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHECK_ASSIGNED_JOBS + "\nREQUEST " + jsonObject.toString());
        new HttpVolleyRequest(mContext, jsonObject, new Const().REQUEST_CHECK_ASSIGNED_JOBS, listenerCheckJobAssigned);
    }

    MyListener listenerCheckJobAssigned = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {
            //showProgress(false);

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {
            try {

                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        checkAssignedResp = response;
                        Gson gson = new Gson();
                        JobDetailsResponse jobDetailsResponse = gson.fromJson(response.trim(), JobDetailsResponse.class);
                        jobDetailsResponseGlobal = jobDetailsResponse;
                        IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHECK_ASSIGNED_JOBS + "\nRESPONSE " + jobDetailsResponse.getSuccess().toString());
                        if (jobDetailsResponse.getRedirect() == true && jobDetailsResponse.getSuccess() == true) {
                            //deleting entries from assigned table is job is deassigned by admin
                            deleteJobDetailsIfJobDeAssigned(job_id);
                            //handle template version
                            handleResponseToCheckVersion(response, jobDetailsResponse, null);

                            //store offline data
                            storeOfflineAssignedJobs(new Const().REQUEST_GET_ASSIGNED_JOBS + "/" + UserPreference.readString(mContext, UserPreference.USER_ID, ""));
                            MyDynamicToast.informationMessage(mContext, "Job Accepted Successfully");
                            latestVersion = jobDetailsResponse.getLatestVersion();
                            ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateAccVerionNo(dbHelper, templateName, nbfcName, UserPreference.getLanguage(mContext), latestVersion);
                            String clientTemplateId = clientTemplate.getID();
                            String clientTemplateVersion = clientTemplate.getVersion();
                            isAvailable = true;
                            if (!TextUtils.isEmpty(clientTemplateId)) {
                                TemplateOperations.getFormsFromDBAndInsertIntoMenus(Integer.parseInt(clientTemplateId), mContext);
                                sendToAllFormActivity(isAvailable, response);
                                AppConstant.isAvilableJobs = true;
                                AppConstant.isAssinedJobs = false;
                                AppConstant.isCompletedJobs = false;
                                onResume();
                            } else {
                                getUpdatedTemplate("Residence");
                            }

                        } else {
                            showJobAlreadyAssignedDialog();
                        }
                    } else {
                        MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                    }

                } else {
                    MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHECK_ASSIGNED_JOBS + "\nRESPONSE " + volleyError.getMessage());
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
                IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHECK_ASSIGNED_JOBS + "\nRESPONSE " + e.getMessage());
            }
        }
    };


    private void showJobAlreadyAssignedDialog() {
        IOUtils.showWarningMessage(getActivity(), getResources().getString(R.string.job_already_assigned));
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

    public String removeSpaceFromUrl(String url) {
        String newUrl = null;
        newUrl = url.replaceAll(" ", "%20");
        return newUrl;
    }

    public void getUpdatedTemplate(String clientTemplateName) {
        String modefiedURL = removeSpaceFromUrl(clientTemplateName);
        IOUtils.startLoading(getActivity(), "Updating template... Please wait.....");
        IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_UPDATED_TEMPLATE);
        //JSONObject jsonObject = new JSONObject();
        new HttpVolleyRequest(mContext, new Const().REQUEST_GET_UPDATED_TEMPLATE + "/" + modefiedURL, listenerGetUpdatedTemplate);
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
                    //Update template update status
                    int clientTemplateId = 0;
                    List<Integer> updatedTemplateList = new ArrayList<>();
                    if (isAvailable) {
                        clientTemplateId = ClientTemplateDB.getClientTemplateID(dbHelper, templateName, nbfcName, UserPreference.getLanguage(mContext), latestVersion);
                        updatedTemplateList.add(Double.valueOf(latestVersion).intValue());
                    } else {
                        ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateByTemplateNameClientLanguage(dbHelper, templateName, new Const().DATABASE_NAME, UserPreference.getLanguage(mContext), "false");
                        clientTemplateId = ClientTemplateDB.getClientTemplateID(dbHelper, templateName, new Const().DATABASE_NAME, UserPreference.getLanguage(mContext), clientTemplate.getVersion());
                        updatedTemplateList.add(Double.valueOf(clientTemplate.getVersion()).intValue());
                    }
                    if (clientTemplateId > 0) {
                        TemplateOperations.getFormsFromDBAndInsertIntoMenus(clientTemplateId, mContext);
                        if (isAvailable) {
                            //sendToAllFormActivity(isAvailable, checkAssignedResp);
                            AppConstant.isAvilableJobs = true;
                            AppConstant.isAssinedJobs = false;
                            AppConstant.isCompletedJobs = false;
                            //add entries in assigned table in case clientTemplate not found and new template downloaded.
                            handleResponseToCheckVersion(checkAssignedResp, jobDetailsResponseGlobal, null);
                            onResume();
                        } else {
                            sendToAllFormActivity(isAvailable, "");
                        }

                        String userID = UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUserID();
                        ConfirmTemplateUpdate.sendTemplateUpdateStatusToserver(mContext, userID, templateName, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            IOUtils.stopLoading();
        }

        @Override
        public void success(Object obj1, JSONObject jsonObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    IOUtils.appendLog(Tag + " :" + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_GET_UPDATED_TEMPLATE + "\nRESPONSE " + volleyError.getMessage().toString());
                    MyDynamicToast.warningMessage(mContext, "Unable to connect");
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

    private void sendToAllFormActivity(boolean isAvailable, String response) {
        Intent intent = new Intent(mContext, AllFormsActivity.class);
        nbfcName = new Const().DATABASE_NAME;
        intent.putExtra("response", response);
        intent.putExtra("isAccept", isAvailable);
        intent.putExtra("job_id", job_id);
        intent.putExtra("nbfcName", nbfcName);
        intent.putExtra("templateName", templateName);
        intent.putExtra("cityName", cityName);
        intent.putExtra("pinCode", pincode);
        intent.putExtra("productName", productName);
        intent.putExtra("templateName", templateName);
        intent.putExtra("creationTime", creationTime);
        intent.putExtra("hasCoApplicant", hasCoApplicant);
        intent.putExtra("applicationFormNo", applicationFormNo);
        intent.putExtra("mobileNo", mobileNo);
        intent.putExtra("address", address);
        intent.putExtra("coApplicantArray", coApplicantArray);
        intent.putExtra("totalAssignedTime", totalAssignedTime);
        intent.putExtra("jobStartTime", jobStartTime);
        intent.putExtra("position", 0);
        intent.putExtra("subCategoryName", DusmileBaseActivity.subCategoryName);
        if (isAvailable) {
            AppConstant.isAvilableJobs = true;
            AppConstant.isAssinedJobs = false;
            AppConstant.isCompletedJobs = false;
        } else {
            AppConstant.isAssinedJobs = true;
            AppConstant.isCompletedJobs = false;
            AppConstant.isAvilableJobs = false;
        }
        mContext.startActivity(intent);
    }


    public void holdJob(String holdReason, String jobId, String nbfcName, String nextDate) {
        IOUtils.startLoading(getActivity(), "Please wait.....");
        IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_HOLD_JOB + "/" + jobId);
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject jobData = new JSONObject();
            jobData.put("holdreason", holdReason);
            jobData.put("nextAppDate", nextDate);
            jsonObject.put("jobData", jobData);
            jsonObject.put("NBFCName", nbfcName);
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        new HttpVolleyRequest(mContext, jsonObject, new Const().REQUEST_HOLD_JOB + "/" + jobId, listenerdeAssignJob);
    }

    MyListener listenerdeAssignJob = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void success(Object obj, JSONObject jsonObject) throws JSONException {

            if (obj != null) {
                String holdJobResponse = obj.toString();
                JSONObject holdjsonObject = new JSONObject(holdJobResponse);
                if (holdjsonObject.length() > 0) {
                    try {
                        gson = new Gson();
                        HoldJobResponseModel holdJobResponseModel = gson.fromJson(holdJobResponse, HoldJobResponseModel.class);
                        IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_HOLD_JOB + "  " + holdJobResponseModel.getSuccess());
                        if (holdJobResponseModel.getSuccess() == true) {
                            MyDynamicToast.successMessage(mContext, "Job is put on hold successfully");
                            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            getActivity().finish();
                        } else {
                            MyDynamicToast.successMessage(mContext, "Job Hold Failed");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    MyDynamicToast.successMessage(mContext, "Something went wrong");
                }
            }
            IOUtils.stopLoading();
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_HOLD_JOB + "\nRESPONSE " + volleyError.getMessage().toString());
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


    private void swipeRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mContext != null) {
                    searchEditText.setText("");
                    onResume();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private String getURL() {
        String url = new Const().REQUEST_GET_AVAILABLE_JOBS;
        String UserID = UserPreference.readString(getActivity(), UserPreference.USER_INT_ID, null);
        if (AppConstant.isAssinedJobs) {
            if (!TextUtils.isEmpty(UserID)) {
                url = new Const().REQUEST_GET_ASSIGNED_JOBS + "/" + UserID;
            }
        } else if (AppConstant.isCompletedJobs) {
            url = new Const().REQUEST_GET_COMPLETED_JOBS + "/" + UserID;
        }
        return url;
    }


    private void showOfflineAssignedJobs() {
        try {
            DBHelper dbHelper = DBHelper.getInstance(mContext);
            ArrayList<OfflineAssignedJobs> allOfflineAssignedJobsJson = OfflineAssignedJobsDB.getAllOfflineAssignedJobsJson(dbHelper);
            if (allOfflineAssignedJobsJson != null && allOfflineAssignedJobsJson.size() > 0) {
                String offlineAssignedJobJson = allOfflineAssignedJobsJson.get(0).getOffline_assigned_jobs_json();
                if (!TextUtils.isEmpty(offlineAssignedJobJson)) {
                    try {
                        JSONObject jsonObject = new JSONObject(offlineAssignedJobJson);
                        String success = String.valueOf(jsonObject.getBoolean("success"));
                        if (!TextUtils.isEmpty(success) && success.equalsIgnoreCase("true")) {
                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + "Showing offline Assigned jobs");
                            reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                            reportDataArray = jsonObject.getJSONArray("reportData");
                            reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                            cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                            if (reportDataArray != null) {
                                if (reportDataArray.length() > 0) {
                                    // exportBtn.setVisibility(View.VISIBLE);
                                    setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                                } else {
                                    // exportBtn.setVisibility(View.GONE);
                                    MyDynamicToast.informationMessage(mContext, "No Data Available");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Something went wrong");
                }
            } else {
                MyDynamicToast.errorMessage(mContext, "Something went wrong");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleResponseToCheckVersion(String response, JobDetailsResponse jobDetailsResponse, ProgressDialog assignJobProgressBar) {

        //check version no with database version no
        try {
            String applicant_Json = "";
            JSONObject jsonObject = new JSONObject(response);
            if (response.contains("details")) {
                JSONObject details = jsonObject.getJSONObject("details");
                if (details.has("ProcessDataJSON")) {
                    JSONObject processDataJson = details.getJSONObject("ProcessDataJSON");
                    if (processDataJson.has("Applicant")) {
                        applicant_Json = processDataJson.getString("Applicant");
                    }
                }
            }
            IOUtils.appendLog(Tag + ": API " + new Const().REQUEST_JOB_DETAILS1 + "\nRESPONSE " + jobDetailsResponse.getSuccess().toString());
            String response_version_no = jobDetailsResponse.getLatestVersion();
            String app_language = UserPreference.getLanguage(mContext);
            ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateAccVerionNo(dbHelper, templateName, nbfcName, app_language, response_version_no);
            String clientTemplateVersion = clientTemplate.getVersion();
            if (clientTemplate.getID() != null) {
                //do not update template use this template to render form
                addEntryInDB(app_language, jobDetailsResponse, clientTemplate.getIs_deprecated(), applicant_Json);
                IOUtils.stopLoading();
            } else {
                if (!TextUtils.isEmpty(clientTemplateVersion)) {
                    getUpdatedTemplate(String.valueOf(clientTemplateVersion).toString());
                } else {
                    getUpdatedTemplate("Residence");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEntryInDB(String app_language, JobDetailsResponse jobDetailsResponse, String isDeprecatedFlag, String applicant_Json) {
        AssignedJobs assignedJob = AssignedJobsDB.getJobsByJobId(dbHelper, jobDetailsResponse.getJOBID());
        if (AllFormsActivity.isAccept == true || assignedJob.getAssigned_jobId() == null) {
            ClientTemplate clientTemplate = ClientTemplateDB.getSingleClientTemplateByTemplateNameClientLanguage(dbHelper, templateName, nbfcName, app_language, isDeprecatedFlag);
            //insert entry into assigned jobs table to maintain assigned jobs
            insertIntoAssignedJobs(clientTemplate.getID(), jobDetailsResponse.getJOBID(), applicant_Json);

        }
    }


    private void insertIntoAssignedJobs(String client_template_id, String jobID, String applicantJson) {

        IOUtils.appendLog(Tag + ": Inserting entries into assigned job");

        //do not add entry if its already present in db
        AssignedJobs assignedJobsExisting = AssignedJobsDB.getJobsByJobId(dbHelper, jobID);
        if (assignedJobsExisting.getID() == null) {

            //String applicantJson = gson.toJson(applicant);
            SimpleDateFormat formatter = null;
            if (creationTime.contains("/")) {
                formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            }
            Date oldDate = null;
            String endTime = null;
            try {
                oldDate = formatter.parse(creationTime);
                Date newDate = DateUtils.addHours(oldDate, 3);
                endTime = formatter.format(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            AssignedJobs assignedJobs = new AssignedJobs();
            assignedJobs.setClient_template_id(client_template_id);
            assignedJobs.setAssigned_jobId(jobID);
            assignedJobs.setOriginal_applicant_json(applicantJson);
            assignedJobs.setApplicant_json(applicantJson);
            assignedJobs.setJob_end_time(endTime);
            assignedJobs.setIS_IN_PROGRESS("true");
            assignedJobs.setIs_submit("false");
            AssignedJobsDB.addAssignedJobsEntry(assignedJobs, dbHelper);
        }
    }


    public void storeOfflineAssignedJobs(String url) {
        try {
            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url);
            new HttpVolleyRequest(mContext, url, listenerAssignedJobs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    MyListener listenerAssignedJobs = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
            try {
                // IOUtils.stopLoading();
                if (obj != null) {
                    String url = getURL();
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);

                        /*gson = new Gson();
                        AvailableJobsDataModel availableJobsModel = gson.fromJson(response, AvailableJobsDataModel.class);*/
                        boolean status = jsonObject.getBoolean("success");
                        if (status == true) {

                            IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + String.valueOf(status));

                            JSONArray reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                            JSONArray reportDataArray = jsonObject.getJSONArray("reportData");
                            JSONArray reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                            DBHelper dbHelper = DBHelper.getInstance(mContext);
                            OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                            OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                            offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                            OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                            UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                //IOUtils.stopLoading();
                if (volleyError != null) {
                    String url = getURL();
                    MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + volleyError.getMessage());
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


    private void deleteJobDetailsIfJobDeAssigned(String jobId) {
        try {
            dbHelper = DBHelper.getInstance(getActivity());
            AssignedJobs assignedJobs = AssignedJobsDB.getJobsByJobId(dbHelper, jobId);
            if (!TextUtils.isEmpty(assignedJobs.getAssigned_jobId())) {
                AssignedJobsDB.removeJobByJobId(dbHelper, jobId);
                AssignedJobsStatusDB.removeJobStatusByJobId(dbHelper, jobId);
                if (UpdateJobStatus.jobIdList.contains(jobId)) {
                    UpdateJobStatus.jobIdList.remove(jobId);
                }
                File sdCard = Environment.getExternalStorageDirectory();
                File directory = new File(sdCard.getAbsolutePath() + "/Dusmile/pdf/" + UserPreference.getUserRecord(DusmileApplication.getAppContext()).getUsername() + "/" + jobId);
                if (directory.isDirectory()) {
                    String[] children = directory.list();
                    for (int i = 0; i < children.length; i++) {
                        new File(directory, children[i]).delete();
                    }
                    directory.delete();
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + jobId + " Job is DeAssign. Deleted job info from table and from folder");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReportMetaData(String url) {

        IOUtils.startLoading(mContext, "Loading......");
        try {
            IOUtils.appendLog(Tag + " : " + IOUtils.getCurrentTimeStamp() + " API " + url);
            new HttpVolleyRequest(mContext, url, listenerReportMetaData);
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
    }

    MyListener listenerReportMetaData = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String url = getURL();
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        reportHeadersArray = new JSONArray();
                        reportHeadersUIArray = new JSONArray();
                        cardHeadersKeyArray = new JSONArray();
                        reportDataArray = new JSONArray();
                        reportHeadersArray = jsonObject.getJSONArray("reportHeaders");
                        reportHeadersUIArray = jsonObject.getJSONArray("reportHeadersKeys");
                        cardHeadersKeyArray = jsonObject.getJSONArray("cardHeadersKeys");
                        resourceJsonObj = jsonObject.getJSONObject("resources");
                        getJobDataApi(jsonObject,resourceJsonObj);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {

        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    String url = getURL();
                    String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String success = jsonObject.getString("success");
                    if (success.equals("false")) {
                        String message = jsonObject.getString("message");
                        MyDynamicToast.informationMessage(mContext, message);
                    } else {
                        MyDynamicToast.warningMessage(mContext, "Unable to Connect");
                        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + url + "\nRESPONSE " + volleyError.getMessage());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
                            IOUtils.sendUserToLogin(mContext, getActivity());
                        }
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

    private void getJobDataApi(JSONObject jsonObject,JSONObject resources) {
        IOUtils.startLoading(mContext, "Loading......");
        try {
            JSONObject reportHeaderKeys = jsonObject.getJSONObject("reportHeaderKeys");
            gson = new Gson();
            AssignedJobsResources assignedJobsResources = gson.fromJson(String.valueOf(resources), AssignedJobsResources.class);
            JSONObject jsonObject1 = new JSONObject();
            String URL = new Const().BASE_URL + assignedJobsResources.getDataApi();
            jsonObject1.put("reportHeaderKeys", reportHeaderKeys);
            new HttpVolleyRequest(mContext, jsonObject1, URL, listenerReporData);
            // reportDataArray = jsonObject.getJSONArray("reportData");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    MyListener listenerReporData = new MyListener() {

        @Override
        public void success(Object obj) throws JSONException {
        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Log.d("DUSMILE", response);
                        reportDataArray = jsonObject.getJSONArray("reportData");
                        if (AppConstant.isAssinedJobs) {
                            DBHelper dbHelper = DBHelper.getInstance(mContext);
                            OfflineAssignedJobsDB.removeOfflineAssignedJobs(dbHelper);
                            OfflineAssignedJobs offlineAssignedJobs = new OfflineAssignedJobs();
                            offlineAssignedJobs.setOffline_assigned_jobs_json(response);
                            OfflineAssignedJobsDB.addOfflineAssignedJobsEntry(offlineAssignedJobs, dbHelper);
                            UserPreference.writeInteger(mContext, UserPreference.ASSIGNED_CNT, reportDataArray.length());
                        }
                        if (reportDataArray != null) {
                            if (reportDataArray.length() > 0) {
                                // exportBtn.setVisibility(View.VISIBLE);
                                setAdapter(reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                            } else {
                                // exportBtn.setVisibility(View.GONE);
                                MyDynamicToast.informationMessage(mContext, "No Data Available");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    MyDynamicToast.errorMessage(mContext, volleyError.getLocalizedMessage());
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }
        }
    };
}
