package com.app.dynamicform.dynamicFields;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.app.dynamicform.MonthYearPicker;
import com.app.dynamicform.R;
import org.json.JSONObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YearMonthComboClass {

    private static HashMap<Integer, EditText> customValidEditTextHashMap = new HashMap<>();

    public static void createYearMonthCombo(final Context mContext,
                                            final JSONObject subProcessField,
                                            final LinearLayout parent, JSONObject applicant,
                                            final LinearLayout mainParent, JSONObject applicant_json,Activity activity) {
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
                    openTemplateYearMonthCalenderView(mContext, editText, mainParent, subProcessField,activity);
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

    private static void openTemplateYearMonthCalenderView(Context mContext,
                                                          final EditText editText, final LinearLayout parent,
                                                          final JSONObject subProcessField, Activity activity){
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

    public static boolean validate(String expression, String text) {
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

}
