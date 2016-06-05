package com.bill56.appmanager.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bill56 on 2016/6/4.
 */
public class AppManagerOpenHelper extends SQLiteOpenHelper {

    /**
     * 创建运行时间记录数据表的语句
     */
    public static final String CREATE_DATETIME_APP = "create table DATETIME_APP (" +
            "id integer primary key autoincrement," +   // id
            "date text," +       // 日期
            "time text," +       // 时间
            "number integer," +  // 启动次数
            "appName text)";        // 对应的某一个app

    /**
     * 创建时间段内解锁次数和手机使用时间数据表的语句
     */
    public static final String CREATE_DATETIME_SCREEN = "create table DATETIME_SCREEN (" +
            "id integer primary key autoincrement," +  //id
            "date text," +                             // 日期
            "time text," +                             // 时间
            "screenTime integer," +                    // 锁屏次数
            "useTime integer)";                        // 使用时间，即屏幕亮着与锁屏的时间差之和，单位秒

    /**
     * 构造方法
     *
     * @param context 上下文换将
     * @param name    数据库名称
     * @param factory 游标工厂
     * @param version 版本
     */
    public AppManagerOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建数据库的方法
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DATETIME_APP);
        db.execSQL(CREATE_DATETIME_SCREEN);
    }

    /**
     * 当数据库版本更新后执行
     *
     * @param db
     * @param oldVersion 老版本号
     * @param newVersion 新版本号
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
