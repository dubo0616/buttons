package com.gaia.button.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.R;
import com.gaia.button.fargment.PersonalSettingFragment;
import com.gaia.button.model.UpdateModel;
import com.gaia.button.net.BaseResult;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.ConstantUtil;

public class AboutActivity extends BaseActivity implements IUserListener {
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
                if(updateModel != null && updateModel.getIsUpdate() == 1 && !TextUtils.isEmpty(updateModel.getUrl())){
                }else {
                    finish();
                }
            }
        });
        mTvVersion.setText(getVersion());

        initData();

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
