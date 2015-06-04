package com.jd.jr.kanbanApp.data;

import com.android.volley.*;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import com.jd.jr.kanbanApp.util.CommonUtil;
import com.jd.jr.kanbanApp.util.JDLog;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class MyJsonRequest extends Request<JSONObject> {
    private final Listener<JSONObject> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public MyJsonRequest(int method, String url, Listener<JSONObject> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
   /* public MyJsonRequest(String url, Listener<JSONObject> listener, ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }*/
    
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
    	// TODO Auto-generated method stub
    	Map<String, String> headerMap = new HashMap<String, String>();
    	String tokenString = CommonUtil.getAccessToken();
    	JDLog.i("MyJsonRequest", "本地缓存的accessToken:" + tokenString);
		headerMap.put("access_token", tokenString);
		return headerMap;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
        	String accessToken = response.headers.get("access_token");
        	CommonUtil.setAccessToken(accessToken);
        	JDLog.i("MyJsonRequest", "parseNetworkResponse-->>服务端返回的accessToken:"+accessToken);
            String jsonString =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }


}
