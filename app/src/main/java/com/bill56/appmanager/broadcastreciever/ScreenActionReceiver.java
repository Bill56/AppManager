package com.bill56.appmanager.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bill56.appmanager.util.LogUtil;

/**
 * Created by Bill56 on 2016/6/5.
 */
public class ScreenActionReceiver extends BroadcastReceiver {

    private String TAG = "ScreenActionReceiver";
    private boolean isRegisterReceiver = false;
    private static ScreenActionReceiver instance;

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
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            LogUtil.d(TAG, "屏幕加锁广播...");
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
