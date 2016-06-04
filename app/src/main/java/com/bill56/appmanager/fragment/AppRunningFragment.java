package com.bill56.appmanager.fragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.adapter.AppInfoAdapter;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.util.AppUtil;
import com.bill56.appmanager.util.DateTimeUtil;
import com.bill56.appmanager.util.DeviceUtil;
import com.bill56.appmanager.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bill56 on 2016/5/30.
 */
public class AppRunningFragment extends BaseFragment {

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

    // 本身实例
    private static AppRunningFragment instance;

    // 可用大小
    private TextView textValibelSize;
    // 总大小
    private TextView textTotalSize;
    // 进度框
    private ProgressBar progressSizePer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        LogUtil.d(LogUtil.TAG, mContext == null ? "null" : "not null");
    }

    public static AppRunningFragment getInstance() {
        if (instance == null)
            instance = new AppRunningFragment();
        return instance;
    }

    /**
     * 获得数据列表
     *
     * @return 数据列表
     */
    public List<AppInfo> getAppData() {
        return runningAppInfos;
    }

    /**
     * 获得应用列表控件
     *
     * @return
     */
    public ListView getListApps() {
        return listApps;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_running, container, false);
        initList(v);
        caculateRomSize(v);
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
     * 计算机身存储的百分比
     *
     * @param v 视图
     */
    private void caculateRomSize(View v) {
        // 初始化控件
        textValibelSize = (TextView) v.findViewById(R.id.text_show_valible);
        textTotalSize = (TextView) v.findViewById(R.id.text_show_total);
        progressSizePer = (ProgressBar) v.findViewById(R.id.progress_size_percent);
        long availibel = DeviceUtil.getRomAvailableSize();
        long total = DeviceUtil.getRomTotalSize();
        // 设置显示
        textValibelSize.setText(getString(R.string.frag_app_running_available) + AppUtil.longSizeToStrSize(mContext, availibel));
        textTotalSize.setText(getString(R.string.frag_app_running_total) + AppUtil.longSizeToStrSize(mContext, total));
        // 设置进度条
        progressSizePer.setProgress((int) (100 - availibel * 100 / total));
    }


    /**
     * 获取正在运行的应用程序
     */
    public void getRunningAppsInfo() {
        // 获得包管理器
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
            // 获得运行在该进程里的所有应用程序包
            String[] pkgNameList = appProcess.pkgList;

            // 输出所有应用程序的包名
            for (int i = 0; i < pkgNameList.length; i++) {
                String pkgName = pkgNameList[i];
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
                runningApp.setIsUserApp(filterApp(app));
                // 获得占用的内存大小
                Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[]{pid});
                long memSize = memoryInfo[0].dalvikPrivateDirty;
                runningApp.setAppSize(memSize);
                runningAppInfos.add(runningApp);
            }
        }
    }

}
