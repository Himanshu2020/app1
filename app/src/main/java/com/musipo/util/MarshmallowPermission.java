package com.musipo.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class MarshmallowPermission {
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;

    public MarshmallowPermission() {
    }

    public boolean checkPermissionForExternalStorage(Activity activity) {
        if(Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public void requestPermissionForExternalStorage(Activity activity) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity,
                    "External Storage permission needed. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
            // user has previously denied runtime permission to external storage
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }
}