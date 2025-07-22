package com.madhavsteel.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.adapter.OrdersListAdapter;
import com.madhavsteel.model.OrderInquiry;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrdersListOldActivity extends BaseActivity {

    private static final String TAG = "OrdersList";
    private Context context;
    private ImageView ivBack;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private OrdersListAdapter adapter;
    private ArrayList<OrderInquiry> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);

        context = OrdersListOldActivity.this;

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }

        init();

        if (Constant.orderArray == null) {
            getOrdersList();
        } else {
            if (Constant.orderArray.length() > 0) {
                bindOrdersList();
            } else {
                getOrdersList();
            }
        }
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("My Orders");
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getOrdersList() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getMyOrders, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(recyclerView, jsonObject.optString("message"));
                    if (status == 1) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        Constant.orderArray = result.optJSONArray("orderArray");
                        bindOrdersList();
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

    private void bindOrdersList() {
        try {
            for (int i = 0; i < Constant.orderArray.length(); i++) {
                JSONObject jsonObject = Constant.orderArray.optJSONObject(i);
                OrderInquiry order = new OrderInquiry();
                order.setQuoteDetailsId(jsonObject.optString("orderInquiryId"));
                order.setCustomerId(jsonObject.optString("customerId"));
                order.setSizeId(jsonObject.optString("sizeId"));
                order.setSizeTitle(jsonObject.optString("sizeTitle"));
                order.setCatId(jsonObject.optString("catId"));
                order.setCatTitle(jsonObject.optString("catTitle"));
                order.setProductId(jsonObject.optString("productId"));
                order.setProductTitle(jsonObject.optString("productTitle"));
                order.setQuantity(jsonObject.optString("quantity"));
                order.setPrice(jsonObject.optString("price"));
                order.setMeasurement(jsonObject.optString("measurement"));
                order.setOrderStatus(jsonObject.optString("orderStatus"));
                order.setStatus(jsonObject.optString("status"));
                order.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                list.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        adapter = new OrdersListAdapter(context, list);

        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }
}
