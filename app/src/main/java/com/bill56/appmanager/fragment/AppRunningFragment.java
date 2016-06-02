package com.bill56.appmanager.fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.adapter.AppInfoAdapter;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.entity.PackagesInfo;
import com.bill56.appmanager.util.DateTimeUtil;
import com.bill56.appmanager.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bill56 on 2016/5/30.
 */
public class AppRunningFragment extends Fragment {

    // 上下文环境
    private Context mContext;
    // 显示当前时间
    private TextView textCurrentDate;
    // 显示app信息的列表项
    private ListView listApps;
    // 适配器
    private AppInfoAdapter appAdapter;
    // 数据
    private List<AppInfo> runningAppInfos;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        LogUtil.d(LogUtil.TAG, mContext == null ? "null" : "not null");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_apps, container, false);
        initList(v);
        return v;
    }

    /**
     * 设置列表显示
     */
    private void initList(View v) {
        listApps = (ListView) v.findViewById(R.id.list_apps);
        textCurrentDate = (TextView) v.findViewById(R.id.text_current_date);
        // 设置当前时间
        textCurrentDate.setText(DateTimeUtil.DateToString(System.currentTimeMillis()));
        // 初始化数据
        getRunningAppsInfo();
        // 设置适配器
        appAdapter = new AppInfoAdapter(mContext, runningAppInfos);
        listApps.setAdapter(appAdapter);
        listApps.setEmptyView(v.findViewById(R.id.ll_app_list_empty));
    }



    /**
     * 获取正在运行的应用程序
     */
    public void getRunningAppsInfo() {
//        PackagesInfo pi = new PackagesInfo(mContext);
//

//        //获取正在运行的应用
//        List<ActivityManager.RunningAppProcessInfo> run = am.getRunningAppProcesses();
//        //获取包管理器，在这里主要通过包名获取程序的图标和程序名

//        List<AppInfo> list = new ArrayList<AppInfo>();
//
//        for(ActivityManager.RunningAppProcessInfo  ra : run) {
//            // AppInfo pr = new AppInfo();
//            LogUtil.d(LogUtil.TAG,ra.processName);
////            pr.setAppIcon(pi.getInfo(ra.processName).loadIcon(pm));
////            pr.setAppName(pi.getInfo(ra.processName).loadLabel(pm).toString());
////            pr.setAppSize(112234);
////            System.out.println(pi.getInfo(ra.processName).loadLabel(pm).toString());
////            list.add(pr);
//        }
//        runningAppInfos = list;
        PackageManager pm = mContext.getPackageManager();
                ActivityManager am = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        // 排序
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));
        // 保存所有正在运行的包名 以及它所在的进程信息
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();
        ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
            int pid = appProcess.pid; // pid
            String processName = appProcess.processName; // 进程名
            LogUtil.d(LogUtil.TAG, "processName: " + processName + "  pid: " + pid);
            // 获得运行在该进程里的所有应用程序包
            String[] pkgNameList = appProcess.pkgList;

            // 输出所有应用程序的包名
            for (int i = 0; i < pkgNameList.length; i++) {
                String pkgName = pkgNameList[i];
                LogUtil.d(LogUtil.TAG, "packageName " + pkgName + " at index " + i + " in process " + pid);
                // 加入至map对象里
                pgkProcessAppMap.put(pkgName, appProcess);
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
                // 获得占用的内存大小
                Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[]{pid});
                long memSize = memoryInfo[0].dalvikPrivateDirty;
                runningApp.setAppSize(memSize);
                runningAppInfos.add(runningApp);
            }
        }
    }

}
