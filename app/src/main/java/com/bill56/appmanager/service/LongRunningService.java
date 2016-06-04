package com.bill56.appmanager.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.bill56.appmanager.broadcastreciever.AlarmReceiver;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 长期在后台运行，用来检测每个时段的app的开启次数和使用时长，并隔断时间保存到数据库
 * Created by Bill56 on 2016/6/4.
 */
public class LongRunningService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 开启线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(LogUtil.TAG, "executed at " + new Date().toString());
                // 读取正在运行的程序
//                getRunningAppsInfo();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // 获取当前的时间
        long currentTime = System.currentTimeMillis();
        // 转成日期时间
        Date currentDate = new Date(currentTime);
        // 获得当前的时间
        int currentHour = currentDate.getHours();
        // 计算一下整点的时间
        Date date;
        if (currentHour < 24) {
            date = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate(),currentHour+1,0,0);
        } else {
            date = new Date(currentDate.getYear(),currentDate.getMonth(),currentDate.getDate() + 1,1,0,0);
        }
        long nextTriggerTime = date.getTime();
        LogUtil.i(LogUtil.TAG,String.format("cur time : %s, next time : %s",String.valueOf(currentTime),String.valueOf(nextTriggerTime)));
        LogUtil.i(LogUtil.TAG,String.format("cur : %s,next : %s",currentDate.toString(),date.toString()));
        // 休眠时间
        long triggerAtTime = SystemClock.elapsedRealtime() + nextTriggerTime - currentTime;
        // 间隔时间到了之后通知广播启动服务
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 获取正在运行的应用程序
     */
    public List<AppInfo> getRunningAppsInfo() {
        List<AppInfo> runningAppInfos = new ArrayList<>();
        // 获得包管理器
        PackageManager pm = getPackageManager();
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        // 排序
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));
        // 保存所有正在运行的包名 以及它所在的进程信息
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();
        ActivityManager mActivityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            int pid = appProcess.pid; // pid
            String processName = appProcess.processName; // 进程名
            // 获得运行在该进程里的所有应用程序包
            String[] pkgNameList = appProcess.pkgList;
            // 输出所有应用程序的包名
            for (int i = 0; i < pkgNameList.length; i++) {
                String pkgName = pkgNameList[i];
                // 加入至map对象里
                pgkProcessAppMap.put(pkgName, appProcess);
                LogUtil.i(LogUtil.TAG,"pkgName : " + pkgName);
            }
        }
        // 保存所有正在运行的应用程序信息
        runningAppInfos = new ArrayList<AppInfo>();
        // 保存过滤查到的AppInfo
        for (ApplicationInfo app : listAppcations) {
            // 如果该包名存在 则构造一个RunningAppInfo对象
            if (pgkProcessAppMap.containsKey(app.packageName)) {
                // 获得该packageName的 pid 和 processName
                int pid = pgkProcessAppMap.get(app.packageName).pid;
                String processName = pgkProcessAppMap.get(app.packageName).processName;
                AppInfo runningApp = new AppInfo();
                runningApp.setAppIcon(app.loadIcon(pm));
                runningApp.setAppName(app.loadLabel(pm).toString());
                runningApp.setAppPackageName(app.packageName);
                runningApp.setIsUserApp(filterApp(app));
                // 获得占用的内存大小
                Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[]{pid});
                long memSize = memoryInfo[0].dalvikPrivateDirty;
                runningApp.setAppSize(memSize);
                runningAppInfos.add(runningApp);
            }
        }
        return runningAppInfos;
    }

    /**
     * 三方应用程序的过滤器
     *
     * @param info 应用信息
     * @return true 三方应用 false 系统应用
     */

    public boolean filterApp(ApplicationInfo info) {
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            // 代表的是系统的应用,但是被用户升级了. 用户应用
            return true;
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            // 代表的用户的应用
            return true;
        }
        return false;
    }

}
