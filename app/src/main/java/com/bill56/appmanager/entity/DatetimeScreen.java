package com.bill56.appmanager.entity;

import java.io.Serializable;

/**
 * 封装时间段内锁屏和使用时间数据的类
 * Created by Bill56 on 2016/6/5.
 */
public class DatetimeScreen implements Serializable {

    // id
    private int id;
    // 日期
    private String date;
    // 时间
    private String time;
    // 锁屏次数
    private int screenTime;
    // 使用时间
    private int useTime;

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

    public int getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(int screenTime) {
        this.screenTime = screenTime;
    }

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }
}
