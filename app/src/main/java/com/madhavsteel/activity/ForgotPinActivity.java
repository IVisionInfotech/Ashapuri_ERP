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

public class ForgotPinActivity extends BaseActivity {

    private static final String TAG = "ForgotPinActivity";
    private Context context;
    private TextView tvForgotPin;
    private EditText etContact;
    private String contact = "";
    boolean updateFlag = false;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin);

        context = ForgotPinActivity.this;
        session = new Session(context);
        realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();

        init();
    }

    private void init() {
        tvForgotPin = findViewById(R.id.tvForgotPin);

        etContact = findViewById(R.id.etContact);

        tvForgotPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goToActivityForResult(context, VerifyOTPActivity.class, 3);
                validate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void validate() {
        contact = etContact.getText().toString();

        if (contact.isEmpty() || contact.equals("")) {
            etContact.setError("Invalid contact");
            etContact.requestFocus();
            return;
        } else if (contact.length() < 10) {
            etContact.setError("Enter 10 digit mobile number");
            etContact.requestFocus();
        } else {
            forgotPin();
        }
    }

    private void forgotPin() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.forgotPin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(tvForgotPin, jsonObject.optString("message"));
                    showToast(jsonObject.optString("message"));
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

                        goToActivityForResult(context, VerifyOTPActivity.class, 3);
                    } else if (status == 2) {
                        etContact.setError(jsonObject.optString("message"));
                        etContact.requestFocus();
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
                params.put("contact", contact);
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

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(6);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }
}
