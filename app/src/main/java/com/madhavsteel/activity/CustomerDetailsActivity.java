package com.madhavsteel.activity;

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
import com.madhavsteel.adapter.PriceListAdapter;
import com.madhavsteel.model.Customer;
import com.madhavsteel.model.Order;
import com.madhavsteel.model.OrderInquiry;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerDetailsActivity extends BaseActivity {

    private static final String TAG = "CustomerDetailsActivity";
    private TextView tvName, tvContact, tvEmail, tvAddress, tvCompanyName, tvCity, tvState, tvGSTIN;
    private RecyclerView recyclerView;
    private ArrayList<Order> list = new ArrayList<>();
    private PriceListAdapter adapter;
    private String customerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        context = CustomerDetailsActivity.this;

        init();

        if (getIntent() != null) {
            if (getIntent().hasExtra("details")) {
                Customer model = (Customer) getIntent().getSerializableExtra("details");
                customerId = model.getId();
                bindData(model);
                setToolbar(model.getName());
                getCustomerQuotation();
            }
        }
    }

    private void init() {

        tvName = findViewById(R.id.tvName);
        tvContact = findViewById(R.id.tvContact);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCity = findViewById(R.id.tvCity);
        tvState = findViewById(R.id.tvState);
        tvGSTIN = findViewById(R.id.tvGSTIN);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(false);
    }

    private void bindData(Customer model) {

        tvName.setText(model.getName());
        tvContact.setText(model.getContact());
        tvEmail.setText(model.getEmail());
        tvAddress.setText(model.getAddress());
        tvCompanyName.setText(model.getcName());
        tvCity.setText(model.getCity());
        tvState.setText(model.getState());
        tvGSTIN.setText(model.getGstNo());
    }

    private void getCustomerQuotation() {

        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getCustomerQuotation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        JSONArray result = jsonObject.optJSONArray("result");
                        bindData(result);
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

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("customerId", customerId);
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

    private void bindData(JSONArray result) {

        int position = 0;
        list.clear();

        try {
            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);

                JSONArray orderInquiryArray = object.optJSONArray("quoteDetailsArray");
                ArrayList<OrderInquiry> orderInquiries = new ArrayList<>();
                for (int j = 0; j < orderInquiryArray.length(); j++) {
                    JSONObject jsonObject = orderInquiryArray.getJSONObject(j);
                    OrderInquiry orderInquiry = new OrderInquiry();
                    orderInquiry.setQuoteDetailsId(jsonObject.optString("id"));
                    orderInquiry.setQuoteId(jsonObject.optString("quoteId"));
                    orderInquiry.setCustomerId(jsonObject.optString("customerId"));
                    orderInquiry.setSizeId(jsonObject.optString("sizeId"));
                    orderInquiry.setSizeTitle(jsonObject.optString("sizeTitle"));
                    orderInquiry.setCatId(jsonObject.optString("catId"));
                    orderInquiry.setCatTitle(jsonObject.optString("catTitle"));
                    orderInquiry.setQuantity(jsonObject.optString("quantity"));
                    orderInquiry.setMeasurement(jsonObject.optString("measurement"));
                    orderInquiry.setProductId(jsonObject.optString("productId"));
                    orderInquiry.setProductTitle(jsonObject.optString("productTitle"));
                    orderInquiry.setPrice(jsonObject.optString("price"));
                    orderInquiry.setPieceQuantity(jsonObject.optString("pieceQuantity"));
                    orderInquiry.setOrderStatus(jsonObject.optString("orderStatus"));
                    orderInquiry.setReadStatus(jsonObject.optString("readStatus"));
                    orderInquiry.setStatus(jsonObject.optString("status"));
                    orderInquiry.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                    orderInquiries.add(orderInquiry);
                    position++;
                }

                Order model = new Order(object.optString("quoteNo"), orderInquiries);
                model.setId(object.optString("id"));
                model.setUserId(object.optString("userId"));
                model.setCustomerId(object.optString("customerId"));
                model.setQuoteNo(object.optString("quoteNo"));
                model.setTotal(object.optString("total"));
                model.setGrandTotal(object.optString("grandTotal"));
                model.setGst(object.optString("gst"));
                model.setCgstTotal(object.optString("cgstTotal"));
                model.setSgstTotal(object.optString("sgstTotal"));
                model.setPdfLink(object.optString("pdfLink"));
                model.setPayStatus(object.optString("payStatus"));
                model.setReadStatus(object.optString("readStatus"));
                model.setOrderStatus(object.optString("orderStatus"));
                model.setStatus(object.optString("status"));
                model.setDateTimeAdded(object.optString("dateTimeAdded"));
                model.setInquiryList(orderInquiries);
                list.add(model);
            }

            adapter = new PriceListAdapter(context, list);

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            for (int i = 0; i < list.size(); i++) {
                expandGroup(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void expandGroup(int gPos) {
        if (adapter.isGroupExpanded(gPos)) {
            return;
        }
        adapter.toggleGroup(gPos);
    }
}
