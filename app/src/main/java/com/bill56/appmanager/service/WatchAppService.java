package com.bill56.appmanager.service;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.bill56.appmanager.activity.LockAppActivity;
import com.bill56.appmanager.dao.AppManagerDB;
import com.bill56.appmanager.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类名称：WatchAppService
 * 类描述：监听应用程序的后台服务，并对启动的应用程序进行操作
 * Created by Bill56 on 2016/6/5.
 */
public class WatchAppService extends Service {

    private AppManagerDB appManagerDB;
    private boolean flag = true;
    private Intent intentLockAppActivity;
    private ActivityManager activityManager;
    public static String runningApp = null;
    public static String lastRunningApp = null;
    //public static boolean isLock = false;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        appManagerDB = LongRunningService.appManagerDB;
        intentLockAppActivity = new Intent(this,LockAppActivity.class);
        // 服务里面是没有任务栈的，所以要指定一个新的任务栈，不然是无法在服务里面启动activity的
        //intentLockAppActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activityManager = (ActivityManager) getSystemService(Service.ACTIVITY_SERVICE);
        new ArrayList<String>();

        new Thread(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                while(flag){
                    try{
                        // 得到当前运行的任务栈，参数就是得到多少个任务栈，1就是只拿一个任务栈
                        // 对应的也就是正在运行的任务栈啦 ,注意别忘了在清单文件中添加获取的权限
                        List<ActivityManager.RunningTaskInfo> runTaskInfos =
                                activityManager.getRunningTasks(1);
                        //拿到当前运行的任务栈
                        ActivityManager.RunningTaskInfo runningTaskInfo = runTaskInfos.get(0);
                        //拿到要运行的Activity的包名
                        String runningpackageName = runningTaskInfo.topActivity.getPackageName();
                        LogUtil.i(LogUtil.TAG,"当前运行的包名：" + runningpackageName);
                        //判断监听的运行包是否加锁
                        if(appManagerDB.find(runningpackageName)){
                            runningApp = runningpackageName;
                            //解决反复出现验证页面BUG：
                            //如果runningApp.equals(lastRunningApp)=true
                            //则说明当前栈顶运行的程序已经解锁了
                            if((runningApp.equals(lastRunningApp)) == false){
                                intentLockAppActivity.putExtra("packageName", runningpackageName);
                                intentLockAppActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intentLockAppActivity);
                            }
                        }else{

                        }
                        //让线程每半秒执行一次
                        sleep(500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //flag = false;
    }
}

