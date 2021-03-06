package com.gaia.button.model;

import android.text.TextUtils;

import com.gaia.button.net.BaseResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AccountInfo extends BaseResult implements Serializable {
    private String token;
    private String mobile;
    private String avtorURL;
    private String userID;
    private boolean setPassword;
    private String person_sign;
    private int is_openid;
    private int is_avatar;

    public int getIs_openid() {
        return is_openid;
    }

    public void setIs_openid(int is_openid) {
        this.is_openid = is_openid;
    }

    public int getIs_avatar() {
        return is_avatar;
    }

    public void setIs_avatar(int is_avatar) {
        this.is_avatar = is_avatar;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    private String openid;

    public int getMobile_network() {
        return mobile_network;
    }

    public void setMobile_network(int mobile_network) {
        this.mobile_network = mobile_network;
    }

    private int mobile_network;
    public String getPerson_name() {
        return nickname;
    }

    public void setPerson_name(String person_name) {
        this.nickname = person_name;
    }

    private String nickname;

    public String getPerson_sign() {
        return person_sign;
    }

    public int getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(int autoplay) {
        this.autoplay = autoplay;
    }

    private int  autoplay;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvtorURL() {
        return avtorURL;
    }

    public void setAvtorURL(String avtorURL) {
        this.avtorURL = avtorURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isSetPassword() {
        return setPassword;
    }

    public void setSetPassword(boolean setPassword) {
        this.setPassword = setPassword;
    }

    public String isPerson_sign() {
        return person_sign;
    }

    public void setPerson_sign(String person_sign) {
        this.person_sign = person_sign;
    }

    public static AccountInfo Parse(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }

        AccountInfo ainfo = new AccountInfo();
        JSONObject jo = null;
        try {
            JSONObject jdata = jo.getJSONObject("data");
            String token = jdata.optString("token");
            String mobile = jdata.optString("mobile");
            String avtorURL = jdata.optString("avtorURL");
            String userID = jdata.optString("userID");
            boolean setPassword = jdata.optBoolean("setPassword");
            String person_sign = jdata.optString("person_sign");
            String person_name = jdata.optString("nickname");
            int autoplay = jdata.optInt("autoplay");
            ainfo.setToken(token);
            ainfo.setMobile(mobile);
            ainfo.setAvtorURL(avtorURL);
            ainfo.setSetPassword(setPassword);
            ainfo.setPerson_sign(person_sign);
            ainfo.setUserID(userID);
            ainfo.setAutoplay(autoplay);
            ainfo.setPerson_name(person_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ainfo;

    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "token='" + token + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avtorURL='" + avtorURL + '\'' +
                ", userID='" + userID + '\'' +
                ", setPassword=" + setPassword +
                ", person_sign='" + person_sign + '\'' +
                ", is_openid=" + is_openid +
                ", is_avatar=" + is_avatar +
                ", openid='" + openid + '\'' +
                ", mobile_network=" + mobile_network +
                ", nickname='" + nickname + '\'' +
                ", autoplay=" + autoplay +
                '}';
    }
}
