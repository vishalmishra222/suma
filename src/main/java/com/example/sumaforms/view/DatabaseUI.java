package com.example.sumaforms.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.sumaforms.preferences.AppConstant;
import com.example.sumaforms.DBmodel.AssignedJobsDB;
import com.example.sumaforms.helper.DBHelper;
import com.example.sumaforms.activity.MapActivity;
import com.example.sumaforms.common.MonthYearPicker;
import com.example.sumaforms.R;
import com.example.sumaforms.model.SubProcessFieldDataResponse;
import com.example.sumaforms.activity.AllFormsActivity;
import com.example.sumaforms.activity.LocationActivity;
import com.example.sumaforms.fragment.MapUpdateFragment;
import com.example.sumaforms.fragment.UploadImageFragment;
import com.example.sumaforms.listener.BtnClickListener;
import com.example.sumaforms.listener.ImageOperationListener;
import com.example.sumaforms.model.JobDetailsResponse;
import com.example.sumaforms.model.ReportFilterModel;
import com.example.sumaforms.utils.IOUtils;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;


/**
 * Created by sumasoft on 23/01/17.
 */

public class DatabaseUI {
    static ArrayList<Integer> arrayListCheckBoxPosition = new ArrayList<>();
    private HashMap<String, RadioButton> customValidRadioButtontHashMap = new HashMap<>();
    private static View viewParent;
    MapUpdateFragment ImageFragment = null;
    private static HashMap<String, Button> buttonsHashMap = new HashMap<>();
    private HashMap<String, CheckBox> checkBoxHashMap = new HashMap<>();
    private static HashMap<String, Button> mapbuttonsHashMap = new HashMap<>();
    private static HashMap<Integer, EditText> customValidEditTextHashMap = new HashMap<>();
    private static HashMap<Integer, EditText> showHideEditTextHashMap = new HashMap<>();
    static ArrayList<EditText> arrayListForMandatoryTableFields = new ArrayList<>();
    static ArrayList<EditText> reportFilters = new ArrayList<>();
    private static boolean otherLoanInformationSelected;
    private static AwesomeValidation mAwesomeValidation;
    SparseArray<EditText> array = new SparseArray<EditText>();
    HashMap<String, String> templateTextViewData = new HashMap<>();
    HashMap<String, String> editTextData = new HashMap<>();
    HashMap<String, String> textViewData = new HashMap<>();
    HashMap<Integer, String> getDateOfBirth = new HashMap<>();
    HashMap<String, EditText> hashMapEditText = new HashMap<>();
    //static HashMap<String,HashMap<String,ArrayList<String>>> hashMapCustomValidation = new HashMap<>();
    static ArrayList<String> arrayListFields = new ArrayList<>();
    HashMap<String, String> staticFieldsController = new HashMap<>();
    HashMap<String, String> dynamicFieldsController = new HashMap<>();
    public static int addCount = 0;
    private static int noOfUncheck = 0;
    private static HashMap<Boolean, ArrayList<String>> hashMapToggleFields = new HashMap<>();
    private static ArrayList<String> dependentList = new ArrayList<>();
    Context context;
    static String jobId;
    static Activity activity;
    static FragmentManager fragmentManager;

    public DatabaseUI(FragmentActivity activity, AwesomeValidation mAwesomeValidation, FragmentActivity fragmentActivity, android.support.v4.app.FragmentManager childFragmentManager, String jobId) {
    }

    public DatabaseUI(Context context, AwesomeValidation mAwesomeValidation, Activity activity, FragmentManager fragmentManager, String jobId) {
        this.context = context;
        this.mAwesomeValidation = mAwesomeValidation;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.jobId = jobId;
    }

    //create UI for textView
    public static TextView createTextView(Context mContext, String text, LinearLayout parent, int textColor, float textSize, String headerType, String tag, String customerName, String coApplicantName) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        textView.setLayoutParams(params);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setTag(tag);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        params.setMargins(0, 0, 0, 0);
        if (text == null) {
            text = "";
        }
        if (headerType.equalsIgnoreCase("tabs")) {
            textView.setText("View Job Details");
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);

        } else if (headerType.equalsIgnoreCase("form")) {
            if (text.equalsIgnoreCase("Applicant Profile") || text.equalsIgnoreCase("Employment") || text.equalsIgnoreCase("Office") || text.equalsIgnoreCase("Business")) {
                textView.setText(text + " : " + customerName);
            } else if (text.equalsIgnoreCase("CoApplicant Profile") || text.equalsIgnoreCase("CoApplicant Office") || text.equalsIgnoreCase("CoApplicant Business")) {
                // textView.setText(text + " : " + coApplicantName);
            } else {
                textView.setText(text);
            }
            textView.setGravity(Gravity.LEFT);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(20, 0, 0, 0);
        } else if (headerType.equalsIgnoreCase("Job")) {
            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
        } else if (headerType.equalsIgnoreCase("fieldHeader")) {
            textView.setText(text);
            textView.setTypeface(null, Typeface.BOLD);
            textView.setGravity(Gravity.LEFT);
            textView.setTextColor(mContext.getResources().getColor(R.color.accent));
        } else if (headerType.equalsIgnoreCase("label")) {
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
        } else {
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
        }
        parent.addView(textView);
        return textView;
    }


   /* //create UI for editText
    public static void createMapView(final Context mContext, final SubProcessFieldDataResponse1.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant) {
        final MapView mapView = new MapView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
        editText.setLayoutParams(params);
        params.setMargins(5,5,5,5);
        textInputLayout.setLayoutParams(params);
        editText.setTag(subProcessField.getKey());
        editText.setPadding(10, 10, 10, 20);
        editText.setId(subProcessField.getFieldID());
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        //add validation pattern
        mAwesomeValidation.addValidation(editText, subProcessField.getValidationPattern(), subProcessField.getValidationMessage());
        if(subProcessField.getIsMandatory()){
            textInputLayout.setHintTextAppearance(R.style.error_appearance);
            editText.setHintTextColor(Color.RED);
        }
        else
        {
            textInputLayout.setHintTextAppearance(R.style.TextLabel);
        }

        Method method = null;
        try {
            method =JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
            String text = (String) method.invoke(applicant,null);
            if(text!=null && !text.isEmpty()) {
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(text);
                textInputLayout.setHintAnimationEnabled(true);
            }
            else{
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());

            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            textInputLayout.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("");
            textInputLayout.setHintAnimationEnabled(true);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
            textInputLayout.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("");
            textInputLayout.setHintAnimationEnabled(true);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            textInputLayout.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("");
            textInputLayout.setHintAnimationEnabled(true);

        }catch (Exception e){
            e.printStackTrace();;
            textInputLayout.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("");
            textInputLayout.setHintAnimationEnabled(true);

        }


        if(subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers") || subProcessField.getValidation().equalsIgnoreCase("Decimal")){
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else if(subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")){
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            Pattern sPattern
                    = Pattern.compile(subProcessField.getValidationPattern());
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText edt = (EditText) v;
                if(!v.hasFocus()){
                    edt.setSelection(edt.getText().length());
                    edt.setHint(subProcessField.getLable());
                    if(subProcessField.getValidationPattern()!=null && !subProcessField.getValidationPattern().isEmpty()) {

                        if (!isValid(edt.getText().toString())) {
                            //   editText.setText("");
                            editText.setError(subProcessField.getValidationMessage());
                        } else {
                            editText.setError(null);
                        }
                    }
                }else{
                    edt.setHint("");
                }
            }
            private boolean isValid(CharSequence s) {
                return sPattern.matcher(s).matches();
            }
        });


        if(subProcessField.getIsreadonly()!=null && subProcessField.getIsreadonly()){
            editText.setBackgroundResource(R.drawable.disabled_edittext_background);
            editText.setEnabled(false);
        }else{
            editText.setEnabled(true);
        }
        try {
            viewParent = parent;
            if(hashMapToggleFields.size()>0){
                Map.Entry<Boolean,ArrayList<String>> entry=hashMapToggleFields.entrySet().iterator().next();
                Boolean hashMapkey= entry.getKey();
                ArrayList<String> arrayListFields=entry.getValue();
                if(arrayListFields != null) {
                    for (int i = 0; i < arrayListFields.size(); i++) {
                        if (subProcessField.getKey().trim().equalsIgnoreCase(arrayListFields.get(i).trim())) {
                            if (hashMapkey == true) {
                                //editText.setBackground(null);
                                editText.setEnabled(true);
                            } else {
                                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                editText.setEnabled(false);
                            }
                            break;
                        }
                        *//*if(editText.getHint().toString().equalsIgnoreCase(arrayListFields.get(i).trim())) {
                            if (hashMapkey == true) {
                                //editText.setBackground(null);
                                editText.setEnabled(true);
                            } else {
                                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                editText.setEnabled(false);
                            }
                            break;
                        }*//*
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        textInputLayout.addView(editText);
        parent.addView(textInputLayout);

    }*/


    //create UI for editText
    public static void createEditText(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant, String applicant_json) {
        final TextInputLayout textInputLayout = new TextInputLayout(mContext);
        final TextInputEditText editText = new TextInputEditText(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        editText.setLayoutParams(params);
        params.setMargins(5, 5, 5, 5);
        textInputLayout.setLayoutParams(params);
        editText.setTag(subProcessField.getKey());
        editText.setPadding(10, 10, 10, 20);
        editText.setId(subProcessField.getFieldID());
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        //subProcessField.getIsHidden()
        //add validation pattern
        mAwesomeValidation.addValidation(editText, subProcessField.getValidationPattern(), subProcessField.getValidationMessage());
        if (subProcessField.getIsMandatory()) {
            textInputLayout.setHintTextAppearance(R.style.error_appearance);
            editText.setHintTextColor(Color.RED);

        } else {
            textInputLayout.setHintTextAppearance(R.style.TextLabel);
        }

        Method method = null;
        JSONObject jsonObject = null;
        String val = null;
        try {
            jsonObject = new JSONObject(applicant_json);
            val = jsonObject.getString(subProcessField.getKey());
            method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
            String text = (String) method.invoke(applicant, null);
            if (text != null && !text.isEmpty()) {
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(text);
                textInputLayout.setHintAnimationEnabled(true);
            } else {
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);

            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            textInputLayout.setHint(subProcessField.getLable());
            editText.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText(val);
            textInputLayout.setHintAnimationEnabled(true);

        } catch (InvocationTargetException e) {

            e.printStackTrace();
            textInputLayout.setHint(subProcessField.getLable());
            editText.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText(val);
            textInputLayout.setHintAnimationEnabled(true);

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            textInputLayout.setHint(subProcessField.getLable());
            editText.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText(val);
            textInputLayout.setHintAnimationEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            textInputLayout.setHint(subProcessField.getLable());
            editText.setHint(subProcessField.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("");
            textInputLayout.setHintAnimationEnabled(true);

        }


        if (subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers") || subProcessField.getValidation().equalsIgnoreCase("Decimal")) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if (subProcessField.getMaxlength() != null && subProcessField.getMaxlength() > 0) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(subProcessField.getMaxlength())});
        }
        boolean isDependentField = false;
        if (dependentList.size() > 0) {
            for (int l = 0; l < dependentList.size(); l++) {
                if (dependentList.get(l).equalsIgnoreCase(subProcessField.getKey())) {
                    isDependentField = true;
                }
            }
        }
        if (subProcessField.getIsHidden() != null && subProcessField.getIsHidden() == true) {
            if (!TextUtils.isEmpty(editText.getText().toString()) || isDependentField) {
                editText.setVisibility(View.VISIBLE);
            } else {
                editText.setVisibility(View.GONE);
            }
        } else {
            editText.setVisibility(View.VISIBLE);
        }
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            Pattern sPattern
                    = Pattern.compile(subProcessField.getValidationPattern());

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText edt = (EditText) v;
                if (!v.hasFocus()) {
                    edt.setSelection(edt.getText().length());
                    edt.setHint(subProcessField.getLable());
                    if (subProcessField.getValidationPattern() != null && !subProcessField.getValidationPattern().isEmpty()) {

                        if (!isValid(edt.getText().toString())) {
                            //   editText.setText("");
                            editText.setError(subProcessField.getValidationMessage());
                        } else {
                            editText.setError(null);
                        }
                    }
                } else {
                    edt.setHint("");
                }
            }

            private boolean isValid(CharSequence s) {
                return sPattern.matcher(s).matches();
            }
        });


        if (subProcessField.getIsreadonly() != null && subProcessField.getIsreadonly()) {
            editText.setBackgroundResource(R.drawable.disabled_edittext_background);
            editText.setEnabled(false);
        } else {
            editText.setEnabled(true);
        }
        try {
            viewParent = parent;
            if (hashMapToggleFields.size() > 0) {
                Map.Entry<Boolean, ArrayList<String>> entry = hashMapToggleFields.entrySet().iterator().next();
                Boolean hashMapkey = entry.getKey();
                ArrayList<String> arrayListFields = entry.getValue();
                if (arrayListFields != null) {
                    for (int i = 0; i < arrayListFields.size(); i++) {
                        if (subProcessField.getKey().trim().equalsIgnoreCase(arrayListFields.get(i).trim())) {
                            if (hashMapkey == true) {
                                //editText.setBackground(null);
                                editText.setEnabled(true);
                            } else {
                                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                editText.setEnabled(false);
                            }
                            break;
                        }
                        /*if(editText.getHint().toString().equalsIgnoreCase(arrayListFields.get(i).trim())) {
                            if (hashMapkey == true) {
                                //editText.setBackground(null);
                                editText.setEnabled(true);
                            } else {
                                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                editText.setEnabled(false);
                            }
                            break;
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        textInputLayout.addView(editText);
        parent.addView(textInputLayout);

    }

   /* public static LinearLayout.LayoutParams getLayoutParams(){
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    //create UI for editText
    public static void createEditText(final Context mContext, final SubProcessFieldDataResponse1.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant) {


        TextInputLayout titleWrapper = new TextInputLayout(mContext);
        //    titleWrapper.setId(Integer.parseInt(dynamicField.getFieldServerID()));
        titleWrapper.setLayoutParams(getLayoutParams());
        TextInputEditText et = new TextInputEditText(mContext);
        et.setLayoutParams(getLayoutParams());
        et.setTextSize(15);
        et.setTextColor(Color.BLACK);
        //et.setId(Integer.parseInt(dynamicField.getFieldServerID()));
        titleWrapper.setHint("Hello");
        titleWrapper.setHintAnimationEnabled(false);
        et.setText("ikjhdsakj");
        titleWrapper.addView(et);
        parent.addView(titleWrapper);

    }*/

    /*public static  void createAdapterUI(final Context mContext, LinearLayout parent, final int position, final List<String> headersArrayList, final List<AvailableJobsDataModel.ReportDatum> dataArrayList, final BtnClickListener btnClickListener){
        try {
            for (int i = 0; i < headersArrayList.size(); i++) {
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mLinearView = inflater.inflate(R.layout.list_items, null);
                TextView textViewHeader = (TextView) mLinearView.findViewById(R.id.jobId);
                final TextView textViewValue = (TextView) mLinearView.findViewById(R.id.jobIdValue);
                final ImageView imageView = (ImageView) mLinearView.findViewById(R.id.ImageView);
                textViewHeader.setTag("header" + position);
                if (headersArrayList.get(i).equalsIgnoreCase("Location"))
                {
                    textViewHeader.setText(headersArrayList.get(i));
                    imageView.setVisibility(View.VISIBLE);
                    textViewValue.setVisibility(View.GONE);
                    imageView.setTag(i);
                    imageView.setBackgroundResource(R.drawable.map2);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = headersArrayList.indexOf("Pincode");
                            String postCode= dataArrayList.get(position).getPinCode();
                            if(IOUtils.isInternetPresent(mContext)) {
                                if (!TextUtils.isEmpty(postCode)) {
                                    Intent i = new Intent(mContext, MapActivity.class);
                                    i.putExtra("postalCode", postCode);
                                    mContext.startActivity(i);
                                }
                            }
                            else
                            {
                                IOUtils.showErrorMessage(mContext,"No internet connection");
                            }

                        }
                    });

                }
                else
                {
                    imageView.setVisibility(View.GONE);
                    textViewValue.setVisibility(View.VISIBLE);
                    if(headersArrayList.get(i).equalsIgnoreCase("status")||headersArrayList.get(i).equalsIgnoreCase("Perform Job")||headersArrayList.get(i).equalsIgnoreCase("View")||headersArrayList.get(i).equalsIgnoreCase("UnAssign Job")||headersArrayList.get(i).equalsIgnoreCase("User Name")) {
                        textViewHeader.setVisibility(View.GONE);
                        textViewValue.setVisibility(View.GONE);
                    }
                    else
                    {
                        textViewHeader.setVisibility(View.VISIBLE);
                        textViewValue.setVisibility(View.VISIBLE);
                        textViewValue.setTag(dataArrayList.get(position) + "_" + position);
                        imageView.setTag(dataArrayList.get(position).getJobId() + "_" + position);
                        textViewHeader.setText(headersArrayList.get(i));
                        if(headersArrayList.get(i).equalsIgnoreCase("Job Creation Date")||headersArrayList.get(i).equalsIgnoreCase("Job Start Time")||headersArrayList.get(i).equalsIgnoreCase("Job End Time")||headersArrayList.get(i).equalsIgnoreCase("Job Completion Date"))
                        {
                            String relativeDate = IOUtils.getRelativeDate(dataArrayList.get(position).get(i),mContext);
                            textViewValue.setText(relativeDate);
                        }
                        else
                        {
                            textViewValue.setText(dataArrayList.get(position).get(i));
                        }
                    }
                }


                parent.addView(mLinearView);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
*/


    public static void createGenericAdapterUI(final Context mContext, LinearLayout parent, LinearLayout cardVerticalLin, final int position, JSONArray reportHeadersArray, final JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray, BtnClickListener btnClickListener) {

        try {
            parent.removeAllViews();
            parent.invalidate();
            cardVerticalLin.removeAllViews();
            cardVerticalLin.invalidate();
            for (int k = 0; k < cardHeadersKeyArray.length(); k++) {
                JSONObject jsonObject = null;
                LinearLayout firstCardLayout = new LinearLayout(mContext);
                //firstCardLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                firstCardLayout.setOrientation(LinearLayout.HORIZONTAL);

                JSONObject jObject = cardHeadersKeyArray.getJSONObject(k);
                firstCardLayout.setWeightSum(jObject.getInt("weightage"));
                JSONArray jArray = jObject.getJSONArray("column");
                for (int x = 0; x < jArray.length(); x++) {
                    JSONObject headerKeyJObject = jArray.getJSONObject(x);
                    String header_key = headerKeyJObject.getString("headerKey");
                    jsonObject = reportDataArray.getJSONObject(position);
                    String data = "";
                    if (header_key != null && header_key.contains(".")) {
                        String fieldDataArray[] = header_key.split("\\.");
                        String fieldDataVal = fieldDataArray[0];
                        JSONObject dataJsonObject = jsonObject.getJSONObject(fieldDataVal);
                        JSONObject applicationJsonObject = dataJsonObject.getJSONObject(fieldDataArray[(fieldDataArray.length) - (fieldDataArray.length - 1)]);
                        String fieldFinalVal = applicationJsonObject.getString(fieldDataArray[fieldDataArray.length - 1]);
                        data = fieldFinalVal;
                    } else {
                        if (header_key.equalsIgnoreCase("Pincode") && !jsonObject.has("Pincode")) {
                            if (jsonObject != null) {
                                if (jsonObject.has("Pincode")) {
                                    data = jsonObject.getString(header_key);
                                } else {
                                    data = "";
                                }
                            } else {
                                data = "-";
                            }
                        } else {
                            data = jsonObject.getString(header_key);
                        }
                    }
                    String layout_weight;
                    TextView cardTextViews;

                    if (reportHeadersArray.get(x).toString().equalsIgnoreCase("Job Creation Date") || reportHeadersArray.get(x).toString().equalsIgnoreCase("Job Start Time") || reportHeadersArray.get(x).toString().equalsIgnoreCase("Job End Time") || reportHeadersArray.get(x).toString().equalsIgnoreCase("Job Completion Date")) {
                        String relativeDate = IOUtils.getRelativeDate(data, mContext);
                        layout_weight = headerKeyJObject.getString("layout_weight");
                        cardTextViews = createTextView(mContext, relativeDate, firstCardLayout, ContextCompat.getColor(mContext, R.color.Black), 16, "", "", "", "");
                    } else {
                        layout_weight = headerKeyJObject.getString("layout_weight");
                        cardTextViews = createTextView(mContext, data, firstCardLayout, ContextCompat.getColor(mContext, R.color.Black), 16, "", "", "", "");
                    }

                    cardTextViews.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, Float.valueOf(layout_weight)));
                }
                cardVerticalLin.addView(firstCardLayout);
            }
            for (int i = 0; i < reportHeadersArray.length(); i++) {
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mLinearView = inflater.inflate(R.layout.list_items, null);
                TextView textViewHeader = (TextView) mLinearView.findViewById(R.id.jobId);
                final TextView textViewValue = (TextView) mLinearView.findViewById(R.id.jobIdValue);
                ImageView imageView = (ImageView) mLinearView.findViewById(R.id.ImageView);
                textViewHeader.setTag("header" + position);
                if (i < 2) {
                    textViewHeader.setTypeface(Typeface.DEFAULT_BOLD);
                    textViewValue.setTypeface(Typeface.DEFAULT_BOLD);
                }
                try {

                    if (reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Start Date") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Location") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Applicant or CoApplicant") || reportHeadersArray.get(i).toString().equalsIgnoreCase("status") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Perform Job") || reportHeadersArray.get(i).toString().equalsIgnoreCase("View") || reportHeadersArray.get(i).toString().equalsIgnoreCase("UnAssign Job") || reportHeadersArray.get(i).toString().equalsIgnoreCase("User Name") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Has CoApplicant") || reportHeadersArray.get(i).toString().equalsIgnoreCase("CoApplicant Subporcesses")) {
                        textViewHeader.setVisibility(View.GONE);
                        textViewValue.setVisibility(View.GONE);
                    } else {
                        textViewHeader.setVisibility(View.VISIBLE);
                        textViewValue.setVisibility(View.VISIBLE);
                    }
                    textViewHeader.setText(reportHeadersArray.get(i).toString());
                    final String uiField = reportHeadersUIArray.getString(i);
                    JSONObject jsonObject = reportDataArray.getJSONObject(position);
                    String fieldFinalVal = jsonObject.getString(uiField);
                    Log.i("MYDATA", fieldFinalVal);
                    /*if (reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Creation Date") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Start Time") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Job End Time") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Completion Date")) {
                        String relativeDate = IOUtils.getRelativeDate(fieldFinalVal, mContext);
                        textViewValue.setText(relativeDate);
                    } else*/

                    if (reportHeadersArray.get(i).toString().equalsIgnoreCase("Location")) {
                        imageView.setVisibility(View.VISIBLE);
                        textViewValue.setVisibility(View.GONE);
                        imageView.setTag(i);
                        imageView.setBackgroundResource(R.drawable.map2);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    JSONObject jsonObject = reportDataArray.getJSONObject(position);
                                    String postCode = jsonObject.getString("Pincode");
                                    if (IOUtils.isInternetPresent(mContext)) {
                                        if (!TextUtils.isEmpty(postCode)) {
                                            Intent i = new Intent(mContext, MapActivity.class);
                                            i.putExtra("postalCode", postCode);
                                            mContext.startActivity(i);
                                        }
                                    } else {
                                        IOUtils.showErrorMessage(mContext, "No internet connection");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {
                        textViewValue.setText(fieldFinalVal);
                    }/*
                    if (uiField.equalsIgnoreCase("job_id")) {
                        jobIdTextView.setText(fieldFinalVal);
                    }

                    if (uiField.equalsIgnoreCase("NBFCName")) {
                        nbfcNameTextView.setText(fieldFinalVal);
                    }
                    if (uiField.equalsIgnoreCase("PinCode")) {
                        pinCodeTextView.setText(fieldFinalVal);
                    }
                    if (uiField.equalsIgnoreCase("TAT")) {
                        //tatTextView.setText(fieldFinalVal + " Hrs");
                        tatTextView.setText("TAT " + fieldFinalVal+ " hrs");
                    }
                    if (uiField.equalsIgnoreCase("job_creation_date_STR") || uiField.equalsIgnoreCase("job_end_date_STR") || uiField.equalsIgnoreCase("RequestDateandTime") || uiField.equalsIgnoreCase("job_start_date_STR")) {
                        if (!TextUtils.isEmpty(fieldFinalVal)) {
                            timeLeftTextView.setText(IOUtils.getRelativeDate(fieldFinalVal, mContext));
                        }
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    textViewValue.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    textViewValue.setText("");
                }
                parent.addView(mLinearView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void createReportAdapterUI(final Context mContext, LinearLayout parent, LinearLayout cardVerticalLin, final int position, JSONArray reportHeadersArray, JSONArray reportDataArray, JSONArray reportHeadersUIArray, JSONArray cardHeadersKeyArray) {

        try {
            parent.removeAllViews();
            parent.invalidate();
            cardVerticalLin.removeAllViews();
            cardVerticalLin.invalidate();
            for (int k = 0; k < cardHeadersKeyArray.length(); k++) {
                LinearLayout firstCardLayout = new LinearLayout(mContext);
                //firstCardLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                firstCardLayout.setOrientation(LinearLayout.HORIZONTAL);

                JSONObject jObject = cardHeadersKeyArray.getJSONObject(k);
                firstCardLayout.setWeightSum(jObject.getInt("weightage"));
                JSONArray jArray = jObject.getJSONArray("column");
                for (int x = 0; x < jArray.length(); x++) {
                    JSONObject headerKeyJObject = jArray.getJSONObject(x);
                    String header_key = headerKeyJObject.getString("headerKey");
                    JSONObject jsonObject = reportDataArray.getJSONObject(position);
                    String data = "";
                    if (header_key != null && header_key.contains(".")) {
                        String fieldDataArray[] = header_key.split("\\.");
                        String fieldDataVal = fieldDataArray[0];
                        JSONObject dataJsonObject = jsonObject.getJSONObject(fieldDataVal);
                        JSONObject applicationJsonObject = dataJsonObject.getJSONObject(fieldDataArray[(fieldDataArray.length) - (fieldDataArray.length - 1)]);
                        String fieldFinalVal = applicationJsonObject.getString(fieldDataArray[fieldDataArray.length - 1]);
                        data = fieldFinalVal;
                    } else {

                        data = jsonObject.getString(header_key);
                    }
                    String layout_weight = headerKeyJObject.getString("layout_weight");
                    TextView cardTextViews = createTextView(mContext, data, firstCardLayout, ContextCompat.getColor(mContext, R.color.Black), 16, "", "", "", "");

                    cardTextViews.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, Float.valueOf(layout_weight)));
                }
                cardVerticalLin.addView(firstCardLayout);
            }

            for (int i = 0; i < reportHeadersArray.length(); i++) {
                LayoutInflater inflater = null;
                inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View mLinearView = inflater.inflate(R.layout.list_items, null);
                TextView textViewHeader = (TextView) mLinearView.findViewById(R.id.jobId);
                final TextView textViewValue = (TextView) mLinearView.findViewById(R.id.jobIdValue);
                ImageView imageView = (ImageView) mLinearView.findViewById(R.id.ImageView);
                textViewHeader.setTag("header" + position);
                if (i < 2) {
                    textViewHeader.setTypeface(Typeface.DEFAULT_BOLD);
                    textViewValue.setTypeface(Typeface.DEFAULT_BOLD);
                }
                try {
                    textViewHeader.setText(reportHeadersArray.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject headersUIJsonObject = reportHeadersUIArray.getJSONObject(i);
                if (headersUIJsonObject.has("data")) {
                    String uiField = headersUIJsonObject.getString("data");
                    JSONObject jsonObject = reportDataArray.getJSONObject(position);
                    if (uiField != null && uiField.contains(".")) {
                        try {
                            String fieldDataArray[] = uiField.split("\\.");
                            String fieldDataVal = fieldDataArray[0];
                            JSONObject dataJsonObject = jsonObject.getJSONObject(fieldDataVal);
                            JSONObject applicationJsonObject = dataJsonObject.getJSONObject(fieldDataArray[(fieldDataArray.length) - (fieldDataArray.length - 1)]);
                            String fieldFinalVal = applicationJsonObject.getString(fieldDataArray[fieldDataArray.length - 1]);
                            Log.i("MYDATA", fieldFinalVal);
                            if (reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Creation Date") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Start Time") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Job End Time")) {
                                String relativeDate = IOUtils.getRelativeDate(fieldFinalVal, mContext);
                                textViewValue.setText(relativeDate);
                            } else {
                                textViewValue.setText(fieldFinalVal);
                            }
                            /*if (uiField.equalsIgnoreCase("data.Applicant.PinCode")) {
                                pinCodeTextView.setText(fieldFinalVal);
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textViewValue.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            textViewValue.setText("");
                        }
                    } else {
                        try {
                            String fieldFinalVal = jsonObject.getString(uiField);
                            Log.i("MYDATA", fieldFinalVal);
                            if (reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Creation Date") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Job Start Time") || reportHeadersArray.get(i).toString().equalsIgnoreCase("Job End Time")) {
                                String relativeDate = IOUtils.getRelativeDate(fieldFinalVal, mContext);
                                textViewValue.setText(relativeDate);
                            } else {
                                textViewValue.setText(fieldFinalVal);
                            }
                            /*if (uiField.equalsIgnoreCase("job_id")) {
                                jobIdTextView.setText(fieldFinalVal);
                            }
                            if (uiField.equalsIgnoreCase("NBFCName")) {
                                nbfcNameTextView.setText(fieldFinalVal);
                            }
                            if (uiField.equalsIgnoreCase("data.Applicant.PinCode")) {
                                pinCodeTextView.setText(fieldFinalVal);
                            }
                            if (uiField.equalsIgnoreCase("job_end_date_STR")) {
                                if (!TextUtils.isEmpty(fieldFinalVal)) {
                                    timeLeftTextView.setText(IOUtils.getRelativeDate(fieldFinalVal, mContext));
                                }
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textViewValue.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            textViewValue.setText("");
                        }
                    }
                    parent.addView(mLinearView);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //dynamic create UI for tabs header and form header
    public static void createUIForHeader(Context mContext, LinearLayout parent, String name, String headerType, String customerName, String coApplicantName) {
        try {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 10);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setTag("name_");
            if (headerType.equalsIgnoreCase("tabs")) {
                // linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tabs_background));
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.clrToolbarBg));
                linearLayout.setGravity(Gravity.CENTER);
            } else {
                // createView(mContext, linearLayout, 15, ViewGroup.LayoutParams.MATCH_PARENT, ContextCompat.getColor(mContext, R.color.clrToolbarBg));
                //linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.form_header_background));
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.clrToolbarBg));
            }
            parent.addView(linearLayout);
            createTextView(mContext, name, linearLayout, ContextCompat.getColor(mContext, R.color.clrToolbarBg), 16, headerType, "", customerName, coApplicantName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //create UI for custom view
    public static void createView(Context mContext, LinearLayout parent, int width, int height, int color) {
        View view = new View(mContext);
        view.setBackgroundColor(color);
        view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        parent.addView(view);
    }


    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout parent, SubProcessFieldDataResponse.SubProcessField subProcessField, String type, JobDetailsResponse.Applicant applicant, String appilcant_json, int formPosition) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        identifyViews(mContext, subProcessField, linearLayout, applicant, parent, appilcant_json, formPosition);
        String name = "";

        Method method = null;
        try {
            method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
            name = (String) method.invoke(applicant, null);
            if (name == null || name.isEmpty()) {
                name = "";
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            name = "";
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            name = "";
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            name = "";
        } catch (Exception e) {
            name = "";
        }

        createTextView(mContext, name, linearLayout, ContextCompat.getColor(mContext, R.color.black), 12, "", subProcessField.getKey(), "", "");
        parent.addView(linearLayout);
    }

    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout parent, SubProcessFieldDataResponse.SubProcessField subProcessField, JobDetailsResponse.Applicant applicant, String applicant_json, int formPosition) {
        try {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            identifyViews(mContext, subProcessField, linearLayout, applicant, parent, applicant_json, formPosition);
            parent.addView(linearLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void identifyViews(Context mContext, SubProcessFieldDataResponse.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant, LinearLayout mainParent, String applicant_json, int formPosition) {
        switch (subProcessField.getFieldType()) {
            case "label":
                if (subProcessField.getIsMandatory()) {
                    createTextView(mContext, subProcessField.getLable(), parent, Color.RED, 14, subProcessField.getFieldType(), "", "", applicant_json);
                } else {
                    createTextView(mContext, subProcessField.getLable(), parent, ContextCompat.getColor(mContext, R.color.black), 14, subProcessField.getFieldType(), "", "", applicant_json);
                }
                break;
            case "fieldHeader":
                createTextView(mContext, subProcessField.getLable(), parent, ContextCompat.getColor(mContext, R.color.black), 14, subProcessField.getFieldType(), "", "", applicant_json);
                break;
            case "text":
                createEditText(mContext, subProcessField, parent, applicant, applicant_json);
                break;
            case "dropDownList":
                createDropdown(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                break;
            case "textArea":
                createTextArea(mContext, subProcessField, parent, applicant, applicant_json);
                break;
            case "Date":
                createDatePickerDialog(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                break;
            case "RadioButton":
                createRadioButton(mContext, subProcessField, parent, applicant, mainParent, applicant_json, formPosition);
                break;
            case "map":
                createGoogleMap(mContext, subProcessField, parent, applicant, mainParent, activity, applicant_json);
                break;
            case "imageUpload":
                createUploadButton(mContext, subProcessField, parent, applicant);
                break;
            case "YearMonth":
                createYearMonthCombo(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                break;
            case "checkbox":
                createCheckBox(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                break;
        }
    }

    public static void createRadioButton(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final LinearLayout mainParent, String applicant_json, final int formPosition) {

        try {

            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams linParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(linParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            final TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textViewParams.setMargins(5, 5, 5, 5);
            textView.setLayoutParams(textViewParams);
            textView.setText(subProcessField.getLable());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(mContext.getResources().getColor(R.color.accent));
            linearLayout.addView(textView);

            final RadioGroup radioGroup = new RadioGroup(mContext);
            radioGroup.setId(subProcessField.getFieldID());
            final RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            radioGroup.setLayoutParams(params);
            radioGroup.setOrientation(RadioGroup.VERTICAL);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    try {
                        dependentList.clear();
                        if (formPosition == 0) {
                            try {
                                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                                RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                                int color = radioButton.getCurrentTextColor();
                                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                                Map<String, Button> stringButtonMap = findButtons(mainParent, mContext.getString(R.string.strSubmit));
                                if (hexColor.equalsIgnoreCase("#FF0000") || hexColor.equalsIgnoreCase("#A77805")) {
                                    AppConstant.isRedRadioButtonClicked = true;
                                    if (stringButtonMap.size() > 0) {
                                        for (Map.Entry<String, Button> buttonMap : stringButtonMap.entrySet()) {
                                            buttonMap.getValue().setVisibility(View.VISIBLE);
                                        }
                                    }
                                    DBHelper dbHelper = DBHelper.getInstance(mContext);
                                    AssignedJobsDB.updateAssignedJobsDependsOnJobId(dbHelper, jobId, "");
                                } else {
                                    AppConstant.isRedRadioButtonClicked = false;
                                    if (stringButtonMap.size() > 0) {
                                        for (Map.Entry<String, Button> buttonMap : stringButtonMap.entrySet()) {
                                            buttonMap.getValue().setVisibility(View.GONE);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (!TextUtils.isEmpty(subProcessField.getDependentField())) {
                            HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, subProcessField.getDependentField());
                            if (editTextHashMap.size() > 0) {
                                for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
/*params.setMargins(5,5,5,5);
 editTextEntry.getValue().setLayoutParams(params);*/
                                    editTextEntry.getValue().setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0) {
                                for (int k = 0; k < subProcessField.getDependentFieldList().size(); k++) {
                                    if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(k))) {
                                        try {
                                            if (subProcessField.getDependentFieldList().get(k).contains("__")) {
                                                String Fields[] = subProcessField.getDependentFieldList().get(k).split("__");
                                                for (int j = 0; j < Fields.length; j++) {
                                                    HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, Fields[j]);
                                                    Map<String, Button> stringButtonMap = findMapButtons(mainParent, Fields[j]);
                                                    if (editTextHashMap.size() > 0) {
                                                        for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                                            editTextEntry.getValue().setText("");
                                                            editTextEntry.getValue().setVisibility(View.GONE);
                                                        }
                                                    }
                                                    if (stringButtonMap.size() > 0) {
                                                        for (Map.Entry<String, Button> buttonEntry : stringButtonMap.entrySet()) {
                                                            buttonEntry.getValue().setVisibility(View.GONE);
                                                        }
                                                    }
                                                }
                                            } else {
                                                HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, subProcessField.getDependentFieldList().get(k));
                                                if (editTextHashMap.size() > 0) {
                                                    for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                                        editTextEntry.getValue().setText("");
/* params.setMargins(0,0,0,0);
 editTextEntry.getValue().setLayoutParams(params);*/
                                                        editTextEntry.getValue().setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                try {
                                    int radioBttnId = radioGroup.getCheckedRadioButtonId();
                                    View radioBttn = radioGroup.findViewById(radioBttnId);
                                    int idx = radioGroup.indexOfChild(radioBttn);
                                    if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(idx))) {

                                        if (subProcessField.getDependentFieldList().get(idx).contains("__")) {
                                            String Fields[] = subProcessField.getDependentFieldList().get(idx).split("__");
                                            for (int j = 0; j < Fields.length; j++) {
                                                dependentList.add(Fields[j]);
                                                HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, Fields[j]);
                                                Map<String, Button> stringButtonMap = findMapButtons(mainParent, Fields[j]);
                                                if (editTextHashMap.size() > 0) {
                                                    for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                                        editTextEntry.getValue().setText("");
                                                        editTextEntry.getValue().setVisibility(View.VISIBLE);
                                                    }
                                                }
                                                if (stringButtonMap.size() > 0) {
                                                    for (Map.Entry<String, Button> buttonEntry : stringButtonMap.entrySet()) {
                                                        buttonEntry.getValue().setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            }
                                        } else {
                                            dependentList.add(subProcessField.getDependentFieldList().get(idx));
                                            HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, subProcessField.getDependentFieldList().get(idx));
                                            if (editTextHashMap.size() > 0) {
                                                for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                                    editTextEntry.getValue().setText("");
/* params.setMargins(0,0,0,0);
 editTextEntry.getValue().setLayoutParams(params);*/
                                                    editTextEntry.getValue().setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Method method = null;
            JSONObject jsonObject = null;
            String val = null;
            try {
                radioGroup.setTag(subProcessField.getKey());
                if (subProcessField.getValue() != null) {
                    if (subProcessField.getValue().size() > 0) {
                        for (int i = 0; i < subProcessField.getValue().size(); i++) {
                            try {
                                RadioButton radioButton = new RadioButton(mContext);
                                if (subProcessField.getIds() != null && subProcessField.getIds().size() > 0) {
                                    radioButton.setId(subProcessField.getIds().get(i));
                                   /* if(i==1) {
                                        radioButton.setChecked(true);
                                    }*/
                                }
                                radioButton.setLayoutParams(params);
                                radioButton.setTag(subProcessField.getKey());
                                radioButton.setText(subProcessField.getValue().get(i).trim());
                                radioGroup.addView(radioButton);
                                if (subProcessField.getColor() != null && subProcessField.getColor().size() > 0) {
                                    radioButton.setTextColor(Color.parseColor(subProcessField.getColor().get(i)));
                                } else {
                                    radioButton.setTextColor(Color.BLACK);
                                }
                                try {
                                    jsonObject = new JSONObject(applicant_json);
                                    val = jsonObject.getString(subProcessField.getKey());
                                    if (val != null && !TextUtils.isEmpty(val)) {
                                        if (subProcessField.getValue().get(i).equalsIgnoreCase(val)) {
                                            radioButton.setChecked(true);
//radioGroup.check(subProcessField.getIds().get(i));
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                                    String text = (String) method.invoke(applicant, null);
                                    if (text != null && !TextUtils.isEmpty(text)) {
                                        if (subProcessField.getValue().get(i).equalsIgnoreCase(text)) {
                                            radioButton.setChecked(true);
// radioGroup.check(subProcessField.getIds().get(i));
                                        }
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                    if (val != null && !TextUtils.isEmpty(val)) {
                                        if (subProcessField.getValue().get(i).equalsIgnoreCase(val)) {
//radioButton.setChecked(true);
                                            //radioGroup.check(subProcessField.getIds().get(i));
                                        }
                                    }

                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                    if (val != null && !TextUtils.isEmpty(val)) {
                                        if (subProcessField.getValue().get(i).equalsIgnoreCase(val)) {
//radioButton.setChecked(true);
                                            //radioGroup.check(subProcessField.getIds().get(i));
                                        }
                                    }

                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                    if (val != null && !TextUtils.isEmpty(val)) {
                                        if (subProcessField.getValue().get(i).equalsIgnoreCase(val)) {
// radioButton.setChecked(true);
                                            //radioGroup.check(subProcessField.getIds().get(i));
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            linearLayout.addView(radioGroup);
            if (radioGroup.getCheckedRadioButtonId() == -1) {
                try {
                    //radioGroup.check(subProcessField.getIds().get(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            parent.addView(linearLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createImageUploadDialog(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final LinearLayout mainParent) {
        try {

            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            imageView.setLayoutParams(params);
            imageView.setId(subProcessField.getFieldID());
            Method method = null;
            try {
                // method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                // String text = (String) method.invoke(applicant, null);
                /* if (text != null && !text.isEmpty()) {*/
                imageView.setTag(subProcessField.getKey());
                imageView.setImageResource(R.drawable.cam2);

            } catch (Exception e) {
                e.printStackTrace();

            }
            parent.addView(imageView);

           /* imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = ((AllFormsActivity) mContext)
                            .getSupportFragmentManager();
                    UploadImageFragment uploadImageFragment = new UploadImageFragment();
                    // Show DialogFragment
                    uploadImageFragment.show(fm, "Dialog Fragment");
                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createGoogleMap(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final LinearLayout mainParent, Activity activity, String applicant_json) {
        try {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final Button button = new Button(new ContextThemeWrapper(mContext, R.style.ThemButton));
            button.setAllCaps(false);
            button.setLayoutParams(params);
            button.setTag(subProcessField.getKey());
            button.setId(subProcessField.getFieldID());
            button.setText("Google Map");
            Drawable img = mContext.getResources().getDrawable(R.drawable.map2);
            img.setBounds(0, 0, 60, 60);
            button.setCompoundDrawables(img, null, null, null);
            button.setTextColor(mContext.getResources().getColor(R.color.white));
            //button.setBackgroundColor(mContext.getResources().getColor(R.color.clrLoginButton));
            button.setGravity(Gravity.CENTER);
            parent.addView(button);
            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(subProcessField.getKey())) {
                        isDependentField = true;
                    }
                }
            }
            if (subProcessField.getIsHidden() != null && subProcessField.getIsHidden() == true) {
                if (isDependentField) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                /*params.setMargins(0,0,0,0);
                editText.setLayoutParams(params);*/
                }
            } else {
                button.setVisibility(View.VISIBLE);
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* android.support.v4.app.FragmentManager fm = ((AllFormsActivity) mContext)
                            .getSupportFragmentManager();
                    MapUpdateFragment mapUpdateFragment = new MapUpdateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("location",subProcessField.getKey());
                    mapUpdateFragment.setArguments(bundle);
                    mapUpdateFragment.show(fm, "Google Map");*/
                    Intent i = new Intent(mContext, LocationActivity.class);
                    i.putExtra("location", subProcessField.getKey());
                    mContext.startActivity(i);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createCheckBox(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final LinearLayout mainParent, String applicant_json) {
        try {
            LinearLayout verticalLinearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams linParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            verticalLinearLayout.setLayoutParams(linParams);
            linParams.setMargins(5, 5, 5, 5);
            verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
            final TextView textView = new TextView(mContext);
            textView.setLayoutParams(linParams);
            textView.setText(subProcessField.getLable());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(mContext.getResources().getColor(R.color.accent));
            verticalLinearLayout.addView(textView);
            LinearLayout horizontalLinearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams linParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            horizontalLinearLayout.setLayoutParams(linParams2);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (subProcessField.getValue() != null) {
                for (int i = 0; i < subProcessField.getValue().size(); i++) {
                    final CheckBox checkBox = new CheckBox(mContext);
                    final TextView checkBoxTextView = new TextView(mContext);
                    checkBox.setLayoutParams(linParams2);
                    checkBoxTextView.setLayoutParams(linParams2);
                    checkBox.setId(subProcessField.getFieldID());
                    checkBoxTextView.setText(subProcessField.getValue().get(i));
                    checkBox.setGravity(Gravity.CENTER_VERTICAL);
                    checkBoxTextView.setGravity(Gravity.CENTER_VERTICAL);
                    checkBox.setTag(subProcessField.getKey());
                    checkBoxTextView.setText(subProcessField.getValue().get(i));
                    checkBoxTextView.setTextColor(Color.BLACK);
                    horizontalLinearLayout.addView(checkBox);
                    horizontalLinearLayout.addView(checkBoxTextView);
                    verticalLinearLayout.addView(horizontalLinearLayout);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                checkBox.setTag(subProcessField.getKey() + "/" + "true");
                            } else {
                                checkBox.setTag(subProcessField.getKey());
                            }
                        }
                    });
                    parent.addView(verticalLinearLayout);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createDatePickerDialog(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final LinearLayout mainParent, String applicant_json) {
        try {
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            childparams.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(childparams);
            textInputLayout.setLayoutParams(childparams);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(subProcessField.getKey());
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            Drawable calenderDrawable = ContextCompat.getDrawable(mContext, R.drawable.calendar);
            calenderDrawable.setBounds(
                    0, // left
                    0, // top
                    calenderDrawable.getIntrinsicWidth(), // right
                    calenderDrawable.getIntrinsicHeight() // bottom
            );

            if (subProcessField.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
            }
            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(subProcessField.getKey())) {
                        isDependentField = true;
                    }
                }
            }
            if (subProcessField.getIsHidden() != null && subProcessField.getIsHidden() == true) {
                if (!TextUtils.isEmpty(editText.getText().toString()) || isDependentField) {
                    editText.setVisibility(View.VISIBLE);
                } else {
                    editText.setVisibility(View.GONE);
                }
            } else {
                editText.setVisibility(View.VISIBLE);
            }
            editText.setId(subProcessField.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textInputLayout.setHint(subProcessField.getLable());
            editText.setHint(subProcessField.getLable());
            editText.setCompoundDrawablePadding(20);
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Calendar myCalendar = Calendar.getInstance();
            //  editText.setText(sdf.format(myCalendar.getTime()));
            // editText.setCompoundDrawables(null,null,ContextCompat.getDrawable(mContext,R.drawable.black_dropdown_arrow),null);

            JSONObject jsonObject = null;
            String val = null;
            Method method = null;
            try {
                jsonObject = new JSONObject(applicant_json);
                val = jsonObject.getString(subProcessField.getKey());
                method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(text);
                    textInputLayout.setHintAnimationEnabled(true);
                } else {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(val);
                    textInputLayout.setHintAnimationEnabled(true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(sdf.format(myCalendar.getTime()));
                textInputLayout.setHintAnimationEnabled(true);
            }

           /* if (subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setEnabled(false);
            }
*/
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTemplateCalenderView(mContext, editText, mainParent, subProcessField);

                }
            });
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {

                        edt.setHint(subProcessField.getLable());
                    }
                }
            });
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);

            //editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void createYearMonthCombo(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final LinearLayout mainParent, String applicant_json) {
        try {
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            childparams.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(childparams);
            textInputLayout.setLayoutParams(childparams);
            editText.setId(subProcessField.getFieldID());
            editText.setCompoundDrawablePadding(20);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(subProcessField.getKey());
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            Drawable calenderDrawable = ContextCompat.getDrawable(mContext, R.drawable.calendar);
            calenderDrawable.setBounds(
                    0, // left
                    0, // top
                    calenderDrawable.getIntrinsicWidth(), // right
                    calenderDrawable.getIntrinsicHeight() // bottom
            );

            if (subProcessField.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
            }

            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(subProcessField.getKey())) {
                        isDependentField = true;
                    }
                }
            }
            if (subProcessField.getIsHidden() != null && subProcessField.getIsHidden() == true) {
                if (!TextUtils.isEmpty(editText.getText().toString()) || isDependentField) {
                    editText.setVisibility(View.VISIBLE);
                } else {
                    editText.setVisibility(View.GONE);
                }
            } else {
                editText.setVisibility(View.VISIBLE);
            }

            JSONObject jsonObject = null;
            String val = null;
            Method method = null;
            try {
                jsonObject = new JSONObject(applicant_json);
                val = jsonObject.getString(subProcessField.getKey());
                method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(text);
                    textInputLayout.setHintAnimationEnabled(true);
                } else {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(val);
                    textInputLayout.setHintAnimationEnabled(true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText("");
                textInputLayout.setHintAnimationEnabled(true);
            }

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTemplateYearMonthCalenderView(mContext, editText, mainParent, subProcessField);

                }
            });
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {

                        edt.setHint(subProcessField.getLable());
                    }
                }
            });
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);

            //editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createDropdown(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final LinearLayout mainParent, String applicant_json) {
        try {
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(5, 5, 5, 5);
            textInputLayout.setLayoutParams(params);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(subProcessField.getKey());
            editText.setId(subProcessField.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (!TextUtils.isEmpty(subProcessField.getDefaultValue())) {
                editText.setText(subProcessField.getDefaultValue());//commented by netra
            }
            Drawable calenderDrawable = ContextCompat.getDrawable(mContext, R.drawable.blue_dropdown_arrow);
            calenderDrawable.setBounds(
                    0, // left
                    0, // top
                    calenderDrawable.getIntrinsicWidth(), // right
                    calenderDrawable.getIntrinsicHeight() // bottom
            );

            if (subProcessField.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);//commented by netra
            }

            editText.setCompoundDrawablePadding(20);
            Method method = null;
            JSONObject jsonObject = null;
            String val = null;
            try {
                jsonObject = new JSONObject(applicant_json);
                val = jsonObject.getString(subProcessField.getKey());
                method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(text);//commented by netra
                    textInputLayout.setHintAnimationEnabled(true);

                    int index = subProcessField.getValue().indexOf(text);
                    if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0 && index >= 0) {
                        if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(index))) {
                            dependentList.add(subProcessField.getDependentFieldList().get(index));
                        }
                    }

                } else {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(val);//commented by netra
                    textInputLayout.setHintAnimationEnabled(true);

                    int index = subProcessField.getValue().indexOf(val);
                    textInputLayout.setHintAnimationEnabled(true);
                    if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0 && index >= 0) {
                        if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(index))) {
                            dependentList.add(subProcessField.getDependentFieldList().get(index));
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);

                int index = subProcessField.getValue().indexOf(val);
                if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0 && index >= 0) {
                    if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(index))) {
                        dependentList.add(subProcessField.getDependentFieldList().get(index));
                    }
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);

                int index = subProcessField.getValue().indexOf(val);
                if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0 && index >= 0) {
                    if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(index))) {
                        dependentList.add(subProcessField.getDependentFieldList().get(index));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);

                int index = subProcessField.getValue().indexOf(val);
                if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0 && index >= 0) {
                    if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(index))) {
                        dependentList.add(subProcessField.getDependentFieldList().get(index));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                if (!TextUtils.isEmpty(subProcessField.getDefaultValue())) {
                    editText.setText(subProcessField.getDefaultValue());
                } else {
                    editText.setText("");
                }
                textInputLayout.setHintAnimationEnabled(true);
            }
            if (subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            if (subProcessField.getCustomValidationFunction() != null && !subProcessField.getCustomValidationFunction().isEmpty()) {
                if (subProcessField.getCustomValidationFunction().contains("toggleFields")) {
                    ArrayList<String> arrayListFields = new ArrayList<>();
                    String fn = subProcessField.getCustomValidationFunction().replace("[", "").replace("]", "").replace("(", "").replace(")", "").replace("'", "");

                    String fn_array[] = fn.split(",");
                    try {
                        for (int i = 3; i < fn_array.length; i++) {
                            arrayListFields.add(fn_array[i]);
                        }
                        if (editText.getText().toString().equalsIgnoreCase("yes")) {
                            otherLoanInformationSelected = true;
                        } else {
                            otherLoanInformationSelected = false;
                        }
                        hashMapToggleFields = new HashMap<>();
                        hashMapToggleFields.put(otherLoanInformationSelected, arrayListFields);
                    } catch (Exception e) {

                    }
                }
            }
            if (subProcessField.getValue().size() > 0) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDropDownOptions(mContext, subProcessField, editText, mainParent, subProcessField.getLable(), params);
                    }
                });
            }
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                Pattern sPattern
                        = Pattern.compile(subProcessField.getValidationPattern());

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {

                        edt.setHint(subProcessField.getLable());
                        if (subProcessField.getValidationPattern() != null && !subProcessField.getValidationPattern().isEmpty()) {

                            if (!isValid(edt.getText().toString())) {
                                //   editText.setText("");
                                editText.setError(subProcessField.getValidationMessage());
                            } else {
                                editText.setError(null);
                            }
                        }
                    } else {
                        edt.setHint("");
                    }
                }

                private boolean isValid(CharSequence s) {
                    return sPattern.matcher(s).matches();
                }
            });
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            if (subProcessField.getIsreadonly() != null && subProcessField.getIsreadonly()) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                //editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }
            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(subProcessField.getKey())) {
                        isDependentField = true;
                    }
                }
            }
            if (subProcessField.getIsHidden() != null && subProcessField.getIsHidden() == true) {
                if (!TextUtils.isEmpty(editText.getText().toString()) || isDependentField) {
                    editText.setVisibility(View.VISIBLE);
                } else {
                    editText.setVisibility(View.GONE);
                }
            } else {
                editText.setVisibility(View.VISIBLE);
            }
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openDropDownOptions(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final EditText editText, final LinearLayout mainParent, final String lable, final LinearLayout.LayoutParams params) {
        try {

            final String value_arr[] = new String[subProcessField.getValue().size() + 1];
            value_arr[0] = lable;
            for (int i = 0; i < subProcessField.getValue().size(); i++) {
                int pos = i + 1;
                value_arr[pos] = subProcessField.getValue().get(i);
            }
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_dropdown);
            ListView dropdownList = (ListView) dialog.findViewById(R.id.dropdownList);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.list_dropdown_items, value_arr);
            adapter.notifyDataSetChanged();
            dropdownList.setAdapter(adapter);
            dropdownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dependentList.clear();
                    if (position > 0) {
                        try {
                            editText.setText(value_arr[position]);
                            if (!TextUtils.isEmpty(subProcessField.getDependentField())) {
                                dependentList.add(subProcessField.getDependentField());
                            } else if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0) {
                                String viewKey = subProcessField.getDependentFieldList().get(position - 1);
                                if (!TextUtils.isEmpty(viewKey)) {
                                    dependentList.add(viewKey);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        editText.setText("");
                    }
                    dialog.dismiss();
                    try {
                        if (value_arr[position].equalsIgnoreCase("No") || value_arr[position].equalsIgnoreCase(lable)) {
                            otherLoanInformationSelected = false;
                        } else {
                            otherLoanInformationSelected = true;
                        }

                        //add this code in save button click
                        ArrayList<String> arrayListFields = new ArrayList<String>();
                        if (hashMapToggleFields.containsKey(otherLoanInformationSelected)) {
                            arrayListFields = hashMapToggleFields.get(otherLoanInformationSelected);
                        } else {
                            arrayListFields = hashMapToggleFields.get(!otherLoanInformationSelected);
                        }
                        hashMapToggleFields = new HashMap<Boolean, ArrayList<String>>();
                        hashMapToggleFields.put(otherLoanInformationSelected, arrayListFields);
                        for (int i = 0; i < arrayListFields.size(); i++) {
                            HashMap<Integer, EditText> loanData = findCustomValidateEdittexts(mainParent, arrayListFields.get(i).trim());
                            if (loanData != null) {
                                try {
                                    for (Map.Entry<Integer, EditText> loanMap : loanData.entrySet()) {
                                        if (loanMap.getValue() != null) {
                                            EditText loanEditText = loanMap.getValue();
                                            loanEditText.setEnabled(otherLoanInformationSelected);
                                            if (otherLoanInformationSelected) {
                                                Drawable originalDrawable = editText.getBackground();
                                                loanEditText.setBackgroundDrawable(originalDrawable);
                                            } else {
                                                loanEditText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {

                        if (!TextUtils.isEmpty(subProcessField.getDependentField())) {
                            HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, subProcessField.getDependentField());
                            if (editTextHashMap.size() > 0) {
                                for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                    if (position == 0) {
                                        editTextEntry.getValue().setText("");
                                        editTextEntry.getValue().setVisibility(View.GONE);
                                    } else {
                                        editTextEntry.getValue().setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else {
                            if (subProcessField.getDependentFieldList() != null && subProcessField.getDependentFieldList().size() > 0) {
                                for (int k = 0; k < subProcessField.getDependentFieldList().size(); k++) {
                                    if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(k))) {
                                        HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, subProcessField.getDependentFieldList().get(k));
                                        if (editTextHashMap.size() > 0) {
                                            for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                                editTextEntry.getValue().setText("");
                                                editTextEntry.getValue().setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                                if (!TextUtils.isEmpty(subProcessField.getDependentFieldList().get(position - 1))) {
                                    HashMap<Integer, EditText> editTextHashMap = DatabaseUI.findEditTextForShowHide(mainParent, subProcessField.getDependentFieldList().get(position - 1));
                                    if (editTextHashMap.size() > 0) {
                                        for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                            editTextEntry.getValue().setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTextArea(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant, String applicant_json) {
        try {
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            editText.setLayoutParams(params);
            params.setMargins(5, 5, 5, 5);
            textInputLayout.setLayoutParams(params);
            editText.setTag(subProcessField.getKey());
            editText.setPadding(10, 10, 10, 20);
            editText.setId(subProcessField.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            //add validation pattern
            mAwesomeValidation.addValidation(editText, subProcessField.getValidationPattern(), subProcessField.getValidationMessage());
            if (subProcessField.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }

            Method method = null;
            JSONObject jsonObject = null;
            String val = null;
            try {
                jsonObject = new JSONObject(applicant_json);
                val = jsonObject.getString(subProcessField.getKey());
                method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(text);
                    editText.setSelection(text.length());
                    textInputLayout.setHintAnimationEnabled(true);
                } else {
                    textInputLayout.setHint(subProcessField.getLable());
                    editText.setHint(subProcessField.getLable());
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(val);
                    editText.setSelection(val.length());
                    textInputLayout.setHintAnimationEnabled(true);

                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                editText.setSelection(val.length());
                textInputLayout.setHintAnimationEnabled(true);

            } catch (InvocationTargetException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                editText.setSelection(val.length());
                textInputLayout.setHintAnimationEnabled(true);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                editText.setSelection(val.length());
                textInputLayout.setHintAnimationEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(subProcessField.getLable());
                editText.setHint(subProcessField.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText("");
                textInputLayout.setHintAnimationEnabled(true);

            }

            if (subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else if (subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
            //editText.setGravity(Gravity.TOP);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                Pattern sPattern
                        = Pattern.compile(subProcessField.getValidationPattern());

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {
                        edt.setSelection(edt.getText().length());
                        edt.setHint(subProcessField.getLable());
                        if (subProcessField.getValidationPattern() != null && !subProcessField.getValidationPattern().isEmpty()) {

                            if (!isValid(edt.getText().toString())) {
                                //   editText.setText("");
                                editText.setError(subProcessField.getValidationMessage());
                            } else {
                                editText.setError(null);
                            }
                        }
                    } else {
                        edt.setHint("");
                    }
                }

                private boolean isValid(CharSequence s) {
                    return sPattern.matcher(s).matches();
                }
            });
            if (subProcessField.getIsreadonly() != null && subProcessField.getIsreadonly()) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                // editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }
            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(subProcessField.getKey())) {
                        isDependentField = true;
                    }
                }
            }
            if (subProcessField.getIsHidden() != null && subProcessField.getIsHidden() == true) {
                if (!TextUtils.isEmpty(editText.getText().toString()) || isDependentField) {
                    editText.setVisibility(View.VISIBLE);
                } else {
                    editText.setVisibility(View.GONE);
                }
            } else {
                editText.setVisibility(View.VISIBLE);
            }
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void crateTableLayoutAndAddButtonsLoan(Context mContext, LinearLayout parent, String tableName, List<String> tableHeaderList, List<String> tableKeysList, List<String> tableMandatoryFieldsList, List<String> clearSubProcessFieldsList, List<JobDetailsResponse.LoanDetailsTable> loanDetailsTable) {
        try {
            addCount = 0;
            arrayListCheckBoxPosition.clear();
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.other_loan_table_buttons, null);
            parent.addView(mLinearView);


            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llParams.setMargins(10, 10, 10, 20);
            linearLayout.setLayoutParams(llParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            parent.addView(linearLayout);

            TableLayout tableLayout = new TableLayout(mContext);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tableLayout.setLayoutParams(params);
            tableLayout.setId(-1);
            tableLayout.setTag("tableHeader_-1");
            //tableLayout.setStretchAllColumns(true);
            TableRow tableRow = new TableRow(mContext);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tabs_background));
            addCheckBoxToTableRow(mContext, "Delete", tableRow, Color.BLACK, 12, -1, parent);
            for (int i = 0; i < tableHeaderList.size(); i++) {
                createView(mContext, tableRow, 1, TableRow.LayoutParams.MATCH_PARENT, ContextCompat.getColor(mContext, R.color.gray));
                addTextViewToTableRow(mContext, tableHeaderList.get(i), tableRow, Color.BLACK, 12, -1, tableKeysList.get(i).trim());
            }
            tableRow.setGravity(Gravity.CENTER);
            tableLayout.addView(tableRow, params);
            linearLayout.addView(tableLayout);
            if (loanDetailsTable != null && loanDetailsTable.size() > 0) {
                for (int i = 0; i < loanDetailsTable.size(); i++) {
                    addCount++;
                    ArrayList<String> arrayListRow = new ArrayList<String>();

                    Method method = null;

                    for (int j = 0; j < tableKeysList.size(); j++) {
                        try {
                            String value = "";
                            String methodName = "get" + StringUtils.capitalize(tableKeysList.get(j).trim());
                            if (tableName.equalsIgnoreCase("AssetsTable")) {
                                method = JobDetailsResponse.AssetsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            } else if (tableName.equalsIgnoreCase("VehicleDetailsTable")) {
                                method = JobDetailsResponse.VehicleDetailsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            } else {
                                method = JobDetailsResponse.LoanDetailsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            }
                            arrayListRow.add(value);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        }

                    }


                    if (arrayListRow.size() > 0) {
                        createTableRows(mContext, linearLayout, arrayListRow, tableKeysList);
                    }
                }
            }
            // createView(mContext,parent,LinearLayout.LayoutParams.MATCH_PARENT,1,ContextCompat.getColor(mContext,R.color.gray));
            findButtonsAndAddListernersToButton(mContext, mLinearView, linearLayout, tableHeaderList, parent, tableMandatoryFieldsList, clearSubProcessFieldsList, tableKeysList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void crateTableLayoutAndAddButtonsVehicle(Context mContext, LinearLayout parent, String tableName, List<String> tableHeaderList, List<String> tableKeysList, List<String> tableMandatoryFieldsList, List<String> clearSubProcessFieldsList, List<JobDetailsResponse.VehicleDetailsTable> loanDetailsTable) {
        try {
            addCount = 0;
            arrayListCheckBoxPosition.clear();
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.other_loan_table_buttons, null);
            parent.addView(mLinearView);


            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llParams.setMargins(10, 10, 10, 20);
            linearLayout.setLayoutParams(llParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            parent.addView(linearLayout);

            TableLayout tableLayout = new TableLayout(mContext);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tableLayout.setLayoutParams(params);
            tableLayout.setId(-1);
            tableLayout.setTag("tableHeader_-1");
            //tableLayout.setStretchAllColumns(true);
            TableRow tableRow = new TableRow(mContext);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tabs_background));
            addCheckBoxToTableRow(mContext, "Delete", tableRow, Color.BLACK, 12, -1, parent);
            for (int i = 0; i < tableHeaderList.size(); i++) {
                createView(mContext, tableRow, 1, TableRow.LayoutParams.MATCH_PARENT, ContextCompat.getColor(mContext, R.color.gray));
                addTextViewToTableRow(mContext, tableHeaderList.get(i), tableRow, Color.BLACK, 12, -1, tableKeysList.get(i).trim());
            }
            tableRow.setGravity(Gravity.CENTER);
            tableLayout.addView(tableRow, params);
            linearLayout.addView(tableLayout);
            if (loanDetailsTable != null && loanDetailsTable.size() > 0) {
                for (int i = 0; i < loanDetailsTable.size(); i++) {
                    addCount++;
                    ArrayList<String> arrayListRow = new ArrayList<String>();

                    Method method = null;

                    for (int j = 0; j < tableKeysList.size(); j++) {
                        try {
                            String value = "";
                            String methodName = "get" + StringUtils.capitalize(tableKeysList.get(j).trim());
                            if (tableName.equalsIgnoreCase("AssetsTable")) {
                                method = JobDetailsResponse.AssetsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            } else if (tableName.equalsIgnoreCase("VehicleDetailsTable")) {
                                method = JobDetailsResponse.VehicleDetailsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            } else {
                                method = JobDetailsResponse.LoanDetailsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            }
                            arrayListRow.add(value);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        }

                    }


                    if (arrayListRow.size() > 0) {
                        createTableRows(mContext, linearLayout, arrayListRow, tableKeysList);
                    }
                }
            }
            // createView(mContext,parent,LinearLayout.LayoutParams.MATCH_PARENT,1,ContextCompat.getColor(mContext,R.color.gray));
            findButtonsAndAddListernersToButton(mContext, mLinearView, linearLayout, tableHeaderList, parent, tableMandatoryFieldsList, clearSubProcessFieldsList, tableKeysList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void crateTableLayoutAndAddButtonsAssets(Context mContext, LinearLayout parent, String tableName, List<String> tableHeaderList, List<String> tableKeysList, List<String> tableMandatoryFieldsList, List<String> clearSubProcessFieldsList, List<JobDetailsResponse.AssetsTable> loanDetailsTable) {
        try {
            addCount = 0;
            arrayListCheckBoxPosition.clear();
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.other_loan_table_buttons, null);
            parent.addView(mLinearView);


            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llParams.setMargins(10, 10, 10, 20);
            linearLayout.setLayoutParams(llParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            parent.addView(linearLayout);

            TableLayout tableLayout = new TableLayout(mContext);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tableLayout.setLayoutParams(params);
            tableLayout.setId(-1);
            tableLayout.setTag("tableHeader_-1");

            //tableLayout.setStretchAllColumns(true);
            TableRow tableRow = new TableRow(mContext);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tableRow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tabs_background));
            addCheckBoxToTableRow(mContext, "Delete", tableRow, Color.BLACK, 12, -1, parent);
            for (int i = 0; i < tableHeaderList.size(); i++) {
                createView(mContext, tableRow, 1, TableRow.LayoutParams.MATCH_PARENT, ContextCompat.getColor(mContext, R.color.gray));
                addTextViewToTableRow(mContext, tableHeaderList.get(i), tableRow, Color.BLACK, 12, -1, tableKeysList.get(i).trim());
            }
            tableRow.setGravity(Gravity.CENTER);
            tableLayout.addView(tableRow, params);
            linearLayout.addView(tableLayout);
            if (loanDetailsTable != null && loanDetailsTable.size() > 0) {
                for (int i = 0; i < loanDetailsTable.size(); i++) {
                    addCount++;
                    ArrayList<String> arrayListRow = new ArrayList<String>();

                    Method method = null;

                    for (int j = 0; j < tableKeysList.size(); j++) {
                        try {
                            String value = "";
                            String methodName = "get" + StringUtils.capitalize(tableKeysList.get(j).trim());
                            if (tableName.equalsIgnoreCase("AssetsTable")) {
                                method = JobDetailsResponse.AssetsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            } else if (tableName.equalsIgnoreCase("VehicleDetailsTable")) {
                                method = JobDetailsResponse.VehicleDetailsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            } else {
                                method = JobDetailsResponse.LoanDetailsTable.class.getDeclaredMethod(methodName);
                                value = (String) method.invoke(loanDetailsTable.get(i), null);
                            }
                            arrayListRow.add(value);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        } catch (Exception e) {
                            e.printStackTrace();
                            arrayListRow.add("");
                        }

                    }


                    if (arrayListRow.size() > 0) {
                        createTableRows(mContext, linearLayout, arrayListRow, tableKeysList);
                    }
                }
            }
            // createView(mContext,parent,LinearLayout.LayoutParams.MATCH_PARENT,1,ContextCompat.getColor(mContext,R.color.gray));
            findButtonsAndAddListernersToButton(mContext, mLinearView, linearLayout, tableHeaderList, parent, tableMandatoryFieldsList, clearSubProcessFieldsList, tableKeysList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void findButtonsAndAddListernersToButton(final Context mContext, View mLinearView, final LinearLayout linearLayout, final List<String> tableHeaderList, final LinearLayout viewParent, final List<String> tableMandatoryFieldsList, final List<String> clearSubProcessFieldsList, final List<String> tableKeysList) {
        try {
            Button btnAdd = (Button) mLinearView.findViewById(R.id.btnAdd);
            Button btnDelete = (Button) mLinearView.findViewById(R.id.btnDelete);
            Button btnClear = (Button) mLinearView.findViewById(R.id.btnClear);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    if (validateAddTableRow(mContext, tableMandatoryFieldsList, viewParent)) {
                        addCount++;
                        ArrayList<String> arrayListRow = new ArrayList<String>();
                        ArrayList<EditText> tempArrayListRow = new ArrayList<EditText>();
                        int validCount = 0;
                        for (int i = 0; i < tableKeysList.size(); i++) {
                            arrayListForMandatoryTableFields = new ArrayList<EditText>();
                            ArrayList<EditText> editTextArrayList = findAllTableEdittexts(viewParent, tableKeysList.get(i).trim());
                            if (editTextArrayList.size() > 0) {
                                boolean validate = false;
                                if (mAwesomeValidation != null) {
                                    mAwesomeValidation.clear();
                                    validate = mAwesomeValidation.validate();
                                }
                                if (validate) { // apply validation pattern here
                                    tempArrayListRow.add(editTextArrayList.get(0));
                                    validCount++;
                                }
                                if (validCount >= 4) {
                                    for (int j = 0; j < tempArrayListRow.size(); j++) {
                                        arrayListRow.add(tempArrayListRow.get(j).getText().toString());
                                        tempArrayListRow.get(j).setText("");
                                    }
                                }

                            }

                        }
                        if (arrayListRow.size() > 0) {
                            createTableRows(mContext, linearLayout, arrayListRow, tableKeysList);
                        }
                    }
                }
            });
            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < clearSubProcessFieldsList.size(); i++) {
                        arrayListForMandatoryTableFields = new ArrayList<EditText>();
                        ArrayList<EditText> editTextArrayList = findAllTableEdittexts(viewParent, clearSubProcessFieldsList.get(i));
                        if (editTextArrayList.size() > 0) {
                            EditText editText = editTextArrayList.get(0);
                            editText.setText("");
                        }
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arrayListCheckBoxPosition.size() > 0) {
                        if (arrayListCheckBoxPosition.contains(-1)) { //delete all rows
                            for (int i = 0; i <= addCount; i++) {
                                try {
                                    TableLayout tableLayout = (TableLayout) linearLayout.findViewById(i);
                                    linearLayout.removeView(tableLayout);
                                    linearLayout.invalidate();
                                    TableLayout tableLayoutHeader = (TableLayout) linearLayout.findViewWithTag("tableHeader_-1");
                                    CheckBox headerCheckBox = (CheckBox) tableLayoutHeader.findViewWithTag("tableHeader_chk_-1");
                                    headerCheckBox.setChecked(false);
                                } catch (Exception e) {

                                }
                            }

                        } else {
                            for (int i = 0; i < arrayListCheckBoxPosition.size(); i++) {
                                try {
                                    TableLayout tableLayout = (TableLayout) linearLayout.findViewById(arrayListCheckBoxPosition.get(i));
                                    linearLayout.removeView(tableLayout);
                                    linearLayout.invalidate();

                                    // addCount = addCount - arrayListCheckBoxPosition.size();
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static boolean validatePattern(EditText editText, List<String> tableMandatoryFieldsList) {
        boolean isValid =true;
        try {
            for (int i = 0; i < tableMandatoryFieldsList.size(); i++) {
                String tag = editText.getTag().toString();
                String key = "";
                if (tag.contains("/")) {
                    key = tag.split("/")[0];
                } else {
                    key = tag;
                }
                if (key.equalsIgnoreCase(tableMandatoryFieldsList.get())) {
                    if (subProcessField.getValidationPattern() != null && !subProcessField.getValidationPattern().isEmpty()) {

                        Pattern sPattern
                                = Pattern.compile(subProcessField.getValidationPattern());
                        String text = editText.getText().toString();
                        if (!sPattern.matcher(text).matches()) {
                            editText.setError(subProcessField.getValidationMessage());
                            isValid = false;
                            break;
                        } else {
                            editText.setError(null);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return isValid;
    }*/
    private static boolean validateAddTableRow(Context mContext, List<String> tableMandatoryFieldsList, LinearLayout viewParent) {
        String text = "";
        EditText editText = null;
        boolean validateFlag = true;
        try {
            for (int i = 0; i < tableMandatoryFieldsList.size(); i++) {
                try {
                    arrayListForMandatoryTableFields = new ArrayList<>();
                    ArrayList<EditText> arrayListeditText = findAllTableEdittexts(viewParent, tableMandatoryFieldsList.get(i));
                    if (arrayListeditText.size() > 0) {
                        editText = arrayListeditText.get(0);
                        text = editText.getText().toString();
                        if (text.isEmpty()) {
                            //  Toast.makeText(mContext, tableMandatoryFieldsList.get(i) + " is mandatory", Toast.LENGTH_LONG).show();
                            MyDynamicToast.errorMessage(mContext, tableMandatoryFieldsList.get(i) + " is mandatory");
                            validateFlag = false;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return validateFlag;
    }


    public static void createTableRows(Context mContext, LinearLayout parent, ArrayList<String> arrayListRow, final List<String> tableKeysList) {
        try {
            TableLayout tableLayout = new TableLayout(mContext);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            tableLayout.setLayoutParams(params);
            tableLayout.setId(addCount);
            //tableLayout.setStretchAllColumns(true);
            TableRow tableRow = new TableRow(mContext);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            addCheckBoxToTableRow(mContext, "", tableRow, Color.BLACK, 12, addCount, parent);
            for (int i = 0; i < arrayListRow.size(); i++) {
                createView(mContext, tableRow, 1, TableRow.LayoutParams.MATCH_PARENT, ContextCompat.getColor(mContext, R.color.gray));
                addTextViewToTableRow(mContext, arrayListRow.get(i), tableRow, Color.BLACK, 12, i, tableKeysList.get(i).trim());
            }
            tableRow.setGravity(Gravity.CENTER);
            tableLayout.addView(tableRow, params);
            parent.addView(tableLayout);
            createView(mContext, parent, LinearLayout.LayoutParams.MATCH_PARENT, 1, ContextCompat.getColor(mContext, R.color.gray));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //create UI for custom view inside table row
    public static void createView(Context mContext, TableRow parent, int width, int height, int color) {
        View view = new View(mContext);
        view.setBackgroundColor(color);
        view.setLayoutParams(new TableRow.LayoutParams(width, height));
        parent.addView(view);
    }


    private static void addCheckBoxToTableRow(Context mContext, String text, final TableRow parentRow, int textColor, float textSize, final int positionToAdd, final LinearLayout parent) {
        try {
            final CheckBox checkBox = new CheckBox(mContext);
            LinearLayout.LayoutParams params = new TableRow.LayoutParams(140, ViewGroup.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(params);
            checkBox.setTextColor(textColor);
            checkBox.setTextSize(textSize);
            checkBox.setTag("tableHeader_chk_" + positionToAdd);
            checkBox.setId(positionToAdd);
            checkBox.setText(text);
            checkBox.setChecked(false);
            checkBox.setGravity(Gravity.CENTER);
            if (arrayListCheckBoxPosition.contains(-1)) {
                int index1 = arrayListCheckBoxPosition.indexOf(-1);
                arrayListCheckBoxPosition.remove(index1);
                CheckBox checkBoxToDelete = (CheckBox) parent.findViewWithTag("tableHeader_chk_-1");
                checkBoxToDelete.setChecked(false);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        arrayListCheckBoxPosition.add(positionToAdd);
                        //if check all then make all checkboxes as checked
                        if (positionToAdd == -1) {
                            for (int i = 1; i < (addCount + 1); i++) {
                                try {
                                    CheckBox checkBoxToDelete = (CheckBox) parent.findViewWithTag("tableHeader_chk_" + i);
                                    checkBoxToDelete.setChecked(true);
                                } catch (Exception e) {

                                }
                            }
                        }
                    } else {
                        if (arrayListCheckBoxPosition.contains(positionToAdd)) {
                            int index = arrayListCheckBoxPosition.indexOf(positionToAdd);
                            arrayListCheckBoxPosition.remove(index);
                            if (arrayListCheckBoxPosition.contains(-1)) {
                                int index1 = arrayListCheckBoxPosition.indexOf(-1);
                                arrayListCheckBoxPosition.remove(index1);
                                CheckBox checkBoxToDelete = (CheckBox) parent.findViewWithTag("tableHeader_chk_-1");
                                checkBoxToDelete.setChecked(false);
                            }
                        }
                        int uncheckCount = 0;
                        for (int i = 1; i < (addCount + 1); i++) {
                            try {
                                CheckBox checkBoxToDelete = (CheckBox) parent.findViewWithTag("tableHeader_chk_" + i);
                                if (checkBoxToDelete.isChecked()) {
                                    uncheckCount++;
                                }
                            } catch (Exception e) {

                            }
                        }
                        //if uncheck all then make all checkboxes as unchecked
                        if (positionToAdd == -1 && uncheckCount == addCount) {
                            for (int i = 1; i < (addCount + 1); i++) {
                                try {
                                    CheckBox checkBoxToDelete = (CheckBox) parent.findViewWithTag("tableHeader_chk_" + i);
                                    checkBoxToDelete.setChecked(false);
                                } catch (Exception e) {

                                }
                            }
                        } else {
                            noOfUncheck = 0;
                            for (int i = 1; i < (addCount + 1); i++) {
                                try {
                                    CheckBox chkBox = (CheckBox) parent.findViewWithTag("tableHeader_chk_" + i);
                                    if (!chkBox.isChecked()) {
                                        noOfUncheck++;
                                    }
                                } catch (Exception e) {

                                }
                            }
                            if (noOfUncheck == addCount) {
                                try {
                                    CheckBox checkBoxToDelete = (CheckBox) parent.findViewWithTag("tableHeader_chk_" + -1);
                                    checkBoxToDelete.setChecked(false);
                                    noOfUncheck = 0;
                                } catch (Exception e) {

                                }
                            }
                        }
                    }
                }
            });
            parentRow.addView(checkBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTextViewToTableRow(Context mContext, String text, TableRow parentRow, int textColor, float textSize, int positionToAdd, String key) {
        try {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams params = new TableRow.LayoutParams(140, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setTextColor(textColor);
            textView.setTextSize(textSize);
            textView.setTag(key + "_" + addCount);
            textView.setPadding(10, 10, 10, 10);
            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            parentRow.addView(textView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //reportFilers

    public static void identifyReportViews(Context mContext, ReportFilterModel.ReportFilter reportField, LinearLayout parent) {
        switch (reportField.getFieldType()) {
            case "label":
                if (reportField.getIsMandatory()) {
                    createTextView(mContext, reportField.getLable(), parent, Color.RED, 12, "", "", "", "");
                } else {
                    createTextView(mContext, reportField.getLable(), parent, ContextCompat.getColor(mContext, R.color.black), 12, "", "", "", "");
                }
                break;
            case "text":
                createReportEditText(mContext, reportField, parent);
                break;
            case "dropDownList":
                createReportDropdown(mContext, reportField, parent);
                break;
            case "textArea":
                createReportTextArea(mContext, reportField, parent);
                break;
            case "Date":
                createDatePickerDialog(mContext, reportField, parent);
                break;
        }
    }

    public static void createReportDropdown(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent) {
        final TextInputEditText editText = new TextInputEditText(mContext);
        final TextInputLayout textInputLayout = new TextInputLayout(mContext);
        try {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 20);
            textInputLayout.setLayoutParams(params);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);
            // editText.setCompoundDrawables(null,null,ContextCompat.getDrawable(mContext,R.drawable.black_dropdown_arrow),null);
            if (reportFilterFeild.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }
            if (reportFilterFeild.getLable() != null) {
                textInputLayout.setHint(reportFilterFeild.getLable());
                editText.setHint(reportFilterFeild.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(reportFilterFeild.getLable());
                textInputLayout.setHintAnimationEnabled(true);
            } else {
                textInputLayout.setHint(reportFilterFeild.getLable());
                editText.setHint(reportFilterFeild.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText("Select");
                textInputLayout.setHintAnimationEnabled(true);
            }
            if (reportFilterFeild.getValidation().equalsIgnoreCase("Numeric") || reportFilterFeild.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (reportFilterFeild.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //openDropDownOptions(mContext,editText);
                }
            });

            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            textInputLayout.setHint("select");
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("Select");
            textInputLayout.setHintAnimationEnabled(true);
        }
        textInputLayout.addView(editText);
        parent.addView(textInputLayout);
    }


    public static void createDatePickerDialog(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent) {
        final TextInputLayout textInputLayout = new TextInputLayout(mContext);
        final TextInputEditText editText = new TextInputEditText(mContext);
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        final Calendar myCalendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        try {

            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            childparams.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(childparams);
            textInputLayout.setLayoutParams(childparams);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            if (reportFilterFeild.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }
            textInputLayout.setHint(reportFilterFeild.getLable());
            editText.setHint(reportFilterFeild.getLable());
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText(sdf.format(myCalendar.getTime()));
            textInputLayout.setHintAnimationEnabled(true);
            editText.setTag(sdf2.format(myCalendar.getTime()));
            // editText.setCompoundDrawables(null,null,ContextCompat.getDrawable(mContext,R.drawable.black_dropdown_arrow),null);


            if (reportFilterFeild.getValidation().equalsIgnoreCase("Numeric") || reportFilterFeild.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (reportFilterFeild.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setEnabled(false);
            }

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCalenderView(mContext, editText);
                }
            });

            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            textInputLayout.setHint("");
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText(sdf.format(myCalendar.getTime()));
            textInputLayout.setHintAnimationEnabled(true);
        }
        textInputLayout.addView(editText);
        parent.addView(textInputLayout);

    }

    private static void openCalenderView(Context mContext, final EditText editText) {
        try {

            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "dd-MM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat, Locale.US);
                    editText.setText(sdf2.format(myCalendar.getTime()));
                    editText.setTag(sdf.format(myCalendar.getTime()));

                }

            };

            new DatePickerDialog(mContext, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void openTemplateCalenderView(Context mContext, final EditText editText, final LinearLayout parent, final SubProcessFieldDataResponse.SubProcessField subProcessField) {
        try {

            final Calendar myCalendar = Calendar.getInstance();

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "dd-MM-yyyy"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat, Locale.US);
                    editText.setText(sdf2.format(myCalendar.getTime()));
                    String dependentKey = "";
                    try {
                        if (subProcessField.getOnchange() != null) {
                            try {

                                String dependentField = subProcessField.getOnchange();
                                if (dependentField.contains(",")) {
                                    String keyData[] = dependentField.split(",");
                                    dependentKey = keyData[1].replaceAll("'", "");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        HashMap<Integer, EditText> ageData = findCustomValidateEdittexts(parent, dependentKey);
                        if (ageData != null) {
                            try {
                                for (Map.Entry<Integer, EditText> ageMap : ageData.entrySet()) {
                                    if (ageMap.getValue() != null) {
                                        EditText ageEditText = ageMap.getValue();
                                        ageEditText.setEnabled(true);
                                        int age = IOUtils.getAge(year, monthOfYear, dayOfMonth);
                                        ageEditText.setText("" + age);
                                        ageEditText.setEnabled(false);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            //datePickerDialog.updateDate(1990, 0, 1);
            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void openTemplateYearMonthCalenderView(Context mContext, final EditText editText, final LinearLayout parent, final SubProcessFieldDataResponse.SubProcessField subProcessField) {
        try {

            final MonthYearPicker myp = new MonthYearPicker(activity);
            myp.build(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editText.setText(myp.getSelectedYear() + " yrs" + " - " + myp.getSelectedMonthName() + " months");
                    String dependentKey = "";
                    try {
                        if (subProcessField.getOnchange() != null) {
                            try {

                                String dependentField = subProcessField.getOnchange();
                                if (dependentField.contains(",")) {
                                    String keyData[] = dependentField.split(",");
                                    dependentKey = keyData[1].replaceAll("'", "");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        HashMap<Integer, EditText> ageData = findCustomValidateEdittexts(parent, dependentKey);
                        if (ageData != null) {
                            try {
                                for (Map.Entry<Integer, EditText> ageMap : ageData.entrySet()) {
                                    if (ageMap.getValue() != null) {
                                        EditText ageEditText = ageMap.getValue();
                                        ageEditText.setEnabled(true);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
            myp.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createHorizontalLayoutAndFieldsForReport(Context mContext, LinearLayout parent, ReportFilterModel.ReportFilter reportFilterFeilds) {
        try {

            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 20, 20);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
           /* if (reportFilterFeilds.getIsMandatory()) {
                createTextView(mContext, reportFilterFeilds.getLable(), linearLayout, Color.RED, 12, "", "");
            } else {
                createTextView(mContext, reportFilterFeilds.getLable(), linearLayout, ContextCompat.getColor(mContext, R.color.black), 12, "", "");
            }*/
            identifyReportViews(mContext, reportFilterFeilds, linearLayout);

            parent.addView(linearLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createReportButton(final Context context, final LinearLayout parent, ReportFilterModel.ReportFilter reportFilterFeilds, final List<ReportFilterModel.ReportFilter> reportFiltersList, final BtnClickListener btnClickListener) {
        try {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final Button button = new Button(new ContextThemeWrapper(context, R.style.ThemButton));
            params.setMargins(20, 20, 20, 20);
            button.setAllCaps(false);
            button.setLayoutParams(params);
            if (reportFilterFeilds != null) {
                button.setTag(reportFilterFeilds.getFieldID());
                button.setId(reportFilterFeilds.getFieldID());
            }
            button.setText("Submit");
            button.setTextColor(context.getResources().getColor(R.color.white));
            button.setBackgroundColor(context.getResources().getColor(R.color.clrLoginButton));
            button.setGravity(Gravity.CENTER);
            parent.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String date = "";
                    if (reportFiltersList != null) {
                        for (int i = 0; i < reportFiltersList.size(); i++) {
                            EditText editText = (EditText) parent.findViewById(reportFiltersList.get(i).getFieldID());
                            date = editText.getTag().toString();
                        }
                    } else {
                        reportFilters = findDateEditText(parent);
                        for (int i = 0; i < reportFilters.size(); i++) {
                            EditText editText = reportFilters.get(i);
                            date = editText.getTag().toString();
                        }
                    }
                    if (!TextUtils.isEmpty(date)) {
                        btnClickListener.clickListener(date);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<EditText> findDateEditText(ViewGroup viewGroup) {
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findDateEditText((ViewGroup) view);
                else if (view instanceof EditText) {
                    EditText edittext = (EditText) view;
                    reportFilters.add(edittext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reportFilters;
    }


    //create button

    public static void createUploadButton(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant) {
        try {
            final ArrayList<String> imagesList = new ArrayList<>();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final Button button = new Button(new ContextThemeWrapper(mContext, R.style.ThemButton));
            button.setAllCaps(false);
            button.setLayoutParams(params);
            button.setTag(subProcessField.getKey());
            button.setId(subProcessField.getFieldID());
            button.setText("Capture Photos");
            Drawable img = mContext.getResources().getDrawable(R.drawable.camera_dialog);
            img.setBounds(0, 0, 60, 60);
            button.setCompoundDrawables(img, null, null, null);
            button.setTextColor(mContext.getResources().getColor(R.color.white));
            //button.setBackgroundColor(mContext.getResources().getColor(R.color.clrLoginButton));
            button.setGravity(Gravity.CENTER);
            parent.addView(button);

            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(subProcessField.getKey())) {
                        isDependentField = true;
                    }
                }
            }
            if (subProcessField.getIsHidden() != null && subProcessField.getIsHidden() == true) {
                if (isDependentField) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                /*params.setMargins(0,0,0,0);
                editText.setLayoutParams(params);*/
                }
            } else {
                button.setVisibility(View.VISIBLE);
            }
            if (subProcessField != null && subProcessField.getValue().size() > 0) {
                for (int i = 0; i < subProcessField.getValue().size(); i++) {
                    imagesList.add(subProcessField.getValue().get(i));
                }
            }
           button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v4.app.FragmentManager fm = ((AllFormsActivity) mContext)
                            .getSupportFragmentManager();
                    UploadImageFragment uploadImageFragment = new UploadImageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("imagesList", imagesList);
                    uploadImageFragment.setArguments(bundle);
                    uploadImageFragment.show(fm, "Capture Photos");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //create UI for editText
    public static void createReportEditText(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent) {

        final TextInputEditText editText = new TextInputEditText(mContext);
        final TextInputLayout textInputLayout = new TextInputLayout(mContext);
        try {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(params);
            textInputLayout.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (reportFilterFeild.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }
            if (reportFilterFeild.getLable() != null) {
                textInputLayout.setHint(reportFilterFeild.getLable());
                editText.setHint(reportFilterFeild.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(reportFilterFeild.getLable());
                editText.setSelection(editText.getText().length());
                textInputLayout.setHintAnimationEnabled(true);
            } else {
                textInputLayout.setHint("");
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText("");
                editText.setSelection(editText.getText().length());
                textInputLayout.setHintAnimationEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            textInputLayout.setHint("");
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("");
            textInputLayout.setHintAnimationEnabled(true);

        }
        textInputLayout.addView(editText);
        parent.addView(textInputLayout);
    }


    public static void createButtonsLayout(Context mContext, final LinearLayout parent, final BtnClickListener btnClickListener, int formPosition, List<SubProcessFieldDataResponse.SubProcessField> subProcessFields) {
        try {
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.main_template_buttons, null);
            parent.addView(mLinearView);
            //Button btnSendLocation = (Button) mLinearView.findViewById(R.id.btnSendLocation);
            final Button btnAudio = (Button) mLinearView.findViewById(R.id.btnAudio);
            Button btnUploadImage = (Button) mLinearView.findViewById(R.id.btnUploadImage);
            Button btnSave = (Button) mLinearView.findViewById(R.id.btnSave);

            final Button btnSubmit = (Button) mLinearView.findViewById(R.id.btnSubmit);
            Button btnCancel = (Button) mLinearView.findViewById(R.id.btnCancel);
          /*  DBHelper dbHelper = DBHelper.getInstance(mContext);
            int loginJsonTemplateId = LoginTemplateDB.getLoginJsontemplateID(dbHelper,"Menu Details", UserPreference.getLanguage(mContext));
            ArrayList<SubCategory> subCategoryMenuList = SubCategoryDB.getSubCategoriesDependsOnIsMenuFlag(dbHelper, String.valueOf(loginJsonTemplateId), "true");*/
            /*if(AllFormsActivity.isCoApplicantExistsInTemplate && AllFormsActivity.hasCoApplicant == false)
            {
                if (formPosition == subCategoryMenuList.size() - 2) {
                    btnSubmit.setVisibility(View.VISIBLE);
                }
                else if(formPosition==0)
                {
                    if(AppConstant.isRedRadioButtonClicked) {
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        btnSubmit.setVisibility(View.GONE);
                    }
                }
                else{
                    btnSubmit.setVisibility(View.GONE);

                }
            }
            else
            {
                if (formPosition == subCategoryMenuList.size() - 1) {
                    btnSubmit.setVisibility(View.VISIBLE);
                }
                else if(formPosition==0)
                {
                    if(AppConstant.isRedRadioButtonClicked) {
                        btnSubmit.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        btnSubmit.setVisibility(View.GONE);
                    }
                }
                else{
                    btnSubmit.setVisibility(View.GONE);

                }
            }*/
/*
            if (AllFormsActivity.subCategoryMenusList != null && AllFormsActivity.subCategoryMenusList.size() > 0) {
                if (formPosition == AllFormsActivity.subCategoryMenusList.size() - 1) {
                    btnSubmit.setVisibility(View.VISIBLE);
                } else if (formPosition == 0) {
                    if (AppConstant.isRedRadioButtonClicked) {
                        btnSubmit.setVisibility(View.VISIBLE);
                    } else {
                        btnSubmit.setVisibility(View.GONE);
                    }
                } else {
                    btnSubmit.setVisibility(View.GONE);

                }
            }
*/
            btnUploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.uploadImageListener();
                }
            });
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.saveListerners();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.submitListener();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.cancelListener();
                }
            });

            btnAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.audioListener();
                }
            });

           /*// btnSendLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.sendGeoLocationListener();
                }
            });
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createViewPager(Context mContext, LinearLayout llGalleryView, int position, String formName) {
        try {
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.image_gallary, null);
            LinearLayout linearLayout = (LinearLayout) mLinearView.findViewById(R.id.llParentGallary);
            TextView textView = (TextView) mLinearView.findViewById(R.id.textView);
            linearLayout.setTag(formName + "_" + position);
            textView.setTag("textView" + formName + "_" + position);
            llGalleryView.addView(mLinearView);
            //  Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.view);
            // setImagesToGallaryView(mContext,llGalleryView,position,formName,bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImagesToGallaryView(final Context mContext, final LinearLayout llGalleryView, final int position, final String formName, final Bitmap imageBitmap, int addPosition, final ImageOperationListener mCallBack) {
        try {
            final TextView textView = (TextView) llGalleryView.findViewWithTag("textView" + formName + "_" + position);
            textView.setText(formName);
            final LinearLayout linearLayout = (LinearLayout) llGalleryView.findViewWithTag(formName + "_" + position);
            LinearLayout linearLayoutInner = new LinearLayout(mContext);
            LinearLayout.LayoutParams relparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayoutInner.setLayoutParams(relparams);
            linearLayoutInner.setOrientation(LinearLayout.VERTICAL);
            linearLayoutInner.setTag("linearLayout_" + formName + "_" + position + "_" + addPosition);
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
            params.setMargins(5, 5, 5, 5);
            imageView.setLayoutParams(params);
            imageView.setTag("imageView_" + formName + "_" + position);
            imageView.setImageBitmap(imageBitmap);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFullImagePopup(mContext, imageBitmap);
                }
            });
            TextView btnDelete = new TextView(mContext);
            LinearLayout.LayoutParams paramsClose = new LinearLayout.LayoutParams(150, 50);
            paramsClose.setMargins(5, 5, 5, 5);
            btnDelete.setLayoutParams(paramsClose);
            btnDelete.setTag("delete_" + formName + "_" + position + "_" + addPosition);
            btnDelete.setText("Delete");
            btnDelete.setPadding(5, 5, 5, 5);
            btnDelete.setTextColor(Color.BLACK);
            btnDelete.setBackgroundColor(Color.GRAY);
            btnDelete.setGravity(Gravity.CENTER_HORIZONTAL);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] tag_arr = v.getTag().toString().split("_");
                    //LinearLayout linearLayout = (LinearLayout)llGalleryView.findViewWithTag(tag_arr[0]+"_"+tag_arr[1]);
                    LinearLayout linearLayoutInner = (LinearLayout) linearLayout.findViewWithTag("linearLayout_" + tag_arr[1] + "_" + tag_arr[2] + "_" + tag_arr[3]);
                    linearLayout.removeView(linearLayoutInner);
                    llGalleryView.invalidate();
                    mCallBack.imageDelete(tag_arr[1], Integer.parseInt(tag_arr[2]), Integer.parseInt(tag_arr[3]));
                    if (linearLayout.getChildCount() == 0) {
                        textView.setText("");
                    }
                }
            });
            linearLayoutInner.addView(imageView);
            linearLayoutInner.addView(btnDelete);
            linearLayout.addView(linearLayoutInner);

            //llGalleryView.invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void openFullImagePopup(final Context mContext, final Bitmap imageBitmap) {
        try {
            final Dialog dialogImages = new Dialog(mContext);
            dialogImages.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogImages.setContentView(R.layout.full_image_layout);
            dialogImages.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            ImageViewTouch img = (ImageViewTouch) dialogImages.findViewById(R.id.zoomImage);
            img.setFitsSystemWindows(true);
            img.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            img.setImageBitmap(imageBitmap);
            dialogImages.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createCropImageDialog(final Context mContext, final Bitmap imageBitmap, final ImageOperationListener cropImageBtnClickListener) {
        try {
            final Dialog dialogCropImage = new Dialog(mContext);
            dialogCropImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCropImage.setContentView(R.layout.image_crop_dialog);
            dialogCropImage.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            final CropImageView cropImageView = (CropImageView) dialogCropImage.findViewById(R.id.cropImageView);
            Button doneButton = (Button) dialogCropImage.findViewById(R.id.doneButton);
            cropImageView.setImageBitmap(imageBitmap);
            dialogCropImage.show();
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap cropped = cropImageView.getCroppedImage();
                    cropImageBtnClickListener.cropImageListener(cropped);
                    if (dialogCropImage != null) {
                        if (dialogCropImage.isShowing()) {
                            dialogCropImage.dismiss();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createReportTextArea(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent) {
        final TextInputEditText editText = new TextInputEditText(mContext);
        final TextInputLayout textInputLayout = new TextInputLayout(mContext);
        try {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(params);
            textInputLayout.setLayoutParams(params);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setPadding(10, 10, 10, 20);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (reportFilterFeild.getIsMandatory()) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }
            if (reportFilterFeild.getLable() != null) {

                textInputLayout.setHint(reportFilterFeild.getLable());
                editText.setHint(reportFilterFeild.getLable());
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(reportFilterFeild.getLable());
                editText.setSelection(editText.getText().length());
                textInputLayout.setHintAnimationEnabled(true);
            }

            if (reportFilterFeild.getValidation().equalsIgnoreCase("Numeric") || reportFilterFeild.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (reportFilterFeild.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
            editText.setGravity(Gravity.TOP);
            if (reportFilterFeild.getValidationPattern() != null && !reportFilterFeild.getValidationPattern().isEmpty()) {
                editText.addTextChangedListener(new TextWatcher() {
                    Pattern sPattern
                            = Pattern.compile(reportFilterFeild.getValidationPattern());

                    private CharSequence mText;

                    private boolean isValid(CharSequence s) {
                        return sPattern.matcher(s).matches();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        //mText = isValid(s) ? new CharSequence(s) : "";
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (isValid(s)) {
                            // Toast.makeText(mContext, reportFilterFeild.getValidationMessage(), Toast.LENGTH_LONG).show();
                            MyDynamicToast.errorMessage(mContext, reportFilterFeild.getValidationMessage());
                            //  editText.setText("");
                        }
                        mText = null;
                    }
                });
            }

            editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            textInputLayout.setHint("");
            textInputLayout.setHintAnimationEnabled(false);
            editText.setText("");
            textInputLayout.setHintAnimationEnabled(true);
        }
        textInputLayout.addView(editText);
        parent.addView(textInputLayout);
    }

    public static ArrayList<EditText> findAllTableEdittexts(ViewGroup viewGroup, String mandatoryTag) {
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findAllTableEdittexts((ViewGroup) view, mandatoryTag);
                else if (view instanceof EditText) {
                    EditText edittext = (EditText) view;
                    if (edittext.getTag().toString().trim().equalsIgnoreCase(mandatoryTag.trim())) {
                        arrayListForMandatoryTableFields.add(edittext);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayListForMandatoryTableFields;
    }

    public HashMap<String, String> findAllEdittexts(ViewGroup viewGroup) {
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findAllEdittexts((ViewGroup) view);
                else if (view instanceof EditText) {
                    EditText edittext = (EditText) view;
                    if (edittext.getTag().toString().contains("_0")) {
                    } else {
                        if (edittext.getText().toString().equalsIgnoreCase("Select")) {
                            editTextData.put(edittext.getTag().toString(), "");
                        } else {
                            editTextData.put(edittext.getTag().toString(), edittext.getText().toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editTextData;
    }

    public HashMap<String, EditText> findAllEdittextsToValidate(ViewGroup viewGroup) {
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findAllEdittextsToValidate((ViewGroup) view);
                else if (view instanceof EditText) {
                    EditText edittext = (EditText) view;
                    String tag = edittext.getTag().toString();
                    String key = "";
                    if (tag.contains("/")) {
                        key = tag.split("/")[0];
                    } else {
                        key = tag;
                    }
                    hashMapEditText.put(key, edittext);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMapEditText;
    }

    public HashMap<String, String> putAllKeysToSave(String[] allFieldsKeysArray, JobDetailsResponse.Applicant applicant, String applicant_json) {
        try {
            templateTextViewData.clear();
            for (int j = 0; j < allFieldsKeysArray.length; j++) {

                String key = allFieldsKeysArray[j];

                Method method = null;
                JSONObject jsonObject = null;
                String val = null;
                try {
                    jsonObject = new JSONObject(applicant_json);
                    val = jsonObject.getString(key);
                    method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(key));
                    String text = (String) method.invoke(applicant, null);
                    if (text != null && !text.isEmpty()) {
                        templateTextViewData.put(key, text);
                    } else if (!TextUtils.isEmpty(val)) {
                        templateTextViewData.put(key, val);
                    } else {
                        templateTextViewData.put(key, "");
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    if (!TextUtils.isEmpty(val)) {
                        templateTextViewData.put(key, val);
                    } else {
                        templateTextViewData.put(key, "");
                    }
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    if (!TextUtils.isEmpty(val)) {
                        templateTextViewData.put(key, val);
                    } else {
                        templateTextViewData.put(key, "");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    if (!TextUtils.isEmpty(val)) {
                        templateTextViewData.put(key, val);
                    } else {
                        templateTextViewData.put(key, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                    templateTextViewData.put(key, "");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateTextViewData;
    }

    public HashMap<String, String> findAllTemplateTextViews(ViewGroup viewGroup) {
        try {

            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findAllTemplateTextViews((ViewGroup) view);
                else if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (textView.getTag() != null) {

                        if (textView.getTag().toString().contains("textView_") || textView.getTag().toString().contains("_0") || textView.getTag().toString().isEmpty() || textView.getTag().toString().contains(" ")) {
                        } else {

                            templateTextViewData.put(textView.getTag().toString(), textView.getText().toString());
                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateTextViewData;
    }


    public HashMap<String, String> findAllTextViews(ViewGroup viewGroup, String tableExist, String tableKeys) {
        try {
            if (tableKeys != null) {
                String tableHeaderArray[] = tableKeys.split(",");
                int count = viewGroup.getChildCount();
                for (int i = 0; i < count; i++) {
                    View view = viewGroup.getChildAt(i);
                    if (view instanceof ViewGroup) {
                        //if (templateFields != null) {
                        findAllTextViews((ViewGroup) view, tableExist, tableKeys);
                        // }
                    } else if (view instanceof TextView) {
                        TextView textView = (TextView) view;
                        if (textView.getTag() != null && !textView.getTag().toString().contains(" ")) {
                            if (tableExist != null && tableExist.equalsIgnoreCase("true")) {
                                for (int l = 0; l < tableHeaderArray.length; l++) {
                                    if (textView.getTag().toString().contains(tableHeaderArray[l].trim())) {
                                        if (textView.getTag().toString().contains("textView_") || textView.getTag().toString().contains("_0")) {
                                        } else {
                                            textViewData.put(textView.getTag().toString(), textView.getText().toString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textViewData;
    }


    public static HashMap<Integer, EditText> findCustomValidateEdittexts(ViewGroup viewGroup, String key) {
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findCustomValidateEdittexts((ViewGroup) view, key);
                else if (view instanceof EditText) {

                    EditText edittext = (EditText) view;
                    if (edittext.getTag() != null) {
                        if (edittext.getTag().toString().equalsIgnoreCase(key) || edittext.getTag().toString().equalsIgnoreCase(key + "/Mandatory")) {
                            customValidEditTextHashMap.put(edittext.getId(), edittext);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customValidEditTextHashMap;
    }

    public static HashMap<Integer, EditText> findEditTextForShowHide(ViewGroup viewGroup, String key) {
        showHideEditTextHashMap.clear();
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    if (showHideEditTextHashMap.size() == 0) {
                        findEditTextForShowHide((ViewGroup) view, key);
                    }
                } else if (view instanceof EditText) {

                    EditText edittext = (EditText) view;
                    if (edittext.getTag() != null) {
                        if (edittext.getTag().toString().equalsIgnoreCase(key) || edittext.getTag().toString().equalsIgnoreCase(key + "/Mandatory")) {
                            showHideEditTextHashMap.put(edittext.getId(), edittext);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showHideEditTextHashMap;
    }


    public HashMap<String, RadioButton> findCustomValidateRadioButtons(ViewGroup viewGroup) {
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findCustomValidateRadioButtons((ViewGroup) view);
                else if (view instanceof RadioButton) {

                    RadioButton radioButton = (RadioButton) view;
                    if (radioButton.getTag() != null) {
                        if (radioButton.isChecked()) {
                            customValidRadioButtontHashMap.put(radioButton.getTag().toString(), radioButton);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customValidRadioButtontHashMap;
    }


    public HashMap<String, CheckBox> findAllCheckBox(ViewGroup viewGroup) {
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    findAllCheckBox((ViewGroup) view);
                } else if (view instanceof CheckBox) {

                    CheckBox checkBox = (CheckBox) view;
                    if (checkBox != null) {
                        if (checkBox.getTag() != null) {
                            checkBoxHashMap.put(checkBox.getTag().toString(), checkBox);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkBoxHashMap;
    }


    public static HashMap<String, Button> findButtons(ViewGroup viewGroup, String key) {
        buttonsHashMap.clear();
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    if (buttonsHashMap.size() == 0) {
                        findButtons((ViewGroup) view, key);
                    }
                } else if (view instanceof Button) {

                    Button button = (Button) view;
                    if (button != null) {
                        if (button.getText().toString().equalsIgnoreCase(key)) {
                            buttonsHashMap.put(button.getText().toString(), button);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buttonsHashMap;
    }


    public static HashMap<String, Button> findMapButtons(ViewGroup viewGroup, String key) {
        mapbuttonsHashMap.clear();
        try {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    if (mapbuttonsHashMap.size() == 0) {
                        findMapButtons((ViewGroup) view, key);
                    }
                } else if (view instanceof Button) {

                    Button button = (Button) view;
                    if (button != null) {
                        if (button.getTag().toString().equalsIgnoreCase(key)) {
                            mapbuttonsHashMap.put(button.getTag().toString(), button);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapbuttonsHashMap;
    }


    public static void createHoldPopup(final Context context, final String[] holdReasonsList, final BtnClickListener btnClickListener, final String jobID, final String nbfcName) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.hold_job_popup);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextInputLayout textInputLayoutDate = (TextInputLayout) dialog.findViewById(R.id.selectDateInput);
        final EditText editTextDate = (EditText) dialog.findViewById(R.id.HoldDateEditText);
        TextInputLayout textInputLayoutReason = (TextInputLayout) dialog.findViewById(R.id.selectReasonInput);
        Button okButton = (Button) dialog.findViewById(R.id.okButton);
        Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        final EditText editTextReason = (EditText) dialog.findViewById(R.id.ReasonEditText);
        editTextReason.setText("Select");
        String myFormat = "dd/MM/yyyy HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar myCalendar = Calendar.getInstance();
        editTextDate.setText(sdf.format(myCalendar.getTime()));
        ((EditText) editTextDate).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        openHoldCalenderView(context, editTextDate);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }

                return true;
            }
        });

        editTextReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHoldDropDownOptions(context, holdReasonsList, editTextReason, "Select");
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editTextDate.getText().toString()) && !TextUtils.isEmpty(editTextReason.getText().toString()) && !editTextReason.getText().toString().equalsIgnoreCase("select")) {
                    String myFormat = "dd/MM/yyyy HH:mm:ss"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    btnClickListener.holdListener(editTextDate.getText().toString(), editTextReason.getText().toString(), jobID, nbfcName);
                    dialog.dismiss();
                } else {
                    MyDynamicToast.errorMessage(context, "Fields can not be empty");
                }
            }

        });
        dialog.show();
    }

    private static void openHoldCalenderView(final Context mContext, final EditText editText) {

        try {
            final Date[] value = {new Date()};
            final Calendar cal = Calendar.getInstance();
            cal.setTime(value[0]);
            new DatePickerDialog(mContext,
                    new DatePickerDialog.OnDateSetListener() {
                        boolean mFirst = true;

                        @Override
                        public void onDateSet(DatePicker view,
                                              int y, int m, int d) {

                            if (mFirst) {
                                mFirst = false;

                                cal.set(Calendar.YEAR, y);
                                cal.set(Calendar.MONTH, m);
                                cal.set(Calendar.DAY_OF_MONTH, d);
                                // now show the time picker
                                new TimePickerDialog(mContext,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view,
                                                                  int h, int min) {
                                                cal.set(Calendar.HOUR_OF_DAY, h);
                                                cal.set(Calendar.MINUTE, min);
                                                value[0] = cal.getTime();
                                                String myFormat = "dd/MM/yyyy HH:mm:ss"; //In which you need put here
                                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                                String date = sdf.format(value[0]);
                                                editText.setText(date);
                                            }
                                        }, cal.get(Calendar.HOUR_OF_DAY),
                                        cal.get(Calendar.MINUTE), true).show();
                            }
                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void openHoldDropDownOptions(final Context mContext, String value[], final EditText editText, final String lable) {
        try {

            final String value_arr[] = new String[value.length + 1];
            value_arr[0] = lable;
            for (int i = 0; i < value.length; i++) {
                int pos = i + 1;
                value_arr[pos] = value[i];
            }
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_dropdown);
            ListView dropdownList = (ListView) dialog.findViewById(R.id.dropdownList);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.list_dropdown_items, value_arr);
            adapter.notifyDataSetChanged();
            dropdownList.setAdapter(adapter);
            dropdownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        editText.setText(value_arr[position]);
                    }
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
