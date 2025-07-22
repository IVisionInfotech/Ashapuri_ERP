package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.adapter.SizeListAdapter;
import com.madhavsteel.model.Category;
import com.madhavsteel.model.Product;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.ClickListener;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SizeListActivity extends BaseActivity {

    private static final String TAG = "SizeListActivity";
    private TextView tvAddMoreItem, tvCalculatePrice;
    private LinearLayout llCart, llPrice;
    private RecyclerView recyclerView;
    private ArrayList<Sizes> list = new ArrayList<>();
    private SizeListAdapter adapter;
    private boolean flagCalculate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);

        context = SizeListActivity.this;

        init();

        if (getIntent() != null) {
            if (getIntent().hasExtra("details")) {
                Constant.sizeStatus = 1;
                Category model = (Category) getIntent().getSerializableExtra("details");
                setToolbar(model.getCatTitle());
                getCategorySize(model.getCatId());
                Constant.catId = model.getCatId();
            } else if (getIntent().hasExtra("productDetails")) {
                Constant.sizeStatus = 2;
                Product model = (Product) getIntent().getSerializableExtra("productDetails");
                setToolbar(model.getProductTitle());
                getCategorySize(model.getProductId());
                Constant.catId = model.getProductId();
//                tvSubTitle.setText(model.getPriceTonne());
            }
        }
    }

    private void init() {

        tvAddMoreItem = findViewById(R.id.tvAddMoreItem);
        tvCalculatePrice = findViewById(R.id.tvCalculatePrice);

        tvAddMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    addToCart();
                } else {
                    setCancelResultOfActivity(12);
                }
            }
        });
        tvCalculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagCalculate = true;
                if (validate()) {
                    addToCart();
                } else {
                    if (Constant.cartCounts > 0) {
                        flagCalculate = false;
                        goToActivityForResult(context, SelectedSizeListActivity.class, 13);
                    } else {
                        showToast("Please select size first");
                    }
                }
            }
        });

        llList = findViewById(R.id.llList);
        llNoList = findViewById(R.id.llNoList);
        llCart = findViewById(R.id.llCart);
        llCart.setVisibility(View.VISIBLE);
        llPrice = findViewById(R.id.llPrice);
        llPrice.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Constant.selectionChange) {
            getCategorySize(Constant.catId);
            Constant.selectionChange = false;
        }
    }

    private void bindRecyclerView() {

        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(false);

        adapter = new SizeListAdapter(context, list, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
            }
        }, new ClickListener() {
            @Override
            public void onItemSelected(int position) {
                clearCart(list.get(position));
            }
        });

        recyclerView.setAdapter(adapter);

        if (list != null) {
            if (list.size() > 0) {
                llList.setVisibility(View.VISIBLE);
                llNoList.setVisibility(View.GONE);
            } else {
                llList.setVisibility(View.GONE);
                llNoList.setVisibility(View.VISIBLE);
            }
        } else {
            llList.setVisibility(View.GONE);
            llNoList.setVisibility(View.VISIBLE);
        }
    }

    private void getCategorySize(final String catId) {

        showProgressDialog(context, "Please wait..");

        String id = "";
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            id = user.getUserId();
        }

        Constant.catId = catId;
        list.clear();
        Constant.selectedSizeList.clear();
        Constant.sizeList.clear();
        SizeListAdapter.editModelArrayList.clear();

        Log.e(TAG, "getCategorySize: " + id + " : " + catId + " : " + Constant.sizeStatus);

        final String finalId = id;
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getCategorySize, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray sizeArray = result.getJSONArray("sizeArray");
                        bindRecyclerView(sizeArray);
                    } else {
                        llList.setVisibility(View.GONE);
                        llNoList.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", finalId);
                params.put("customerId", Constant.customerId);
                params.put("catId", catId);
                params.put("sizeStatus", String.valueOf(Constant.sizeStatus));
                Log.e(TAG, "getParams: " + params.toString());
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

    private void bindRecyclerView(JSONArray sizeArray) {
        list.clear();
        try {
            for (int i = 0; i < sizeArray.length(); i++) {
                JSONObject object = sizeArray.getJSONObject(i);
                Sizes model = new Sizes();
                model.setSizeId(object.optString("sizeId"));
                model.setProductId(object.optString("productId"));
                model.setProductTitle(object.optString("productTitle"));
                model.setCatId(object.optString("catId"));
                model.setCatTitle(object.optString("catTitle"));
                model.setSizeTitle(object.optString("sizeTitle"));
                model.setWidth(object.optString("weight"));
                model.setPriceType(object.optString("priceType"));
                model.setNosKg(object.optString("nosKg"));
                model.setSizeImage(object.optString("sizeImage"));
                model.setSizeThumbImage(object.optString("sizeThumbImage"));
                model.setSizeMediumImage(object.optString("sizeMediumImage"));
                model.setHeight(object.optString("height"));
                model.setWidth(object.optString("width"));
                model.setRound(object.optString("round"));
                model.setSquare(object.optString("square"));
                model.setRectangle(object.optString("rectangle"));
                model.setHexagon(object.optString("hexagon"));
                model.setOctagon(object.optString("octagon"));
                model.setCartId(object.optString("cartId"));
                model.setQuantity(object.optString("quantity"));
                model.setPrice(object.optString("price"));
                model.setMeasurement(object.optString("measurement"));
                model.setPieceQuantity(object.optString("pieceQuantity"));
                model.setCartStatus(object.optString("cartStatus"));
                model.setShowPieceStatus(object.optString("showPieceStatus"));
                model.setStatus(object.optString("status"));
                model.setDateTimeAdded(object.optString("dateTimeAdded"));
                list.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bindRecyclerView();
    }

    private boolean validate() {

        /*Constant.quantityList.clear();
        Constant.pieceQuantityList.clear();
        Constant.measurementList.clear();*/
        Constant.selectedSize = "";
        Constant.selectedPieceQuantity = "";
        Constant.selectedQuantity = "";
        Constant.selectedPrice = "";
        Constant.selectedMeasurement = "";

        Log.e(TAG, "validate: " + SizeListAdapter.editModelArrayList.size());

        if (SizeListAdapter.editModelArrayList != null) {
            if (SizeListAdapter.editModelArrayList.size() > 0) {
                for (int i = 0; i < SizeListAdapter.editModelArrayList.size(); i++) {
                    if (SizeListAdapter.editModelArrayList.get(i).getPieceInQuantity().isEmpty() ||
                            Float.parseFloat(SizeListAdapter.editModelArrayList.get(i).getPieceInQuantity()) <= 0) {
                        showToast("Enter valid quantity");
                        return false;
                    } else if (SizeListAdapter.editModelArrayList.get(i).getEditTextValue().isEmpty() ||
                            Float.parseFloat(SizeListAdapter.editModelArrayList.get(i).getEditTextValue()) <= 0) {
                        showToast("Enter valid measurement in kg");
                        return false;
                    } else if (SizeListAdapter.editModelArrayList.get(i).getRate().isEmpty() ||
                            Float.parseFloat(SizeListAdapter.editModelArrayList.get(i).getRate()) <= 0) {
                        showToast("Enter valid price");
                        return false;
                    } else {
                        Constant.selectedSize = Constant.selectedSize + SizeListAdapter.editModelArrayList.get(i).getSizeId() + ", ";
                        Constant.selectedPieceQuantity = Constant.selectedPieceQuantity + SizeListAdapter.editModelArrayList.get(i).getPieceInQuantity() + ", ";
                        Constant.selectedQuantity = Constant.selectedQuantity + SizeListAdapter.editModelArrayList.get(i).getEditTextValue() + ", ";
                        Constant.selectedPrice = Constant.selectedPrice + SizeListAdapter.editModelArrayList.get(i).getRate() + ", ";
                        Constant.selectedMeasurement = Constant.selectedMeasurement + SizeListAdapter.editModelArrayList.get(i).getSpinnerValue() + ", ";
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

        String str3 = Constant.selectedSize.trim();
        if (!str3.isEmpty()) str3 = str3.substring(0, str3.length() - 1);
        Constant.selectedSize = str3;

        String str4 = Constant.selectedPrice.trim();
        if (!str4.isEmpty()) str4 = str4.substring(0, str4.length() - 1);
        Constant.selectedPrice = str4;

        String str2 = Constant.selectedQuantity.trim();
        if (!str2.isEmpty()) str2 = str2.substring(0, str2.length() - 1);
        Constant.selectedQuantity = str2;

        String str5 = Constant.selectedPieceQuantity.trim();
        if (!str5.isEmpty()) str5 = str5.substring(0, str5.length() - 1);
        Constant.selectedPieceQuantity = str5;

        String str = Constant.selectedMeasurement.trim();
        if (!str.isEmpty()) str = str.substring(0, str.length() - 1);
        Constant.selectedMeasurement = str;

        return true;
    }

    private void addToCart() {

        showProgressDialog(context, "Please wait..");

        String id = "";
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            id = user.getUserId();
        }

        final String finalId = id;
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.addToCart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        Constant.cartCounts = jsonObject.optInt("cartCount");
                        if (flagCalculate) {
                            flagCalculate = false;
                            goToActivityForResult(context, SelectedSizeListActivity.class, 13);
                        } else {
                            setCancelResultOfActivity(12);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", finalId);
                params.put("customerId", Constant.customerId);
                params.put("sizeId", Constant.selectedSize);
                params.put("measurement", Constant.selectedMeasurement);
                params.put("pieceQuantity", Constant.selectedPieceQuantity);
                params.put("quantity", Constant.selectedQuantity);
                params.put("price", Constant.selectedPrice);
                Log.e(TAG, "getParams: " + params.toString());
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

    private void clearCart(final Sizes model) {

        showProgressDialog(context, "Please wait..");

        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.clearCart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                hideProgressDialog();
                if (model.getSizeId().equals("0")) {
                    Constant.cartCounts = Constant.cartCounts - 1;
                    getCategorySize(Constant.catId);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cartId", model.getCartId());
                Log.e(TAG, "getParams: " + params.toString());
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
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 13) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(12);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                setCancelResultOfActivity(12);
            }
        }
    }
}
