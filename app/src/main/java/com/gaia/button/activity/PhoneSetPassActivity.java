package com.gaia.button.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.PhoneCheckUtil;

public class PhoneSetPassActivity extends BaseActivity implements View.OnClickListener, IUserListener {

    private EditText mPassnew,mPassConfirm;
    private TextView mTvLogin;
    private View mViewPassShow,mViewPassShowTwo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass);
        initView();
    }
    private void initView(){
        mPassnew = findViewById(R.id.et_phone_new_pass);
        mPassConfirm = findViewById(R.id.et_phone_confir_pass);
        mTvLogin = findViewById(R.id.tv_login);
        mViewPassShow = findViewById(R.id.tv_pass_show);
        mViewPassShowTwo = findViewById(R.id.tv_pass_show_two);
        mViewPassShow.setOnClickListener(this);
        mViewPassShowTwo.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mPassnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtnStatus();
            }
        });
        mPassConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkBtnStatus();
            }
        });
    }
    private void checkBtnStatus(){
        if(!TextUtils.isEmpty(mPassnew.getText().toString())
                &&!TextUtils.isEmpty(mPassConfirm.getText().toString())) {
            mTvLogin.setEnabled(true);
        }else{
            mTvLogin.setEnabled(false);
        }

    }
    boolean isPassShow,isPassShowTwo;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.tv_pass_show:
                if(isPassShow){
                    mPassnew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPassShow = false;
                }else {
                    mPassnew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPassShow = true;
                }
                mPassnew.setSelection(mPassnew.getText().length());
                break;
            case R.id.tv_pass_show_two:
                if(isPassShowTwo){
                    mPassConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPassShowTwo = false;
                }else {
                    mPassConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPassShowTwo = true;
                }
                mPassConfirm.setSelection(mPassConfirm.getText().length());
                break;
        }
    }
    private void doLogin(){

        if(TextUtils.isEmpty(mPassnew.getText().toString())){
            Toast.makeText(this,"请输入新密码",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(mPassConfirm.getText().toString())){
            Toast.makeText(this,"请输入确认密码",Toast.LENGTH_SHORT).show();
            return;

        }
        if(!TextUtils.equals(mPassnew.getText().toString(),mPassConfirm.getText().toString())){
            Toast.makeText(this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
            return;

        }
//        if(!mBox.isChecked()){
//            Toast.makeText(this,"请选择协议",Toast.LENGTH_SHORT).show();
//            return;
//        }
        UserManager.getRequestHandler().requestSetPass(PhoneSetPassActivity.this, mPassnew.getText().toString(),mPassConfirm.getText().toString());
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(requestTag == ConstantUtil.Net_Tag_User_Login_SETPASS) {
            showTotast("密码设置成功");
            Intent intent = new Intent(PhoneSetPassActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("Tab", 1);
            startActivity(intent);
            finish();
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
