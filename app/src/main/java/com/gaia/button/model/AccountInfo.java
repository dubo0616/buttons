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
            boolean setPassword = jdata.optBoolean("mobile");
            String person_sign = jdata.optString("person_sign");
            ainfo.setToken(token);
            ainfo.setMobile(mobile);
            ainfo.setAvtorURL(avtorURL);
            ainfo.setSetPassword(setPassword);
            ainfo.setPerson_sign(person_sign);
            ainfo.setUserID(userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ainfo;

    }
}
