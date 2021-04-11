package com.gaia.button.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.UpdateModel;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.BaseUtils;
import com.gaia.button.utils.ButtonsInstaller;
import com.gaia.button.utils.Config;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.view.UpdateInfoDialog;

public class CheckAppActivity extends BaseActivity implements IUserListener {
    public static final int UPDATE_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 11001;
    public static final int UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE = 11002;
    private TextView mTvVersion,mTvNewVersion;
    private ImageView mBack;
    private TextView mBtnConfirm;
    private UpdateModel updateModel;
    private ConstraintLayout mNewVersionLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mTvVersion = findViewById(R.id.tv_version);
        mBack = findViewById(R.id.iv_back);
        mBtnConfirm = findViewById(R.id.tv_login);
        mTvNewVersion = findViewById(R.id.tv_hversion);
        mNewVersionLayout =findViewById(R.id.cl_newversion);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateModel != null && updateModel.getIsUpdate() == 1 && !TextUtils.isEmpty(updateModel.getUrl())) {
                    if (!BaseUtils.isWifiConnected(CheckAppActivity.this) && (PreferenceManager.getInstance().getAccountInfo() != null && PreferenceManager.getInstance().getAccountInfo().getMobile_network() != 1)) {
                        UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(CheckAppActivity.this, new UpdateInfoDialog.OnConfirmClickListener() {
                            @Override
                            public void onConfirm() {
                                if (isHasPermission()) {
                                    checkUpdate(updateModel.getUrl());
                                }
                            }
                        });
                        infoDialog.show();
                        infoDialog.setData("提示", "当前使用移动网络,确认下载吗？", "");
                        return;
                    }
                    if (isHasPermission()) {
                        checkUpdate(updateModel.getUrl());
                    }
                } else {
                    finish();
                }
            }
        });
        mTvVersion.setText(getVersion());

        initData();

    }
    private boolean isLoading = false;
    private boolean isHasPermission(){
        int result = 0;
        //先检测权限
        result = ButtonsInstaller.requestWriteExternalStoragePermission(this, UPDATE_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
        if (0 != result) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = ButtonsInstaller.requestInstallThirdApkPermission(this, UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE);
            if (0 != result) {
                return false;
            }
        }
        return true;
    }
    private void checkUpdate(String url){
        if (!isLoading) {
            isLoading = true;

            String apkName = Config.splitFilePath(url);
            int apkIndex = apkName.indexOf(".apk");
            if(apkIndex < 0){
                showTotast("文件格式错误");
                return;
            }
            final String fileName = apkName.substring(0, apkIndex) + ".apk";

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(false);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName); // Environment.getExternalStoragePublicDirectory(String)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            // in order for this if to run, you must use the android 3.2 to
            // compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }

            DownloadManager  mDownloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

            final long enqueue = mDownloadManager.enqueue(request);
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    isLoading = false;
                    int result = ButtonsInstaller.installApk(CheckAppActivity.this, fileName, UPDATE_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE, UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE);
                    if (0 != result) {
                        showTotast("安装失败");
                    }

                }
            };
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } else {
            showTotast("正在下载...");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE) {
            PackageManager pm = this.getPackageManager();
            boolean hasInstallPermission = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hasInstallPermission = pm.canRequestPackageInstalls();
                if (hasInstallPermission) {
                    if(isHasPermission()){
                        checkUpdate(updateModel.getUrl());
                    }
                } else {
                    showTotast("没有开放第三方应用安装权限");
                }
            }
        }
    }
    private void initData(){
        UserManager.getRequestHandler().requestUpdate(this, getVersion());

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }

    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(requestTag == ConstantUtil.Net_Tag_User_GetVersion){
            updateModel = (UpdateModel) data;
            if(updateModel.getIsUpdate() == 1 && !TextUtils.isEmpty(updateModel.getUrl())){
                mBtnConfirm.setText("更新");
                mTvNewVersion.setText(updateModel.getVersion());
                mNewVersionLayout.setVisibility(View.VISIBLE);
            }else {
                mBtnConfirm.setText("确定");
            }

        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {

    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
