package com.bill56.appmanager.util;

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

}
