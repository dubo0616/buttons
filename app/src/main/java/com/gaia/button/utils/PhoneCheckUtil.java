package com.gaia.button.utils;

import android.text.TextUtils;

public class PhoneCheckUtil {

    public static boolean checkPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }

        boolean isValidNum = phoneNum.matches("^[0-9]*$");
        if (!isValidNum) {
            return false;
        }
        if(phoneNum.length() != 11){
            return false;
        }

        return isValidNum;
    }
    public static boolean checkPhoneCode(String phonecode) {
        if (TextUtils.isEmpty(phonecode)) {
            return false;
        }

        boolean isValidNum = phonecode.matches("^[0-9]*$") ;
        if (!isValidNum) {
            return false;
        }
        if(phonecode.length() != 6){
            return false;
        }

        return isValidNum;
    }

    public static boolean checkPhonePass(String phonecode) {
        if (TextUtils.isEmpty(phonecode)) {
            return false;
        }

        boolean isValidNum = phonecode.matches("^[0-9]*$") ;
        if (!isValidNum) {
            return false;
        }
        if(phonecode.length() != 6){
            return false;
        }

        return isValidNum;
    }
}
