package com.gaia.button.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.utils.ConstantUtil;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener,IUserListener {
    private EditText mEtText;
    private TextView mTvTitle,mTvSumbit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }
    private void initView(){
        mEtText = findViewById(R.id.et_text);
        mTvTitle = findViewById(R.id.tv_title);
        mTvSumbit = findViewById(R.id.tv_sumit);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mTvSumbit.setOnClickListener(this);
        mEtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTvSumbit.setEnabled(s.length() > 0?true:false);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sumit:
                UserManager.getRequestHandler().requestFeedBack(FeedBackActivity.this,mEtText.getText().toString());
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(ConstantUtil.Net_Tag_User_FeedBack == requestTag){
            showTotast("问题反馈成功");
            finish();
        }

    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        if(ConstantUtil.Net_Tag_User_FeedBack == requestTag){
            showTotast("网络异常，稍候重试");
        }
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
