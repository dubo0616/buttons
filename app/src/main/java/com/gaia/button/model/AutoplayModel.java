package com.gaia.button.model;

import com.gaia.button.net.BaseResult;

import java.io.Serializable;

public class AutoplayModel extends BaseResult implements Serializable {
    private int autoplay;

    public int getAutoplay() {
        return autoplay;
    }

    public void setAutoplay(int autoplay) {
        this.autoplay = autoplay;
    }
}
