package com.madhavsteel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.madhavsteel.R;
import com.madhavsteel.adapter.AddSizeListAdapter;
import com.madhavsteel.model.Category;
import com.madhavsteel.model.Product;
import com.madhavsteel.model.Sizes;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;
import com.madhavsteel.views.MySpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSizeListActivity extends BaseActivity {

    private static final String TAG = "AddSizeListActivity";
    private Context context;
    private ImageView ivBack;
    private TextView tvTitle, tvCalculatePrice, tvAddMoreItem, tvAddSize;
    private RecyclerView recyclerView;
    private EditText etSize, etPieceQuantity, etQuantity, etPrice;
    private Spinner spnMeasurement;
    private AddSizeListAdapter adapter;
    private ArrayList<Sizes> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId = "", size = "", pieceQuantity = "", quantity = "", measurment = "", price = "", catId = "", productId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_size_list);

        context = AddSizeListActivity.this;

        adapter = new AddSizeListAdapter(context, list, new AddSizeListAdapter.AdapterListener() {
            @Override
            public void onItemSelected() {
                if (getIntent() != null) {
                    if (getIntent().hasExtra("details")) {
                        Category model = (Category) getIntent().getSerializableExtra("details");
                        bindData(model);
                        Constant.sizeStatus = 1;
                    } else if (getIntent().hasExtra("productDetails")) {
                        Product model = (Product) getIntent().getSerializableExtra("productDetails");
                        bindData(model);
                        Constant.sizeStatus = 2;
                    }
                }
            }
        });

        init();
    }

    private void init() {

        tvTitle = findViewById(R.id.tvTitle);
        tvCalculatePrice = findViewById(R.id.tvCalculatePrice);
        tvAddMoreItem = findViewById(R.id.tvAddMoreItem);
        tvAddSize = findViewById(R.id.tvAddSize);

        tvCalculatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityForResult(context, SelectedSizeListActivity.class, 13);
            }
        });
        tvAddMoreItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCancelResultOfActivity(12);
            }
        });

        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etSize = findViewById(R.id.etSize);
        etPieceQuantity = findViewById(R.id.etPieceQuantity);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);

        spnMeasurement = findViewById(R.id.spnMeasurement);

        List<String> measurementList = new ArrayList<>();

//        measurementList.add("Tonne");
        measurementList.add("Kilogram");
//        measurementList.add("Piece");

        MySpinnerAdapter mySpinnerAdapter = new MySpinnerAdapter(context, android.R.layout.simple_spinner_item, measurementList);
        mySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMeasurement.setAdapter(mySpinnerAdapter);

        tvAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                size = etSize.getText().toString();
                quantity = etQuantity.getText().toString();
                pieceQuantity = etPieceQuantity.getText().toString();
                price = etPrice.getText().toString();
                measurment = spnMeasurement.getSelectedItem().toString();
                if (size.equals("")) {
                    etSize.setError("Enter Size");
                    etSize.requestFocus();
                } else if (measurment.equals("")) {
                    showToast("Please select measurement");
                } else if (pieceQuantity.equals("")) {
                    etPieceQuantity.setError("Enter Quantity");
                    etPieceQuantity.requestFocus();
                } else if (quantity.equals("")) {
                    etQuantity.setError("Enter Measurement");
                    etQuantity.requestFocus();
                } else if (price.equals("")) {
                    etPrice.setError("Enter Price");
                    etPrice.requestFocus();
                } else {
                    addToCart();
                }
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

    @Override
    protected void onResume() {
        super.onResume();

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }

        if (getIntent() != null) {
            if (getIntent().hasExtra("details")) {
                Category model = (Category) getIntent().getSerializableExtra("details");
                bindData(model);
                Constant.sizeStatus = 1;
                catId = model.getCatId();
                productId = model.getProductId();
            } else if (getIntent().hasExtra("productDetails")) {
                Product model = (Product) getIntent().getSerializableExtra("productDetails");
                bindData(model);
                Constant.sizeStatus = 2;
            }
        }

        if (Constant.selectionChange) {
//            getCategorySize(Constant.catId);
            Constant.selectionChange = false;
        }
    }

    private void bindData(Category model) {

        tvTitle.setText(model.getCatTitle());

        if (model.getAddStatus().equals("1")) {
            getCartSizes();
        } else {
//            getCategorySize(model.getCatId());
        }

    }

    private void bindData(Product model) {

        tvTitle.setText(model.getProductTitle());

        if (model.getAddStatus().equals("1")) {
            getCartSizes();
        } else {
//            getCategorySize(model.getProductId());
        }

    }

    private void getCartSizes() {
        list.clear();
        adapter.notifyDataSetChanged();
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
                        bindRecyclerView(cartArray);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void bindRecyclerView(JSONArray cartArray) {
        list.clear();
        try {
            if (cartArray != null) {
                for (int i = 0; i < cartArray.length(); i++) {
                    JSONObject object = cartArray.getJSONObject(i);
                    JSONArray sizeCartArray = object.getJSONArray("sizeCartArray");
                    for (int j = 0; j < sizeCartArray.length(); j++) {
                        JSONObject jsonObject = sizeCartArray.getJSONObject(j);
                        Sizes model = new Sizes();
                        model.setCartId(jsonObject.optString("cartId"));
                        model.setSizeId(jsonObject.optString("sizeId"));
                        model.setSizeTitle(jsonObject.optString("size"));
                        model.setCatId(jsonObject.optString("catId"));
                        model.setProductId(jsonObject.optString("productId"));
                        model.setWeight(jsonObject.optString("weight"));
                        model.setPriceType(jsonObject.optString("priceType"));
                        model.setPrice(jsonObject.optString("price"));
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
                        model.setCatTitle(jsonObject.optString("catTitle"));
                        model.setQuantity(jsonObject.optString("quantity"));
                        model.setPieceQuantity(jsonObject.optString("pieceQuantity"));
                        model.setMeasurement(jsonObject.optString("measurement"));
                        model.setProductId(jsonObject.optString("productId"));
                        model.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                        if (jsonObject.optString("sizeId").equals("0") && catId.equals(jsonObject.optString("catId"))) {
                            model.setCartStatus("1");
                            list.add(model);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addToCart() {
        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.addSizeToCart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    if (status == 1) {
                        Constant.cartCounts = jsonObject.optInt("cartCount");
                        etSize.setText("");
                        etPieceQuantity.setText("");
                        etQuantity.setText("");
                        etPrice.setText("");
                        getCartSizes();
                    }
                } catch (JSONException e) {
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
                params.put("userId", userId);
                params.put("customerId", Constant.customerId);
                params.put("sizeId", "0");
                params.put("measurement", measurment);
                params.put("pieceQuantity", pieceQuantity);
                params.put("quantity", quantity);
                params.put("price", price);
                params.put("size", size);
                params.put("catId", catId);
                params.put("productId", productId);
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