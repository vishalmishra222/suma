package com.app.dynamicform;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.GregorianCalendar;
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

public class UI {
    static ArrayList<Integer> arrayListCheckBoxPosition = new ArrayList<>();
    private static View viewParent;
    private static HashMap<Integer, EditText> customValidEditTextHashMap = new HashMap<>();
    SparseArray<EditText> array = new SparseArray<EditText>();
    HashMap<String,String> templateTextViewData = new HashMap<>();
    HashMap<String,String> editTextData = new HashMap<>();
    HashMap<String,String> textViewData = new HashMap<>();
    HashMap<Integer, String> getDateOfBirth = new HashMap<>();
    HashMap<String,EditText> hashMapEditText = new HashMap<>();
    static HashMap<String,HashMap<String,ArrayList<String>>> hashMapCustomValidation = new HashMap<>();
    static ArrayList<String> arrayListFields = new ArrayList<>();
    HashMap<String,String> staticFieldsController = new HashMap<>();
    HashMap<String,String> dynamicFieldsController = new HashMap<>();
    public static int addCount = 0;
    private static int noOfUncheck = 0;
    Context context;
    JSONObject jsonObject;
    public UI() {
    }

    public UI(Context context) {
        this.context = context;
    }

    //create UI for textView
    public static void createTextView(Context mContext, String text, LinearLayout parent, int textColor, float textSize, String headerType,String tag) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
        textView.setLayoutParams(params);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setTag(tag);
        textView.setPadding(10,10,10,10);
        if(text == null){
            text ="";
        }
        if(headerType.equalsIgnoreCase("tabs")){
            textView.setText("View Job Details");
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
        }else if(headerType.equalsIgnoreCase("form")){
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
            textView.setTypeface(null, Typeface.BOLD);
        }else{
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
        }
        parent.addView(textView);
    }

    //create UI for editText
    public static void createEditText(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant) {
        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50,1f);
        params.setMargins(10,10,10,10);
        editText.setLayoutParams(params);
        editText.setPadding(10,10,10,10);
        if(subProcessField.getIsMandatory()) {
            editText.setTag(subProcessField.getKey()+"/Mandatory");
        }
        else
        {
            editText.setTag(subProcessField.getKey());
        }
        editText.setId(subProcessField.getFieldID());
        editText.setTextColor(Color.BLACK);
        editText.setTextSize(12);

        Method method = null;
        try {
            method =JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
            String text = (String) method.invoke(applicant,null);
            if(text!=null && !text.isEmpty()) {
                editText.setText(text);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            editText.setText("");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            editText.setText("");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            editText.setText("");
        }catch (Exception e){
            e.printStackTrace();;
            editText.setText("");
        }


        if(subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers") || subProcessField.getValidation().equalsIgnoreCase("Decimal")){
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }else if(subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")){
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if(subProcessField.getValidationPattern()!=null && !subProcessField.getValidationPattern().isEmpty()) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                Pattern sPattern
                        = Pattern.compile(subProcessField.getValidationPattern());
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!v.hasFocus()){
                        EditText edt = (EditText)v;
                        if (!isValid(edt.getText().toString())) {
                            //   editText.setText("");
                            editText.setError(subProcessField.getValidationMessage());
                        }else{
                            editText.setError(null);
                        }
                    }
                }
                private boolean isValid(CharSequence s) {
                    return sPattern.matcher(s).matches();
                }
            });
        }

        if(subProcessField.getIsreadonly()!=null && subProcessField.getIsreadonly()){
            editText.setBackgroundResource(R.drawable.disabled_edittext_background);
            editText.setEnabled(false);
        }else{
            editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);
        }
        try {
            viewParent = parent;
            String key = "";
            if(editText.getTag().toString().contains("/Mandatory")){
                key = editText.getTag().toString().split("/")[0] ;
            }else{
                key = editText.getTag().toString();
            }

            for (Map.Entry<String, HashMap<String, ArrayList<String>>> customeValidationData  : hashMapCustomValidation.entrySet()) {
                HashMap<String, ArrayList<String>> hmStateFields = customeValidationData.getValue();

                for (HashMap.Entry<String, ArrayList<String>> validationData : hmStateFields.entrySet()) {

                    if (validationData.getKey().contains("No") || validationData.getKey().contains("Select")) {
                        for (int i = 0; i < validationData.getValue().size(); i++) {
                            if(validationData.getValue().get(i).trim().equalsIgnoreCase(key)) {
                                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                editText.setEnabled(false);
                                break;
                            }
                        }
                    } else {
                        for (int i = 0; i < validationData.getValue().size(); i++) {
                            if(validationData.getValue().get(i).trim().equalsIgnoreCase(key)) {
                                editText.setBackgroundResource(R.drawable.form_header_background);
                                editText.setEnabled(true);
                                break;
                            }
                        }
                    }
                }

            }


        }catch (Exception e){

        }

        parent.addView(editText);
    }

    //dynamic create UI for tabs header and form header
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void createUIForHeader(Context mContext, LinearLayout parent, String name, String headerType){
        try {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setTag("name_");
            if (headerType.equalsIgnoreCase("tabs")) {
                linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tabs_background));
                linearLayout.setGravity(Gravity.CENTER);
            } else {
                createView(mContext, linearLayout, 15, ViewGroup.LayoutParams.MATCH_PARENT, ContextCompat.getColor(mContext, R.color.clrToolbarBg));
                linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.form_header_background));
            }
            parent.addView(linearLayout);
            createTextView(mContext, name, linearLayout, ContextCompat.getColor(mContext, R.color.clrToolbarBg), 16, headerType, "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }



    //create UI for custom view
    public static void createView(Context mContext, LinearLayout parent,  int width, int height,int color){
        View view = new View(mContext);
        view.setBackgroundColor(color);
        view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        parent.addView(view);
    }


    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout parent, SubProcessFieldDataResponse.SubProcessField subProcessField1, String type, JobDetailsResponse.Applicant applicant) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        identifyViews(mContext,subProcessField1,linearLayout, applicant,"",parent);
        String name = "";

        Method method = null;
        try {
            method =JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField1.getKey()));
            name = (String) method.invoke(applicant,null);
            if(name==null || name.isEmpty()){
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
            name ="";
        }catch (Exception e){
            name = "";
        }

        createTextView(mContext,name,linearLayout,ContextCompat.getColor(mContext,R.color.black),12,"",subProcessField1.getKey());
        parent.addView(linearLayout);
    }

    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout parent, SubProcessFieldDataResponse.SubProcessField subProcessField, JobDetailsResponse.Applicant applicant, String value) {
        try {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (subProcessField.getIsMandatory()) {
                createTextView(mContext, subProcessField.getLable(), linearLayout, Color.RED, 14, "", "");
            } else {
                createTextView(mContext, subProcessField.getLable(), linearLayout, ContextCompat.getColor(mContext, R.color.black), 14, "", "");
            }
            identifyViews(mContext, subProcessField, linearLayout, applicant, value, parent);
            parent.addView(linearLayout);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void identifyViews(Context mContext, SubProcessFieldDataResponse.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant, String value, LinearLayout mainParent) {
        switch (subProcessField.getFieldType()){
            case "label":
                if(subProcessField.getIsMandatory()){
                    createTextView(mContext, subProcessField.getLable(), parent, Color.RED, 14, "","");
                }else {
                    createTextView(mContext, subProcessField.getLable(), parent, ContextCompat.getColor(mContext, R.color.black), 14, "","");
                }
                break;
            case "text":
                createEditText(mContext,subProcessField,parent,applicant);
                break;
            case "dropDownList":
                createDropdown(mContext,subProcessField,parent,applicant,value,mainParent);
                break;
            case "textArea":
                createTextArea(mContext,subProcessField,parent,applicant);
                break;
            case "Date":
                createDatePickerDialog (mContext,subProcessField,parent,applicant,value,mainParent);
                break;
        }
    }

    public static void createDatePickerDialog(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final String value, final LinearLayout mainParent)
    {
        try {

            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1f);
            editText.setLayoutParams(childparams);
            editText.setPadding(10, 10, 10, 10);
            if (subProcessField.getIsMandatory()) {
                editText.setTag(subProcessField.getKey() + "/Mandatory");
            } else {
                editText.setTag(subProcessField.getKey());
            }
            editText.setId(subProcessField.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            final Calendar myCalendar = Calendar.getInstance();
            //  editText.setText(sdf.format(myCalendar.getTime()));
            // editText.setCompoundDrawables(null,null,ContextCompat.getDrawable(mContext,R.drawable.black_dropdown_arrow),null);


            Method method = null;
            try {
                method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    editText.setText(text);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                editText.setText(sdf.format(myCalendar.getTime()));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                editText.setText(sdf.format(myCalendar.getTime()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                editText.setText(sdf.format(myCalendar.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
                editText.setText(sdf.format(myCalendar.getTime()));
            }

            if (subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setEnabled(false);
            }

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTemplateCalenderView(mContext, editText, mainParent,subProcessField);
                }
            });

            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);
            parent.addView(editText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void createDropdown(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, final LinearLayout parent, JobDetailsResponse.Applicant applicant, final String value, final LinearLayout mainParent) {
        try {
            final EditText editText = new EditText(mContext);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 10);
            if (subProcessField.getIsMandatory()) {
                editText.setTag(subProcessField.getKey() + "/Mandatory");
            } else {
                editText.setTag(subProcessField.getKey());
            }
            editText.setId(subProcessField.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);

            Method method = null;
            try {
                method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    editText.setText(text);
                } else {
                    editText.setText("Select");
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                editText.setText("Select");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                editText.setText("Select");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                editText.setText("Select");
            } catch (Exception e) {
                e.printStackTrace();
                editText.setText("Select");
            }
            if (subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            if(subProcessField.getCustomValidationFunction()!=null && !subProcessField.getCustomValidationFunction().isEmpty()){
                if(subProcessField.getCustomValidationFunction().contains("toggleFields")) {
                    ArrayList<String> arrayListFields = new ArrayList<>();
                    String fn = subProcessField.getCustomValidationFunction().replace("[", "").replace("]", "").replace("(", "").replace(")", "").replace("'", "");

                    String fn_array[] = fn.split(",");
                    String current_state = fn_array[1];
                    try {
                        for (int i = 3; i < fn_array.length; i++) {
                            arrayListFields.add(fn_array[i]);
                        }
                        HashMap<String, ArrayList<String>> hmStatefields = new HashMap<>();
                        if(editText.getText().toString().equalsIgnoreCase("Yes")) {
                            hmStatefields.put(editText.getText().toString(), arrayListFields);
                        }else{
                            hmStatefields.put(current_state, arrayListFields);
                        }
                        hashMapCustomValidation.put(subProcessField.getKey(), hmStatefields);
                    } catch (Exception e) {

                    }
                }
            }
            if (!value.isEmpty()) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDropDownOptions(mContext, value, editText,mainParent);
                    }
                });
            }
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            if (subProcessField.getIsreadonly() != null && subProcessField.getIsreadonly()) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }

            parent.addView(editText);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void openDropDownOptions(Context mContext, String value, final EditText editText, final LinearLayout mainParent) {
        try {
            JSONArray jsonArray = new JSONArray(value);

            final String value_arr[] = new String[jsonArray.length()+1];
            value_arr[0] = "Select";
            for(int i=0;i<jsonArray.length();i++){
                int pos = i+1;
                value_arr[pos] = jsonArray.getString(i);
            }
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_dropdown);
            ListView dropdownList = (ListView)dialog.findViewById(R.id.dropdownList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.list_dropdown_items,value_arr);
            adapter.notifyDataSetChanged();
            dropdownList.setAdapter(adapter);
            dropdownList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editText.setText(value_arr[position]);
                    dialog.dismiss();
                    try {
                        if (hashMapCustomValidation.containsKey(editText.getTag().toString().split("/")[0])) {
                            String key = editText.getTag().toString();
                            if (editText.getTag().toString().contains("/Mandatory")) {
                                key = editText.getTag().toString().split("/")[0];
                            } else {
                                key = editText.getTag().toString();
                            }
                            HashMap<String, ArrayList<String>> hmStateFields = hashMapCustomValidation.get(key);
                            for (HashMap.Entry<String, ArrayList<String>> validationData : hmStateFields.entrySet()) {

                                if (value_arr[position].equalsIgnoreCase("No") || value_arr[position].equalsIgnoreCase("Select")) {
                                    for (int i = 0; i < validationData.getValue().size(); i++) {
                                        HashMap<Integer,EditText> hm = findCustomValidateEdittexts(mainParent, validationData.getValue().get(i).trim());
                                        try {
                                            for (Map.Entry<Integer, EditText> hmEntry : hm.entrySet()) {
                                                EditText editTextField = hmEntry.getValue();
                                                editTextField.setBackgroundResource(R.drawable.disabled_edittext_background);
                                                editTextField.setEnabled(false);
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                    HashMap<String, ArrayList<String>> hmStateFieldsNew = new HashMap<String, ArrayList<String>>();
                                    if(hmStateFields.containsKey("Yes")) {
                                        hmStateFieldsNew.put("No", hmStateFields.get("Yes"));
                                        hashMapCustomValidation.put(key, hmStateFieldsNew);
                                    }
                                } else {
                                    for (int i = 0; i < validationData.getValue().size(); i++) {
                                        HashMap<Integer,EditText> hm = findCustomValidateEdittexts(mainParent, validationData.getValue().get(i).trim());
                                        try {
                                            for (Map.Entry<Integer, EditText> hmEntry : hm.entrySet()) {
                                                EditText editTextField = hmEntry.getValue();
                                                editTextField.setBackgroundResource(R.drawable.form_header_background);
                                                editTextField.setEnabled(true);
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                    HashMap<String, ArrayList<String>> hmStateFieldsNew = new HashMap<String, ArrayList<String>>();
                                    if(hmStateFieldsNew.containsKey("No")) {
                                        hmStateFieldsNew.put("Yes", hmStateFields.get("No"));
                                        hashMapCustomValidation.put(key, hmStateFieldsNew);
                                    }
                                }
                            }


                        }
                    }catch (Exception e){

                    }

                }
            });
            dialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void createTextArea(final Context mContext, final SubProcessFieldDataResponse.SubProcessField subProcessField, LinearLayout parent, JobDetailsResponse.Applicant applicant) {
        try {
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);

            if (subProcessField.getIsMandatory()) {
                editText.setTag(subProcessField.getKey() + "/Mandatory");
            } else {
                editText.setTag(subProcessField.getKey());
            }
            editText.setId(subProcessField.getFieldID());
            editText.setPadding(10, 10, 10, 10);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
        /*if(subProcessField.getFieldName().equalsIgnoreCase("Residential Address"))
        {
            editText.setText(applicant.getResidentialAddress());
        }
        else{
            editText.setText("");
        }*/
            Method method = null;
            try {
                method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessField.getKey()));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    editText.setText(text);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                editText.setText("");
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                editText.setText("");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                editText.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                editText.setText("");
            }
            if (subProcessField.getValidation().equalsIgnoreCase("Numeric") || subProcessField.getValidation().equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else if (subProcessField.getValidation().equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
            editText.setGravity(Gravity.TOP);
            if (subProcessField.getValidationPattern() != null && !subProcessField.getValidationPattern().isEmpty()) {
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    Pattern sPattern
                            = Pattern.compile(subProcessField.getValidationPattern());

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!v.hasFocus()) {
                            EditText edt = (EditText) v;
                            if (!isValid(edt.getText().toString())) {
                                editText.setText("");
                                editText.setError(subProcessField.getValidationMessage());
                            }
                        }
                    }

                    private boolean isValid(CharSequence s) {
                        return sPattern.matcher(s).matches();
                    }
                });
            }
            if (subProcessField.getIsreadonly() != null && subProcessField.getIsreadonly()) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }
            parent.addView(editText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //reportFilers
    public static void identifyReportViews(Context mContext, ReportFilterModel.ReportFilter reportField, LinearLayout parent) {
        switch (reportField.getFieldType()){
            case "label":
                if(reportField.getIsMandatory()){
                    createTextView(mContext, reportField.getLable(), parent, Color.RED, 12, "","");
                }else {
                    createTextView(mContext, reportField.getLable(), parent, ContextCompat.getColor(mContext, R.color.black), 12, "","");
                }
                break;
            case "text":
                createReportEditText(mContext,reportField,parent);
                break;
            case "dropDownList":
                createReportDropdown(mContext,reportField,parent);
                break;
            case "textArea":
                createReportTextArea(mContext,reportField,parent);
                break;
            case "Date":
                createDatePickerDialog (mContext,reportField,parent);
                break;
        }
    }

    public static void createReportDropdown(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent) {
        try {
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 10);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);
            // editText.setCompoundDrawables(null,null,ContextCompat.getDrawable(mContext,R.drawable.black_dropdown_arrow),null);
            if (reportFilterFeild.getLable() != null) {
                editText.setText(reportFilterFeild.getLable());
            } else {
                editText.setText("Select");
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
            parent.addView(editText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void createDatePickerDialog(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent)
    {
        try {
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1f);
            editText.setLayoutParams(childparams);
            editText.setPadding(10, 10, 10, 10);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            final Calendar myCalendar = Calendar.getInstance();
            editText.setText(sdf.format(myCalendar.getTime()));
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

            editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);
            parent.addView(editText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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
        }
        catch(Exception e)
        {
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
                        if(subProcessField.getOnchange()!=null)
                        {
                            try {

                                String dependentField = subProcessField.getOnchange();
                                if (dependentField.contains(",")) {
                                    String keyData[] = dependentField.split(",");
                                    dependentKey = keyData[1].replaceAll("'", "");
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        HashMap<Integer, EditText> ageData = findCustomValidateEdittexts(parent,dependentKey);
                        if (ageData != null) {
                            try {
                                for (Map.Entry<Integer, EditText> ageMap : ageData.entrySet()) {
                                    if (ageMap.getValue() != null) {
                                        EditText ageEditText = ageMap.getValue();
                                        ageEditText.setEnabled(true);
                                        int age = getAge(year, monthOfYear, dayOfMonth);
                                        ageEditText.setText("" + age);
                                        ageEditText.setEnabled(false);
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

            };

            new DatePickerDialog(mContext, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public static void setImagesToGallaryView(final Context mContext, final LinearLayout llGalleryView, final int position, final String formName, final Bitmap imageBitmap, int addPosition, final ImageOperationListener mCallBack){
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
                    if(linearLayout.getChildCount()==0){
                        textView.setText("");
                    }
                }
            });
            linearLayoutInner.addView(imageView);
            linearLayoutInner.addView(btnDelete);
            linearLayout.addView(linearLayoutInner);

            //llGalleryView.invalidate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void openFullImagePopup(final Context mContext,final Bitmap imageBitmap) {
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
        }
        catch (Exception e)
        {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void createButtonsLayout(Context mContext, final LinearLayout parent, final BtnClickListener btnClickListener) {
        try {
            LayoutInflater inflater = null;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.main_template_buttons, null);
            parent.addView(mLinearView);
            // Button btnSendLocation = (Button) mLinearView.findViewById(R.id.btnSendLocation);
            Button btnUploadImage = (Button) mLinearView.findViewById(R.id.btnUploadImage);
            Button btnSave = (Button) mLinearView.findViewById(R.id.btnSave);

            final Button btnAudio = (Button) mLinearView.findViewById(R.id.btnAudio);
            final Button btnSubmit = (Button) mLinearView.findViewById(R.id.btnSubmit);
            Button btnCancel = (Button) mLinearView.findViewById(R.id.btnCancel);
            if(AppConstant.formPosition == 6){
                //  btnSendLocation.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
            }
            else if(AppConstant.formPosition==0)
            {
                btnSubmit.setVisibility(View.GONE);
                // btnSendLocation.setVisibility(View.GONE);
            }
            else{
                // btnSendLocation.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);

            }

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

            btnAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.audioListener();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.cancelListener();
                }
            });

           /* btnSendLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnClickListener.sendGeoLocationListener();
                }
            });*/

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

    //create UI for editText
    public static void createReportEditText(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent) {
        try {
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 70, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 10);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            if (reportFilterFeild.getLable() != null) {
                editText.setText(reportFilterFeild.getLable());
            } else {
                editText.setText("");
            }
            parent.addView(editText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void createReportTextArea(final Context mContext, final ReportFilterModel.ReportFilter reportFilterFeild, LinearLayout parent) {
        try {
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);
            editText.setTag(reportFilterFeild.getFieldID());
            editText.setId(reportFilterFeild.getFieldID());
            editText.setPadding(10, 10, 10, 10);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            if (reportFilterFeild.getLable() != null) {
                editText.setText(reportFilterFeild.getLable());
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
                            Toast.makeText(mContext, reportFilterFeild.getValidationMessage(), Toast.LENGTH_LONG).show();
                            //  editText.setText("");
                        }
                        mText = null;
                    }
                });
            }

            editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);
            parent.addView(editText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public  HashMap<String,String> findAllEdittexts(ViewGroup viewGroup) {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return editTextData;
    }

    public  HashMap<String,EditText> findAllEdittextsToValidate(ViewGroup viewGroup) {
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return hashMapEditText;
    }

    public  HashMap<String,String> putAllKeysToSave(List<SubProcessFieldDataResponse.TemplateField> templateFields, JobDetailsResponse.Applicant applicant) {
        try {
            for(int j=0;j<templateFields.size();j++){
                List<SubProcessFieldDataResponse.SubProcessField> subProcessFields = templateFields.get(j).getSubProcessFields();
                for(int k=0;k<subProcessFields.size();k++){
                    String key = subProcessFields.get(k).getKey();
                    if(subProcessFields.get(k).getIsMandatory()){
                        key = key+"/Mandatory";
                    }
                    Method method = null;
                    try {
                        method =JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(subProcessFields.get(k).getKey()));
                        String text = (String) method.invoke(applicant,null);
                        if(text!=null && !text.isEmpty()) {
                            templateTextViewData.put(key,text);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        templateTextViewData.put(key,"");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        templateTextViewData.put(key,"");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        templateTextViewData.put(key,"");
                    }catch (Exception e){
                        e.printStackTrace();;
                        templateTextViewData.put(key,"");
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return templateTextViewData;
    }

    public  HashMap<String,String> findAllTemplateTextViews(ViewGroup viewGroup) {
        try {

            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup)
                    findAllTemplateTextViews((ViewGroup) view);
                else if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (textView.getTag() != null) {

                        if (textView.getTag().toString().contains("textView_") || textView.getTag().toString().contains("_0") || textView.getTag().toString().isEmpty()) {
                        } else {

                            templateTextViewData.put(textView.getTag().toString(), textView.getText().toString());
                        }

                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return templateTextViewData;
    }


    public  HashMap<String,String> findAllTextViews(ViewGroup viewGroup,  List<SubProcessFieldDataResponse.TemplateField> templateFields) {
        try {

            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    if (templateFields != null) {
                        findAllTextViews((ViewGroup) view, templateFields);
                    }
                } else if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    if (textView.getTag() != null) {
                        for (int k = 0; k < templateFields.size(); k++) {
                            if (templateFields.get(k).getIsTableExist() != null) {
                                if (templateFields.get(k).getIsTableExist() == true) {
                                    for (int l = 0; l < templateFields.get(k).getTableHeaders().size(); l++) {
                                        if (textView.getTag().toString().contains(templateFields.get(k).getTableHeaders().get(l))) {
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
            }
        }
        catch (Exception e)
        {
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
                    findCustomValidateEdittexts((ViewGroup) view,key);
                else if (view instanceof EditText) {

                    EditText edittext = (EditText) view;
                    if (edittext.getTag() != null) {
                        if (edittext.getTag().toString().equalsIgnoreCase(key) || edittext.getTag().toString().equalsIgnoreCase(key+"/Mandatory")) {
                            customValidEditTextHashMap.put(edittext.getId(), edittext);
                        }
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  customValidEditTextHashMap;
    }


}
