package com.gaia.button.activity;

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

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_UpdatePassword;

public class ModPasswordActivity extends BaseActivity implements View.OnClickListener, IUserListener {
    private EditText mPassold,mPassnew,mPassConfirm;
    private TextView mTvLogin;
    private View mViewPassShow,mViewPassShowTwo,mViewPassShowThree;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_password);
        mPassold= findViewById(R.id.et_phone_new_pass);
        mPassnew = findViewById(R.id.et_phone_new_pass_new);
        mPassConfirm = findViewById(R.id.et_phone_confir_pass);
        mTvLogin = findViewById(R.id.tv_login);
        mViewPassShow = findViewById(R.id.tv_pass_show);
        mViewPassShowTwo = findViewById(R.id.tv_pass_show_two);
        mViewPassShowThree = findViewById(R.id.tv_pass_show_three);
        mViewPassShow.setOnClickListener(this);
        mViewPassShowTwo.setOnClickListener(this);
        mViewPassShowThree.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                &&!TextUtils.isEmpty(mPassConfirm.getText().toString())&& !TextUtils.isEmpty(mPassold.getText().toString())) {
            mTvLogin.setEnabled(true);
        }else{
            mTvLogin.setEnabled(false);
        }

    }
    boolean isPassShow,isPassShowTwo,isPassShowThree;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.tv_pass_show:
                if(isPassShow){
                    mPassold.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPassShow = false;
                }else {
                    mPassold.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPassShow = true;
                }
                mPassold.setSelection(mPassold.getText().length());
                break;
            case R.id.tv_pass_show_two:
                if(isPassShowTwo){
                    mPassnew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPassShowTwo = false;
                }else {
                    mPassnew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPassShowTwo = true;
                }
                mPassnew.setSelection(mPassnew.getText().length());
                break;
            case R.id.tv_pass_show_three:
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

        if(TextUtils.isEmpty(mPassold.getText().toString())){
            Toast.makeText(this,"请输入旧密码",Toast.LENGTH_SHORT).show();
            return;

        }
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
        showWaitDialog();
        UserManager.getRequestHandler().requestModPass(ModPasswordActivity.this, mPassold.getText().toString(),mPassnew.getText().toString(),mPassConfirm.getText().toString());
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        hideWaitDialog();
        if(Net_Tag_User_UpdatePassword == requestTag){
            Toast.makeText(ModPasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        hideWaitDialog();
        Toast.makeText(ModPasswordActivity.this,errorMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
