package com.bill56.appmanager.entity;

/**
 * Created by Bill56 on 2016/6/5.
 */
public class TimeFieldApp {

    // 时间段
    private String time;
    // app使用shuliang
    private int appNumber;
    // app启动次数
    private int appStrupNumber;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(int appNumber) {
        this.appNumber = appNumber;
    }

    public int getAppStrupNumber() {
        return appStrupNumber;
    }

    public void setAppStrupNumber(int appStrupNumber) {
        this.appStrupNumber = appStrupNumber;
    }

    @Override
    public String toString() {
        return "TimeFieldApp{" +
                "time='" + time + '\'' +
                ", appNumber=" + appNumber +
                ", appStrupNumber=" + appStrupNumber +
                '}';
    }
}
