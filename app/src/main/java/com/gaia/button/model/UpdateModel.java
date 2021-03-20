package com.gaia.button.model;

import com.gaia.button.net.BaseResult;

import java.io.Serializable;

public class UpdateModel extends BaseResult implements Serializable {
    private int isUpdate;
    private int isForce;
    private String version;
    private String url;
    private String content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getIsForce() {
        return isForce;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsForce(int isForce) {
        this.isForce = isForce;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
