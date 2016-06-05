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
    // 锁屏次数
    private int screenOff;
    // 使用时长
    private int useTime;

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

    public int getScreenOff() {
        return screenOff;
    }

    public void setScreenOff(int screenOff) {
        this.screenOff = screenOff;
    }

    public int getUseTime() {
        return useTime;
    }

    public void setUseTime(int useTime) {
        this.useTime = useTime;
    }

    @Override
    public String toString() {
        return "TimeFieldApp{" +
                "time='" + time + '\'' +
                ", appNumber=" + appNumber +
                ", appStrupNumber=" + appStrupNumber +
                ", screenOff=" + screenOff +
                ", useTime=" + useTime +
                '}';
    }

}
