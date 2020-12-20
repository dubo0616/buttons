package com.gaia.button.model;

import com.gaia.button.net.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class DiscoverList extends BaseResult implements Serializable {
    public ArrayList<DiscoveryModel> getData() {
        return data;
    }

    public void setData(ArrayList<DiscoveryModel> data) {
        this.data = data;
    }

    ArrayList<DiscoveryModel> data;
}
