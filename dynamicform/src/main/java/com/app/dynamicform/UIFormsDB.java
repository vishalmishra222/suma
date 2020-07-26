package com.app.dynamicform;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.basgeekball.awesomevalidation.AwesomeValidation;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

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
    static ArrayList<EditText> reportFilters = new ArrayList<>();
    private static boolean otherLoanInformationSelected;
    private static AwesomeValidation mAwesomeValidation;
    SparseArray<EditText> array = new SparseArray<EditText>();
    HashMap<String, String> templateTextViewData = new HashMap<>();
    HashMap<String, String> editTextData = new HashMap<>();
    HashMap<String, String> textViewData = new HashMap<>();
    HashMap<Integer, String> getDateOfBirth = new HashMap<>();
    HashMap<String, EditText> hashMapEditText = new HashMap<>();
    HashMap<String, String> staticFieldsController = new HashMap<>();
    HashMap<String, String> dynamicFieldsController = new HashMap<>();
    public static int addCount = 0;
    private static int noOfUncheck = 0;
    private static HashMap<Boolean, ArrayList<String>> hashMapToggleFields = new HashMap<>();
    private static ArrayList<String> dependentList = new ArrayList<>();
    Context context;
    static String jobId;
    static Activity activity;
    static android.support.v4.app.FragmentManager fragmentManager;

    public UIFormsDB() {
    }

    public UIFormsDB(Context context, AwesomeValidation mAwesomeValidation, Activity activity, android.support.v4.app.FragmentManager fragmentManager, String jobId) {
        this.context = context;
        this.mAwesomeValidation = mAwesomeValidation;
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
            String validationPattern = subProcessField.getString("validationPattern");
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

            mAwesomeValidation.addValidation(editText, validationPattern, validationMessage);
            if (isMandatory) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);

            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }

            String val;
            try {
                //   jsonObject = new JSONObject(applicant_json);
                val = applicant_json.getString(key);
                //  method = JobDetailsResponse.Applicant.class.getDeclaredMethod("get" + StringUtils.capitalize(key));
              //  method = (Method) applicant.get(StringUtils.capitalize(key));
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
                        = Pattern.compile(validationPattern);

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {
                        edt.setSelection(edt.getText().length());
                        edt.setHint(label);
                        if (validationPattern != null && !validationPattern.isEmpty()) {

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
                                    //editText.setBackground(null);
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
                // linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tabs_background));
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.clrToolbarBg));
                linearLayout.setGravity(Gravity.CENTER);
            } else {
                // createView(mContext, linearLayout, 15, ViewGroup.LayoutParams.MATCH_PARENT, ContextCompat.getColor(mContext, R.color.clrToolbarBg));
                //linearLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.form_header_background));
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
                        createTextView(mContext, label, parent, Color.RED, 14, fieldType, "", "");
                    } else {
                        createTextView(mContext, label, parent, ContextCompat.getColor(mContext, R.color.black), 14, fieldType, "", "");
                    }
                    break;
                case "fieldHeader":
                    createTextView(mContext, label, parent, ContextCompat.getColor(mContext, R.color.black), 14, fieldType, "", "");
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
                    //createRadioButton(mContext, subProcessField, parent, applicant, mainParent, applicant_json, formPosition);
                    break;
                case "map":
                    //createGoogleMap(mContext, subProcessField, parent, applicant, mainParent, activity, applicant_json);
                    break;
                case "imageUpload":
                    // createUploadButton(mContext, subProcessField, parent, applicant);
                    break;
                case "YearMonth":
                    createYearMonthCombo(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                    break;
                case "checkbox":
                    createCheckBox(mContext, subProcessField, parent, applicant, mainParent, applicant_json);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createCheckBox(final Context mContext,
                                      final JSONObject subProcessField,
                                      final LinearLayout parent, JSONObject applicant,
                                      final LinearLayout mainParent, JSONObject applicant_json) {
        try {
            String label = subProcessField.getString("lable");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            List<String> value = Collections.singletonList(subProcessField.getString("value"));
            LinearLayout verticalLinearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams linParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            verticalLinearLayout.setLayoutParams(linParams);
            linParams.setMargins(5, 5, 5, 5);
            verticalLinearLayout.setOrientation(LinearLayout.VERTICAL);
            final TextView textView = new TextView(mContext);
            textView.setLayoutParams(linParams);
            textView.setText(label);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextColor(mContext.getResources().getColor(R.color.accent));
            verticalLinearLayout.addView(textView);
            LinearLayout horizontalLinearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams linParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            horizontalLinearLayout.setLayoutParams(linParams2);
            horizontalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            if (value != null) {
                for (int i = 0; i < value.size(); i++) {
                    final CheckBox checkBox = new CheckBox(mContext);
                    final TextView checkBoxTextView = new TextView(mContext);
                    checkBox.setLayoutParams(linParams2);
                    checkBoxTextView.setLayoutParams(linParams2);
                    checkBox.setId(fieldID);
                    checkBoxTextView.setText(value.get(i));
                    checkBox.setGravity(Gravity.CENTER_VERTICAL);
                    checkBoxTextView.setGravity(Gravity.CENTER_VERTICAL);
                    checkBox.setTag(key);
                    checkBoxTextView.setText(value.get(i));
                    checkBoxTextView.setTextColor(Color.BLACK);
                    horizontalLinearLayout.addView(checkBox);
                    horizontalLinearLayout.addView(checkBoxTextView);
                    verticalLinearLayout.addView(horizontalLinearLayout);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (checkBox.isChecked()) {
                                checkBox.setTag(key + "/" + "true");
                            } else {
                                checkBox.setTag(key);
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


    public static void createDatePickerDialog(final Context mContext,
                                              final JSONObject subProcessField,
                                              final LinearLayout parent, JSONObject applicant,
                                              final LinearLayout mainParent, JSONObject applicant_json) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            childparams.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(childparams);
            textInputLayout.setLayoutParams(childparams);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(key);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            Drawable calenderDrawable = ContextCompat.getDrawable(mContext, R.drawable.calendar);
            calenderDrawable.setBounds(
                    0, // left
                    0, // top
                    calenderDrawable.getIntrinsicWidth(), // right
                    calenderDrawable.getIntrinsicHeight() // bottom
            );

            if (isMandatory) {
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
                    if (dependentList.get(l).equalsIgnoreCase(key)) {
                        isDependentField = true;
                    }
                }
            }
            editText.setId(fieldID);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textInputLayout.setHint(label);
            editText.setHint(label);
            editText.setCompoundDrawablePadding(20);
            String myFormat = "dd-MM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Calendar myCalendar = Calendar.getInstance();
            JSONObject jsonObject = null;
            String val = null;
            Method method = null;
            try {
                val = applicant_json.getString(key);
                String text = (String) method.invoke(applicant, null);
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
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(sdf.format(myCalendar.getTime()));
                textInputLayout.setHintAnimationEnabled(true);
            }

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

                        edt.setHint(label);
                    }
                }
            });
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setEnabled(true);
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void createYearMonthCombo(final Context mContext,
                                            final JSONObject subProcessField,
                                            final LinearLayout parent, JSONObject applicant,
                                            final LinearLayout mainParent, JSONObject applicant_json) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            LinearLayout.LayoutParams childparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            childparams.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(childparams);
            textInputLayout.setLayoutParams(childparams);
            editText.setId(fieldID);
            editText.setCompoundDrawablePadding(20);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(key);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            Drawable calenderDrawable = ContextCompat.getDrawable(mContext, R.drawable.calendar);
            calenderDrawable.setBounds(
                    0, // left
                    0, // top
                    calenderDrawable.getIntrinsicWidth(), // right
                    calenderDrawable.getIntrinsicHeight() // bottom
            );

            if (isMandatory) {
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
                    if (dependentList.get(l).equalsIgnoreCase(key)) {
                        isDependentField = true;
                    }
                }
            }

            JSONObject jsonObject = null;
            String val = null;
            Method method = null;
            try {
                val = applicant_json.getString(key);
                String text = (String) method.invoke(applicant, null);
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
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                editText.setHint(label);
                textInputLayout.setHintAnimationEnabled(false);
                editText.setText(val);
                textInputLayout.setHintAnimationEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                editText.setHint(label);
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

                        edt.setHint(label);
                    }
                }
            });
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setEnabled(true);
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void createDropdown(final Context mContext,
                                      final JSONObject subProcessField,
                                      final LinearLayout parent, JSONObject applicant,
                                      final LinearLayout mainParent, JSONObject applicant_json) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            String validation = subProcessField.getString("validation");
            boolean isreadonly = subProcessField.getBoolean("isreadonly");
            String defaultValue = subProcessField.getString("defaultValue");
            List<String> value = Collections.singletonList(subProcessField.getString("value"));
            List<String> dependentFieldList = Collections.singletonList(subProcessField.getString("value"));
            String CustomValidationFunction = subProcessField.getString("CustomValidationFunction");
            final TextInputLayout textInputLayout = new TextInputLayout(mContext);
            final TextInputEditText editText = new TextInputEditText(mContext);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            params.setMargins(5, 5, 5, 5);
            textInputLayout.setLayoutParams(params);
            editText.setLayoutParams(params);
            editText.setPadding(10, 10, 10, 20);
            editText.setTag(key);
            editText.setId(fieldID);
            editText.setTextColor(Color.BLACK);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            if (!TextUtils.isEmpty(defaultValue)) {
                editText.setText(defaultValue);//commented by netra
            }
            Drawable calenderDrawable = ContextCompat.getDrawable(mContext, R.drawable.blue_dropdown_arrow);
            calenderDrawable.setBounds(
                    0, // left
                    0, // top
                    calenderDrawable.getIntrinsicWidth(), // right
                    calenderDrawable.getIntrinsicHeight() // bottom
            );

            if (isMandatory) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.blue_dropdown_arrow, 0);//commented by netra
            }

            editText.setCompoundDrawablePadding(20);
            Method method = null;
            String val = null;
            try {
                val = applicant_json.getString(key);
                String text = val;
                if (text != null && !text.isEmpty()) {
                    textInputLayout.setHint(label);
                    editText.setHint(label);
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(text);//commented by netra
                    textInputLayout.setHintAnimationEnabled(true);

                    int index = value.indexOf(text);
                    if (dependentFieldList != null && dependentFieldList.size() > 0 && index >= 0) {
                        if (!TextUtils.isEmpty(dependentFieldList.get(index))) {
                            dependentList.add(dependentFieldList.get(index));
                        }
                    }

                } else {
                    textInputLayout.setHint(label);
                    editText.setHint(label);
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(val);//commented by netra
                    textInputLayout.setHintAnimationEnabled(true);

                    int index = value.indexOf(val);
                    textInputLayout.setHintAnimationEnabled(true);
                    if (dependentFieldList != null && dependentFieldList.size() > 0 && index >= 0) {
                        if (!TextUtils.isEmpty(dependentFieldList.get(index))) {
                            dependentList.add(dependentFieldList.get(index));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                textInputLayout.setHint(label);
                editText.setHint(label);
                textInputLayout.setHintAnimationEnabled(false);
                if (!TextUtils.isEmpty(defaultValue)) {
                    editText.setText(defaultValue);
                } else {
                    editText.setText("");
                }
                textInputLayout.setHintAnimationEnabled(true);
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
            if (value.size() > 0) {
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDropDownOptions(mContext, subProcessField, editText, mainParent,label, params);
                    }
                });
            }
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                Pattern sPattern
                        = Pattern.compile(validationPattern);

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {

                        edt.setHint(label);
                        if (validationPattern != null && !validationPattern.isEmpty()) {

                            if (!isValid(edt.getText().toString())) {
                                //   editText.setText("");
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
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            if (isreadonly) {
                editText.setBackgroundResource(R.drawable.disabled_edittext_background);
                editText.setEnabled(false);
            } else {
                //editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }
            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(key)) {
                        isDependentField = true;
                    }
                }
            }
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void openDropDownOptions(final Context mContext,
                                            final JSONObject subProcessField, final EditText editText,
                                            final LinearLayout mainParent, final String lable, final LinearLayout.LayoutParams params) {
        try {
            List<String> value = Collections.singletonList(subProcessField.getString("value"));
            String dependentField = subProcessField.getString("dependentField");
            List<String> dependentFieldList = Collections.singletonList(subProcessField.getString("value"));
            final String value_arr[] = new String[value.size() + 1];
            value_arr[0] = lable;
            for (int i = 0; i < value.size(); i++) {
                int pos = i + 1;
                value_arr[pos] = value.get(i);
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
                            if (!TextUtils.isEmpty(dependentField)) {
                                dependentList.add(dependentField);
                            } else if (dependentFieldList != null && dependentFieldList.size() > 0) {
                                String viewKey = dependentFieldList.get(position - 1);
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

                        if (!TextUtils.isEmpty(dependentField)) {
                            HashMap<Integer, EditText> editTextHashMap = UIFormsDB.findEditTextForShowHide(mainParent, dependentField);
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
                            if (dependentFieldList != null && dependentFieldList.size() > 0) {
                                for (int k = 0; k < dependentFieldList.size(); k++) {
                                    if (!TextUtils.isEmpty(dependentFieldList.get(k))) {
                                        HashMap<Integer, EditText> editTextHashMap = UIFormsDB.findEditTextForShowHide(mainParent,dependentFieldList.get(k));
                                        if (editTextHashMap.size() > 0) {
                                            for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                                editTextEntry.getValue().setText("");
                                                editTextEntry.getValue().setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                                if (!TextUtils.isEmpty(dependentFieldList.get(position - 1))) {
                                    HashMap<Integer, EditText> editTextHashMap = UIFormsDB.findEditTextForShowHide(mainParent, dependentFieldList.get(position - 1));
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

    public static void createTextArea(final Context mContext,
                                      final JSONObject subProcessField, LinearLayout
                                              parent, JSONObject applicant, JSONObject applicant_json) {
        try {
            boolean isMandatory = subProcessField.getBoolean("isMandatory");
            String label = subProcessField.getString("lable");
            String key = subProcessField.getString("key");
            int fieldID = subProcessField.getInt("fieldID");
            String validationPattern = subProcessField.getString("validationPattern");
            String validationMessage = subProcessField.getString("validationMessage");
            String validation = subProcessField.getString("validation");
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
            //add validation pattern
            mAwesomeValidation.addValidation(editText, validationPattern, validationMessage);
            if (isMandatory) {
                textInputLayout.setHintTextAppearance(R.style.error_appearance);
                editText.setHintTextColor(Color.RED);
            } else {
                textInputLayout.setHintTextAppearance(R.style.TextLabel);
            }

            Method method = null;
            JSONObject jsonObject = null;
            String val = null;
            try {
                val = applicant_json.getString(key);
                String text = val;
                if (text != null && !text.isEmpty()) {
                    textInputLayout.setHint(label);
                    editText.setHint(label);
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(text);
                    editText.setSelection(text.length());
                    textInputLayout.setHintAnimationEnabled(true);
                } else {
                    textInputLayout.setHint(label);
                    editText.setHint(label);
                    textInputLayout.setHintAnimationEnabled(false);
                    editText.setText(val);
                    editText.setSelection(val.length());
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

            if (validation.equalsIgnoreCase("Numeric") || validation.equalsIgnoreCase("Numbers")) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else if (validation.equalsIgnoreCase("alphaNumeric")) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else {
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
            //editText.setGravity(Gravity.TOP);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                Pattern sPattern
                        = Pattern.compile(validationPattern);

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText edt = (EditText) v;
                    if (!v.hasFocus()) {
                        edt.setSelection(edt.getText().length());
                        edt.setHint(label);
                        if (validationPattern != null && !validationPattern.isEmpty()) {
                            if (!isValid(edt.getText().toString())) {
                                //   editText.setText("");
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
                // editText.setBackgroundResource(R.drawable.form_header_background);
                editText.setEnabled(true);
            }
            boolean isDependentField = false;
            if (dependentList.size() > 0) {
                for (int l = 0; l < dependentList.size(); l++) {
                    if (dependentList.get(l).equalsIgnoreCase(key)) {
                        isDependentField = true;
                    }
                }
            }
            textInputLayout.addView(editText);
            parent.addView(textInputLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean validateAddTableRow(Context
                                                       mContext, List<String> tableMandatoryFieldsList, LinearLayout viewParent) {
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

    private static void openTemplateCalenderView(Context mContext, final EditText editText,
                                                 final LinearLayout parent, final JSONObject subProcessField) {
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
                        String onchange = subProcessField.getString("onchange");
                        if (onchange!= null) {
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


    private static void openTemplateYearMonthCalenderView(Context mContext,
                                                          final EditText editText, final LinearLayout parent,
                                                          final JSONObject subProcessField) {
        try {

            final MonthYearPicker myp = new MonthYearPicker(activity);
            myp.build(new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editText.setText(myp.getSelectedYear() + " yrs" + " - " + myp.getSelectedMonthName() + " months");
                    String dependentKey = "";
                    try {
                        String onchange = subProcessField.getString("onchange");
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

    public static ArrayList<EditText> findAllTableEdittexts(ViewGroup viewGroup, String
            mandatoryTag) {
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

    public HashMap<String, String> putAllKeysToSave(String[]
                                                            allFieldsKeysArray, JSONObject applicant, JSONObject applicant_json) {
        try {
            templateTextViewData.clear();
            for (int j = 0; j < allFieldsKeysArray.length; j++) {

                String key = allFieldsKeysArray[j];

                Method method = null;
                JSONObject jsonObject = null;
                String val = null;
                try {
                    val = jsonObject.getString(key);
                  //  method = (Method) applicant.get(StringUtils.capitalize(key));
                    String text = (String) method.invoke(applicant, null);
                    if (text != null && !text.isEmpty()) {
                        templateTextViewData.put(key, text);
                    } else if (!TextUtils.isEmpty(val)) {
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


    public HashMap<String, String> findAllTextViews(ViewGroup viewGroup, String
            tableExist, String tableKeys) {
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


    public static HashMap<Integer, EditText> findCustomValidateEdittexts(ViewGroup
                                                                                 viewGroup, String key) {
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

    private static void openHoldDropDownOptions(final Context mContext, String value[],
                                                final EditText editText, final String lable) {
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
