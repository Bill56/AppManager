package com.bill56.appmanager.util;

import android.content.Context;

import com.bill56.appmanager.dao.AppManagerDB;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.entity.DatetimeApp;
import com.bill56.appmanager.entity.DatetimeScreen;

import java.util.List;

/**
 * 线程处理类
 * Created by Bill56 on 2016/6/4.
 */
public class Utility {

    /**
     * 根据传来的列表，将app信息保存到sqlite数据库
     *
     * @param appManagerDB 数据库操作对象
     * @param apps         正在运行的app列表
     * @param date         日期
     * @param time         时间段
     */
    public static void handleAppRunningRecode(AppManagerDB appManagerDB, List<AppInfo> apps, String date, String time) {
        // 保存
        for (int i = 0; i < apps.size(); i++) {
            AppInfo appInfo = apps.get(i);
            // 先查询，如果对应时段已经存在该数据，则修改启动启动次数即可
            DatetimeApp datetimeApp = appManagerDB.loadDatetimeAppByDateTimeAndAppname(date, time, appInfo.getAppName());
            if (datetimeApp != null) {
                int oldNumber = datetimeApp.getNumber();
                datetimeApp.setNumber(oldNumber + 1);
                appManagerDB.updateDatetimeAppByDateTimeAndAppname(datetimeApp);
            } else {
                // 将数据封装
                datetimeApp = new DatetimeApp();
                datetimeApp.setAppName(appInfo.getAppName());
                datetimeApp.setTime(time);
                datetimeApp.setDate(date);
                datetimeApp.setNumber((int) (Math.random() * 4 + 1));
                appManagerDB.saveDatetimeApp(datetimeApp);
            }
        }
    }

    /**
     * 向数据库写入锁屏次数和使用时长
     *
     * @param appManagerDB 数据库操作类
     * @param date         日期
     * @param time         时间
     * @param useTime      使用时间
     */
    public static void handleScreenTimeRecode(AppManagerDB appManagerDB, String date, String time, int useTime) {
        // 查询是否有这一时刻的，如果有则修改
        DatetimeScreen dscreen = appManagerDB.loadDatetimeScreenByDateTime(date, time);
        if (dscreen != null) {
            // 修改锁屏次数和使用时间
            int oldScreenTime = dscreen.getScreenTime();
            int oldUseTime = dscreen.getUseTime();
            dscreen.setScreenTime(oldScreenTime + 1);
            dscreen.setUseTime(oldUseTime + useTime);
            appManagerDB.updateDatetimeScreenByDateTime(dscreen);
        } else {
            // 没有查询到则新增
            dscreen = new DatetimeScreen();
            dscreen.setDate(date);
            dscreen.setTime(time);
            dscreen.setScreenTime(1);
            dscreen.setUseTime(useTime);
            appManagerDB.saveDatetimeScreen(dscreen);
        }


    }

}
