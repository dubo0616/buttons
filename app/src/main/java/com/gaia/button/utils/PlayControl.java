package com.gaia.button.utils;

public enum PlayControl {
    //    1:backward（上一曲）
//            2:forward（下一曲）
//            3:play（播放）
//            4:pause（暂停）
    BACKWARD(1),
    FORWARD(2),
    PLAY(3),
    PAUSE(4);
    private int value;

    PlayControl(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
