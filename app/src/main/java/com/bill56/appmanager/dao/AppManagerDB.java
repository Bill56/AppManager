package com.bill56.appmanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bill56.appmanager.entity.DatetimeApp;
import com.bill56.appmanager.entity.DatetimeScreen;
import com.bill56.appmanager.util.LogUtil;

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
    public static final int VERSION = 2;

    private static AppManagerDB appManagerDB;

    private SQLiteDatabase db;

    private static AppManagerDB instance;


    /**
     * 运用单例模式将构造方法私有化
     *
     * @param context 上下文环境
     */
    private AppManagerDB(Context context) {
        AppManagerOpenHelper dbHelper = new AppManagerOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
        instance = this;
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
            LogUtil.i(LogUtil.TAG, datetimeApp.getDate() + " " + datetimeApp.getTime() + " , 次数:" + datetimeApp.getNumber() + " name :" + datetimeApp.getAppName() + "插入成功");
        }
    }

    /**
     * 从数据库获取对应日期的所有时段的app使用情况
     *
     * @param date 日期
     * @return 数据列表
     */
    public ArrayList<DatetimeApp> loadDatetimeAppByDate(String date) {
        ArrayList<DatetimeApp> list = new ArrayList<>();
        // 指定查询的条件
        Cursor cursor = db.query("DATETIME_APP", null, "date=?", new String[]{date}, null, null, "id desc", null);
        // Cursor cursor = db.query("DATETIME_APP",null,null,null,null,null,null,null);
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
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 从数据库获取对应日期的app使用情况
     *
     * @param date    日期
     * @param time    时间
     * @param appName 应用名称
     * @return 数据
     */
    public DatetimeApp loadDatetimeAppByDateTimeAndAppname(String date, String time, String appName) {
        DatetimeApp datetimeApp = new DatetimeApp();
        // 指定查询的条件
        Cursor cursor = db.query("DATETIME_APP", null, "date=? and time=? and appName=?", new String[]{date, time, appName}, null, null, "id desc", null);
        // Cursor cursor = db.query("DATETIME_APP",null,null,null,null,null,null,null);
        // 查到数据
        if (cursor.moveToFirst()) {
            // 将数据封装
            datetimeApp.setId(cursor.getInt(cursor.getColumnIndex("id")));
            datetimeApp.setDate(date);
            datetimeApp.setTime(time);
            datetimeApp.setNumber(cursor.getInt(cursor.getColumnIndex("number")));
            datetimeApp.setAppName(appName);
        } else {
            // 没有找到，返回Null
            datetimeApp = null;
        }
        if (cursor != null) {
            cursor.close();
        }
        return datetimeApp;
    }

    /**
     * 根据日期和时间修改app的使用次数
     *
     * @param datetimeApp 封装了待修改数据的对象
     */
    public void updateDatetimeAppByDateTimeAndAppname(DatetimeApp datetimeApp) {
        if (datetimeApp != null) {
            ContentValues values = new ContentValues();
            values.put("number", datetimeApp.getNumber());
            // 更新数据
            db.update("DATETIME_APP", values, "date=? and time=? and appName=?",
                    new String[]{datetimeApp.getDate(), datetimeApp.getTime(), datetimeApp.getAppName()});
            LogUtil.i(LogUtil.TAG, datetimeApp.getDate() + " " + datetimeApp.getTime() + " , 次数:" + datetimeApp.getNumber() + " 名称 :" + datetimeApp.getAppName() + "修改成功");
        }
    }

    /**
     * 将DatetimeScreen实例保存到数据库
     *
     * @param datetimeScreen 待保存的实例
     */
    public void saveDatetimeScreen(DatetimeScreen datetimeScreen) {
        if (datetimeScreen != null) {
            ContentValues values = new ContentValues();
            values.put("date", datetimeScreen.getDate());
            values.put("time", datetimeScreen.getTime());
            values.put("screenTime", datetimeScreen.getScreenTime());
            values.put("useTime", datetimeScreen.getUseTime());
            db.insert("DATETIME_SCREEN", null, values);
            LogUtil.i(LogUtil.TAG, datetimeScreen.getDate() + " " + datetimeScreen.getTime() + " , 次数:" + datetimeScreen.getScreenTime() + " 时间 :" + datetimeScreen.getUseTime() + "插入成功");
        }
    }

    /**
     * 从数据库获取对应日期的所有时段的屏幕解锁和手机使用情况
     *
     * @param date 日期
     * @return 数据列表
     */
    public ArrayList<DatetimeScreen> loadDatetimeScreenByDate(String date) {
        ArrayList<DatetimeScreen> list = new ArrayList<>();
        // 指定查询的条件
        Cursor cursor = db.query("DATETIME_SCREEN", null, "date=?", new String[]{date}, null, null, "id desc", null);
        // Cursor cursor = db.query("DATETIME_APP",null,null,null,null,null,null,null);
        // 查到数据
        if (cursor.moveToFirst()) {
            do {
                DatetimeScreen datetimeScreen = new DatetimeScreen();
                // 将数据封装
                datetimeScreen.setId(cursor.getInt(cursor.getColumnIndex("id")));
                datetimeScreen.setDate(date);
                datetimeScreen.setTime(cursor.getString(cursor.getColumnIndex("time")));
                datetimeScreen.setScreenTime(cursor.getInt(cursor.getColumnIndex("screenTime")));
                datetimeScreen.setUseTime(cursor.getInt(cursor.getColumnIndex("useTime")));
                list.add(datetimeScreen);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 从数据库获取对应日期时间的时段的屏幕解锁和手机使用情况
     *
     * @param date 日期
     * @param time 时间
     * @return 数据对象
     */
    public DatetimeScreen loadDatetimeScreenByDateTime(String date, String time) {
        DatetimeScreen datetimeScreen = new DatetimeScreen();
        // 指定查询的条件
        Cursor cursor = db.query("DATETIME_SCREEN", null, "date=? and time=?", new String[]{date, time}, null, null, "id desc", null);
        // Cursor cursor = db.query("DATETIME_APP",null,null,null,null,null,null,null);
        // 查到数据
        if (cursor.moveToFirst()) {
            // 将数据封装
            datetimeScreen.setId(cursor.getInt(cursor.getColumnIndex("id")));
            datetimeScreen.setDate(date);
            datetimeScreen.setTime(cursor.getString(cursor.getColumnIndex("time")));
            datetimeScreen.setScreenTime(cursor.getInt(cursor.getColumnIndex("screenTime")));
            datetimeScreen.setUseTime(cursor.getInt(cursor.getColumnIndex("useTime")));
        } else {
            // 没有找到返回null
            datetimeScreen = null;
        }
        if (cursor != null) {
            cursor.close();
        }
        return datetimeScreen;
    }

    /**
     * 根据日期和时间修改手机的锁屏次数和使用时长
     *
     * @param datetimeScreen 封装了待修改数据的对象
     */
    public void updateDatetimeScreenByDateTime(DatetimeScreen datetimeScreen) {
        if (datetimeScreen != null) {
            ContentValues values = new ContentValues();
            values.put("screenTime", datetimeScreen.getScreenTime());
            values.put("useTime", datetimeScreen.getUseTime());
            // 更新数据
            db.update("DATETIME_SCREEN", values, "date=? and time=?",
                    new String[]{datetimeScreen.getDate(), datetimeScreen.getTime()});
            LogUtil.i(LogUtil.TAG, datetimeScreen.getDate() + " " + datetimeScreen.getTime() + " , 次数:" + datetimeScreen.getScreenTime() + " 时间 :" + datetimeScreen.getUseTime() + "修改成功");
        }
    }

    /**
     * 在数据库中查找包名的方法
     *
     * @param packageName 包名
     * @return 是否查到
     */
    public boolean find(String packageName) {
        boolean result = false;
        Cursor cursor = db.query("applock",null,"packagename=?",new String[]{packageName},null,null,null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        LogUtil.i(LogUtil.TAG,String.valueOf(result));
        return result;
    }

    /**
     * 向数据库添加加锁的程序
     *
     * @param packageName 程序包名
     */
    public void add(String packageName) {
        if (find(packageName)) {
            return;
        }
        db.execSQL("insert into applock (packagename) values (?)",
                new String[]{packageName});

    }

    /**
     * 删除加锁的程序
     *
     * @param packageName 程序包名
     */

    public void delete(String packageName) {
        if (db.isOpen()) {
            db.execSQL("delete from applock where packagename = ? ",
                    new Object[]{packageName});
        }
    }

    /**
     * 获得所有的加锁的程序包名
     *
     * @return 程序包名列表
     */
    public List<String> getAllPackageName() {
        List<String> packageNames = new ArrayList<String>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select packagename from applock", null);
            while (cursor.moveToNext()) {
                String packageName = cursor.getString(0);
                packageNames.add(packageName);
            }
            cursor.close();
        }
        return packageNames;
    }

}
