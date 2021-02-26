package com.kt.mapdemo.adapter;

import com.kt.mapdemo.base.BaseModel;

public class HeaderModel extends BaseModel {
    private String title;

    public HeaderModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
