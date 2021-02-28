package com.gaia.button.model;

import com.gaia.button.net.BaseResult;

import java.io.Serializable;

public class AutoplayModel extends BaseResult implements Serializable {
    private int autoplay;

    public int getMobile_network() {
        return mobile_network;
    }

    public void setMobile_network(int mobile_network) {
        this.mobile_network = mobile_network;
    }

    private int mobile_network;

    public int getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(int autoplay) {
        this.autoplay = autoplay;
    }
}
