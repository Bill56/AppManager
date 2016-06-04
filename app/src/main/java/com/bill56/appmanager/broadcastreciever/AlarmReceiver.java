package com.bill56.appmanager.broadcastreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bill56.appmanager.service.LongRunningService;

/**
 * 用于接收服务发来的消息的广播接收器
 * Created by Bill56 on 2016/6/4.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 间隔时间到了之后再启动服务
        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }
}
