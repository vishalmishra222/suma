package com.app.dusmile.connection;


import com.android.volley.NetworkResponse;

        import com.android.volley.Response;
        import com.android.volley.Response.ErrorListener;
        import com.android.volley.Response.Listener;
import com.android.volley.error.ParseError;
import com.android.volley.request.JsonRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.protocol.HTTP;
import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.UnsupportedEncodingException;

public class Utf8JsonObjectRequest extends JsonRequest<JSONObject> {
    public Utf8JsonObjectRequest(int method, String url, JSONObject jsonRequest,
                           Listener<JSONObject> listener, ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            // solution 1:
            String jsonString = new String(response.data, "UTF-8");
            // solution 2:
            /*response.headers.put(HTTP.CONTENT_TYPE,
                    response.headers.get("content-type"));
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));*/
            //*/
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
