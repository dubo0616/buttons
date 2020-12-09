package com.gaia.button.model;

import java.io.Serializable;

public class LoginResult implements Serializable {
    private String token;
    private String mobile;
    private String avtorURL;
    private String userID;
    private boolean setPassword;
    private boolean person_sign;

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

    public boolean isPerson_sign() {
        return person_sign;
    }

    public void setPerson_sign(boolean person_sign) {
        this.person_sign = person_sign;
    }
}
