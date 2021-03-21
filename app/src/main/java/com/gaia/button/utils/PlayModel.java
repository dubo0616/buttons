package com.gaia.button.utils;

public enum PlayModel {

    AMBIENT(3),//环境音
    Standard(1),//标准
    NOISE(2);//降噪
    private int value;

    PlayModel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public String getStringValue() {
        return String.valueOf(value);
    }

    public void setValue(int value) {
        this.value = value;
    }
}
