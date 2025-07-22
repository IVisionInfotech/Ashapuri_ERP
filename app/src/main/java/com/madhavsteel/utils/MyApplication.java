package com.madhavsteel.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private RequestQueue mRequestQueue;
    private static Context context;
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();
        FirebaseApp.initializeApp(this);

        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name(Realm.DEFAULT_REALM_NAME).schemaVersion(0).deleteRealmIfMigrationNeeded().allowWritesOnUiThread(true) // Allow writes on UI thread
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
}