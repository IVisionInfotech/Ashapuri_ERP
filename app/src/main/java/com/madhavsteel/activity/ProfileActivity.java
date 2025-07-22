package com.madhavsteel.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ProfileActivity";
    private Context context;
    private ImageView ivBack;
    private TextView tvUpdate, tvTitle;
    private EditText etName, etEmail, etContact;
    private String name = "", email = "", contact = "", userId = "";
    private Pattern pattern;
    private Matcher matcher;
    boolean updateFlag = false;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        context = ProfileActivity.this;
        session = new Session(context);
        realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();

        init();

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            etName.setText(user.getName());
            etContact.setText(user.getContact());
            etEmail.setText(user.getEmail());
        }
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Update Profile");
        tvUpdate = findViewById(R.id.tvUpdate);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etContact = findViewById(R.id.etContact);

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean emailValidator(String email) {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void validate() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        contact = etContact.getText().toString();

        if (name.isEmpty() || name.equals("")) {
            etName.setError("Invalid name");
            etName.requestFocus();
            return;
        } else if (!emailValidator(email)) {
            etEmail.setError("Invalid Email");
            etEmail.requestFocus();
            return;
        } else if (contact.isEmpty() || contact.equals("")) {
            etContact.setError("Invalid contact");
            etContact.requestFocus();
            return;
        } else {
            updateProfile();
        }
    }

    private void updateProfile() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.updateProfile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(tvUpdate, jsonObject.optString("message"));
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

                        finish();
                    } else if (status == 2) {
                        etEmail.setError(jsonObject.optString("message"));
                        etEmail.requestFocus();
                    } else if (status == 3) {
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
                params.put("name", name);
                params.put("email", email);
                params.put("contact", contact);
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
}