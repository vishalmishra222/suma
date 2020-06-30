package com.app.dusmile.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.app.dusmile.DBModel.SubCategory;
import com.app.dusmile.R;
import com.app.dusmile.activity.ReportDetailsActivity;
import com.app.dusmile.adapter.ReportDetailsAdapter;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.database.LoginTemplateDB;
import com.app.dusmile.database.SubCategoryDB;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.app.dusmile.utils.RecyclerItemClickListener;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms2.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by suma on 03/02/17.
 */

public class ReportDetailsFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReportDetailsAdapter reportDetailsAdapter;
    private EditText searchEditText;
    private Button cancelButton;
    public static FragmentTransaction fragmentTransaction;
    private Toast toast;
    private List<SubCategory> subCategoryMenuList;
    private static String PURL = null;
    private DBHelper dbHelper;
    private static TextView tv_msg;
    private static LinearLayout parent_layout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View toastRoot;
    private String Tag = "ReportDetailsFragment";
    private JSONArray reportHeadersArray;
    private JSONArray reportDataArray;
    private JSONArray reportHeadersUIArray;
    private JSONArray cardHeadersKeyArray;
    private Button exportBtn;
    JSONArray filterJsonArray;
    JSONArray exportJsonArray = new JSONArray();

    public ReportDetailsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_details, container, false);
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        mLayoutManager = new LinearLayoutManager(mContext);
        this.findViews(rootView);
        recyclerView.setLayoutManager(mLayoutManager);
        dbHelper = DBHelper.getInstance(mContext);
        Activity activity = this.getActivity();
        mContext = activity;
        this.searchReportListener();
        this.onCancelButtonClicked();
        //this.onExportButtonClicked();
        this.swipeRefreshListener();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchEditText.setText("");
        if (reportDetailsAdapter != null) {
            reportHeadersUIArray = new JSONArray();
            reportHeadersArray = new JSONArray();
            reportDataArray = new JSONArray();
            cardHeadersKeyArray = new JSONArray();
            reportDetailsAdapter.refresh(reportDataArray);
            reportDetailsAdapter.notifyDataSetChanged();
        }
        String date = ReportDetailsActivity.date;

        if (IOUtils.isInternetPresent(mContext)) {
            assignUrl();
            getReportDetails(date);
        } else {
            IOUtils.showErrorMessage(mContext, "No internet connection");
        }
    }


    private void assignUrl() {
        subCategoryMenuList = new ArrayList<>();
        int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper, "Menu Details", UserPreference.getLanguage(mContext));
        subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnIsMenuFlag(dbHelper, String.valueOf(loginJsonTemplateId), "false");
        for (int i = 0; i <= subCategoryMenuList.size() - 1; i++) {
            String jobClicked = subCategoryMenuList.get(i).getSubcategory_name();
            String usrId = UserPreference.readString(mContext, UserPreference.USER_INT_ID, "");
            if (jobClicked.equalsIgnoreCase("Jobs Submitted To Supervisor")) {
                String act = subCategoryMenuList.get(i).getAction();
                String st = act.substring(1, act.length());
                String replaceString = st.replace("userId", usrId);
                PURL = new Const().URL + replaceString;
            }
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

        recyclerView = (RecyclerView) view.findViewById(R.id.report_recycler_view);
        searchEditText = (EditText) view.findViewById(R.id.searchbox);
        cancelButton = (Button) view.findViewById(R.id.searchBoxCancel);
        exportBtn = (Button) view.findViewById(R.id.Exportbtn);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }

    public void getReportDetails(String date) {

        IOUtils.startLoading(mContext, "Loading......");

        JSONObject criteriajsonObject = new JSONObject();

        try {
            // criteriajsonObject.put("startDate", date);
            // criteriajsonObject.put("endDate", date);
            // criteriajsonObject.put("FOSExecutiveID", UserPreference.getUserRecord(mContext).getUserID());
            // jsonObject.put("reportName", "Status Report");
            // jsonObject.put("redirectPage", "displayReport");
            //jsonObject.put("database", Const.DATABASE_NAME);
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // String currentTime = sdf.format(new Date());
            // criteriajsonObject.put("startDate", currentTime);
            // criteriajsonObject.put("endDate", currentTime);
            criteriajsonObject.put("FOSExecutiveID", UserPreference.getUserRecord(mContext).getUserID());
            // criteriajsonObject.put("process_queue_id", "103");
            //Here Add Firebase LogEvent
            //  Bundle bundle = new Bundle();
            // bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, jsonObject.toString());

            // DssApp.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + PURL + "\nREQUEST " + criteriajsonObject.toString());
        new HttpVolleyRequest(mContext, criteriajsonObject, PURL, listenerJobs);
    }

    MyListener listenerJobs = new MyListener() {
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
                        if (jsonObject.getBoolean("success") == true) {
                            //IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_REPORT_DETAILS + "\nRESPONSE " + true);
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
                        } else {
                            // IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_REPORT_DETAILS + "\nRESPONSE " + false);
                            String message = jsonObject.getString("message");
                            MyDynamicToast.informationMessage(mContext, message);
                        }
                    } else {
                        MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                }
            } catch (Exception e) {
                // IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_REPORT_DETAILS + "\nRESPONSE " + e.getMessage());
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                IOUtils.stopLoading();
                if (volleyError != null) {
                    // IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_REPORT_DETAILS + "\nRESPONSE " + volleyError.getMessage());
                    MyDynamicToast.warningMessage(mContext, "Unable to connect");
                    if (volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
                    }
                } else {
                    MyDynamicToast.errorMessage(mContext, "Server Error !!");
                }
            } catch (Exception e) {
                // IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_REPORT_DETAILS + "\nRESPONSE " + e.getMessage());
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
            }
        }
    };

    public void setAdapter(JSONArray reportHeadersArray, JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray) {
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " " + "Showing reports in List");
        reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);

        recyclerView.setAdapter(reportDetailsAdapter);
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

    private void searchReportListener() {
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
                            reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, filterJsonArray, reportHeadersUIArray, cardHeadersKeyArray);
                            recyclerView.setAdapter(reportDetailsAdapter);

                        }

                    } else {
                        try {
                            cancelButton.setVisibility(View.GONE);
                            if (reportDataArray != null) {
                                if (reportDataArray.length() == 0) {
                                    // exportBtn.setVisibility(View.GONE);
                                    showAToast("No Data Available");
                                } else {
                                    // exportBtn.setVisibility(View.VISIBLE);
                                }
                                reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                                recyclerView.setAdapter(reportDetailsAdapter);

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
                            showAToast("No Data Available");
                        } else {
                            // exportBtn.setVisibility(View.VISIBLE);
                        }
                        reportDetailsAdapter = new ReportDetailsAdapter(mContext, reportHeadersArray, reportDataArray, reportHeadersUIArray, cardHeadersKeyArray);
                        recyclerView.setAdapter(reportDetailsAdapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
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


    private void onExportButtonClicked() {
      /*  exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExportChoiceDialog();
            }
        });*/
    }

   /* private void showExportChoiceDialog()
    {
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" "+"Showing export reports menus");
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
    }*/


    /*public class doExportWork extends AsyncTask<String,Void,Boolean>
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

    /*private boolean exportReportListToCSV()
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
            for(int i =0;i<reportHeadersArray.length();i++)
            {
                if(reportHeadersArray.get(i).toString().contains("Location")||reportHeadersArray.get(i).toString().contains("View")||reportHeadersArray.get(i).toString().contains("Perform Job")){}
                else {headers.add(reportHeadersArray.get(i).toString());
                }
            }
            if(filterJsonArray!=null&& searchEditText.getText().toString().length()>0 && filterJsonArray.length()>0)
            {
                exportJsonArray = filterJsonArray;
            }
            else
            {
                exportJsonArray = reportDataArray;
            }
            for(int j=0;j<exportJsonArray.length();j++) {
                List<String> data = new ArrayList<>();
                for (int k = 0; k < reportHeadersUIArray.length(); k++) {
                    JSONObject headersUIJsonObject = reportHeadersUIArray.getJSONObject(k);
                    String uiField = headersUIJsonObject.getString("data");
                    JSONObject jsonObject = exportJsonArray.getJSONObject(j);
                    if (uiField != null && uiField.contains(".")) {
                        try {
                            String fieldDataArray[] = uiField.split("\\.");
                            String fieldDataVal = fieldDataArray[0];
                            JSONObject dataJsonObject = jsonObject.getJSONObject(fieldDataVal);
                            JSONObject applicationJsonObject = dataJsonObject.getJSONObject(fieldDataArray[(fieldDataArray.length) - (fieldDataArray.length - 1)]);
                            String fieldFinalVal = applicationJsonObject.getString(fieldDataArray[fieldDataArray.length - 1]);
                            Log.i("MYDATA", fieldFinalVal);
                            data.add(fieldFinalVal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            data.add("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            data.add("");
                        }
                    } else {
                        try {
                            String fieldFinalVal = jsonObject.getString(uiField);
                            Log.i("MYDATA", fieldFinalVal);
                            data.add(fieldFinalVal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            data.add("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            data.add("");
                        }
                    }
                }
                Data.add(data);
            }

            fileName = "Report".concat(sdf.format(myCalendar.getTime()))+".csv".trim();
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
        }
        catch (Exception e)
        {
            result = false;
        }
        return  result;
    }


    public boolean exportReportListToExcel(){
        IOUtils.appendLog(Tag+" "+IOUtils.getCurrentTimeStamp()+" Exporting Reports to Excel ");
        boolean result = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
            final Calendar myCalendar = Calendar.getInstance();
            String fileName = "";
            File directory = null;
            File sdCard = Environment.getExternalStorageDirectory();
            fileName = "Report".concat(sdf.format(myCalendar.getTime()))+".xls".trim();
            directory = new File(sdCard.getAbsolutePath() + "/Dusmile/EXCEL");

            int val = 0;
            //create directory if not exist
            if (!directory.isDirectory()) {
                directory.mkdirs();
            }

            List<String> headers = new ArrayList<>();
            List<List<String>> Data = new ArrayList<>();

            headers.clear();
            for (int i=0;i<reportHeadersArray.length();i++) {
                if (reportHeadersArray.get(i).toString().contains("Location") || reportHeadersArray.get(i).toString().contains("View") || reportHeadersArray.get(i).toString().contains("Perform Job")) {
                } else {
                    headers.add(reportHeadersArray.get(i).toString());
                }
            }

            if(filterJsonArray!=null&& searchEditText.getText().toString().length()>0 && filterJsonArray.length()>0)
            {
                exportJsonArray = filterJsonArray;
            }
            else
            {
                exportJsonArray = reportDataArray;
            }

            for(int j=0;j<exportJsonArray.length();j++) {
                List<String> data = new ArrayList<>();
                for (int k = 0; k < reportHeadersUIArray.length(); k++) {
                    JSONObject headersUIJsonObject = reportHeadersUIArray.getJSONObject(k);
                    String uiField = headersUIJsonObject.getString("data");
                    JSONObject jsonObject = exportJsonArray.getJSONObject(j);
                    if (uiField != null && uiField.contains(".")) {
                        try {
                            String fieldDataArray[] = uiField.split("\\.");
                            String fieldDataVal = fieldDataArray[0];
                            JSONObject dataJsonObject = jsonObject.getJSONObject(fieldDataVal);
                            JSONObject applicationJsonObject = dataJsonObject.getJSONObject(fieldDataArray[(fieldDataArray.length) - (fieldDataArray.length - 1)]);
                            String fieldFinalVal = applicationJsonObject.getString(fieldDataArray[fieldDataArray.length - 1]);
                            Log.i("MYDATA", fieldFinalVal);
                            data.add(fieldFinalVal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            data.add("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            data.add("");
                        }
                    } else {
                        try {
                            String fieldFinalVal = jsonObject.getString(uiField);
                            Log.i("MYDATA", fieldFinalVal);
                            data.add(fieldFinalVal);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            data.add("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            data.add("");
                        }
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
        return  result;

    }*/

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

}
