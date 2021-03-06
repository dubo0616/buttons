package com.gaia.button.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.GaiaApplication;
import com.gaia.button.R;
import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.net.user.IUserListener;
import com.gaia.button.net.user.UserManager;
import com.gaia.button.net.user.UserRequestProxy;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.view.LoginBtn;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import static com.gaia.button.utils.ConstantUtil.Net_Tag_User_WechatLogin;

/***
 * 主登录页面
 */
public class LoginMainActivity extends BaseActivity implements View.OnClickListener,IUserListener {
    private LoginBtn mWechatLoginBtn;
    private LoginBtn mPhoneLoginBtn;
    private ConstraintLayout mClLayout,ClAllow;
    private TextView mTvKnow,tv_add_allow,tv_add_exit,tv_content;
    private ImageView mIvClose;
    /**
     * 微信授权结果的广播
     */
    public final static String ACTION_AUTH_WEIXIN = "action_auth_weixin";
    private BroadcastReceiver mReceiverWeixinLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        initView();
    }
    private void initView(){
        mClLayout = findViewById(R.id.cl_per);
        tv_add_allow = findViewById(R.id.tv_add_allow);
        tv_content = findViewById(R.id.tv_content);
        tv_add_exit = findViewById(R.id.tv_add_exit);
        ClAllow = findViewById(R.id.cl_allow);
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Tab", 1);
            startActivity(intent);
            finish();
            return;
        }

        if(!PreferenceManager.getInstance().getFristInstall()){
            ClAllow.setVisibility(View.VISIBLE);
        }else{
            mClLayout.setVisibility(View.GONE);
            ClAllow.setVisibility(View.GONE);
        }
        init(tv_content);
        tv_add_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClAllow.setVisibility(View.GONE);
                mClLayout.setVisibility(View.VISIBLE);

            }
        });
        mTvKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                PreferenceManager.getInstance().setFristInstall();
                mClLayout.setVisibility(View.GONE);
            }
        });
        tv_add_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void init(TextView textView){
        ForegroundColorSpan defaultTextColorSpan = new ForegroundColorSpan(Color.parseColor("#1E1E1E")); // 默认文本颜色
        final String termsOfUse = getResources().getString(R.string.login_server_one);
        final String privacyPolicy =getResources().getString(R.string.login_server_two);
        String tips = getResources().getString(R.string.start_app);
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
                    Intent intent = new Intent(LoginMainActivity.this, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL_KEY, ConstantUtil.USER_URL);
                    intent.putExtra(WebViewActivity.TITLE_KEY,termsOfUse);
                    startActivity(intent);
                }
            }, indexOfTermsOfUse-1, indexOfTermsOfUse + termsOfUse.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                    Intent intent = new Intent(LoginMainActivity.this, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL_KEY,ConstantUtil.PRIVATE_URL);
                    intent.putExtra(WebViewActivity.TITLE_KEY,privacyPolicy);
                    startActivity(intent);
                }
            }, indexOfPrivacyPolicy-1, indexOfPrivacyPolicy + privacyPolicy.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
        textView.setText(spannableStr);
    }
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
        Log.e("MMM","11111111111111111");
         access_token = intent.getStringExtra("access_token");
         openId = intent.getStringExtra("openId");
         nickName = intent.getStringExtra("nickName");
         avatar = intent.getStringExtra("avatar");
         sex = intent.getStringExtra("sex");
         UserManager.getRequestHandler().requestLoginWeixin(this, openId, access_token, nickName, avatar);


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

    private void handleWechatLogin() {
        showWaitDialog();
        IWXAPI api = GaiaApplication.getWeinAPIHandler(this);
        if (!api.isWXAppInstalled()) {
            showTotast("请安装微信客户端");
            return;
        }
        registerReceiver();
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.state = "com.gaia.button";
        api.sendReq(req);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiverWeixinLogin != null){
            unregisterReceiver(mReceiverWeixinLogin);
        }
    }

    private void handlePhoneLogin(){
        Intent intent = new Intent(this,PhoneLoginActivity.class);
        startActivityForResult(intent,1000);
    }

    @Override
    public void onRequestSuccess(int requestTag, Object data) {
        hideWaitDialog();
        if(requestTag == Net_Tag_User_WechatLogin){
            if(data != null && data instanceof AccountInfo) {
                AccountInfo info = (AccountInfo) data;
                PreferenceManager.getInstance().save(info);
                Intent intent = new Intent(LoginMainActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Tab", 1);
                startActivity(intent);
                finish();
            }else{
                Intent i = new Intent(LoginMainActivity.this, BindPhoneActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000) {
            finish();
        }
    }

    @Override
    public void onRequestError(int requestTag, int errorCode, String errorMsg, Object data) {
        hideWaitDialog();
        if(requestTag == Net_Tag_User_WechatLogin){
            if(errorCode == 1200){
                Intent intent = new Intent(LoginMainActivity.this, BindPhoneActivity.class);
                startActivity(intent);
                finish();
            }
        }
//        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startProgressDialog(int requestTag) {

    }

    @Override
    public void endProgressDialog(int requestTag) {

    }
}
