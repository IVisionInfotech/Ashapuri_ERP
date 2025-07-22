package com.madhavsteel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.madhavsteel.R;
import com.madhavsteel.utils.PermissionUtils;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback, PermissionUtils.PermissionResultCallback {

    private static final String TAG = "HomeActivity";
    private static long back;
    private Context context;
    //    private RelativeLayout rlSkip, rlLogin;
    private CardView cvLogin, cvSkip;
    private ArrayList<String> permissions = new ArrayList<>();
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = HomeActivity.this;

        permissionUtils = new PermissionUtils(this);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionUtils.check_permission(permissions, "Need storage permission.", 1);

        init();
    }

    private void init() {

        /*rlLogin = findViewById(R.id.rlLogin);
        rlSkip = findViewById(R.id.rlSkip);*/

        cvLogin = findViewById(R.id.cvLogin);
        cvSkip = findViewById(R.id.cvSkip);

        cvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivity(context, MainActivity.class);
            }
        });
        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToActivityForResult(context, PreLoginActivity.class, 24);
            }
        });
    }

    @Override
    public void onBackPressed() {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 24) {
            if (resultCode == Activity.RESULT_OK) {
                goToActivity(context, MainActivity.class);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
//                setResultOfActivity(1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onActivityResult: " + requestCode);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    permissionDenied();
                }
        }
    }

    private void permissionDenied() {
        Log.e("PERMISSION", "DENIED");
        showMessageOKCancel(context, "Need storage permission", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionUtils.check_permission(permissions, "Need storage permission", 1);
            }
        });
    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.e("PERMISSION", "GRANTED");
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
}
