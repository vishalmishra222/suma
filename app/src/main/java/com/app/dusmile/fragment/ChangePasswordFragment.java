package com.app.dusmile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.NetworkResponse;
import com.android.volley.error.VolleyError;
import com.app.dusmile.R;
import com.app.dusmile.connection.HttpVolleyRequest;
import com.app.dusmile.connection.MyListener;
import com.app.dusmile.model.ChangePasswordErrorModel;
import com.app.dusmile.model.ChangePasswordModel;
import com.app.dusmile.preferences.Const;
import com.app.dusmile.preferences.UserPreference;
import com.app.dusmile.utils.IOUtils;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by suma on 02/02/17.
 */

public class ChangePasswordFragment extends Fragment {
    private String Tag = "ChangePasswordFragment";
    private View layout = null;
    private Context mContext;
    private EditText currentPasswordEditTextView, newPasswordEditTextView, confirmPasswordEditTextView;
    private Button changePassButton;
    private UserPreference userPreference;

    public ChangePasswordFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
        this.findViews(rootView);
        Activity activity = this.getActivity();
        mContext = activity;
        this.onChangePasswordButtonClicked();
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        currentPasswordEditTextView = (EditText) view.findViewById(R.id.currentPassEditText);
        newPasswordEditTextView = (EditText) view.findViewById(R.id.newPassEditText);
        confirmPasswordEditTextView = (EditText) view.findViewById(R.id.confirmPassEditText);
        changePassButton = (Button) view.findViewById(R.id.changePassButton);
    }

    private void onChangePasswordButtonClicked() {
        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPass = currentPasswordEditTextView.getText().toString();
                String newpassword = newPasswordEditTextView.getText().toString();
                String confirmpassword = confirmPasswordEditTextView.getText().toString();
                if (IOUtils.isInternetPresent(mContext)) {
                    if (validateChangePassword(currentPass, newpassword, confirmpassword)) {
                        doChangePassword(currentPass, newpassword, confirmpassword);
                    }
                } else {
                    IOUtils.showErrorMessage(mContext, "No internet connection");
                }
            }
        });

    }

    private boolean validateChangePassword(String currentPass, String newPass, String confirmPass) {
        if (currentPass.isEmpty() && newPass.isEmpty() && confirmPass.isEmpty()) {
            //Toast.makeText(mContext,getResources().getString(R.string.enter_data),Toast.LENGTH_LONG).show();
            MyDynamicToast.errorMessage(mContext, getResources().getString(R.string.enter_data));
            return false;
        } else if (!newPass.equalsIgnoreCase(confirmPass)) {
            // Toast.makeText(mContext,getResources().getString(R.string.match_pass),Toast.LENGTH_LONG).show();
            MyDynamicToast.errorMessage(mContext, getResources().getString(R.string.match_pass));
            return false;
        } else if (currentPass.isEmpty()) {
            currentPasswordEditTextView.setError(getResources().getString(R.string.enter_current_pass));
            currentPasswordEditTextView.requestFocus();
            return false;
        } else if (newPass.isEmpty()) {
            newPasswordEditTextView.setError(getResources().getString(R.string.enter_new_password));
            newPasswordEditTextView.requestFocus();
            return false;
        } else if (confirmPass.isEmpty()) {
            confirmPasswordEditTextView.setError(getResources().getString(R.string.enter_confirm_password));
            confirmPasswordEditTextView.requestFocus();
            return false;
        } else if (currentPass.equalsIgnoreCase(newPass)) {
            MyDynamicToast.errorMessage(mContext, "Currunt Password and new Password is same please try again");
            return false;
        }
        return true;
    }

    public void doChangePassword(String currentPass, String newPass, String confirmPass) {

        IOUtils.startLoading(mContext, "Loading......");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", UserPreference.getUserRecord(mContext).getUsername());
            jsonObject.put("oldPassword", currentPass);
            jsonObject.put("password", newPass);
        } catch (JSONException e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        } catch (Exception e) {
            IOUtils.stopLoading();
            e.printStackTrace();
        }
        IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHANGE_PASS + "\nREQUEST" + jsonObject.toString());
        new HttpVolleyRequest(mContext, jsonObject, new Const().REQUEST_CHANGE_PASS, listenerChangePass);
    }

    MyListener listenerChangePass = new MyListener() {
        @Override
        public void success(Object obj) throws JSONException {

        }

        @Override
        public void success(Object obj, JSONObject jsonReqObject) throws JSONException {
            try {
                IOUtils.stopLoading();
                if (obj != null) {
                    String response = obj.toString();
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHANGE_PASS + "\nRESPONSE" + response.toString());
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.length() > 0) {
                        Gson gson = new Gson();
                        ChangePasswordModel changePasswordModel = gson.fromJson(response, ChangePasswordModel.class);
                        Log.i(Tag, "Response " + changePasswordModel.getSuccess());
                        IOUtils.appendLog(Tag + " API " + new Const().REQUEST_CHANGE_PASS + "\nRESPONSE" + changePasswordModel.getSuccess());
                        if (changePasswordModel.getSuccess() == true) {
                            MyDynamicToast.successMessage(mContext, "Password changed successfully");
                            IOUtils.sendUserToLogin(mContext, getActivity());
                        } else {
                            MyDynamicToast.errorMessage(mContext, changePasswordModel.getMessage());
                        }
                    } else {
                        MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                    }
                } else {
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHANGE_PASS + "\nREQUEST" + obj.toString());
                    MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDynamicToast.errorMessage(mContext, "Unexpected Response");
                Log.i(Tag, "Exception occured " + e.getMessage());
            }
        }

        @Override
        public void failure(VolleyError volleyError) {
            try {
                volleyError.getMessage();
                IOUtils.stopLoading();
                String json;
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHANGE_PASS + "\nRESPONSE " + volleyError.getMessage());
                if (volleyError != null) {
                    IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHANGE_PASS + "\nRESPONSE " + volleyError.getMessage());
                    NetworkResponse response = volleyError.networkResponse;
                    if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 800) {
                        IOUtils.sendUserToLogin(mContext, getActivity());
                    } else if (response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 400:
                                json = new String(response.data);
                                Gson gson = new Gson();
                                ChangePasswordErrorModel changePasswordErrorModel = gson.fromJson(json, ChangePasswordErrorModel.class);
                                List<String> errorList = changePasswordErrorModel.getDetails();
                                if(errorList.size() > 0){
                                    MyDynamicToast.warningMessage(mContext,errorList.get(0));
                                }
                                break;
                        }
                        //Additional cases
                    } else {
                        MyDynamicToast.errorMessage(mContext, "Server Error !!");
                    }
                    Log.i(Tag, "Error occured " + volleyError.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                IOUtils.appendLog(Tag + " " + IOUtils.getCurrentTimeStamp() + " API " + new Const().REQUEST_CHANGE_PASS + "\nRESPONSE " + volleyError.getMessage());
                MyDynamicToast.errorMessage(mContext, "Server Error !!");
                Log.i(Tag, "Exception occured " + e.getMessage());
            }
        }
    };
}
