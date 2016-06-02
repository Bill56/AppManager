package com.bill56.appmanager.entity;

import android.graphics.drawable.Drawable;

/**
 * 实体类，封装App信息
 * Created by Bill56 on 2016/5/30.
 */
public class AppInfo {

    // 应用的图标
    private Drawable appIcon;
    // 应用的名称
    private String appName;
    // 应用的包名
    private String appPackageName;
    // 应用的大小
    private long appSize;
    // 应用所占用的内存
    private long appUseROM;
    // 应用所使用的时间
    private long appUserTime;
    // 应用启动的次数
    private int appstartFrequency;
    //应用的类型,true表示用户，false表示系统
    private boolean isUserApp;

    /**
     * 构造方法
     */
    public AppInfo() {

    }

    // getter与setter方法
    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public long getAppUseROM() {
        return appUseROM;
    }

    public void setAppUseROM(long appUseROM) {
        this.appUseROM = appUseROM;
    }

    public long getAppUserTime() {
        return appUserTime;
    }

    public void setAppUserTime(long appUserTime) {
        this.appUserTime = appUserTime;
    }

    public int getAppstartFrequency() {
        return appstartFrequency;
    }

    public void setAppstartFrequency(int appstartFrequency) {
        this.appstartFrequency = appstartFrequency;
    }

    public boolean isUserApp() {
        return isUserApp;
    }

    public void setIsUserApp(boolean isUserApp) {
        this.isUserApp = isUserApp;
    }

}
