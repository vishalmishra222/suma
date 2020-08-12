package com.app.dynamicform.dynamicFields;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.app.dynamicform.R;
import com.app.dynamicform.UIFormsDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropDownClass {

    static HashMap<String, Boolean> editTextValidate = new HashMap<>();
    private ArrayList<String> dependentList = new ArrayList<>();
    private boolean otherLoanInformationSelected;
    private HashMap<Boolean, ArrayList<String>> hashMapToggleFields = new HashMap<>();
    private HashMap<Integer, EditText> customValidEditTextHashMap = new HashMap<>();

    public void createDropdown(final Context mContext,
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

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    boolean editTextValid = validate(validationPattern, charSequence.toString());
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
                        openDropDownOptions(mContext, subProcessField, editText, mainParent, label, params);
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

    private void openDropDownOptions(final Context mContext,
                                            final JSONObject subProcessField, final EditText editText,
                                            final LinearLayout mainParent, final String lable, final LinearLayout.LayoutParams params) {
        try {
            String dependentField = subProcessField.getString("dependentField");
            List<String> dependentFieldList = Collections.singletonList(subProcessField.getString("value"));
            JSONArray values = subProcessField.getJSONArray("value");
            final String value_arr[] = new String[values.length() + 1];
            value_arr[0] = lable;
            for (int i = 0; i < values.length(); i++) {
                int pos = i + 1;
                value_arr[pos] = values.getString(i);
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
                            UIFormsDB uiDB = new UIFormsDB();
                            HashMap<Integer, EditText> editTextHashMap = uiDB.findEditTextForShowHide(mainParent, dependentField);
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
                                        UIFormsDB uiDB = new UIFormsDB();
                                        HashMap<Integer, EditText> editTextHashMap = uiDB.findEditTextForShowHide(mainParent, dependentFieldList.get(k));
                                        if (editTextHashMap.size() > 0) {
                                            for (Map.Entry<Integer, EditText> editTextEntry : editTextHashMap.entrySet()) {
                                                editTextEntry.getValue().setText("");
                                                editTextEntry.getValue().setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                }
                                if (!TextUtils.isEmpty(dependentFieldList.get(position - 1))) {
                                    UIFormsDB uiDB = new UIFormsDB();
                                    HashMap<Integer, EditText> editTextHashMap = uiDB.findEditTextForShowHide(mainParent, dependentFieldList.get(position - 1));
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

    public HashMap<Integer, EditText> findCustomValidateEdittexts(ViewGroup
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

    public boolean validate(String expression, String text) {
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public boolean getDropDownValidation(){
        boolean isValid = true;
        for (Map.Entry<String, Boolean> entry : editTextValidate.entrySet()) {
            if (!entry.getValue()){
                isValid = false;
            }
        }
        return isValid;
    }

}
