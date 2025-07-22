package com.madhavsteel.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import com.madhavsteel.adapter.NotificationsListAdapter;
import com.madhavsteel.model.Notification;
import com.madhavsteel.model.User;
import com.madhavsteel.utils.Constant;
import com.madhavsteel.utils.MyApplication;
import com.madhavsteel.utils.RealmController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsActivity extends BaseActivity {

    private static final String TAG = "NotificationsActivity";
    private Context context;
    private ImageView ivBack, ivDelete;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private NotificationsListAdapter adapter;
    private ArrayList<Notification> list = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        context = NotificationsActivity.this;

        RealmController.with(context).refresh();
        User user = RealmController.with(context).getUser();
        if (user != null) {
            userId = user.getUserId();
        }

        init();

        adapter = new NotificationsListAdapter(context, list, new NotificationsListAdapter.AdapterListener() {
            @Override
            public void onItemSelected(Notification model) {
                clearNotification(model.getNotificationId());
            }
        });

        getNotifications();
    }

    private void init() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Notifications");
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivDelete = findViewById(R.id.ivDelete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearNotifications();
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getNotifications();
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(Constant.messageEvent));
    }

    private void clearNotification(final String notificationId) {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.clearNotification, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                hideProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(recyclerView, jsonObject.optString("message"));
                    if (status == 1) {
                        getNotifications();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                params.put("notificationId", notificationId);
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

    private void clearNotifications() {
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.clearNotifications, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(recyclerView, jsonObject.optString("message"));
                    if (status == 1) {
                        list.clear();
                        adapter.notifyDataSetChanged();
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

    private void getNotifications() {
        list.clear();
        adapter.notifyDataSetChanged();
        showProgressDialog(context, "Please wait..");
        StringRequest strReq = new StringRequest(Request.Method.POST, Constant.getAllNotifications, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    showSnackBar(recyclerView, jsonObject.optString("message"));
                    if (status == 1) {
                        JSONObject result = jsonObject.getJSONObject("result");
                        Constant.notificationArray = result.optJSONArray("notificationArray");
                        bindNotifications();
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

    private void bindNotifications() {
        list.clear();
        try {
            for (int i = 0; i < Constant.notificationArray.length(); i++) {
                JSONObject jsonObject = Constant.notificationArray.optJSONObject(i);
                Notification model = new Notification();
                model.setNotificationId(jsonObject.optString("notificationId"));
                model.setUserId(jsonObject.optString("userId"));
                model.setNotificationType(jsonObject.optString("notificationType"));
                model.setTitle(jsonObject.optString("title"));
                model.setBody(jsonObject.optString("body"));
                model.setImage(jsonObject.optString("image"));
                model.setReadStatus(jsonObject.optString("readStatus"));
                model.setDateTimeAdded(jsonObject.optString("dateTimeAdded"));
                list.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        if (list != null) {
            if (list.size() > 0) {
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                ivDelete.setVisibility(View.GONE);
            }
        } else {
            ivDelete.setVisibility(View.GONE);
        }
    }
}
