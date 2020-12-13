package com.gaia.button.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.view.LoginBtn;

/***
 * 主登录页面
 */
public class LoginMainActivity extends BaseActivity implements View.OnClickListener{
    private LoginBtn mWechatLoginBtn;
    private LoginBtn mPhoneLoginBtn;
    private ConstraintLayout mClLayout;
    private TextView mTvKnow;
    private ImageView mIvClose;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        initView();
    }
    private void initView(){
        mClLayout = findViewById(R.id.cl_per);
        mTvKnow = findViewById(R.id.tv_know);
        mIvClose = findViewById(R.id.iv_close);
        mWechatLoginBtn = findViewById(R.id.btn_login_wechat);
        mPhoneLoginBtn = findViewById(R.id.btn_login_phone);
        mPhoneLoginBtn.setBtnLoginIcon(R.drawable.icon_phone_login);
        mPhoneLoginBtn.setBtnLoginTextStyle(Color.WHITE,R.string.login_phone);
        mWechatLoginBtn.setBtnLoginTextStyle(Color.WHITE,R.string.login_wechat);
        mPhoneLoginBtn.setOnClickListener(this);
        mWechatLoginBtn.setOnClickListener(this);
        if(PreferenceManager.getInstance().isLogin()){
            Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
            intent.putExtra("Tab", 1);
            startActivity(intent);
            finish();
        }
        if(!PreferenceManager.getInstance().getFristInstall()){
            mClLayout.setVisibility(View.VISIBLE);
        }else{
            mClLayout.setVisibility(View.GONE);
        }
        mTvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                PreferenceManager.getInstance().setFristInstall();
                mClLayout.setVisibility(View.GONE);
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_wechat:
                handleWechatLogin();
                break;
            case R.id.btn_login_phone:
                handlePhoneLogin();
                break;
        }
    }
    private void handleWechatLogin(){

    }
    private void handlePhoneLogin(){
        Intent intent = new Intent(this,PhoneLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
