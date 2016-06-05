package com.bill56.appmanager.util;

import android.content.Context;

import com.bill56.appmanager.dao.AppManagerDB;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.entity.DatetimeApp;

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
            // 将数据封装
            DatetimeApp datetimeApp = new DatetimeApp();
            datetimeApp.setAppName(appInfo.getAppName());
            datetimeApp.setTime(time);
            datetimeApp.setDate(date);
            datetimeApp.setNumber((int) (Math.random()*4+1));
            appManagerDB.saveDatetimeApp(datetimeApp);
        }
    }

}
