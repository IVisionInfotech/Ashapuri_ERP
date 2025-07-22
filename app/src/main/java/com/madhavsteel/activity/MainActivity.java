package com.madhavsteel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.madhavsteel.R;
import com.madhavsteel.adapter.ProductListAdapter;
import com.madhavsteel.model.Category;
import com.madhavsteel.model.Product;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.PermissionUtils;
import com.madhavsteel.utils.RealmController;
import com.madhavsteel.views.CustomTypefaceSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ActivityCompat.OnRequestPermissionsResultCallback, PermissionUtils.PermissionResultCallback {

    private static final String TAG = "MainActivity";
    private static long back;
    private Context context;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Menu menu;
    private Toolbar toolbar;
    private ImageView ivMenu;
    private CardView cvWork1, cvWork2;
    private TextView tvTitle, tvUserName, tvEmail, tvNotificationCount, tvCart;
    private FrameLayout flNotifications, flCart;
    private RecyclerView recyclerView;
    private ProductListAdapter productListAdapter;
    private ArrayList<Product> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> permissions = new ArrayList<>();
    private PermissionUtils permissionUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        permissionUtils = new PermissionUtils(this);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionUtils.check_permission(permissions, "Need storage permission.", 1);

        init();

        productListAdapter = new ProductListAdapter(context, list, new ProductListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Product model) {
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
            }
        });

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvUserName = headerView.findViewById(R.id.tvUserName);
        tvEmail = headerView.findViewById(R.id.tvEmail);

        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        if (session.getLoginStatus()) {
            menu.findItem(R.id.navMyProfile).setVisible(true);
            menu.findItem(R.id.navMyOrders).setVisible(true);
            menu.findItem(R.id.navChangePin).setVisible(true);
            menu.findItem(R.id.navLogout).setVisible(true);
            menu.findItem(R.id.navLogin).setVisible(false);
        } else {
            menu.findItem(R.id.navMyProfile).setVisible(false);
            menu.findItem(R.id.navMyOrders).setVisible(false);
            menu.findItem(R.id.navChangePin).setVisible(false);
            menu.findItem(R.id.navLogout).setVisible(false);
            menu.findItem(R.id.navLogin).setVisible(true);
        }

        recyclerView = findViewById(R.id.recyclerView);

        mLayoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        ivMenu = findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        tvCart = findViewById(R.id.tvCart);

        flNotifications = findViewById(R.id.flNotifications);
        flCart = findViewById(R.id.flCart);
        flNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(context, NotificationsActivity.class);
            }
        });
        flCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.sizeList == null) {
                    Toast.makeText(context, "Please select atleast one size", Toast.LENGTH_SHORT).show();
                } else {
                    if (Constant.sizeList.size() > 0) {
                        goToActivity(context, SelectedSizeListActivity.class);
                    } else {
                        Toast.makeText(context, "Please select atleast one size", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cvWork1 = findViewById(R.id.cvWork1);
        cvWork2 = findViewById(R.id.cvWork2);

        cvWork1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(context, QuotationActivity.class);
            }
        });
        cvWork2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(context, CustomerListActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (session.getLoginStatus()) {
                if (back + 1000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(home);
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                } else {
                    Toast.makeText(this, "Press once again to exit...", Toast.LENGTH_SHORT).show();
                }
                back = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (session.getLoginStatus()) {
            menu.findItem(R.id.navMyProfile).setVisible(true);
            menu.findItem(R.id.navMyOrders).setVisible(true);
            menu.findItem(R.id.navChangePin).setVisible(true);
            menu.findItem(R.id.navLogout).setVisible(true);
            menu.findItem(R.id.navLogin).setVisible(false);

            RealmController.with(context).refresh();
            User user = RealmController.with(context).getUser();
            if (user != null) {
                tvUserName.setText(user.getName());
                tvEmail.setText(user.getEmail());
            }
        } else {
            menu.findItem(R.id.navMyProfile).setVisible(false);
            menu.findItem(R.id.navMyOrders).setVisible(false);
            menu.findItem(R.id.navChangePin).setVisible(false);
            menu.findItem(R.id.navLogout).setVisible(false);
            menu.findItem(R.id.navLogin).setVisible(true);
        }

        notificationCount();

        if (Constant.sizeList != null) {
            if (Constant.sizeList.size() > 0) {
                tvCart.setText(String.valueOf(Constant.sizeList.size()));
                tvCart.setVisibility(View.VISIBLE);
            } else {
                tvCart.setVisibility(View.GONE);
            }
        } else {
            tvCart.setVisibility(View.GONE);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(Constant.messageEvent));

        if (session.getLoginStatus()) {
            flNotifications.setVisibility(View.VISIBLE);
        } else {
            flNotifications.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Signika-Regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        applyFontToMenuItem(item);

        if (id == R.id.navDashboard) {
        } else if (id == R.id.navMyProfile) {
            if (session.getLoginStatus()) {
                goToActivity(context, ProfileActivity.class);
            } else {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                session.setStartActivity("ProfileActivity");
                goToActivityForResult(context, PreLoginActivity.class, 24);
            }
        } else if (id == R.id.navMyOrders) {
            if (session.getLoginStatus()) {
                goToActivity(context, OrdersListActivity.class);
            } else {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                session.setStartActivity("OrdersListActivity");
                goToActivityForResult(context, PreLoginActivity.class, 24);
            }
        } else if (id == R.id.navChangePin) {
            if (session.getLoginStatus()) {
                goToActivity(context, ChangePasswordActivity.class);
            } else {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                session.setStartActivity("ChangePasswordActivity");
                goToActivityForResult(context, PreLoginActivity.class, 24);
            }
        } else if (id == R.id.navLogout) {
            logout();
        } else if (id == R.id.navContactUs) {
            goToActivity(context, ContactUsActivity.class);
        } else if (id == R.id.navLogin) {
            goToActivity(context, PreLoginActivity.class);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure, You want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.setLoginStatus(false);
                        RealmController.with(context).clearAll();
                        tvUserName.setText("Userame");
                        tvEmail.setText("Userame@gmail.com");

                        if (session.getLoginStatus()) {
                            menu.findItem(R.id.navMyProfile).setVisible(true);
                            menu.findItem(R.id.navMyOrders).setVisible(true);
                            menu.findItem(R.id.navChangePin).setVisible(true);
                            menu.findItem(R.id.navLogout).setVisible(true);
                            menu.findItem(R.id.navLogin).setVisible(false);
                        } else {
                            menu.findItem(R.id.navMyProfile).setVisible(false);
                            menu.findItem(R.id.navMyOrders).setVisible(false);
                            menu.findItem(R.id.navChangePin).setVisible(false);
                            menu.findItem(R.id.navLogout).setVisible(false);
                            menu.findItem(R.id.navLogin).setVisible(true);
                        }
                        goToActivity(context, LoginActivity.class);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

        recyclerView.setAdapter(productListAdapter);
        productListAdapter.notifyDataSetChanged();

        hideProgressDialog();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            notificationCount();
        }
    };

    private void notificationCount() {

        String userId = "";

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }

        final String finalUserId = userId;
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.notificationCount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Constant.notificationCounts = jsonObject.optInt("notificationCount");
                    if (Constant.notificationCounts > 0) {
                        tvNotificationCount.setVisibility(View.VISIBLE);
                        tvNotificationCount.setText(String.valueOf(Constant.notificationCounts));
                    } else {
                        tvNotificationCount.setVisibility(View.GONE);
                    }
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", finalUserId);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted();
                } else {
                    permissionDenied();
                }
        }
    }

    private void permissionGranted() {
        Log.e("PERMISSION", "GRANTED");
    }

    private void permissionDenied() {
        Log.e("PERMISSION", "DENIED");
        showMessageOKCancel(context, "Need GPS permission for detect your location and show you great restaurants around you.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionUtils.check_permission(permissions, "Need GPS permission for detect your location and show you great restaurants around you.", 1);
            }
        });
    }

    @Override
    public void PermissionGranted(int request_code) {
        permissionGranted();
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.e("PERMISSION PARTIALLY", "GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        permissionDenied();
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.e("PERMISSION", "NEVER ASK AGAIN");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 || requestCode == 12) {
            if (resultCode == Activity.RESULT_OK) {
                goToActivity(context, OrdersListActivity.class);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
//                setResultOfActivity(1);
            }
        }
    }
}
