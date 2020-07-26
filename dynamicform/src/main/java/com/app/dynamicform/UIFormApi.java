package com.app.dynamicform;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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


/**
 * Created by sumasoft on 23/01/17.
 */

public class UIFormApi {
    static ArrayList<Integer> arrayListCheckBoxPosition = new ArrayList<>();
    private static View viewParent;
    private static HashMap<Integer, EditText> customValidEditTextHashMap = new HashMap<>();
    SparseArray<EditText> array = new SparseArray<EditText>();
    HashMap<String, String> templateTextViewData = new HashMap<>();
    HashMap<String, String> editTextData = new HashMap<>();
    HashMap<String, String> textViewData = new HashMap<>();
    HashMap<Integer, String> getDateOfBirth = new HashMap<>();
    HashMap<String, EditText> hashMapEditText = new HashMap<>();
    static HashMap<String, HashMap<String, ArrayList<String>>> hashMapCustomValidation = new HashMap<>();
    static ArrayList<String> arrayListFields = new ArrayList<>();
    HashMap<String, String> staticFieldsController = new HashMap<>();
    HashMap<String, String> dynamicFieldsController = new HashMap<>();
    public static int addCount = 0;
    private static int noOfUncheck = 0;
    Context context;
    String response;
    JSONObject subProcessFieldJsonObject;
    JSONObject jobDetailsJsonObj;
    LinearLayout linearLayout;
    String defaultValueForDropDown;

  /*  public UI(Context context,LinearLayout linearLayout,JSONObject subProcessFieldJsonObject,JSONObject jobDetailsJsonObj,String defaultValueForDropDown) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.subProcessFieldJsonObject = subProcessFieldJsonObject;
        this.jobDetailsJsonObj = jobDetailsJsonObj;
        this.defaultValueForDropDown = defaultValueForDropDown;
    }*/

/*    public UI(Context context,LinearLayout linearLayout,JSONObject subProcessFieldJsonObject,JSONObject jobDetailsJsonObj) {
        this.context = context;
        this.linearLayout = linearLayout;
        this.subProcessFieldJsonObject = subProcessFieldJsonObject;
        this.jobDetailsJsonObj = jobDetailsJsonObj;
    }*/

    public UIFormApi(Context context, String response) {
        this.context = context;
        this.response = response;
    }

    //create UI for textView
    public static void createTextView(Context mContext, String text, LinearLayout parent, int textColor, float textSize, String headerType, String tag) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        textView.setLayoutParams(params);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setTag(tag);
        textView.setPadding(10, 10, 10, 10);
        if (text == null) {
            text = "";
        }
        if (headerType.equalsIgnoreCase("tabs")) {
            textView.setText("View Job Details");
            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(null, Typeface.BOLD);
        } else if (headerType.equalsIgnoreCase("form")) {
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
            textView.setTypeface(null, Typeface.BOLD);
        } else {
            textView.setText(text);
            textView.setGravity(Gravity.LEFT);
        }
        parent.addView(textView);
    }

    //create UI for editText
    public static void createEditText(final Context mContext, final JSONObject subProcessField, LinearLayout parent, JSONObject applicant) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String fieldType = subProcessField.getString("fieldType");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validation = subProcessField.getString("validation");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 10);
            if (isMandatory) {
                editText.setTag(key+ "/Mandatory");
            } else {
                editText.setTag(key);
            }
            editText.setId(fieldID);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            Method method = null;
            try {
                method = (Method) applicant.get(StringUtils.capitalize(key));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    editText.setText(text);
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                editText.setText("");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                editText.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                ;
                editText.setText("");
            }

            if (validation.equalsIgnoreCase("Numeric") || validation.equalsIgnoreCase("Numbers") || validation.equalsIgnoreCase("Decimal")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (validation.equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            if (validationPattern != null && !validationPattern.isEmpty()) {
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    Pattern sPattern
                            = Pattern.compile(validationPattern);

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!v.hasFocus()) {
                            EditText edt = (EditText) v;
                            if (!isValid(edt.getText().toString())) {
                                //   editText.setText("");
                                editText.setError(validationMessage);
                            } else {
                                editText.setError(null);
                            }
                        }
                    }

                    private boolean isValid(CharSequence s) {
                        return sPattern.matcher(s).matches();
                    }
                });
            }

            if (isreadonly) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }
            try {
                viewParent = parent;
                if (editText.getTag().toString().contains("/Mandatory")) {
                    key = editText.getTag().toString().split("/")[0];
                } else {
                    key = editText.getTag().toString();
                }

                for (Map.Entry<String, HashMap<String, ArrayList<String>>> customeValidationData : hashMapCustomValidation.entrySet()) {
                    HashMap<String, ArrayList<String>> hmStateFields = customeValidationData.getValue();
                    for (HashMap.Entry<String, ArrayList<String>> validationData : hmStateFields.entrySet()) {
                        if (validationData.getKey().contains("No") || validationData.getKey().contains("Select")) {
                            for (int i = 0; i < validationData.getValue().size(); i++) {
                                if (validationData.getValue().get(i).trim().equalsIgnoreCase(key)) {
                                    editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                    editText.setEnabled(false);
                                    break;
                                }
                            }
                        } else {
                            for (int i = 0; i < validationData.getValue().size(); i++) {
                                if (validationData.getValue().get(i).trim().equalsIgnoreCase(key)) {
                                    editText.setBackgroundResource(R.drawable.form_header_background);
                                    editText.setEnabled(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
            parent.addView(editText);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //dynamic create UI for tabs header and form header
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void createUIForHeader(Context mContext, LinearLayout parent, String name, String headerType) {
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


    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout parent, String value) {
        try {
            Gson gson = new Gson();
            JsonObject body = gson.fromJson(value, JsonObject.class);
            JsonArray results = body.get("results").getAsJsonArray();
            JSONObject json = new JSONObject(value);
            JSONObject json_LL = json.getJSONObject("LL");
            String str_value = json_LL.getString("value");
        } catch (Exception e) {

        }
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
       /* identifyViews(mContext,,linearLayout, applicant,"",parent);
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
*/
        // createTextView(mContext,name,linearLayout,ContextCompat.getColor(mContext,R.color.black),12,"",subProcessField1.getKey());
        parent.addView(linearLayout);
    }

    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout parent,
                                                       JSONObject subProcessField, JSONObject applicant, String value) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            JSONObject json = new JSONObject(value);
            JSONObject json_LL = json.getJSONObject("LL");
            String str_value = json_LL.getString("value");
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (isMandatory) {
                createTextView(mContext, label, linearLayout, Color.RED, 14, "", "");
            } else {
                createTextView(mContext, label, linearLayout, ContextCompat.getColor(mContext, R.color.black), 14, "", "");
            }
            identifyViews(mContext, subProcessField, linearLayout, applicant, value, parent);
            parent.addView(linearLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void identifyViews(Context mContext, JSONObject subProcessField, LinearLayout parent, JSONObject applicant, String value, LinearLayout mainParent) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String fieldType = subProcessField.getString("fieldType");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validation = subProcessField.getString("validation");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            switch (fieldType) {
                case "label":
                    if (isMandatory) {
                        createTextView(mContext,label, parent, Color.RED, 14, "", "");
                    } else {
                        createTextView(mContext, label, parent, ContextCompat.getColor(mContext, R.color.black), 14, "", "");
                    }
                    break;
                case "text":
                    createEditText(mContext, subProcessField, parent, applicant);
                    break;
                case "dropDownList":
                    createDropdown(mContext, subProcessField, parent, applicant, value, mainParent);
                    break;
                case "textArea":
                    createTextArea(mContext, subProcessField, parent, applicant);
                    break;
                case "Date":
                    createDatePickerDialog(mContext, subProcessField, parent, applicant, value, mainParent);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void createDatePickerDialog(final Context mContext, final JSONObject subProcessField, final LinearLayout parent, JSONObject applicant, final String value, final LinearLayout mainParent) {
        try {

            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String fieldType = subProcessField.getString("fieldType");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validation = subProcessField.getString("validation");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1f);
            editText.setLayoutParams(childparams);
            editText.setPadding(10, 10, 10, 10);
            if (isMandatory) {
                editText.setTag(key + "/Mandatory");
            } else {
                editText.setTag(key);
            }
            editText.setId(fieldID);
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
                method = (Method) applicant.get(StringUtils.capitalize(key));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    editText.setText(text);
                }
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

            if (validation.equalsIgnoreCase("Numeric") || validation.equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (validation.equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setEnabled(false);
            }

            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openTemplateCalenderView(mContext, editText, mainParent, subProcessField);
                }
            });

            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setBackgroundResource(R.drawable.form_header_background);
            editText.setEnabled(true);
            parent.addView(editText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createDropdown(final Context mContext, final JSONObject subProcessField, final LinearLayout parent, JSONObject applicant, final String value, final LinearLayout mainParent) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String fieldType = subProcessField.getString("fieldType");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validation = subProcessField.getString("validation");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            String CustomValidationFunction = subProcessField.getString("CustomValidationFunction");
            final EditText editText = new EditText(mContext);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 10);
            if (isMandatory) {
                editText.setTag(key + "/Mandatory");
            } else {
                editText.setTag(key);
            }
            editText.setId(fieldID);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(12);
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);

            Method method = null;
            try {
                method = (Method) applicant.get(StringUtils.capitalize(key));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    editText.setText(text);
                } else {
                    editText.setText("Select");
                }
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
            if (validation.equalsIgnoreCase("Numeric") || validation.equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (validation.equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }

            if (CustomValidationFunction != null && !CustomValidationFunction.isEmpty()) {
                if (CustomValidationFunction.contains("toggleFields")) {
                    ArrayList<String> arrayListFields = new ArrayList<>();
                    String fn = CustomValidationFunction.replace("[", "").replace("]", "").replace("(", "").replace(")", "").replace("'", "");

                    String fn_array[] = fn.split(",");
                    String current_state = fn_array[1];
                    try {
                        for (int i = 3; i < fn_array.length; i++) {
                            arrayListFields.add(fn_array[i]);
                        }
                        HashMap<String, ArrayList<String>> hmStatefields = new HashMap<>();
                        if (editText.getText().toString().equalsIgnoreCase("Yes")) {
                            hmStatefields.put(editText.getText().toString(), arrayListFields);
                        } else {
                            hmStatefields.put(current_state, arrayListFields);
                        }
                        hashMapCustomValidation.put(key, hmStatefields);
                    } catch (Exception e) {

                    }
                }
            }
            if (!value.isEmpty()) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDropDownOptions(mContext, value, editText, mainParent);
                    }
                });
            }
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            if (isreadonly) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }

            parent.addView(editText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openDropDownOptions(Context mContext, String value, final EditText editText, final LinearLayout mainParent) {
        try {
            JSONArray jsonArray = new JSONArray(value);

            final String value_arr[] = new String[jsonArray.length() + 1];
            value_arr[0] = "Select";
            for (int i = 0; i < jsonArray.length(); i++) {
                int pos = i + 1;
                value_arr[pos] = jsonArray.getString(i);
            }
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_dropdown);
            ListView dropdownList = (ListView) dialog.findViewById(R.id.dropdownList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.list_dropdown_items, value_arr);
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
                                        HashMap<Integer, EditText> hm = findCustomValidateEdittexts(mainParent, validationData.getValue().get(i).trim());
                                        try {
                                            for (Map.Entry<Integer, EditText> hmEntry : hm.entrySet()) {
                                                EditText editTextField = hmEntry.getValue();
                                                editTextField.setBackgroundResource(R.drawable.disabled_edittext_background);
                                                editTextField.setEnabled(false);
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                    HashMap<String, ArrayList<String>> hmStateFieldsNew = new HashMap<String, ArrayList<String>>();
                                    if (hmStateFields.containsKey("Yes")) {
                                        hmStateFieldsNew.put("No", hmStateFields.get("Yes"));
                                        hashMapCustomValidation.put(key, hmStateFieldsNew);
                                    }
                                } else {
                                    for (int i = 0; i < validationData.getValue().size(); i++) {
                                        HashMap<Integer, EditText> hm = findCustomValidateEdittexts(mainParent, validationData.getValue().get(i).trim());
                                        try {
                                            for (Map.Entry<Integer, EditText> hmEntry : hm.entrySet()) {
                                                EditText editTextField = hmEntry.getValue();
                                                editTextField.setBackgroundResource(R.drawable.form_header_background);
                                                editTextField.setEnabled(true);
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                    HashMap<String, ArrayList<String>> hmStateFieldsNew = new HashMap<String, ArrayList<String>>();
                                    if (hmStateFieldsNew.containsKey("No")) {
                                        hmStateFieldsNew.put("Yes", hmStateFields.get("No"));
                                        hashMapCustomValidation.put(key, hmStateFieldsNew);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            });
            dialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTextArea(final Context mContext, final JSONObject subProcessField, LinearLayout parent, JSONObject applicant) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String fieldType = subProcessField.getString("fieldType");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validation = subProcessField.getString("validation");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            String CustomValidationFunction = subProcessField.getString("CustomValidationFunction");
            final EditText editText = new EditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150, 1f);
            params.setMargins(10, 10, 10, 10);
            editText.setLayoutParams(params);

            if (isMandatory) {
                editText.setTag(key + "/Mandatory");
            } else {
                editText.setTag(key);
            }
            editText.setId(fieldID);
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
                method = (Method) applicant.get(StringUtils.capitalize(key));
                String text = (String) method.invoke(applicant, null);
                if (text != null && !text.isEmpty()) {
                    editText.setText(text);
                }
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
            if (validation.equalsIgnoreCase("Numeric") || validation.equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else if (validation.equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
            editText.setGravity(Gravity.TOP);
            if (validationPattern!= null && !validationPattern.isEmpty()) {
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    Pattern sPattern
                            = Pattern.compile(validationPattern);

                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!v.hasFocus()) {
                            EditText edt = (EditText) v;
                            if (!isValid(edt.getText().toString())) {
                                editText.setText("");
                                editText.setError(validationMessage);
                            }
                        }
                    }

                    private boolean isValid(CharSequence s) {
                        return sPattern.matcher(s).matches();
                    }
                });
            }
            if (isreadonly) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }
            parent.addView(editText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public static void createDatePickerDialog(final Context mContext, final JSONObject reportFilterFeild, LinearLayout parent) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

/*
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
*/

    private static void openTemplateCalenderView(Context mContext, final EditText editText, final LinearLayout parent, final JSONObject subProcessField) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String fieldType = subProcessField.getString("fieldType");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validation = subProcessField.getString("validation");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            String CustomValidationFunction = subProcessField.getString("CustomValidationFunction");
            String onchange = subProcessField.getString("onchange");
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
                        if (onchange != null) {
                            try {

                                String dependentField = onchange;
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
                                        int age = getAge(year, monthOfYear, dayOfMonth);
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

            new DatePickerDialog(mContext, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getAge(int _year, int _month, int _day) {

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
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }

  /*  //create UI for editText
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
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

    public HashMap<String, String> putAllKeysToSave(JSONObject subProcessField, JSONObject applicant) {
        try {
               // List<SubProcessFieldDataResponse.SubProcessField> subProcessFields = templateFields.get(j).getSubProcessFields();
                for (int k = 0; k < subProcessField.length(); k++) {
                    boolean isMandatory = subProcessField.getBoolean("isMandatory");
                    String label = subProcessField.getString("lable");
                    String fieldType = subProcessField.getString("fieldType");
                    String key = subProcessField.getString("key");
                    int fieldID = subProcessField.getInt("fieldID");
                    String validation = subProcessField.getString("validation");
                    String validationPattern = subProcessField.getString("validationPattern");
                    String validationMessage = subProcessField.getString("validationMessage");
                    boolean isreadonly = subProcessField.getBoolean("isreadonly");
                    if (isMandatory) {
                        key = key + "/Mandatory";
                    }
                    Method method = null;
                    try {
                        method = (Method) applicant.get(StringUtils.capitalize(key));
                        String text = (String) method.invoke(applicant, null);
                        if (text != null && !text.isEmpty()) {
                            templateTextViewData.put(key, text);
                        }
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        templateTextViewData.put(key, "");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        templateTextViewData.put(key, "");
                    } catch (Exception e) {
                        e.printStackTrace();
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

                        if (textView.getTag().toString().contains("textView_") || textView.getTag().toString().contains("_0") || textView.getTag().toString().isEmpty()) {
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


    public HashMap<String, String> findAllTextViews(ViewGroup viewGroup, JSONObject templateFields) {
        try {
            boolean isTableExist = templateFields.getBoolean("isTableExist");
          //  List<String> tableHeaders = new ArrayList<>();
            List<String> tableHeaders = (List<String>) templateFields.getJSONArray("tableHeaders");
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
                                if (isTableExist == true) {
                                    for (int l = 0; l < tableHeaders.size(); l++) {
                                        if (textView.getTag().toString().contains(tableHeaders.get(l))) {
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


}
