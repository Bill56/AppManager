package com.bill56.appmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bill56.appmanager.entity.DatetimeApp;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 * Created by Bill56 on 2016/6/4.
 */
public class AppManagerDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "APP_MANAGER";

    /**
     * 数据库版本号
     */
    public static final int VERSION = 1;

    private static AppManagerDB appManagerDB;

    private SQLiteDatabase db;

    /**
     * 运用单例模式将构造方法私有化
     *
     * @param context 上下文环境
     */
    private AppManagerDB(Context context) {
        AppManagerOpenHelper dbHelper = new AppManagerOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获得数据库实例
     *
     * @param context 上下文环境
     * @return 实例
     */
    public synchronized static AppManagerDB getInstance(Context context) {
        if (appManagerDB == null) {
            appManagerDB = new AppManagerDB(context);
        }
        return appManagerDB;
    }

    /**
     * 将DatetimeApp实例保存到数据库
     *
     * @param datetimeApp 待保存的实例
     */
    public void saveDatetimeApp(DatetimeApp datetimeApp) {
        if (datetimeApp != null) {
            ContentValues values = new ContentValues();
            values.put("date", datetimeApp.getDate());
            values.put("time", datetimeApp.getTime());
            values.put("number", datetimeApp.getNumber());
            values.put("appName", datetimeApp.getAppName());
            db.insert("DATETIME_APP", null, values);
        }
    }

    /**
     * 从数据库获取对应日期的所有时段的app使用情况
     *
     * @param date 日期
     * @return 数据列表
     */
    public List<DatetimeApp> loadDatetimeAppByDate(String date) {
        List<DatetimeApp> list = new ArrayList<>();
        // 指定查询的条件
        Cursor cursor = db.query("DATETIME_APP",null,"date=?",new String[]{date},null,null,null,null);
        // 查到数据
        if (cursor.moveToFirst()) {
            do {
                DatetimeApp datetimeApp = new DatetimeApp();
                // 将数据封装
                datetimeApp.setId(cursor.getInt(cursor.getColumnIndex("id")));
                datetimeApp.setDate(date);
                datetimeApp.setTime(cursor.getString(cursor.getColumnIndex("time")));
                datetimeApp.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
                datetimeApp.setAppName(cursor.getString(cursor.getColumnIndex("appName")));
                list.add(datetimeApp);
            }while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }


}
