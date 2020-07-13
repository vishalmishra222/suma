package com.example.sumaforms.volley;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.example.sumaforms.common.Utf8JsonObjectRequest;
import com.example.sumaforms.application.DusmileApplication;
import com.example.sumaforms.listener.MyListener;
import com.example.sumaforms.preferences.Const;
import com.example.sumaforms.preferences.UserPreference;
import com.example.sumaforms.utils.IOUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HttpVolleyRequest {

    private static final String TAG = "HttpVolleyRequest";
    Context mContext;
    HashMap<String, String> map;
    MyListener listener;
    JSONObject jsonRequest;
    JSONArray jsonArrayRequest;
    DusmileApplication app;
    String requestUrl;
    Boolean isJsonArray;
    int constructorType;
    String filePath;

    public HttpVolleyRequest() {
        constructorType = 0;
    }

    public HttpVolleyRequest(Context mContext) {
        this.mContext = mContext;
        constructorType = 1;
    }

    public HttpVolleyRequest(Context mContext, JSONObject jsonRequest, String requestUrl, MyListener listener, String isLogin) {
        constructorType = 2;
        IOUtils.printLogError(jsonRequest.toString());
        IOUtils.printLogError(requestUrl);
        //  HttpsTrustManager.allowAllSSL();
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();
        doPostLoginOperation();
    }

    public HttpVolleyRequest(Context mContext, JSONObject jsonRequest, String requestUrl, MyListener listener) {
        constructorType = 3;
        IOUtils.printLogError(jsonRequest.toString());
        IOUtils.printLogError(requestUrl);
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();
        doPostOperation();
    }

    public HttpVolleyRequest(Context mContext, JSONObject jsonRequest, String requestUrl, MyListener listener, Boolean isJsonArray) {
        constructorType = 4;
        IOUtils.printLogError("URL ---> " + requestUrl);
        IOUtils.printLogError("Request Param ----- " + jsonRequest.toString());
        //  HttpsTrustManager.allowAllSSL();
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        this.isJsonArray = isJsonArray;
        app = (DusmileApplication) this.mContext.getApplicationContext();
        if (isJsonArray)
            doPostOperationJsonArray();
        else
            doPostOperation();
    }


    public HttpVolleyRequest(Context mContext, JSONObject jsonRequest, String requestUrl, MyListener listener, int msgVisible) {
        constructorType = 5;
        IOUtils.printLogError(jsonRequest.toString());
        IOUtils.printLogError("URL --> " + requestUrl);
        // HttpsTrustManager.allowAllSSL();
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();
        doPostOperationWithoutLoadingMsg();
    }

    public void HttpVolleyRequest(Context mContext, JSONObject jsonRequest, String requestUrl, MyListener listener) {
        IOUtils.printLogError("URL --> " + requestUrl);
        IOUtils.printLogError(jsonRequest.toString());
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();
        if (IOUtils.isInternetPresent(mContext))
            doPostOperationJsonArray();
        else
            IOUtils.myToast("Internet not available", mContext);
    }

    public void JsonObjectRequest(Context mContext, JSONObject jsonRequest, String requestUrl, MyListener listener) {
        IOUtils.printLogError("URL --> " + requestUrl);
        IOUtils.printLogError(jsonRequest.toString());

        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();
        if (IOUtils.isInternetPresent(mContext))
            doPostOperationJsonObject();
        else
            IOUtils.myToast("Internet not available", mContext);
    }

    public void JsonArrayRequest(Context mContext, String requestUrl, MyListener listener) {
        IOUtils.printLogError("URL --> " + requestUrl);

        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();
        if (IOUtils.isInternetPresent(mContext))
            doPostOperationJsonArray();
        else
            IOUtils.myToast("Internet not available", mContext);
    }

    public HttpVolleyRequest(Context mContext, String requestUrl, MyListener listener) {
        constructorType = 7;
        IOUtils.printLogError("URL --> " + requestUrl);

        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();

        if (IOUtils.isInternetPresent(mContext))
            doGetOperation();
        else
            IOUtils.myToast("Internet not available", mContext);
    }


    public HttpVolleyRequest(Context mContext, String requestUrl, MyListener listener, boolean noparams) {
        constructorType = 8;
        IOUtils.printLogError("URL --> " + requestUrl);
        //HttpsTrustManager.allowAllSSL();
        // AllCertificatesAndHostsTruster.apply();
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();

        if (IOUtils.isInternetPresent(mContext))
            doPostOperationWithoutParams();
        else
            IOUtils.myToast("Internet not available", mContext);
    }

    public HttpVolleyRequest(Context mContext, String requestUrl, MyListener listener, String filePath) {
        constructorType = 9;
        IOUtils.printLogError("URL --> " + requestUrl);
        //   HttpsTrustManager.allowAllSSL();
        //    AllCertificatesAndHostsTruster.apply();
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonRequest = jsonRequest;
        this.listener = listener;
        this.filePath = filePath;
        app = (DusmileApplication) this.mContext.getApplicationContext();

        if (IOUtils.isInternetPresent(mContext))
            doUploadMultipartFile(filePath);
        else
            IOUtils.myToast("Internet not available", mContext);
    }


    public HttpVolleyRequest(String requestUrl, Context mContext, MyListener listener) {
        constructorType = 10;
        IOUtils.printLogError("URL --> " + requestUrl);
        //    AllCertificatesAndHostsTruster.apply();
        //    HttpsTrustManager.allowAllSSL();
        this.mContext = mContext;
        this.requestUrl = requestUrl;
        this.jsonArrayRequest = jsonArrayRequest;
        this.listener = listener;
        app = (DusmileApplication) this.mContext.getApplicationContext();

        if (IOUtils.isInternetPresent(mContext))
            doPostOperationJsonArray();
        else
            IOUtils.myToast("Internet not available", mContext);
    }

    //---------------------------- POST JSON OBJECT REQUEST------------------------------
    public void doPostOperation() {
        //IOUtils.startLoadingPleaseWait(mContext);
        //    AllCertificatesAndHostsTruster.apply();
        //    HttpsTrustManager.allowAllSSL();
        //System.setProperty("http.keepAlive", "false");
        // JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonRequest, listenerSuccess, listenerError);
        Utf8JsonObjectRequest request = new Utf8JsonObjectRequest(Request.Method.POST, requestUrl, jsonRequest, listenerSuccess, listenerError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        try {
            request.setHeaders(getHeaders());

        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        app.addToRequestQueue(request, "post");

    }

    public void doPostLoginOperation() {
        //IOUtils.startLoadingPleaseWait(mContext);
        //    AllCertificatesAndHostsTruster.apply();
        //    HttpsTrustManager.allowAllSSL();
        //System.setProperty("http.keepAlive", "false");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonRequest, listenerSuccess, listenerError);
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        try {
            request.setHeaders(getLoginHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        app.addToRequestQueue(request, "post");

    }

    public void doPostOperationJsonArray() {
        //IOUtils.startLoadingPleaseWait(mContext);
        //JsonArrayRequest request = new JsonArrayRequest(requestUrl,listenerSuccess1,listenerError1);
        System.setProperty("http.keepAlive", "false");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, requestUrl, null, listenerSuccess1, listenerError1);
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        //request.setRetryPolicy(new DefaultRetryPolicy(5* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        //request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        try {
            request.setHeaders(getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        app.addToRequestQueue(request, "post");
    }


    public void doPostOperationJsonObject() {
        //IOUtils.startLoadingPleaseWait(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonRequest, listenerSuccess, listenerError);
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        //request.setRetryPolicy(new DefaultRetryPolicy(5* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        //request.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        try {
            request.setHeaders(getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        app.addToRequestQueue(request, "post");
    }

    public void doGetOperation() {
        //IOUtils.startLoadingPleaseWait(mContext);
        //   AllCertificatesAndHostsTruster.apply();
        //   HttpsTrustManager.allowAllSSL();
        StringRequest request = new StringRequest(Request.Method.GET, requestUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                //IOUtils.stopLoading();
                if (!TextUtils.isEmpty(response)) {
                    IOUtils.printLogError("RESPONSE --> " + response);
                    try {
                        listener.success(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = error.getMessage();
                if (msg.contains("java.net.NoRouteToHostException: Host unreachable") || msg.contains("Network is unreachable")){
                    changeURL(error);
                }else {
                    IOUtils.printLogError("RESPONSE --> " + error.getMessage());
                    listener.failure(error);
                }
            }
        });
        try {
            request.setHeaders(getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        app.addToRequestQueue(request, "get");
    }

    public void doPostOperationWithoutParams() {
        //IOUtils.startLoadingPleaseWait(mContext);
        //   AllCertificatesAndHostsTruster.apply();
        //   HttpsTrustManager.allowAllSSL();
        StringRequest request = new StringRequest(Request.Method.POST, requestUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                //IOUtils.stopLoading();
                if (!TextUtils.isEmpty(response)) {
                    IOUtils.printLogError("RESPONSE --> " + response);
                    try {
                        listener.success(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = error.getMessage();
                if (msg.contains("java.net.NoRouteToHostException: Host unreachable") || msg.contains("Network is unreachable")){
                    changeURL(error);
                }else {
                    IOUtils.printLogError("RESPONSE --> " + error.getMessage());
                    listener.failure(error);
                }
            }

        });
        try {
            request.setHeaders(getHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        app.addToRequestQueue(request, "post");

    }

    /////////////////////////
    public void doUploadMultipartFile(String FilePath) {

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, requestUrl,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            IOUtils.printLogError("RESPONSE --> " + response);
                            try {
                                listener.success(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg = error.getMessage();
                if (msg.contains("java.net.NoRouteToHostException: Host unreachable") || msg.contains("Network is unreachable")){
                    changeURL(error);
                }else {
                    IOUtils.printLogError("RESPONSE --> " + error.getMessage());
                    listener.failure(error);
                }
            }
        });

        try {
            smr.setHeaders(getFileUploadHeaders());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        smr.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        System.setProperty("http.keepAlive", "false");
        smr.addFile("pdf", FilePath);
        smr.setFixedStreamingMode(true);
        Log.i("File Path", FilePath);

        app.getInstance().addToRequestQueue(smr);
    }


    ////////////////////////////


    public void doPostOperationWithoutLoadingMsg() {
        //   HttpsTrustManager.allowAllSSL();
        //   AllCertificatesAndHostsTruster.apply();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonRequest, listenerSuccess, listenerError);
        //request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        app.addToRequestQueue(request, "post");
    }


    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + UserPreference.getUserRecord(mContext).getToken());
        Log.e(TAG, "Header ------> " + headers.toString());
        return headers;
    }

    public Map<String, String> getFileUploadHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "multipart/form-data");
        headers.put("Authorization", "Bearer " + UserPreference.getUserRecord(mContext).getToken());
        Log.e(TAG, "Header ------> " + headers.toString());
        return headers;
    }

    public Map<String, String> getLoginHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "First ");
        Log.e(TAG, "Header ------> " + headers.toString());
        return headers;
    }


    //----------------------- LISTENER'S---------------------------------------------

    Listener<JSONObject> listenerSuccess = new Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
           // IOUtils.printLogError(jsonObject.toString());
            //IOUtils.stopLoading();
            try {
                listener.success(jsonObject, jsonRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };

    Listener<JSONArray> listenerSuccess1 = new Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray jsonArray) {
            IOUtils.printLogError(jsonArray.toString());
            //IOUtils.stopLoading();
            try {
                listener.success(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    Response.ErrorListener listenerError1 = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {
            IOUtils.printLogError(e.getMessage());
            changeURL(e);
            listener.failure(e);
        }
    };

    Response.ErrorListener listenerError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {
            IOUtils.printLogError(e.getMessage());
            String msg = e.getMessage();
            if (msg.contains("java.net.NoRouteToHostException: Host unreachable") || msg.contains("Network is unreachable")) {
                changeURL(e);
            }else {
                //IOUtils.stopLoading();
                listener.failure(e);
            }
        }
    };

    public void changeURL(VolleyError e) {
        String msg = e.getMessage();
        if (msg.contains("java.net.NoRouteToHostException: Host unreachable") || msg.contains("Network is unreachable")) {
            IOUtils.stopLoading();
            String newURL = requestUrl.replace(Const.BASE_URL, Const.ALTERNATE_BASE_IP);
            requestUrl = newURL;
            switch (constructorType) {
                case 0:
                    new HttpVolleyRequest();
                    break;
                case 1:
                    new HttpVolleyRequest(mContext);
                    break;
                case 2:
                    new HttpVolleyRequest(mContext, jsonRequest, requestUrl, listener, "");
                    break;
                case 3:
                    new HttpVolleyRequest(mContext, jsonRequest, requestUrl, listener);
                    break;
                case 4:
                    new HttpVolleyRequest(mContext, jsonRequest, requestUrl, listener, isJsonArray);
                    break;
                case 5:
                    new HttpVolleyRequest(mContext, jsonRequest, requestUrl, listener, 0);
                    break;
                case 7:
                    new HttpVolleyRequest(mContext,requestUrl,listener);
                    break;
                case 8:
                    new HttpVolleyRequest(mContext,requestUrl,listener,true);
                    break;
                case 9 :
                    new HttpVolleyRequest(mContext,requestUrl,listener,filePath);
                    break;
                case 10 :
                    new HttpVolleyRequest(requestUrl,mContext,listener);
                    break;
            }
        }
    }
}
