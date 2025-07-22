package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;
import com.madhavsteel.utils.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class VerifyOTPActivity extends BaseActivity {

    private static final String TAG = "VerifyOTPActivity";
    private Context context;
    private TextView tvVerifyOTP, tvResendOTP;
    private EditText etOTP;
    boolean updateFlag = false;
    private Realm realm;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        context = VerifyOTPActivity.this;
        session = new Session(context);
        realm = RealmController.with(this).getRealm();

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }

        init();
    }

    private void init() {
        tvVerifyOTP = findViewById(R.id.tvVerifyOTP);
        tvResendOTP = findViewById(R.id.tvResendOTP);

        etOTP = findViewById(R.id.etOTP);

        tvVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goToActivityForResult(context, GeneratePinActivity.class, 4);
                validate();
            }
        });
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void validate() {
        if (etOTP.getText().toString().isEmpty() || etOTP.getText().toString().equals("")) {
            etOTP.setError("Invalid OTP");
            etOTP.requestFocus();
            return;
        } else {
            verifyOTP();
        }
    }

    private void verifyOTP() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.verifyOTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(tvVerifyOTP, jsonObject.optString("message"));
                    if (status == 1) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        User user = RealmController.with(context).getUser();
                        if (user != null) {
                            updateFlag = true;
                            realm.beginTransaction();
                        } else {
                            updateFlag = false;
                            user = new User();
                        }

                        user.setUserId(result.optString("userId"));
                        user.setName(result.optString("name"));
                        user.setEmail(result.optString("email"));
                        user.setContact(result.optString("contact"));
                        user.setPIN(result.optString("PIN"));
                        user.setOtp(result.optString("otp"));
                        user.setEmailVerified(result.optString("emailVerified"));
                        user.setOtpVerified(result.optString("otpVerified"));
                        user.setStatus(result.optString("status"));
                        user.setDateTimeAdded(result.optString("dateTimeAdded"));

                        if (updateFlag) {
                            realm.commitTransaction();
                        } else {
                            realm.beginTransaction();
                            realm.copyToRealm(user);
                            realm.commitTransaction();
                        }

                        goToActivityForResult(context, GeneratePinActivity.class, 4);
                    } else if (status == 2) {
                        etOTP.setError(jsonObject.optString("message"));
                        etOTP.requestFocus();
                    }
                    hideProgressDialog();
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", etOTP.getText().toString().trim());
                params.put("userId", userId);
                return params;
            }

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

    private void resendOTP() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.resendOTP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(tvVerifyOTP, jsonObject.optString("message"));
                    hideProgressDialog();
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                return params;
            }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.e(TAG, "onActivityResult: " + requestCode);
        if (requestCode == 4) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(3);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }
}