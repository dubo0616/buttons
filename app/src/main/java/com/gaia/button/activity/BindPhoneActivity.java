package com.gaia.button.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.PhoneCheckUtil;

public class BindPhoneActivity extends BaseActivity implements IUserListener,View.OnClickListener {

    private ImageView mBack;
    private TextView mTvLogin;
    private EditText mPhoneNumber;
    private EditText mPhoneCode;
    private TextView mTvGetcode;
    private TextView mTvServer;

    private String access_token;
    private String openId;
    private String nickName;
    private String avatar;
    private String sex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        access_token = intent.getStringExtra("access_token");
        openId = intent.getStringExtra("openId");
        nickName = intent.getStringExtra("nickName");
        avatar = intent.getStringExtra("avatar");
        sex = intent.getStringExtra("sex");
        setContentView(R.layout.activity_login_bind_phone);
        initView();
    }
    private void initView(){
        mBack=  findViewById(R.id.iv_back);
        mBack.setOnClickListener(this);
        mTvLogin = findViewById(R.id.tv_login);
        mTvLogin.setOnClickListener(this);
        mPhoneNumber = findViewById(R.id.et_phone);
        mPhoneCode = findViewById(R.id.et_phone_code);
        mTvGetcode = findViewById(R.id.tv_get_code);
        mTvGetcode.setOnClickListener(this);
        mTvServer = findViewById(R.id.tv_server);
        mPhoneNumber.addTextChangedListener(new TextWatcher() {
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
        mPhoneCode.addTextChangedListener(new TextWatcher() {
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
        if(mPhoneCode!= null && !TextUtils.isEmpty(mPhoneCode.getText())
                && mPhoneNumber!= null && !TextUtils.isEmpty(mPhoneNumber.getText())) {
            mTvLogin.setEnabled(true);
        }else{
            mTvLogin.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.tv_get_code:
                startResendCountDown();
                break;
        }

    }
    private void doLogin(){
        if(!PhoneCheckUtil.checkPhoneNum(mPhoneNumber.getText().toString())){
            Toast.makeText(this,"请检查手机号码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!PhoneCheckUtil.checkPhoneCode(mPhoneCode.getText().toString())){
            Toast.makeText(this,"请检查验证码",Toast.LENGTH_SHORT).show();
            return;

        }
        UserManager.getRequestHandler().requestLoginBindPhone(this, openId, access_token, nickName, avatar,mPhoneNumber.getText().toString(),mPhoneCode.getText().toString());
    }

    private boolean doSmsCode(){
        if(!PhoneCheckUtil.checkPhoneNum(mPhoneNumber.getText().toString())){
            Toast.makeText(this,"请检查手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        UserManager.getRequestHandler().requestGetCode(BindPhoneActivity.this,mPhoneNumber.getText().toString().trim());
        return true;
    }

    private void startResendCountDown() {
        if(!doSmsCode()){
            return;
        }
        mSmsHandler.removeMessages(RESEND_COUNT_DOWN);
        mCountDown = 60;
        mSmsHandler.sendEmptyMessageDelayed(RESEND_COUNT_DOWN, 1000L);
    }
    private void resendCountDown() {
        mCountDown--;
        if (mCountDown <= 0) {
            doSmsCode();
            mTvGetcode.setEnabled(true);
            mTvGetcode.setText(getString(R.string.login_get_phone_code));
        } else {
            mTvGetcode.setEnabled(false);
            mTvGetcode.setText(getString(R.string.resent_phone_code, mCountDown));

            mSmsHandler.sendEmptyMessageDelayed(RESEND_COUNT_DOWN, 1000L);
        }
    }
    private int mCountDown;
    private static final int RESEND_COUNT_DOWN = 1;
    private Handler mSmsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isFinishing() || isDestroyed() || msg == null) {
                return;
            }
            switch (msg.what) {
                case RESEND_COUNT_DOWN:
                    resendCountDown();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(requestTag == ConstantUtil.Net_Tag_User_WechatBindPhone) {
            if(data instanceof AccountInfo){
                AccountInfo info = (AccountInfo) data;
                PreferenceManager.getInstance().save(info);
                if(info.isSetPassword()) {
                    Intent intent = new Intent(BindPhoneActivity.this, MainActivity.class);
                    intent.putExtra("Tab", 1);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(BindPhoneActivity.this, PhoneSetPassActivity.class);
                    startActivity(intent);
                }
                finish();
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
