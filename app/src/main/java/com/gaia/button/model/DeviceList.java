package com.gaia.button.model;

import com.gaia.button.net.BaseResult;

import java.io.Serializable;
import java.util.List;

public class DeviceList extends BaseResult implements Serializable {
    private List<DeviceMode> data;

    public List<DeviceMode> getData() {
        return data;
    }

    public void setData(List<DeviceMode> data) {
        this.data = data;
    }
}
