package com.bill56.appmanager.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bill56.appmanager.entity.DatetimeScreen;
import com.bill56.appmanager.service.LongRunningService;
import com.bill56.appmanager.util.DateTimeUtil;
import com.bill56.appmanager.util.LogUtil;
import com.bill56.appmanager.util.Utility;

import java.util.Date;

/**
 * Created by Bill56 on 2016/6/5.
 */
public class ScreenActionReceiver extends BroadcastReceiver {

    // 调试标志
    private String TAG = "ScreenActionReceiver";
    // 是否已经注册的标志
    private boolean isRegisterReceiver = false;
    // 唯一实例
    private static ScreenActionReceiver instance;
    // 上一次点亮屏幕的时间
    private static long lastScreenOnTime;


    /**
     * 利用单例模式获取唯一实例
     *
     * @return 唯一实例
     */
    public static ScreenActionReceiver getInstance() {
        if (instance == null) {
            instance = new ScreenActionReceiver();
        }
        return instance;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_ON)) {
            LogUtil.d(TAG, "屏幕解锁广播...");
            // 记录下此刻的时间
            lastScreenOnTime = System.currentTimeMillis();
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            LogUtil.d(TAG, "屏幕加锁广播...");
            // 获取当前时间
            long currentTime = System.currentTimeMillis();
            long useTime = 0;
            // 当上一次屏幕点亮时间不为0的时候，计算屏幕亮的时长，即手机使用时长
            if (lastScreenOnTime > 0) {
                useTime = currentTime - lastScreenOnTime;
            }
            int userScondTime = (int) (useTime / 1000);
            // 将数据进行封装
            String date = DateTimeUtil.DateToString(currentTime);
            String time = DateTimeUtil.DateToTimeFieldString2(currentTime);
            // 想数据库写入数据
            Utility.handleScreenTimeRecode(LongRunningService.appManagerDB,date,time,userScondTime);
        }
    }

    public void registerScreenActionReceiver(Context mContext) {
        if (!isRegisterReceiver) {
            isRegisterReceiver = true;
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            LogUtil.d(TAG, "注册屏幕解锁、加锁广播接收者...");
            mContext.registerReceiver(ScreenActionReceiver.this, filter);
        }
    }

    public void unRegisterScreenActionReceiver(Context mContext) {
        if (isRegisterReceiver) {
            isRegisterReceiver = false;
            LogUtil.d(TAG, "注销屏幕解锁、加锁广播接收者...");
            mContext.unregisterReceiver(ScreenActionReceiver.this);
        }
    }

}
