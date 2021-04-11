package com.gaia.button.fargment;

import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.GaiaApplication;
import com.gaia.button.R;
import com.gaia.button.activity.CheckAppActivity;
import com.gaia.button.activity.AccountActivity;
import com.gaia.button.activity.CustomerActivity;
import com.gaia.button.activity.FeedBackActivity;
import com.gaia.button.activity.LoginMainActivity;
import com.gaia.button.activity.WebViewActivity;
import com.gaia.button.adapter.PersonalSettingAdapter;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AutoplayModel;
import com.gaia.button.model.UpdateModel;
import com.gaia.button.net.NetConfig;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.BaseUtils;
import com.gaia.button.utils.Config;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.ButtonsInstaller;
import com.gaia.button.view.UpdateInfoDialog;

public class PersonalSettingFragment extends BaseFragment implements PersonalSettingAdapter.OnItemListener, IUserListener {
    private View mRootView;
//    private RecyclerView mRecyclerView;
//    private List<PersonalSettingModel> mList = new ArrayList<>();
//    private PersonalSettingAdapter mSettingAdapter;
    private TextView mTvLoginout;
    private ConstraintLayout mCLAbout,mClCustomer,mClAccount,mClUpdate,mClPrivate,mClUser ,mClDownLoad,mClFeedBack;
    private ImageView mAutoPlay,mDownLoad;
    public static final int UPDATE_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 11001;
    public static final int UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE = 11002;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_personal_setting, container, false);
            initView();
        }
        return mRootView;
    }
    private void initView(){
        mTvLoginout = mRootView.findViewById(R.id.tv_login_out);
        mTvLoginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetConfig.isGet = true;
                UserManager.getRequestHandler().requestLoginOut(PersonalSettingFragment.this);
            }
        });
        mCLAbout = mRootView.findViewById(R.id.cl_about);
        mCLAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), CheckAppActivity.class));
            }
        });
        mClCustomer = mRootView.findViewById(R.id.cl_kehu);
        mClCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerActivity.class));

            }
        });
        mClAccount = mRootView.findViewById(R.id.cl_account_info);
        mClAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AccountActivity.class));
            }
        });

        mClUpdate = mRootView.findViewById(R.id.cl_update);
        mClUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checkAPP();
                startActivity(new Intent(getActivity(), CheckAppActivity.class));

            }
        });
        mAutoPlay = mRootView.findViewById(R.id.iv_auto_play);
        mAutoPlay.setSelected(PreferenceManager.getInstance().getAccountInfo().getAutoplay() ==1);
        mAutoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAutoPlay.isSelected()){
                    mAutoPlay.setSelected(false);
                }else{
                    mAutoPlay.setSelected(true);

                }
                UserManager.getRequestHandler().requestSetAutoPlay(PersonalSettingFragment.this,mAutoPlay.isSelected()?1:2);

            }
        });
        mClPrivate = mRootView.findViewById(R.id.cl_yinsi);
        mClPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL_KEY,ConstantUtil.PRIVATE_URL);
                intent.putExtra(WebViewActivity.TITLE_KEY,"隐私条款");
                startActivity(intent);
            }
        });
        mClUser = mRootView.findViewById(R.id.cl_xieyi);
        mClUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL_KEY,ConstantUtil.USER_URL);
                intent.putExtra(WebViewActivity.TITLE_KEY,"使用协议");
                startActivity(intent);
            }
        });
        mDownLoad = mRootView.findViewById(R.id.iv_download_play);
        mDownLoad.setSelected(PreferenceManager.getInstance().getIntValue(PreferenceManager.ACC_LOGIN_MOBILE_NETWORK)==1);
        mDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDownLoad.isSelected()){
                    mDownLoad.setSelected(false);
                }else{
                    mDownLoad.setSelected(true);

                }
                UserManager.getRequestHandler().requestSetAllowDown(PersonalSettingFragment.this,mDownLoad.isSelected()?1:2);

            }
        });
        mClFeedBack = mRootView.findViewById(R.id.cl_feddback);
        mClFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
            }
        });

    }
//    private void checkAPP(){
//        if(!BaseUtils.isWifiConnected(getActivity()) && (PreferenceManager.getInstance().getAccountInfo() != null && PreferenceManager.getInstance().getAccountInfo().getMobile_network() !=1)){
//            UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getContext(), new UpdateInfoDialog.OnConfirmClickListener() {
//                @Override
//                public void onConfirm() {
//                    if(isHasPermission()) {
//                        UserManager.getRequestHandler().requestUpdate(PersonalSettingFragment.this, getVersion());
//                    }
//                }
//            });
//            infoDialog.show();
//            infoDialog.setData("提示","当前使用移动网络,确认下载吗？","");
//          return;
//        }
//        if(isHasPermission()) {
//            UserManager.getRequestHandler().requestUpdate(PersonalSettingFragment.this, getVersion());
//        }
//    }
//    /**
//     * 获取版本号
//     *
//     * @return 当前应用的版本号
//     */
//    private String getVersion() {
//        if(getActivity() == null){
//            return "1.0";
//        }
//        try {
//            PackageManager manager = getActivity().getPackageManager();
//            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
//            String version = info.versionName;
//            return version;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "1.0";
//        }
//
//    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if (requestTag == ConstantUtil.Net_Tag_LogOut) {
            UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getContext(), new UpdateInfoDialog.OnConfirmClickListener() {
                @Override
                public void onConfirm() {
                    PreferenceManager.getInstance().setLoginOut();
                    GaiaApplication.getInstance().clearActivities();
                    Intent intent = new Intent(getActivity(), LoginMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            infoDialog.show();
            infoDialog.setData("提示","确定退出？","");
        }else if(requestTag == ConstantUtil.Net_Tag_User_AUTOPLAY){
            AutoplayModel model = (AutoplayModel) data;
            if(model != null){
                PreferenceManager.getInstance().getAccountInfo().setAutoplay(model.getAutoplay());
                PreferenceManager.getInstance().setIntValue(PreferenceManager.ACC_AUTO_PLAY,model.getAutoplay());
            }
            displayShortToast("设置成功");
        }else if(requestTag == ConstantUtil.Net_Tag_User_AUTODown){
            AutoplayModel model = (AutoplayModel) data;
            if(model != null){
                PreferenceManager.getInstance().getAccountInfo().setMobile_network(model.getMobile_network());
                PreferenceManager.getInstance().setIntValue(PreferenceManager.ACC_LOGIN_MOBILE_NETWORK,model.getMobile_network());
            }
            displayShortToast("设置成功");
        } else if(requestTag == ConstantUtil.Net_Tag_User_GetVersion){
            UpdateModel model = (UpdateModel) data;
//            checkUpdate("http://buttons.oss-cn-zhangjiakou.aliyuncs.com/app-apk/buttons-1608556167852700.apk");
            if(model.getIsUpdate() == 1 && !TextUtils.isEmpty(model.getUrl())){
                showUpdateDialog(model);
            }else {
                displayShortToast("已经是最新版本");
            }

        }
    }
    private void showUpdateDialog(UpdateModel model){
        if(getActivity() == null || isDetached() || model == null){
            return;
        }
        UpdateInfoDialog infoDialog = UpdateInfoDialog.getInstance(getActivity(), new UpdateInfoDialog.OnConfirmClickListener() {
            @Override
            public void onConfirm() {
                if (isHasPermission()) {
                    checkUpdate(model.getUrl());
                }
            }
        });
        infoDialog.show();
        infoDialog.setData("升级提示", model.getVersion(), model.getContent());
    }
    private boolean isLoading = false;
    private boolean isHasPermission(){
        int result = 0;
        //先检测权限
        result = ButtonsInstaller.requestWriteExternalStoragePermission(getActivity(), UPDATE_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
        if (0 != result) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            result = ButtonsInstaller.requestInstallThirdApkPermission(getActivity(), UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE);
            if (0 != result) {
                return false;
            }
        }
        return true;
    }
    /****
     * 检查app升级
     * @param url
     */
    private void checkUpdate(String url){
        if (!isLoading) {
            isLoading = true;

            String apkName = Config.splitFilePath(url);
            int apkIndex = apkName.indexOf(".apk");
            if(apkIndex < 0){
                displayShortToast("文件格式错误");
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

            DownloadManager  mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);

            final long enqueue = mDownloadManager.enqueue(request);
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    isLoading = false;
                    int result = ButtonsInstaller.installApk(getActivity(), fileName, UPDATE_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE, UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE);
                    if (0 != result) {
                        displayLongToast("安装失败");
                    }

                }
            };
            mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } else {
            displayShortToast("正在下载...");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_INSTALL_THIRD_PLATFORM_APK_PERMISSION_CODE) {
            PackageManager pm = getActivity().getPackageManager();
            boolean hasInstallPermission = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hasInstallPermission = pm.canRequestPackageInstalls();
                if (hasInstallPermission) {
//                    checkAPP();
                } else {
                  displayLongToast("没有开放第三方应用安装权限");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions.length <= 0) {
            return;
        }

        if (UPDATE_WRITE_EXTERNAL_STORAGE_PERMISSION_CODE == requestCode) {
            if (permissions[0].contentEquals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    checkAPP();
                } else {
                 displayLongToast("没有存储权限");
                }
            }
        }
    }
    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        if (requestTag == ConstantUtil.Net_Tag_LogOut) {
            displayShortToast("退出登录失败");
        }else if(requestTag == ConstantUtil.Net_Tag_User_AUTOPLAY){
            displayShortToast("设置失败");
        } else if(requestTag == ConstantUtil.Net_Tag_User_GetVersion){
            displayShortToast("网络异常");
        }
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
