package com.gaia.button.data;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.gaia.button.GaiaApplication;
import com.gaia.button.model.AccountInfo;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceManager {
    private static final String KEY ="com.gaia.button.data";
    private static volatile PreferenceManager mInstance;
    private SharedPreferences mySharedPreferences;
    private AccountInfo mAccountInfo;

    private static final String ACC_LOGIN_TYPE = "acc_login_type";
    private static final String ACC_LOGIN_MOBILE = "acc_login_mobile";
    private static final String ACC_LOGIN_INFO_TOKEN = "acc_login_info_token";
    public static final String ACC_LOGIN_AVTOR_URL = "acc_login_avtor_url";
    private static final String ACC_LOGIN_USER_ID = "acc_login_user_ID";
    private static final String ACC_LOGIN_PASSWORD = "acc_login_password";
    public static final String ACC_LOGIN_PERSON_SGIN = "acc_login_person_sgin";
    public static final String ACC_LOGIN_PERSON_NAME = "acc_login_person_name";
    public static final String ACC_LOGIN_MOBILE_NETWORK = "acc_login_mobile_network";
    public static final String ACC_LOGIN_OPENID = "acc_login_openid";

    private static final String FRIST_INSTALL = "first_install";
    private static final String PLAY_MODE = "paly_mode";
    private static final String PLAY_SOUND_MODE = "paly_sound_mode";
    private static final String SEARCH_HISTORY = "search_history";
    public static final String ACC_AUTO_PLAY = "acc_auto_play";
    public static final String CONNECT_ARRAESS = "connect_ardss";
    public static final String CONNECT_VOICE = "connect_Voice";

    private PreferenceManager(){
        mySharedPreferences = GaiaApplication.getInstance().getSharedPreferences(PreferenceManager.KEY,MODE_PRIVATE);
    }
    public static PreferenceManager getInstance(){
        if(mInstance == null){
            synchronized (PreferenceManager.class){
                if(mInstance == null){
                    mInstance = new PreferenceManager();
                }
            }

        }
        return mInstance;
    }

    public void saveUserinfo(String value) {
         SharedPreferences.Editor mEditor = mySharedPreferences.edit();
        mEditor.putString("userinfo", value);
        mEditor.commit();
    }

    public String getUserinfo() {
        return mySharedPreferences.getString("userinfo", "");

    }
    public AccountInfo getAccountInfo(){
        if(mAccountInfo == null){
            mAccountInfo = new AccountInfo();
            mAccountInfo.setUserID(getStringValue(ACC_LOGIN_USER_ID));
            mAccountInfo.setPerson_name(getStringValue(ACC_LOGIN_PERSON_NAME));
            mAccountInfo.setToken(getStringValue(ACC_LOGIN_INFO_TOKEN));
            mAccountInfo.setMobile(getStringValue(ACC_LOGIN_MOBILE));
            mAccountInfo.setAvtorURL(getStringValue(ACC_LOGIN_AVTOR_URL));
            mAccountInfo.setPerson_sign(getStringValue(ACC_LOGIN_PERSON_SGIN));
            mAccountInfo.setSetPassword(getBlooeanValue(ACC_LOGIN_PASSWORD));
            mAccountInfo.setAutoplay(getIntValue(ACC_AUTO_PLAY));
            mAccountInfo.setMobile_network(getIntValue(ACC_LOGIN_MOBILE_NETWORK));
            mAccountInfo.setOpenid(getStringValue(ACC_LOGIN_OPENID));


        }
       return  mAccountInfo;
    }
    public void setLoginOut(){
        setStringValue(ACC_LOGIN_TYPE,"");
        setStringValue(ACC_LOGIN_MOBILE,"");
        setStringValue(ACC_LOGIN_INFO_TOKEN,"");
        setStringValue(ACC_LOGIN_AVTOR_URL,"");
        setStringValue(ACC_LOGIN_USER_ID,"");
        setStringValue(ACC_LOGIN_PERSON_SGIN,"");
        setStringValue(ACC_LOGIN_PERSON_NAME,"");
        setBlooeanValue(ACC_LOGIN_PASSWORD,false);
        setIntValue(ACC_AUTO_PLAY,-1);
        setIntValue(ACC_LOGIN_MOBILE_NETWORK,-1);
        setStringValue(ACC_LOGIN_OPENID,"");
        mAccountInfo=null;
    }

    /****
     *
     * @return
     */
    public boolean isLogin(){
        String token = getStringValue(ACC_LOGIN_INFO_TOKEN);
        return !TextUtils.isEmpty(token);
    }
    public void setIntValue(String key,int value){
        SharedPreferences.Editor mEditor = mySharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }
    public int getIntValue(String key){
        return mySharedPreferences.getInt(key,-1);
    }
    public void setStringValue(String key,String value){
        SharedPreferences.Editor mEditor = mySharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.commit();
    }
    public String getStringValue(String key){
        return mySharedPreferences.getString(key,"");
    }
    public void setBlooeanValue(String key,boolean value){
        SharedPreferences.Editor mEditor = mySharedPreferences.edit();
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }
    public boolean getBlooeanValue(String key){
        return mySharedPreferences.getBoolean(key,false);
    }
    public  void save(AccountInfo info) {
        if(info != null && !TextUtils.isEmpty(info.getToken())){
            setStringValue(ACC_LOGIN_TYPE,"");
            setStringValue(ACC_LOGIN_MOBILE,info.getMobile());
            setStringValue(ACC_LOGIN_INFO_TOKEN,info.getToken());
            setStringValue(ACC_LOGIN_AVTOR_URL,info.getAvtorURL());
            setStringValue(ACC_LOGIN_USER_ID,info.getUserID());
            setStringValue(ACC_LOGIN_PERSON_SGIN,info.isPerson_sign());
            setStringValue(ACC_LOGIN_PERSON_NAME,info.getPerson_name());
            setBlooeanValue(ACC_LOGIN_PASSWORD,info.isSetPassword());
            setIntValue(ACC_AUTO_PLAY,info.getAutoplay());
            setIntValue(ACC_LOGIN_MOBILE_NETWORK,info.getMobile_network());
            setStringValue(ACC_LOGIN_OPENID,info.getOpenid() );
            mAccountInfo = info;
        }
    }

    public boolean getFristInstall() {
        return getBlooeanValue(FRIST_INSTALL);
    }

    public void setFristInstall() {
        setBlooeanValue(FRIST_INSTALL, true);
    }

    public String getPlaymode(String uid) {
        return getStringValue(uid+PLAY_MODE);
    }

    public void setPlaymode(String uid,int value) {
        setStringValue(uid+PLAY_MODE,value+"");
    }

    public String getPlaySoundMode(String uid) {
        return getStringValue(uid+PLAY_SOUND_MODE);
    }

    public void setPlaySoundMode(String uid,String value) {
        setStringValue(uid+PLAY_SOUND_MODE,value);
    }

    public String getSearchHistory(String uid) {
        return getStringValue(uid+SEARCH_HISTORY);
    }

    public void setSearchHistoryString(String uid,String value) {
        setStringValue(uid+SEARCH_HISTORY,value);
    }

}
