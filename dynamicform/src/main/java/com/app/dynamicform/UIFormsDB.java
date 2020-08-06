package com.app.dynamicform;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.app.dynamicform.dynamicFields.CheckBoxClass;
import com.app.dynamicform.dynamicFields.DatePickerDialogClass;
import com.app.dynamicform.dynamicFields.DropDownClass;
import com.app.dynamicform.dynamicFields.EditTextClass;
import com.app.dynamicform.dynamicFields.TextAreaClass;
import com.app.dynamicform.dynamicFields.TextViewClass;
import com.app.dynamicform.dynamicFields.YearMonthComboClass;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by sumasoft on 26/07/2020.
 */

public class UIFormsDB {
    static ArrayList<Integer> arrayListCheckBoxPosition = new ArrayList<>();
    private HashMap<String, RadioButton> customValidRadioButtontHashMap = new HashMap<>();
    private static View viewParent;
    private static HashMap<String, Button> buttonsHashMap = new HashMap<>();
    private HashMap<String, CheckBox> checkBoxHashMap = new HashMap<>();
    private static HashMap<String, Button> mapbuttonsHashMap = new HashMap<>();
    private static HashMap<Integer, EditText> customValidEditTextHashMap = new HashMap<>();
    private static HashMap<Integer, EditText> showHideEditTextHashMap = new HashMap<>();
    static ArrayList<EditText> arrayListForMandatoryTableFields = new ArrayList<>();
    private static boolean otherLoanInformationSelected;
    //private static AwesomeValidation mAwesomeValidation;
    SparseArray<EditText> array = new SparseArray<EditText>();
    HashMap<String, String> templateTextViewData = new HashMap<>();
    HashMap<String, String> editTextData = new HashMap<>();
    static HashMap<String, Boolean> editTextValidate = new HashMap<>();
    HashMap<String, String> textViewData = new HashMap<>();
    HashMap<Integer, String> getDateOfBirth = new HashMap<>();
    HashMap<String, EditText> hashMapEditText = new HashMap<>();
    public static int addCount = 0;
    private static int noOfUncheck = 0;
    private static HashMap<Boolean, ArrayList<String>> hashMapToggleFields = new HashMap<>();
    private static ArrayList<String> dependentList = new ArrayList<>();
    private static boolean valid;
    Context context;
    static String jobId;
    static Activity activity;
    static android.support.v4.app.FragmentManager fragmentManager;

    public UIFormsDB() {
    }

    public UIFormsDB(Context context, Activity activity, android.support.v4.app.FragmentManager fragmentManager, String jobId) {
        this.context = context;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.jobId = jobId;
    }

    //create UI for textView
    public static TextView createTextView(Context mContext, String text, LinearLayout parent, int textColor, float textSize, String headerType, String tag, String customerName) {
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


    //create UI for editText
    public static void createEditText(final Context mContext, final JSONObject subProcessField, LinearLayout parent, JSONObject applicant, JSONObject applicant_json) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            final String[] validationPattern = {subProcessField.getString("validationPattern")};
            String validationMessage = subProcessField.getString("validationMessage");
            String validation = subProcessField.getString("validation");
            int maxlength = subProcessField.getInt("maxlength");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            editText.setLayoutParams(params);
            params.setMargins(5, 5, 5, 5);
            textInputLayout.setLayoutParams(params);
            editText.setTag(key);
            editText.setPadding(10, 10, 10, 20);
            editText.setId(fieldID);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    boolean editTextValid = ValidateField.validateDataField(validationPattern[0], charSequence.toString());
                    if (!editTextValid) {
                        editText.setError(validationMessage);
                    } else {
                        editText.setError(null);
                    }
                    editTextValidate.put(key, editTextValid);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            if (isMandatory) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }
            String val;
            try {
                val = applicant_json.getString(key);
                String text = val;
                if (text != null && !text.isEmpty()) {
                    textInputLayout.setHint(label);
                    editText.setHint(label);
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(text);
                    textInputLayout.setHintAnimationEnabled(true);
                } else {
                    textInputLayout.setHint(label);
                    editText.setHint(label);
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(val);
                    textInputLayout.setHintAnimationEnabled(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                editText.setHint(label);
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText("");
                textInputLayout.setHintAnimationEnabled(true);
            }

            if (validation.equalsIgnoreCase("Numeric") || validation.equalsIgnoreCase("Numbers") || validation.equalsIgnoreCase("Decimal")) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            } else if (validation.equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            if (maxlength != 0 && maxlength > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxlength)});
            }
            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(key)) {
                        isDependentField = true;
                    }
                }
            }
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                Pattern sPattern
                        = Pattern.compile(validationPattern[0]);

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {
                        edt.setSelection(edt.getText().length());
                        edt.setHint(label);
                        if (validationPattern[0] != null && !validationPattern[0].isEmpty()) {

                            if (!isValid(edt.getText().toString())) {
                                editText.setError(validationMessage);
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


            if (isreadonly) {
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
                            if (key.trim().equalsIgnoreCase(arrayListFields.get(i).trim())) {
                                if (hashMapkey == true) {
                                    editText.setEnabled(true);
                                } else {
                                    editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                                    editText.setEnabled(false);
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);
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
                    TextView cardTextViews = createTextView(mContext, data, firstCardLayout, ContextCompat.getColor(mContext, R.color.Black), 16, "", "", "");
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
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.clrToolbarBg));
                linearLayout.setGravity(Gravity.CENTER);
            } else {
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.clrToolbarBg));
            }
            parent.addView(linearLayout);
            createTextView(mContext, name, linearLayout, ContextCompat.getColor(mContext, R.color.clrToolbarBg), 16, headerType, "", customerName);
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

    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout parent, JSONObject subProcessField, String type, JSONObject applicant, JSONObject appilcant_json, int formPosition) {
        try {
            String key = subProcessField.getString("key");
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            linearLayout.setLayoutParams(params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            identifyViews(mContext, subProcessField, linearLayout, applicant, parent, appilcant_json, formPosition);
            String name = "";
            Method method = null;
            try {
                name = (String) method.invoke(applicant, null);
                if (name == null || name.isEmpty()) {
                    name = "";
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                name = "";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                name = "";
            } catch (Exception e) {
                name = "";
            }
            createTextView(mContext, name, linearLayout, ContextCompat.getColor(mContext, R.color.black), 12, "", key, "");
            parent.addView(linearLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createHorizontalLayoutAndFields(Context mContext, LinearLayout
            parent, JSONObject subProcessField, JSONObject applicant, JSONObject applicant_json,
                                                       int formPosition) {
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


    private static void identifyViews(Context mContext, JSONObject
            subProcessField, LinearLayout parent, JSONObject applicant, LinearLayout
                                              mainParent, JSONObject applicant_json, int formPosition) {
        try {
            String fieldType = subProcessField.getString("fieldType");
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            switch (fieldType) {
                case "label":
                    if (isMandatory) {
                        TextViewClass.createTextView(mContext, label, parent, Color.RED, 14, fieldType, "", "");
                    } else {
                        TextViewClass.createTextView(mContext, label, parent, ContextCompat.getColor(mContext, R.color.black), 14, fieldType, "", "");
                    }
                    break;
                case "fieldHeader":
                    TextViewClass.createTextView(mContext, label, parent, ContextCompat.getColor(mContext, R.color.black), 14, fieldType, "", "");
                    break;
                case "text":
                    EditTextClass.createEditText(mContext, subProcessField, parent, applicant, applicant_json);
                    break;
                case "dropDownList":
                    DropDownClass.createDropdown(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                    break;
                case "textArea":
                    TextAreaClass.createTextArea(mContext, subProcessField, parent, applicant, applicant_json);
                    break;
                case "Date":
                    DatePickerDialogClass.createDatePickerDialog(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                    break;
                case "RadioButton":
                    //createRadioButton(mContext, subProcessField, parent, applicant, mainParent, applicant_json, formPosition);
                    break;
                case "map":
                    //createGoogleMap(mContext, subProcessField, parent, applicant, mainParent, activity, applicant_json);
                    break;
                case "imageUpload":
                    // createUploadButton(mContext, subProcessField, parent, applicant);
                    break;
                case "YearMonth":
                    YearMonthComboClass.createYearMonthCombo(mContext, subProcessField, parent, applicant, mainParent, applicant_json,activity);
                    break;
                case "checkbox":
                    CheckBoxClass.createCheckBox(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTableRows(Context mContext, LinearLayout
            parent, ArrayList<String> arrayListRow, final List<String> tableKeysList) {
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
    public static void createView(Context mContext, TableRow parent, int width, int height,
                                  int color) {
        View view = new View(mContext);
        view.setBackgroundColor(color);
        view.setLayoutParams(new TableRow.LayoutParams(width, height));
        parent.addView(view);
    }


    private static void addCheckBoxToTableRow(Context mContext, String text,
                                              final TableRow parentRow, int textColor, float textSize, final int positionToAdd,
                                              final LinearLayout parent) {
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

    private static void addTextViewToTableRow(Context mContext, String text, TableRow parentRow,
                                              int textColor, float textSize, int positionToAdd, String key) {
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

    public static HashMap<Integer, EditText> findEditTextForShowHide(ViewGroup
                                                                             viewGroup, String key) {
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
}
