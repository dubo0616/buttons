package com.gaia.button.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.view.LoginBtn;

/***
 * 主登录页面
 */
public class LoginMainActivity extends BaseActivity implements View.OnClickListener{
    private LoginBtn mWechatLoginBtn;
    private LoginBtn mPhoneLoginBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        initView();
    }
    private void initView(){
        mWechatLoginBtn = findViewById(R.id.btn_login_wechat);
        mPhoneLoginBtn = findViewById(R.id.btn_login_phone);
        mPhoneLoginBtn.setBtnLoginIcon(R.drawable.icon_phone_login);
        mPhoneLoginBtn.setBtnLoginTextStyle(Color.WHITE,R.string.login_phone);
        mPhoneLoginBtn.setOnClickListener(this);
        mWechatLoginBtn.setOnClickListener(this);
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
    }
}
