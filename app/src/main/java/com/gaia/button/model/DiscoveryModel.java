package com.gaia.button.model;

import java.io.Serializable;

public class DiscoveryModel  implements Serializable {
    private String cate_name;
    private String id;
    private String title;
    private String list_img;
    private String is_collect;
    private String detailUrl;

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getList_img() {
        return list_img;
    }

    public void setList_img(String list_img) {
        this.list_img = list_img;
    }

    public String getIs_collect() {
        return is_collect;
    }
    public boolean isCollect(){
        return "1".equals(is_collect);
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }
}
