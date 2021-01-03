package com.gaia.button.net.user;

import android.text.TextUtils;
import android.widget.TextView;


import com.gaia.button.data.PreferenceManager;
import com.gaia.button.net.INetListener;
import com.gaia.button.net.NetManager;
import com.gaia.button.net.builder.HttpSubmitDataManager;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.StringConstant;
import com.gaia.button.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 接口代理
 *  <p>
 * 该类只建议提供给UserImpl类使用，外部其他类请通过{@link }的 getRequestHandler() 方法来使用)
 * Created by larry on 2017/4/12.
 */
public class UserRequestProxy implements IUserInterface {

    private static UserRequestProxy instance = new UserRequestProxy();
    private UserRequestProxy (){
        mObservers = new HashMap<Integer,IUserListener>();
        mRequestPageNums = new HashMap<Integer, Integer>();
    }
    public static UserRequestProxy getInstance() {
        return instance;
    }

    private HashMap<Integer, IUserListener> mObservers;
    /** 保存页面查询的起始位置 */
    private HashMap<Integer, Integer> mRequestPageNums;

    private INetListener iNetListener = null;

    private int mCurrentRequestId = 0;

    public INetListener getINetListener() {
        return iNetListener;
    }

    public void setINetListener(INetListener iNetListener) {
        this.iNetListener = iNetListener;
    }

    public IUserListener getObservers(int requestId){
        return mObservers.get(requestId);
    }

    public void addObserver(IUserListener observer, int requestId) {
        mObservers.put(requestId, observer);
    }

    public void removeObserver(int requestId) {
        mObservers.remove(requestId);
    }

    public HashMap<Integer, Integer> getRequestPageNums () {
        return mRequestPageNums;
    }

    public void handleSuccessResult(int requestId, final int requestType,
                                    final Object data, String responseStr) {
        IUserListener _listener = (IUserListener) getObservers(requestId);
        if (_listener != null) {
            _listener.onRequestSuccess(requestType, data);
            removeObserver(requestId);
        } else {
            _listener = (IUserListener) getObservers(requestType);
            if (_listener != null) {
                _listener.onRequestSuccess(requestType, data);
            }
            removeObserver(requestType);
        }
    }

    public void handleFailedResult(int requestId, final int requestType,
                                   final int errorCode, final String errorMsg, Object responseData) {
        IUserListener _listener = (IUserListener) getObservers(requestId);
        if (_listener != null) {
            _listener.onRequestError(requestType, errorCode, errorMsg, responseData);
            removeObserver(requestId);
        } else {
            _listener = (IUserListener) getObservers(requestType);
            if(_listener != null) {
                _listener.onRequestError(requestType, errorCode, errorMsg, responseData);
            }
            removeObserver(requestType);
        }
    }

    public void requestLogout(IUserListener observer) {
        //先发送用户退出请求
        if (PreferenceManager.getInstance().getAccountInfo() == null) {
            return;
        }
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_LOGOUT);
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL, ConstantUtil.Net_Tag_LogOut, str, iNetListener);
        addObserver(observer, mCurrentRequestId);

        handleSuccessResult(mCurrentRequestId, ConstantUtil.Net_Tag_LogOut,
               PreferenceManager.getInstance().getAccountInfo(), "logout");

    }

    // new 510
    public void requestRegistWithPhoneNumber(IUserListener observer, String phonenumber, String msmcode) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_PHONE, phonenumber);
        params.put(StringConstant.JSON_USER_PHONE_MSM_CODE, msmcode);

        // 使用公共方法传递HashMap参数组合以及funName
        Object str = HttpSubmitDataManager.getBuilder().buildDefaultString(
                ConstantUtil.SERVER_URL_NAME_REGISTER, params);

        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_NAME_DEFAULT + ConstantUtil.NEW_API_URL_SUFFIX,
                ConstantUtil.Net_Tag_RegistWithPhone, str, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    public void requestLoginBySms(IUserListener observer, String phonenum, String smscode) {
        // 将参数与key值对应放入HashMap中
        HashMap<String, String> params = new HashMap<>();
        params.put(StringConstant.JSON_USER_PHONE, phonenum);
        params.put(StringConstant.JSON_USER_PHONE_MSM_CODE, smscode);
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("mobile",phonenum);
            mJsonObject.put("verifyCode",smscode);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // 使用公共方法传递HashMap参数组合以及funName
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_LOGIN_SMS,
                ConstantUtil.Net_Tag_UserLogin_Sms, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    public void requestLoginByPwd(IUserListener observer, String phonenum, String pwd) {
        // 将参数与key值对应放入HashMap中
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("mobile",phonenum);
            mJsonObject.put("password",pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_PWDLOGIN,
                ConstantUtil.Net_Tag_UserLogin_Pwd, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);

    }

    @Override
    public void requestGetCode(IUserListener observer, String mobile) {
        // TODO Auto-generated method stub

        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("mobile",mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_SMSCODE,
                ConstantUtil.Net_Tag_User_Login_SEND_CODE, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestForgetPass(IUserListener observer, String mobile,String code,String pass,String passnew) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("mobile",mobile);
            mJsonObject.put("verifyCode",code);
            mJsonObject.put("password",pass);
            mJsonObject.put("ture_password",passnew);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_FORGETPASS,
                ConstantUtil.Net_Tag_User_Login_FORGETPASS, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);

    }

    @Override
    public void requestSetPass(IUserListener observer, String newpass, String confirm) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("password",newpass);
            mJsonObject.put("ture_password",confirm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_LOGINPWD_SET,
                ConstantUtil.Net_Tag_User_Login_SETPASS, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestModPass(IUserListener observer, String oldpa, String newpass, String confirm) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("old_password",oldpa);
            mJsonObject.put("password",newpass);
            mJsonObject.put("ture_password",confirm);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_LOGINPW_UPDATE,
                ConstantUtil.Net_Tag_User_UpdatePassword, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestgetDiscover(IUserListener observer, int page, String title) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("page",page+"");
            if(!TextUtils.isEmpty(title)) {
                mJsonObject.put("title", title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_ArticleList,
                ConstantUtil.Net_Tag_User_ArticleList, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestCollect(IUserListener observer, String id) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("id",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_ArticleCollect,
                ConstantUtil.Net_Tag_User_ArticleCollect, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);

    }

    @Override
    public void requestProductList(IUserListener observer, int page, String title) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("page",page+"");
            if(!TextUtils.isEmpty(title)) {
                mJsonObject.put("title", title);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_ProductList,
                ConstantUtil.Net_Tag_User_ProductList, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestGetCollectIUserListener(IUserListener observer) {
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_GetCollect,
                ConstantUtil.Net_Tag_User_GetCollect, null, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestGetDevice(IUserListener observer) {
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_GetDevice,
                ConstantUtil.Net_Tag_User_GetDevice, null, iNetListener);
        addObserver(observer, mCurrentRequestId);

    }

    @Override
    public void requestLoginOut(IUserListener observer) {
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_LOGOUT,
                ConstantUtil.Net_Tag_LogOut, null, iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestLoginWeixin(IUserListener observer, String openid, String access_token, String nickname, String avatar) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("openid",openid);
            mJsonObject.put("access_token",access_token);
            mJsonObject.put("nickname",nickname);
            mJsonObject.put("avatar",avatar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_WechatLogin,
                ConstantUtil.Net_Tag_User_WechatLogin, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestLoginBindPhone(IUserListener observer, String openid, String access_token, String nickname, String avatar, String phone, String code) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("openid",openid);
            mJsonObject.put("access_token",access_token);
            mJsonObject.put("nickname",nickname);
            mJsonObject.put("avatar",avatar);
            mJsonObject.put("mobile",phone);
            mJsonObject.put("verifyCode",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_WechatBindphone,
                ConstantUtil.Net_Tag_User_WechatBindPhone, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestSetAutoPlay(IUserListener observer, int auto) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("autoplay",auto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_SetAutoplay,
                ConstantUtil.Net_Tag_User_AUTOPLAY, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestUpdate(IUserListener observer, String version) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("version",version);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_GetVersion,
                ConstantUtil.Net_Tag_User_GetVersion, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }

    @Override
    public void requestAirUpdate(IUserListener observer, String name, String version) {
        JSONObject mJsonObject = new JSONObject();
        try {
            mJsonObject.put("version",version);
            mJsonObject.put("device_name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mCurrentRequestId = NetManager.getHttpConnect().sendRequest(
                ConstantUtil.NEW_BAPI_URL+ ConstantUtil.SERVER_URL_NAME_GetFirmwareVersion,
                ConstantUtil.Net_Tag_User_GetFirmwareVersion, mJsonObject.toString(), iNetListener);
        addObserver(observer, mCurrentRequestId);
    }


}
