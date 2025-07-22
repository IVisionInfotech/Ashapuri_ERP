package com.madhavsteel.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends BaseActivity {

    private static final String TAG = "SplashScreen";
    private Context context;
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        context = SplashScreen.this;
        session = new Session(context);

        getHomeScreenData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getHomeScreenData() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getHomeScreenData, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        Constant.productArray = result.getJSONArray("productArray");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (session.getLoginStatus()) {
                    goToActivity(context, MainActivity.class);
                } else {
                    goToActivity(context, LoginActivity.class);
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                if (session.getLoginStatus()) {
                    goToActivity(context, MainActivity.class);
                } else {
                    goToActivity(context, LoginActivity.class);
                }
                finish();
            }
        }) {
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                return params;
            }*/

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                String auth = "Basic " + Base64.encodeToString(Constant.credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
