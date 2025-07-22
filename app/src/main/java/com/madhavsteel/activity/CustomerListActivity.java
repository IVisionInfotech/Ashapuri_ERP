package com.madhavsteel.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.adapter.AllCustomerListAdapter;
import com.madhavsteel.model.Customer;
import com.madhavsteel.utils.ClickListener;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerListActivity extends BaseActivity {

    private static final String TAG = "CustomerListActivity";
    private RecyclerView recyclerView;
    private ArrayList<Customer> list = new ArrayList<>();
    private AllCustomerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        context = CustomerListActivity.this;

        setToolbar("Customer's");

        init();

        if (Constant.customerArray == null) {
            getCustomer();
        } else {
            if (Constant.customerArray.length() > 0) {
                bindData();
            } else {
                getCustomer();
            }
        }
    }

    private void init() {

        recyclerView = findViewById(R.id.recyclerView);
    }

    private void bindRecyclerView() {

        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(false);

        adapter = new AllCustomerListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {

                Intent intent = new Intent(context, CustomerDetailsActivity.class);
                intent.putExtra("details", list.get(position));
                startActivity(intent);
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                Uri number = Uri.parse("tel:" + list.get(position).getContact());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                showAddressDialog(list.get(position).getAddress() + "\n" + list.get(position).getCity() + "\n" + list.get(position).getState());
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void getCustomer() {

        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getCustomer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.customerArray = jsonObject.optJSONArray("result");
                        bindData();
                    } else {
                        if (adapter != null) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                        showSnackBar(recyclerView, jsonObject.optString("message"));
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

    private void bindData() {
        list.clear();
        try {
            for (int i = 0; i < Constant.customerArray.length(); i++) {
                JSONObject jsonObject = Constant.customerArray.optJSONObject(i);
                Customer model = new Customer();
                model.setId(jsonObject.optString("id"));
                model.setName(jsonObject.optString("name"));
                model.setContact(jsonObject.optString("contact"));
                model.setEmail(jsonObject.optString("email"));
                model.setAddress(jsonObject.optString("address"));
                model.setcName(jsonObject.optString("cName"));
                model.setGstNo(jsonObject.optString("gstNo"));
                model.setCity(jsonObject.optString("city"));
                model.setState(jsonObject.optString("state"));
                model.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                list.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        bindRecyclerView();
    }

    private void showAddressDialog(String address) {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.popup_address, null);

        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvSubmit = view.findViewById(R.id.tvSubmit);
        TextView tvAddress = view.findViewById(R.id.tvAddress);

        if (address.equals("") || address.isEmpty()) {
            tvAddress.setText("N/A");
        } else {
            tvAddress.setText(address);
        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
}
