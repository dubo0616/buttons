package com.gaia.button.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.gaia.button.GaiaApplication;
import com.gaia.button.R;
import com.gaia.button.utils.Consts;
import com.gaia.button.view.ProgressDialogUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private String[] mPermissions;
    private ProgressDialogUtil mPd;
    private ProgressDialogUtil getWaitPd() {
        if (mPd == null) {
            mPd = new ProgressDialogUtil(this, "加载中...");
        }
        return mPd;
    }
    protected void showTotast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    public void showWaitDialog() {
        showWaitDialog("加载中...");
    }

    public void showWaitDialog(String title) {
        if (!isFinishing()) {
            if (mPd == null) {
                mPd = new ProgressDialogUtil(this, title);
            }
            if (!mPd.isShowing()) {
                mPd.showDialog();
            }
        }
    }

    public void showWaitDialog(boolean isCanceled) {
        if (!isFinishing()) {
            if (mPd == null) {
                mPd = new ProgressDialogUtil(this, "加载中...");

            }
            if (!mPd.isShowing()) {
                mPd.showDialog();
                mPd.setCanceledOnTouchOutside(isCanceled);
            }
        }
    }

    public void hideWaitDialog() {
        if (mPd != null && mPd.isShowing()) {
            mPd.hideDialog();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // we retrieve the list of permissions from the manifest
            mPermissions = getPackageManager().getPackageInfo(getPackageName(), PackageManager
                    .GET_PERMISSIONS).requestedPermissions;
        } catch (Exception e) {
            e.printStackTrace(); // PackageManager.NameNotFoundException or NullPointerException
        }
        if (mPermissions == null) {
            mPermissions = new String[0];
        }
        GaiaApplication.getInstance().addActivity(this);
    }

    @SuppressWarnings("UnusedReturnValue") // the return value is used for some implementations
    protected boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> notGrantedPermissions = new ArrayList<>(); // all permissions which needs to be enabled
            boolean needsMessage = false;

            for (int i = 0; i < mPermissions.length; i++) {
                if(mPermissions[i].contains(Manifest.permission.CALL_PHONE)){
                    continue;
                }
                if (ActivityCompat.checkSelfPermission(this, mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    notGrantedPermissions.add(mPermissions[i]);
                    needsMessage = needsMessage
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, mPermissions[i]);
                }
            }

            // if some permissions are not granted we request them
            if (notGrantedPermissions.size() > 0) {
                requestPermissions(notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]), needsMessage);
            }

            return notGrantedPermissions.size() == 0;
        }

        return true;
    }
    protected boolean checkPermissions( String[] mPermissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> notGrantedPermissions = new ArrayList<>(); // all permissions which needs to be enabled
            boolean needsMessage = false;

            for (int i = 0; i < mPermissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(this, mPermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    notGrantedPermissions.add(mPermissions[i]);
                    needsMessage = needsMessage
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, mPermissions[i]);
                }
            }

            // if some permissions are not granted we request them
            if (notGrantedPermissions.size() > 0) {
                requestPermissions(notGrantedPermissions.toArray(new String[notGrantedPermissions.size()]), needsMessage);
            }

            return notGrantedPermissions.size() == 0;
        }

        return true;
    }

    protected void requestPermissions(final String[] permissions, boolean needsMessage) {
        // the permissions management has only to be performed for Android 6 (API 23) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, permissions, Consts.ACTION_REQUEST_PERMISSIONS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
