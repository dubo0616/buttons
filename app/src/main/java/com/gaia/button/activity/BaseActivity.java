package com.gaia.button.activity;

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

    private void requestPermissions(final String[] permissions, boolean needsMessage) {
        // the permissions management has only to be performed for Android 6 (API 23) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (needsMessage) {
                // the user needs to be informed, we prompt a message
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.alert_permissions_title);
                builder.setMessage(R.string.alert_permissions_message);
                builder.setCancelable(false); // the user cannot cancel the dialog with native back button
                builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // when the user taps "OK" we request the needed permissions
                        ActivityCompat.requestPermissions(BaseActivity.this, permissions,
                                Consts.ACTION_REQUEST_PERMISSIONS);
                    }
                });
                builder.show();
            } else {
                // no need to inform the user, we directly request the permissions
                ActivityCompat.requestPermissions(this, permissions, Consts.ACTION_REQUEST_PERMISSIONS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
