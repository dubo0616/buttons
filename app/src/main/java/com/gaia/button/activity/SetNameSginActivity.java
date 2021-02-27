package com.gaia.button.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_GSetName;
import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_SetSgin;

public class SetNameSginActivity extends BaseActivity implements View.OnClickListener, IUserListener {
    public static final String TYPE = "type";
    private int type=0;//0签名1昵称
    private EditText mEtText;
    private TextView mTvTitle,mTvSumbit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_sgin);
        type= getIntent().getIntExtra(TYPE,0);
        initView();

    }
    private void initView(){
        mEtText = findViewById(R.id.et_text);
        mTvTitle = findViewById(R.id.tv_title);
        mTvSumbit = findViewById(R.id.tv_sumit);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mTvSumbit.setOnClickListener(this);
        if(type == 1){
            mEtText.setHint("请输入你的昵称");
            mTvTitle.setText("更改昵称");
        }
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
        switch (v.getId()){
            case R.id.tv_sumit:
                UserManager.getRequestHandler().setNameSgin(SetNameSginActivity.this,type,mEtText.getText().toString());
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        if(Net_Tag_User_SetSgin== requestTag){
            PreferenceManager.getInstance().getAccountInfo().setPerson_sign(mEtText.getText().toString());
            PreferenceManager.getInstance().setStringValue(PreferenceManager.ACC_LOGIN_PERSON_SGIN,mEtText.getText().toString());
            showTotast("签名设置成功");
        } else if(Net_Tag_User_GSetName== requestTag){
            showTotast("昵称设置成功");
            PreferenceManager.getInstance().getAccountInfo().setPerson_name(mEtText.getText().toString());
            PreferenceManager.getInstance().setStringValue(PreferenceManager.ACC_LOGIN_PERSON_NAME,mEtText.getText().toString());

        }
        Intent idata = new Intent();
        idata.putExtra(TYPE,type);
        setResult(Activity.RESULT_OK,idata);
        finish();

    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        if(Net_Tag_User_SetSgin== requestTag){
            showTotast("签名设置失败");
        } else if(Net_Tag_User_GSetName== requestTag){
            showTotast("昵称设置失败");
        }
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
