package com.madhavsteel.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ChangePasswordActivity extends BaseActivity {

    private static final String TAG = "ChangePassword";
    private Context context;
    private ImageView ivBack;
    private TextView tvChangePin, tvTitle;
    private EditText etPin01, etPin02, etPin03, etPin04, etPin11, etPin12, etPin13, etPin14, etPin21, etPin22, etPin23, etPin24;
    private String pin01 = "", pin02 = "", pin03 = "", pin04 = "", pin11 = "", pin12 = "", pin13 = "", pin14 = "", pin21 = "", pin22 = "", pin23 = "", pin24 = "",
            pin0 = "", pin1 = "", pin2 = "", userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        context = ChangePasswordActivity.this;
        session = new Session(context);

        init();

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Change Pin");
        tvChangePin = findViewById(R.id.tvChangePin);

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etPin01 = findViewById(R.id.etPin01);
        etPin02 = findViewById(R.id.etPin02);
        etPin03 = findViewById(R.id.etPin03);
        etPin04 = findViewById(R.id.etPin04);
        etPin11 = findViewById(R.id.etPin11);
        etPin12 = findViewById(R.id.etPin12);
        etPin13 = findViewById(R.id.etPin13);
        etPin14 = findViewById(R.id.etPin14);
        etPin21 = findViewById(R.id.etPin21);
        etPin22 = findViewById(R.id.etPin22);
        etPin23 = findViewById(R.id.etPin23);
        etPin24 = findViewById(R.id.etPin24);

        etPin01.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin01.getText().toString().length() == 1) {
                    etPin02.requestFocus();
                }
            }
        });
        etPin02.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin02.getText().toString().length() == 1) {
                    etPin03.requestFocus();
                } else {
                    etPin01.requestFocus();
                }
            }
        });
        etPin03.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin03.getText().toString().length() == 1) {
                    etPin04.requestFocus();
                } else {
                    etPin02.requestFocus();
                }
            }
        });
        etPin04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etPin04.getText().toString().length() == 1) {
                    etPin11.requestFocus();
                } else {
                    etPin03.requestFocus();
                }
            }
        });
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

        tvChangePin.setOnClickListener(new View.OnClickListener() {
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

    private void validate() {
        pin01 = etPin01.getText().toString().trim();
        pin02 = etPin02.getText().toString().trim();
        pin03 = etPin03.getText().toString().trim();
        pin04 = etPin04.getText().toString().trim();
        pin11 = etPin11.getText().toString().trim();
        pin12 = etPin12.getText().toString().trim();
        pin13 = etPin13.getText().toString().trim();
        pin14 = etPin14.getText().toString().trim();
        pin21 = etPin21.getText().toString().trim();
        pin22 = etPin22.getText().toString().trim();
        pin23 = etPin23.getText().toString().trim();
        pin24 = etPin24.getText().toString().trim();

        pin0 = pin01 + pin02 + pin03 + pin04;
        pin1 = pin11 + pin12 + pin13 + pin14;
        pin2 = pin21 + pin22 + pin23 + pin24;

        if (pin01.isEmpty() || pin01.equals("") || !pin01.matches("\\d+(?:\\.\\d+)?")) {
            etPin01.setError("Invalid Digit");
            etPin01.requestFocus();
            return;
        } else if (pin02.isEmpty() || pin02.equals("") || !pin02.matches("\\d+(?:\\.\\d+)?")) {
            etPin02.setError("Invalid Digit");
            etPin02.requestFocus();
            return;
        } else if (pin03.isEmpty() || pin03.equals("") || !pin03.matches("\\d+(?:\\.\\d+)?")) {
            etPin03.setError("Invalid Digit");
            etPin03.requestFocus();
            return;
        } else if (pin04.isEmpty() || pin04.equals("") || !pin04.matches("\\d+(?:\\.\\d+)?")) {
            etPin04.setError("Invalid Digit");
            etPin04.requestFocus();
            return;
        } else if (pin11.isEmpty() || pin11.equals("") || !pin11.matches("\\d+(?:\\.\\d+)?")) {
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
        } else if (pin21.isEmpty() || pin21.equals("") || !pin21.matches("\\d+(?:\\.\\d+)?")) {
            etPin21.setError("Invalid Digit");
            etPin21.requestFocus();
            return;
        } else if (pin22.isEmpty() || pin22.equals("") || !pin22.matches("\\d+(?:\\.\\d+)?")) {
            etPin22.setError("Invalid Digit");
            etPin22.requestFocus();
            return;
        } else if (pin23.isEmpty() || pin23.equals("") || !pin23.matches("\\d+(?:\\.\\d+)?")) {
            etPin23.setError("Invalid Digit");
            etPin23.requestFocus();
            return;
        } else if (pin24.isEmpty() || pin24.equals("") || !pin24.matches("\\d+(?:\\.\\d+)?")) {
            etPin24.setError("Invalid Digit");
            etPin24.requestFocus();
            return;
        } else if (!pin1.equals(pin2)) {
            showSnackBar(tvChangePin, "Pin does not match");
            return;
        } else {
            changePin();
        }
    }

    private void changePin() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.changePin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(tvChangePin, jsonObject.optString("message"));
                    showToast(jsonObject.optString("message"));
                    if (status == 1) {
                        session.setLoginStatus(false);
                        goToActivity(context, LoginActivity.class);
                        finish();
                    } else {
                        etPin24.setError(jsonObject.optString("message"));
                        etPin24.requestFocus();
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
                params.put("oldPIN", pin0);
                params.put("newPIN", pin1);
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