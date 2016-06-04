package com.bill56.appmanager.util;

import android.content.Context;

import com.bill56.appmanager.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间的工具类
 * Created by Bill56 on 2016/5/30.
 */
public class DateTimeUtil {

    /**
     * 将时间转成日期格式，只显示年月日
     *
     * @param mills 毫秒
     * @return 对应的字符串
     */
    public static String DateToString(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 将时间转成日期格式，只显示年月日
     *
     * @param mills 毫秒
     * @return 对应的字符串
     */
    public static String DateToDetailString(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 根据安装时间和当前时间计算时间差
     *
     * @param context     上下文环境
     * @param instalTime  安装时间
     * @param currentTime 当前时间
     * @return 时间差的字符串
     */
    public static String caculateSubTime(Context context, long instalTime, long currentTime) {
        try {
            Date d1 = new Date(currentTime);
            Date d2 = new Date(instalTime);
            long diff = d1.getTime() - d2.getTime();
            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            return day + context.getString(R.string.app_days) + hour + context.getString(R.string.app_hours);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
