package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.adapter.ProductsSizeListAdapter;
import com.madhavsteel.model.EditModel;
import com.madhavsteel.model.Products;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SelectedSizeListActivity extends BaseActivity {

    private static final String TAG = "SelectedSizeList";
    private Context context;
    private ImageView ivBack, ivDelete;
    private TextView tvTitle, tvCalculatePrice, tvAddMoreItem, tvOrder, tvInfo;
    private LinearLayout llList, llNoList, llPrice, llCart;
    private RecyclerView recyclerView;
    private ProductsSizeListAdapter adapter;
    private ArrayList<Products> list = new ArrayList<>();
    public ArrayList<EditModel> editModelArrayList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId = "", userName = "", userContact = "", price = "";
    private boolean flagCalculate = false;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_list);

        context = SelectedSizeListActivity.this;
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            userName = user.getName();
            userContact = user.getContact();
        }

        init();

        getCartSizes();
    }

    private void init() {

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Selected Size");
        tvCalculatePrice = findViewById(R.id.tvCalculatePrice);
        tvAddMoreItem = findViewById(R.id.tvAddMoreItem);
        tvOrder = findViewById(R.id.tvOrder);
        tvInfo = findViewById(R.id.tvInfo);

        tvAddMoreItem.setVisibility(View.GONE);
        tvCalculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getLoginStatus()) {
                    if (validate()) {
                        calculatePrice();
                    }
                } else {
                    goToActivityForResult(context, PreLoginActivity.class, 24);
                }
            }
        });
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDialog();
            }
        });

        ivBack = findViewById(R.id.ivBack);
        ivDelete = findViewById(R.id.ivDelete);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivDelete.setVisibility(View.VISIBLE);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCarts();
            }
        });

        llPrice = findViewById(R.id.llPrice);
        llList = findViewById(R.id.llList);
        llNoList = findViewById(R.id.llNoList);
        llCart = findViewById(R.id.llCart);
        llCart.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
            userName = user.getName();
            userContact = user.getContact();
        }
    }

    private void getCartSizes() {
        position = 0;
        list.clear();
        ProductsSizeListAdapter.editModelArrayList.clear();
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getCartSizes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        JSONArray cartArray = result.getJSONArray("cartArray");
                        bindList(cartArray);
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
                hideProgressDialog();
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("customerId", Constant.customerId);
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

    public void expandGroup(int gPos) {
        if (adapter.isGroupExpanded(gPos)) {
            return;
        }
        adapter.toggleGroup(gPos);
    }

    private void bindList(JSONArray cartArray) {

        list.clear();
        editModelArrayList.clear();
        position = 0;
        try {
            if (cartArray != null) {
                for (int i = 0; i < cartArray.length(); i++) {
                    JSONObject object = cartArray.getJSONObject(i);
                    JSONArray sizeCartArray = object.getJSONArray("sizeCartArray");
                    ArrayList<Sizes> sizes = new ArrayList<>();
                    for (int j = 0; j < sizeCartArray.length(); j++) {
                        JSONObject jsonObject = sizeCartArray.getJSONObject(j);
                        Sizes model = new Sizes();
                        model.setPosition(String.valueOf(position));
                        model.setCartId(jsonObject.optString("cartId"));
                        model.setSizeId(jsonObject.optString("sizeId"));
                        if (jsonObject.optString("sizeId").equals("0")) {
                            model.setSizeTitle(jsonObject.optString("size"));
                        } else {
                            model.setSizeTitle(jsonObject.optString("sizeTitle"));
                        }
                        model.setCatId(jsonObject.optString("catId"));
                        model.setProductId(jsonObject.optString("productId"));
                        model.setWeight(jsonObject.optString("weight"));
                        model.setPriceType(jsonObject.optString("priceType"));
                        model.setSizeImage(jsonObject.optString("sizeImage"));
                        model.setSizeThumbImage(jsonObject.optString("sizeThumbImage"));
                        model.setSizeMediumImage(jsonObject.optString("sizeMediumImage"));
                        model.setHeight(jsonObject.optString("height"));
                        model.setWidth(jsonObject.optString("width"));
                        model.setRound(jsonObject.optString("round"));
                        model.setSquare(jsonObject.optString("square"));
                        model.setRectangle(jsonObject.optString("rectangle"));
                        model.setHexagon(jsonObject.optString("hexagon"));
                        model.setOctagon(jsonObject.optString("octagon"));
                        model.setShowPieceStatus(jsonObject.optString("showPieceStatus"));
                        model.setCatTitle(jsonObject.optString("catTitle"));
                        model.setPieceQuantity(jsonObject.optString("pieceQuantity"));
                        model.setQuantity(jsonObject.optString("quantity"));
                        model.setPrice(jsonObject.optString("price"));
                        model.setMeasurement(jsonObject.optString("measurement"));
                        model.setProductId(jsonObject.optString("productId"));
                        model.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                        sizes.add(model);
                        position++;
                    }
                    Log.e(TAG, "bindList: sizes " + sizes.size());
                    Products product = new Products(object.getString("productTitle"), sizes);
                    product.setProductId(object.optString("productId"));
                    product.setProductTitle(object.optString("productTitle"));
                    product.setSizeList(sizes);
                    list.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bindData();
    }

    private void bindData() {

        adapter = new ProductsSizeListAdapter(context, list, editModelArrayList, new ProductsSizeListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Sizes model) {
                clearCart(model);
            }
        }, new ProductsSizeListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Sizes model) {
                tvCalculatePrice.setVisibility(View.VISIBLE);
                llPrice.setVisibility(View.GONE);
                tvInfo.setVisibility(View.GONE);
            }
        }, new ProductsSizeListAdapter.AdapterListenerProducts() {
            @Override
            public void onItemSelected(Products model) {
            }
        });

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

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        for (int i = 0; i < position; i++) {
            expandGroup(i);
        }
    }

    private boolean validate() {

        Constant.selectedCart = "";
        Constant.selectedSize = "";
        Constant.selectedPieceQuantity = "";
        Constant.selectedQuantity = "";
        Constant.selectedPrice = "";
        Constant.selectedMeasurement = "";

        for (int i = 0; i < ProductsSizeListAdapter.editModelArrayList.size(); i++) {
            if (ProductsSizeListAdapter.editModelArrayList.get(i).getPieceInQuantity().isEmpty() ||
                    Float.parseFloat(ProductsSizeListAdapter.editModelArrayList.get(i).getPieceInQuantity()) <= 0) {
                showToast("Enter valid quantity");
                return false;
            } else if (ProductsSizeListAdapter.editModelArrayList.get(i).getEditTextValue().isEmpty() ||
                    Float.parseFloat(ProductsSizeListAdapter.editModelArrayList.get(i).getEditTextValue()) <= 0) {
                showToast("Enter valid measurement in kg");
                return false;
            } else if (ProductsSizeListAdapter.editModelArrayList.get(i).getRate().isEmpty() ||
                    Float.parseFloat(ProductsSizeListAdapter.editModelArrayList.get(i).getRate()) <= 0) {
                showToast("Enter valid price");
                return false;
            } else {
                Constant.selectedCart = Constant.selectedCart + ProductsSizeListAdapter.editModelArrayList.get(i).getCartId() + ", ";
                Constant.selectedSize = Constant.selectedSize + ProductsSizeListAdapter.editModelArrayList.get(i).getSizeId() + ", ";
                Constant.selectedPieceQuantity = Constant.selectedPieceQuantity + ProductsSizeListAdapter.editModelArrayList.get(i).getPieceInQuantity() + ", ";
                Constant.selectedQuantity = Constant.selectedQuantity + ProductsSizeListAdapter.editModelArrayList.get(i).getEditTextValue() + ", ";
                Constant.selectedPrice = Constant.selectedPrice + ProductsSizeListAdapter.editModelArrayList.get(i).getRate() + ", ";
                Constant.selectedMeasurement = Constant.selectedMeasurement + ProductsSizeListAdapter.editModelArrayList.get(i).getSpinnerValue() + ", ";
            }
        }

        String str4 = Constant.selectedCart.trim();
        if (!str4.isEmpty()) str4 = str4.substring(0, str4.length() - 1);
        Constant.selectedCart = str4;

        String str3 = Constant.selectedSize.trim();
        if (!str3.isEmpty()) str3 = str3.substring(0, str3.length() - 1);
        Constant.selectedSize = str3;

        String str2 = Constant.selectedQuantity.trim();
        if (!str2.isEmpty()) str2 = str2.substring(0, str2.length() - 1);
        Constant.selectedQuantity = str2;

        String str5 = Constant.selectedPrice.trim();
        if (!str5.isEmpty()) str5 = str5.substring(0, str5.length() - 1);
        Constant.selectedPrice = str5;

        str5 = Constant.selectedPieceQuantity.trim();
        if (!str5.isEmpty()) str5 = str5.substring(0, str5.length() - 1);
        Constant.selectedPieceQuantity = str5;

        String str = Constant.selectedMeasurement.trim();
        if (!str.isEmpty()) str = str.substring(0, str.length() - 1);
        Constant.selectedMeasurement = str;

        return true;
    }

    private void calculatePrice() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.calculatePrice, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.selectedSizeList.clear();
                        Constant.sizeList.clear();
                        Constant.quantityList.clear();
                        Constant.measurementList.clear();
                        Constant.selectedCart = "";
                        Constant.selectedSize = "";
                        Constant.selectedQuantity = "";
                        Constant.selectedPrice = "";
                        Constant.selectedMeasurement = "";
                        Constant.priceResult = jsonObject.optJSONObject("result");
                        goToActivityForResult(context, PriceListActivity.class, 14);
                    }
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
                hideProgressDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("customerId", Constant.customerId);
                params.put("cartId", Constant.selectedCart);
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

    private void clearCarts() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.clearCarts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.selectedSizeList.clear();
                        Constant.sizeList.clear();
                        Constant.measurementList.clear();
                        Constant.quantityList.clear();
                        Constant.selectedSize = "";
                        Constant.selectedQuantity = "";
                        Constant.selectedMeasurement = "";
                        setCancelResultOfActivity(13);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
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
                params.put("userId", userId);
                params.put("customerId", Constant.customerId);
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
                try {
                    hideProgressDialog();
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    if (status == 1) {
                        Constant.selectionChange = true;
                        Constant.selectedSizeList.remove(model.getSizeId());
                        for (int i = 0; i < Constant.sizeList.size(); i++) {
                            if (model.getSizeId().equals(Constant.sizeList.get(i).getSizeId())) {
                                Constant.sizeList.remove(i);
                                break;
                            }
                        }
                        list.remove(model);
                        adapter.notifyDataSetChanged();
                        getCartSizes();
                        tvCalculatePrice.setVisibility(View.VISIBLE);
                        llPrice.setVisibility(View.GONE);
                        tvInfo.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    hideProgressDialog();
                    e.printStackTrace();
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
        if (requestCode == 24) {
            if (resultCode == Activity.RESULT_OK) {
                if (validate()) {
                    calculatePrice();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        if (requestCode == 14) {
            if (resultCode == Activity.RESULT_OK) {
                setResultOfActivity(13);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                getCartSizes();
            }
        }
    }

    private ArrayList<EditModel> populateList() {

        ArrayList<EditModel> list = new ArrayList<>();

        for (int i = 0; i < this.list.size(); i++) {
            for (int j = 0; j < this.list.get(i).getSizeList().size(); j++) {
                EditModel editModel = new EditModel();
                editModel.setSizeId(this.list.get(i).getSizeList().get(j).getSizeId());
                editModel.setEditTextValue(String.valueOf(1));
                if (this.list.get(i).getSizeList().get(j).getPriceType().equals("1")) {
                    editModel.setSpinnerValue("Piece");
                } else {
                    editModel.setSpinnerValue("Tonne");
                }
                list.add(editModel);
            }
        }

        return list;
    }

    class ProductSorting implements Comparator<Sizes> {

        @Override
        public int compare(Sizes model1, Sizes model2) {
            if ((Integer.parseInt(model1.getProductId())) < (Integer.parseInt(model2.getProductId()))) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
