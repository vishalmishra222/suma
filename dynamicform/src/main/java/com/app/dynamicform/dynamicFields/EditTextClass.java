package com.app.dynamicform.dynamicFields;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.dynamicform.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextClass {
    HashMap<String, Boolean> editTextValidate = new HashMap<>();
    private HashMap<Boolean, ArrayList<String>> hashMapToggleFields = new HashMap<>();
    private View viewParent;

    //create UI for editText
    public void createEditText(final Context mContext, final JSONObject subProcessField, LinearLayout parent, JSONObject applicant, JSONObject applicant_json) {
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
                    boolean editTextValid = validate(validationPattern[0], charSequence.toString());
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

    public boolean validate(String expression, String text) {
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public boolean getEditTextValidation(){
        boolean isValid = true;
        for (Map.Entry<String, Boolean> entry : editTextValidate.entrySet()) {
            if (!entry.getValue()){
                isValid = false;
            }
        }
        return isValid;
    }

}
