package com.madhavsteel.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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

public class GeneratePinActivity extends BaseActivity {

    private static final String TAG = "GeneratePinActivity";
    private Context context;
    private TextView tvGeneratePin;
    private EditText etPin11, etPin12, etPin13, etPin14, etPin21, etPin22, etPin23, etPin24;
    boolean updateFlag = false;
    private Realm realm;
    private String userId = "", pin11 = "", pin12 = "", pin13 = "", pin14 = "", pin21 = "", pin22 = "", pin23 = "", pin24 = "", pin1 = "", pin2 = "", firebaseId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pin);

        context = GeneratePinActivity.this;
        session = new Session(context);
        realm = RealmController.with(this).getRealm();

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        firebaseId = task.getResult();
                    }
                });

        init();
    }

    private void init() {
        tvGeneratePin = findViewById(R.id.tvGeneratePin);

        etPin11 = findViewById(R.id.etPin11);
        etPin12 = findViewById(R.id.etPin12);
        etPin13 = findViewById(R.id.etPin13);
        etPin14 = findViewById(R.id.etPin14);
        etPin21 = findViewById(R.id.etPin21);
        etPin22 = findViewById(R.id.etPin22);
        etPin23 = findViewById(R.id.etPin23);
        etPin24 = findViewById(R.id.etPin24);

        etPin11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin11.getText().toString().length() == 1) {
                    etPin12.requestFocus();
                }
            }
        });
        etPin12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin12.getText().toString().length() == 1) {
                    etPin13.requestFocus();
                } else {
                    etPin11.requestFocus();
                }
            }
        });
        etPin13.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin13.getText().toString().length() == 1) {
                    etPin14.requestFocus();
                } else {
                    etPin12.requestFocus();
                }
            }
        });
        etPin14.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin14.getText().toString().length() == 1) {
                    etPin21.requestFocus();
                } else {
                    etPin13.requestFocus();
                }
            }
        });
        etPin21.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin21.getText().toString().length() == 1) {
                    etPin22.requestFocus();
                }
            }
        });
        etPin22.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin22.getText().toString().length() == 1) {
                    etPin23.requestFocus();
                } else {
                    etPin21.requestFocus();
                }
            }
        });
        etPin23.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin23.getText().toString().length() == 1) {
                    etPin24.requestFocus();
                } else {
                    etPin22.requestFocus();
                }
            }
        });
        etPin24.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin24.getText().toString().length() == 1) {
                    hideSoftKeyboard();
                } else {
                    etPin23.requestFocus();
                }
            }
        });

        tvGeneratePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setResultOfActivity(4);
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

        pin11 = etPin11.getText().toString().trim();
        pin12 = etPin12.getText().toString().trim();
        pin13 = etPin13.getText().toString().trim();
        pin14 = etPin14.getText().toString().trim();
        pin21 = etPin21.getText().toString().trim();
        pin22 = etPin22.getText().toString().trim();
        pin23 = etPin23.getText().toString().trim();
        pin24 = etPin24.getText().toString().trim();

        pin1 = pin11 + pin12 + pin13 + pin14;
        pin2 = pin21 + pin22 + pin23 + pin24;

        if (pin11.isEmpty() || pin11.equals("") || !pin11.matches("\\d+(?:\\.\\d+)?")) {
            etPin11.setError("Invalid Digit");
            etPin11.requestFocus();
            return;
        } else if (pin12.isEmpty() || pin12.equals("") || !pin12.matches("\\d+(?:\\.\\d+)?")) {
            etPin12.setError("Invalid Digit");
            etPin12.requestFocus();
            return;
        } else if (pin13.isEmpty() || pin13.equals("") || !pin13.matches("\\d+(?:\\.\\d+)?")) {
            etPin13.setError("Invalid Digit");
            etPin13.requestFocus();
            return;
        } else if (pin14.isEmpty() || pin14.equals("") || !pin14.matches("\\d+(?:\\.\\d+)?")) {
            etPin14.setError("Invalid Digit");
            etPin14.requestFocus();
            return;
        } else if (!pin1.equals(pin2)) {
            showSnackBar(tvGeneratePin, "Pin does not match");
            return;
        } else {
            generatePin(pin1);
        }
    }

    private void generatePin(final String pin) {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.generatePin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(tvGeneratePin, jsonObject.optString("message"));
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

                        Constant.orderArray = null;
                        session.setLoginStatus(true);
                        setResultOfActivity(4);
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
                params.put("userId", userId);
                params.put("PIN", pin);
                params.put("firebaseId", firebaseId);
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