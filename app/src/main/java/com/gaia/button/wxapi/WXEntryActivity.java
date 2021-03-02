package com.gaia.button.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gaia.button.GaiaApplication;
import com.gaia.button.net.NetworkUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static com.gaia.button.activity.LoginMainActivity.ACTION_AUTH_WEIXIN;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, GaiaApplication.WX_APPID,false);
        //将你收到的intent和实现IWXAPIEventHandler接口的对象传递给handleIntent方法
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        Log.e("NNNNN","==============="+baseResp.errCode);
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if(baseResp instanceof SendAuth.Resp ) {
                    String code = ((SendAuth.Resp) baseResp).code;
                    requestOtherInfo(code);
                }else{
//                    Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "登录取消";
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "拒绝授权";
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                result = "登录失败";
//                Toast.makeText(this, result, Toast.LENGTH_LONG).show();

                finish();
                break;
        }
    }

    private String access_token = "";
    private String openId = "";

    private String nickName = "";
    private String avatar = "";
    private String sex = "";

    //log use
    private String errorMsg = "";
    private String resultStr = "";

    private void requestOtherInfo(final String code) {
        new Thread() {
            public void run() {
                String uriAPI = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + GaiaApplication.WX_APPID
                        + "&secret="
                        + GaiaApplication.WX_APPSECRET
                        + "&code="
                        + code + "&grant_type=authorization_code";
                /* 建立HTTP Get对象 */
                HttpGet httpRequest = new HttpGet(uriAPI);
                try {/* 发送请求并等待响应 */
                    HttpResponse httpResponse = new DefaultHttpClient()
                            .execute(httpRequest);

                    Log.e("TTTTTT","======="+httpResponse.getStatusLine().getStatusCode());
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        String strResult = EntityUtils.toString(httpResponse
                                .getEntity(), "utf-8");
                        resultStr = strResult;
                        JSONObject obj = new JSONObject(strResult);
                        access_token = obj.optString("access_token");
                        openId = obj.optString("openid");

                        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="
                                + access_token + "&openid=" + openId;
                        HttpGet httpRequestUserInfo = new HttpGet(userInfoUrl);
                        HttpResponse httpResponseUserInfo = new DefaultHttpClient()
                                .execute(httpRequestUserInfo);
                        if (httpResponseUserInfo.getStatusLine()
                                .getStatusCode() == 200) {
                            String strResultUserInfo = EntityUtils
                                    .toString(httpResponseUserInfo.getEntity(), "utf-8");
                            resultStr = strResultUserInfo;
                            if (!TextUtils.isEmpty(strResultUserInfo)) {
								JSONObject data = new JSONObject(strResultUserInfo);
                                nickName = data.optString("nickname");
                                avatar = data.optString("headimgurl");
                                sex = data.optString("sex");
                                mHandler.sendEmptyMessage(100);
                            }
                        } else {
                            errorMsg = httpResponseUserInfo.getStatusLine().getStatusCode() + "";
                            mHandler.sendEmptyMessage(101);

                        }
                    } else {
                        errorMsg = httpResponse.getStatusLine().getStatusCode() + "";
                        mHandler.sendEmptyMessage(101);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorMsg = e.getMessage();
                    mHandler.sendEmptyMessage(101);
                }

            };
        }.start();

    }

    Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            Intent i = new Intent();
            i.setAction(ACTION_AUTH_WEIXIN);
            if (msg.what == 100) {
                i.putExtra("name", nickName);
                i.putExtra("openId", openId);
                i.putExtra("avatar", avatar);
                i.putExtra("access_token", access_token);
                i.putExtra("sex", sex);
            } else {
                i.putExtra("openId", "");
                i.putExtra("name", "");
                i.putExtra("avatar", "");
                i.putExtra("access_token", "");

            }
            WXEntryActivity.this.sendBroadcast(i);
            WXEntryActivity.this.finish();
        };
    };

}

