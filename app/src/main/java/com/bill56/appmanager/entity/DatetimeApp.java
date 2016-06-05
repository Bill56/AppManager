package com.bill56.appmanager.entity;

import java.io.Serializable;

/**
 * Created by Bill56 on 2016/6/4.
 */
public class DatetimeApp implements Serializable{

    // id
    private int id;
    // 日期
    private String date;
    // 时间
    private String time;
    // app启动次数
    private int number;
    // app名称
    private String appName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
