package com.madhavsteel.activity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCustomerActivity extends BaseActivity {

    private static final String TAG = "AddCustomerActivity";
    private TextView tvSubmit;
    private EditText etName, etMobile, etEmail, etAddress, etCompanyName, etGSTNo, etCity, etState;
    private String name = "", mobile = "", email = "", address = "", companyName = "", gstNo = "", city = "", state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        context = AddCustomerActivity.this;

        setToolbar("Add Customer");

        init();
    }

    private void init() {

        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etCompanyName = findViewById(R.id.etCompanyName);
        etGSTNo = findViewById(R.id.etGSTNo);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);

        tvSubmit = findViewById(R.id.tvSubmit);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {

        name = etName.getText().toString();
        mobile = etMobile.getText().toString();
        email = etEmail.getText().toString();
        address = etAddress.getText().toString();
        companyName = etCompanyName.getText().toString();
        gstNo = etGSTNo.getText().toString();
        city = etCity.getText().toString();
        state = etState.getText().toString();

        if (name.equals("")) {
            etName.setError("Enter name");
            etName.requestFocus();
        } else if (mobile.equals("")) {
            etMobile.setError("Enter mobile");
            etMobile.requestFocus();
        } else if (companyName.equals("")) {
            etCompanyName.setError("Enter company name");
            etCompanyName.requestFocus();
        } else {
            addCustomer();
        }
    }

    private void addCustomer() {

        showProgressDialog(context, "Please wait..");

        String userId = "";
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }

        final String finalUserId = userId;
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.addCustomer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.customerArray = null;
                        JSONObject result = jsonObject.optJSONObject("result");
                        Constant.customerId = result.optString("customeId");
                        Constant.customerName = result.optString("name");
                        setResultOfActivity(1);
                    } else {
                        showSnackBar(tvSubmit, jsonObject.optString("message"));
                    }
                    hideProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideProgressDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", finalUserId);
                params.put("name", name);
                params.put("contact", mobile);
                params.put("email", email);
                params.put("address", address);
                params.put("cName", companyName);
                params.put("gstNo", gstNo);
                params.put("city", city);
                params.put("state", state);
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
