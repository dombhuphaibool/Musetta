package com.bandonleon.musetta.activity;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dombhuphaibool on 11/15/15.
 */
public class PermissionsAwareActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private int mRequestCode = 0;

    /**
     * Called to request permissions
     *
     * @param permissions - A list of permissions to request
     * @return - A request code the will be used in onPermissionsGranted() callback
     */
    protected int requestPermissions(final List<String> permissions) {
        final int requestCode;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            requestCode = ++mRequestCode;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    onPermissionsGranted(requestCode, permissions);
                }
            });
        } else {
            requestCode = requestRuntimePermissions(permissions);
        }
        return requestCode;
    }

    /**
     * Override onShowRequestPermissionRationale() if the subclass would like to show the
     * user a dialog or something to explain why the permission was needed.
     *
     * @param permission
     */
    protected void onShowRequestPermissionRationale(String permission) {
    }

    /**
     * onPermissionsGranted() will be called after the subclass calls requestPermissions().
     * It is the responsibility of the subclass to check which of the requested permissions were
     * actually granted.
     *
     * @param requestCode - The same request code returned from requestPermissions() call
     * @param grantedPermissions - A list of permissions that was granted by the user
     */
    protected void onPermissionsGranted(int requestCode, List<String> grantedPermissions) {
    }

    @TargetApi(Build.VERSION_CODES.M)
    private int requestRuntimePermissions(List<String> permissions) {
        int requestCode = 0;
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(permission)) {
                    onShowRequestPermissionRationale(permission);
                    // Break here. It's the responsibility of the caller to call
                    // requestPermissions() again after displaying the dialog
                    break;
                } else {
                    permissionsToRequest.add(permission);
                }
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            String[] permissionsNeeded = new String[permissionsToRequest.size()];
            permissionsNeeded = permissionsToRequest.toArray(permissionsNeeded);
            requestCode = ++mRequestCode;
            requestPermissions(permissionsNeeded, requestCode);
        }
        return requestCode;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        List<String> grantedPermissions = new ArrayList<>();
        int numPermissions = permissions.length;
        for (int i=0; i<numPermissions; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                grantedPermissions.add(permissions[i]);
            }
        }
        onPermissionsGranted(requestCode, grantedPermissions);
    }
}
