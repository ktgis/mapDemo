package com.kt.mapdemo.adapter;

import com.kt.mapdemo.base.BaseModel;

import androidx.appcompat.app.AppCompatActivity;

public class DemoModel extends BaseModel {
    private String title;
    private String description;
    private Class<? extends AppCompatActivity> activityClass;

    public DemoModel(String title, String description, Class<? extends AppCompatActivity> activityClass) {
        this.title = title;
        this.description = description;
        this.activityClass = activityClass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<? extends AppCompatActivity> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<? extends AppCompatActivity> activityClass) {
        this.activityClass = activityClass;
    }
}
