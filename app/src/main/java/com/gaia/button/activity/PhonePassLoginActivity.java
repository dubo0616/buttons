package com.gaia.button.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.net.BaseResult;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.PhoneCheckUtil;

public class PhonePassLoginActivity extends BaseActivity implements View.OnClickListener, IUserListener {

    private ImageView mBack;
    private TextView mTvLogin;
    private EditText mPhoneNumber;
    private EditText mPhoneCode;
    private TextView mTvServer;
    private CheckBox mBox;
    private TextView mTvFoget;
    private TextView mTvPass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_password);
        initView();
    }
    private void initView(){
        mBack=  findViewById(R.id.iv_back);
        mBack.setOnClickListener(this);
        mTvLogin = findViewById(R.id.tv_login);
        mTvLogin.setOnClickListener(this);
        mPhoneNumber = findViewById(R.id.et_phone);
        mPhoneCode = findViewById(R.id.et_phone_code);
        mTvServer = findViewById(R.id.tv_server);
        mBox = findViewById(R.id.cb_box);
        mTvFoget = findViewById(R.id.tv_forget);
        mTvFoget.setOnClickListener(this);
        init(mTvServer);
        mTvPass = findViewById(R.id.tv_pass_login);
        mTvPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhonePassLoginActivity.this,PhoneLoginActivity.class));
                finish();
            }
        });
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
    private void init(TextView textView){
        ForegroundColorSpan defaultTextColorSpan = new ForegroundColorSpan(Color.parseColor("#949494")); // 默认文本颜色
        String termsOfUse = getResources().getString(R.string.login_server_one);
        String privacyPolicy =getResources().getString(R.string.login_server_two);
        String tips = getResources().getString(R.string.login_server);
        SpannableString spannableStr = new SpannableString(tips);
        spannableStr.setSpan(defaultTextColorSpan, 0, tips.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int indexOfTermsOfUse = tips.indexOf(termsOfUse);
        if (indexOfTermsOfUse >= 0) {
            spannableStr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#FA4421"));
                    ds.setUnderlineText(false);
                }
                @Override
                public void onClick(View widget) {
                }
            }, indexOfTermsOfUse, indexOfTermsOfUse + termsOfUse.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        int indexOfPrivacyPolicy = tips.indexOf(privacyPolicy);
        if (indexOfPrivacyPolicy >= 0) {
            spannableStr.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#FA4421"));
                    ds.setUnderlineText(false);
                }

                @Override
                public void onClick(View widget) {
                }
            }, indexOfPrivacyPolicy, indexOfPrivacyPolicy + privacyPolicy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setText(spannableStr);
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
            case R.id.tv_forget:
                startActivity(new Intent(PhonePassLoginActivity.this,ForgetActivity.class));
                break;
        }

    }
    private void doLogin(){
        if(!PhoneCheckUtil.checkPhoneNum(mPhoneNumber.getText().toString())){
            Toast.makeText(this,"请检查手机号码",Toast.LENGTH_SHORT).show();
            return;
        }
//        if(!PhoneCheckUtil.checkPhonePass(mPhoneCode.getText().toString())){
//            Toast.makeText(this,"请检查验证码",Toast.LENGTH_SHORT).show();
//            return;
//
//        }
        if(!mBox.isChecked()){
            Toast.makeText(this,"请选择协议",Toast.LENGTH_SHORT).show();
            return;
        }
        UserManager.getRequestHandler().requestLoginByPwd(PhonePassLoginActivity.this,mPhoneNumber.getText().toString().trim(),mPhoneCode.getText().toString().trim());
    }

    private boolean doSmsCode(){
        if(!PhoneCheckUtil.checkPhoneNum(mPhoneNumber.getText().toString())){
            Toast.makeText(this,"请检查手机号码",Toast.LENGTH_SHORT).show();
            return false;
        }
        UserManager.getRequestHandler().requestGetCode(PhonePassLoginActivity.this,mPhoneNumber.getText().toString().trim());
        return true;
    }



    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(requestTag == ConstantUtil.Net_Tag_UserLogin_Pwd) {

            if(data instanceof AccountInfo){
                AccountInfo info = (AccountInfo) data;
                PreferenceManager.getInstance().save(info);
                Intent intent = new Intent(PhonePassLoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Tab", 1);
                startActivityForResult(intent,1000);
                finish();
            }

        }

    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {

        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
