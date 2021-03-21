package com.gaia.button.utils;

import com.google.gson.internal.$Gson$Preconditions;

public enum DeviceType{
    AIROLD(3),//air 老
    AIRNEW(4), //air 新
    AIRX(5),//airx
    DEFAULT(-1);// 默认
    private int type;
    public int getType() {
        return type;
    }
     DeviceType(int v){
        this.type = v;
    }

}
