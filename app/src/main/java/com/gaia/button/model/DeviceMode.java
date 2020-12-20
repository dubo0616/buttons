package com.gaia.button.model;

import java.io.Serializable;

public class DeviceMode implements Serializable {
    private String id;
    private String device_name;
    private String device_img;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getDevice_img() {
        return device_img;
    }

    public void setDevice_img(String device_img) {
        this.device_img = device_img;
    }
}
