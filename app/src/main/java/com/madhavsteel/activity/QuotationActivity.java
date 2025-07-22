package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.madhavsteel.R;
import com.madhavsteel.adapter.ProductListAdapter;
import com.madhavsteel.fragment.CustomerListFragment;
import com.madhavsteel.model.Category;
import com.madhavsteel.model.Product;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuotationActivity extends BaseActivity implements FullScreenDialogFragment.OnConfirmListener, FullScreenDialogFragment.OnDiscardListener {

    private static final String TAG = "QuotationActivity";
    private TextView tvCustomerName;
    private CardView cvWork1, cvWork2;
    private RecyclerView recyclerView;
    private ProductListAdapter productListAdapter;
    private ArrayList<Product> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        context = QuotationActivity.this;

        if (savedInstanceState != null) {
            dialogFragment = (FullScreenDialogFragment) getSupportFragmentManager().findFragmentByTag(dialogTag);
            if (dialogFragment != null) {
                dialogFragment.setOnConfirmListener(this);
                dialogFragment.setOnDiscardListener(this);
            }
        }

        setToolbar("Create Quotation");

        init();

        if (Constant.productArray != null) {
            if (Constant.productArray.length() > 0) {
                bindCategoryData();
            } else {
                getHomeScreenData();
            }
        } else {
            getHomeScreenData();
        }
    }

    private void init() {

        recyclerView = findViewById(R.id.recyclerView);

        tvCustomerName = findViewById(R.id.tvCustomerName);

        cvWork1 = findViewById(R.id.cvWork1);
        cvWork2 = findViewById(R.id.cvWork2);

        cvWork1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityForResult(context, AddCustomerActivity.class, 1);
            }
        });
        cvWork2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (dialogFragment != null && dialogFragment.isAdded()) {
            dialogFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void bindRecyclerView() {

        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        productListAdapter = new ProductListAdapter(context, list, new ProductListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Product model) {
                if (!Constant.customerId.equals("")) {
                    if (model.getCategoryArrayList() != null) {
                        if (model.getCategoryArrayList().size() > 0) {
                            Intent intent = new Intent(context, CategoryListActivity.class);
                            intent.putExtra("details", model);
                            startActivityForResult(intent, 11);
                        } else {
                            Intent intent;
                            if (model.getAddStatus().equals("1")) {
                                intent = new Intent(context, AddSizeListActivity.class);
                            } else {
                                intent = new Intent(context, SizeListActivity.class);
                            }
                            intent.putExtra("productDetails", model);
                            startActivityForResult(intent, 12);
                        }
                    } else {
                        Intent intent;
                        if (model.getAddStatus().equals("1")) {
                            intent = new Intent(context, AddSizeListActivity.class);
                        } else {
                            intent = new Intent(context, SizeListActivity.class);
                        }
                        intent.putExtra("productDetails", model);
                        startActivityForResult(intent, 12);
                    }
                } else {
                    showSnackBar(recyclerView, "Select customer first");
                }
            }
        });

        recyclerView.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();
    }

    private void getHomeScreenData() {
        showProgressDialog(context, "Please wait..");
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
                        bindCategoryData();
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
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        Log.e(TAG, "onErrorResponse: " + res);
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                hideProgressDialog();
            }
        }) {

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

    private void bindCategoryData() {
        this.list.clear();
        try {
            for (int i = 0; i < Constant.productArray.length(); i++) {
                JSONObject object = Constant.productArray.optJSONObject(i);
                Product model = new Product();
                model.setProductId(object.optString("productId"));
                model.setProductTitle(object.optString("productTitle"));
                model.setProductImage(object.optString("productImage"));
                model.setProductThumbImage(object.optString("productThumbImage"));
                model.setProductMediumImage(object.optString("productMediumImage"));
                model.setStatus(object.optString("status"));
                model.setAddStatus(object.optString("addStatus"));
                model.setDateTimeAdded(object.optString("dateTimeAdded"));

                JSONArray categoryArray = object.optJSONArray("categoryArray");
                ArrayList<Category> categoryList = new ArrayList<>();
                for (int j = 0; j < categoryArray.length(); j++) {
                    JSONObject jsonObject = categoryArray.optJSONObject(j);
                    Category category = new Category();
                    category.setCatId(jsonObject.optString("catId"));
                    category.setProductId(jsonObject.optString("productId"));
                    category.setCatTitle(jsonObject.optString("catTitle"));
                    category.setCatImage(jsonObject.optString("catImage"));
                    category.setCatThumbImage(jsonObject.optString("catThumbImage"));
                    category.setCatMediumImage(jsonObject.optString("catMediumImage"));
                    category.setAddStatus(jsonObject.optString("addStatus"));
                    category.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                    categoryList.add(category);
                }
                model.setCategoryArrayList(categoryList);
                this.list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bindRecyclerView();

        hideProgressDialog();
    }

    private void openFragment() {
        Bundle args = new Bundle();
        dialogFragment = new FullScreenDialogFragment.Builder(context)
                .setTitle("Select Customer")
                .setConfirmButton("")
                .setOnConfirmListener(this)
                .setOnDiscardListener(this)
                .setContent(CustomerListFragment.class, args)
                .build();
        dialogFragment.show(getSupportFragmentManager(), dialogTag);
    }

    @Override
    public void onConfirm(@Nullable Bundle result) {
        if (result.containsKey("result1")) {
            tvCustomerName.setText(Constant.customerName);
        }
    }

    @Override
    public void onDiscard() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                tvCustomerName.setText(Constant.customerName);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }
}
