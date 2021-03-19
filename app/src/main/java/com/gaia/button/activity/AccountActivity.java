package com.gaia.button.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.GaiaApplication;
import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_WechatLogin;

public class AccountActivity extends BaseActivity implements IUserListener {
    private ImageView mBack;
    private ConstraintLayout mClAccount,mClWeixin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mBack = findViewById(R.id.iv_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mClAccount = findViewById(R.id.cl_account_info);
        mClAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this,ModPasswordActivity.class));

            }
        });
        mClWeixin = findViewById(R.id.cl_account_weixin);
        final boolean isBind = PreferenceManager.getInstance().getAccountInfo() != null && PreferenceManager.getInstance().getAccountInfo().getIs_openid() ==1;
        if(isBind){
            findViewById(R.id.bind_weichat).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.bind__into).setVisibility(View.VISIBLE);
        }
        mClWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBind){
                    return;
                }
                handleWechatLogin();
            }
        });

    }

    private void handleWechatLogin() {
        showWaitDialog();
        IWXAPI api = GaiaApplication.getWeinAPIHandler(this);
        if (!api.isWXAppInstalled()) {
            showTotast(getResources().getString(R.string.install_weichat));
            return;
        }
        registerReceiver();
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.state = "com.gaia.button";
        api.sendReq(req);

    }
    /**
     * 微信授权结果的广播
     */
    public final static String ACTION_AUTH_WEIXIN = "action_auth_weixin";
    private BroadcastReceiver mReceiverWeixinLogin;
    private void registerReceiver() {
        if (mReceiverWeixinLogin == null) {
            mReceiverWeixinLogin = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(ACTION_AUTH_WEIXIN)) {
                        broadcastAuthWeixin(intent);
                    }
                }
            };
            final IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_AUTH_WEIXIN);
            registerReceiver(mReceiverWeixinLogin, filter);
        }
    }
    private String access_token;
    private String openId;
    private String nickName;
    private String avatar;
    private String sex;
    private void broadcastAuthWeixin(Intent intent) {
        if(intent == null){
            return;
        }
        access_token = intent.getStringExtra("access_token");
        openId = intent.getStringExtra("openId");
        nickName = intent.getStringExtra("nickName");
        avatar = intent.getStringExtra("avatar");
        sex = intent.getStringExtra("sex");
        UserManager.getRequestHandler().requestLoginWeixin(this, openId, access_token, nickName, avatar);


    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        hideWaitDialog();
        if(requestTag == Net_Tag_User_WechatLogin){
            if(data != null && data instanceof AccountInfo) {
                AccountInfo info = (AccountInfo) data;
                PreferenceManager.getInstance().save(info);
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Tab", 1);
                startActivity(intent);
                finish();
            }else{
                Intent i = new Intent(AccountActivity.this, BindPhoneActivity.class);
                i.putExtra("name", nickName);
                i.putExtra("openId", openId);
                i.putExtra("avatar", avatar);
                i.putExtra("access_token", access_token);
                i.putExtra("sex", sex);
                startActivity(i);
                finish();
            }

        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        hideWaitDialog();
        if(requestTag == Net_Tag_User_WechatLogin){
            if(errorCode == 1200){
                Intent intent = new Intent(AccountActivity.this, BindPhoneActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
