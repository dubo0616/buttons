package com.gaia.button.model;

import com.gaia.button.net.BaseResult;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductModelList extends BaseResult implements Serializable {

    public ArrayList<ProductModel> getData() {
        return data;
    }

    public void setData(ArrayList<ProductModel> data) {
        this.data = data;
    }

    ArrayList<ProductModel> data;
}
