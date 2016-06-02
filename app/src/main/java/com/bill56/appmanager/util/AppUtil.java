package com.bill56.appmanager.util;

import android.content.Context;
import android.text.format.Formatter;

/**
 * App工具类，封装了对AppInfo的一些操作
 * Created by Bill56 on 2016/5/30.
 */
public class AppUtil {

    /**
     * 将文件大小的长整形转成对象的字符串
     *
     * @param context 上下文环境
     * @param appSize long类型的原始大小
     * @return 文件格式的字符串大小
     */
    public static String longSizeToStrSize(Context context, long appSize) {
        return Formatter.formatFileSize(context, appSize);
    }

    /**
     * 将app使用时间的Long转成字符串，只显示小时，分钟和秒
     * @param appUseTime
     * @return
     */
    public static String longTimeToStrTime(long appUseTime) {
        return null;
    }

}
