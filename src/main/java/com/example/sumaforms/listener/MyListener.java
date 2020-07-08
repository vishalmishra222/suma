package com.example.sumaforms.listener;

import com.android.volley.error.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface MyListener {

	public void success(Object obj) throws JSONException;

	public void success(Object obj, JSONObject jsonObject) throws JSONException;

	public void failure(VolleyError volleyError);
}
