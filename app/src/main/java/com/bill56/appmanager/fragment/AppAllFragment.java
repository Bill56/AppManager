package com.bill56.appmanager.fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.bill56.appmanager.util.DateTimeUtil;
import com.bill56.appmanager.util.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 加载所有应用的碎片
 * Created by Bill56 on 2016/5/30.
 */
public class AppAllFragment extends Fragment {

    // 上下文环境
    private Context mContext;
    // 显示当前时间
    private TextView textCurrentDate;
    // 显示app信息的列表项
    private ListView listApps;
    // 适配器
    private AppInfoAdapter appAdapter;
    // 数据
    private List<AppInfo> appData;

    private static AppAllFragment instance;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        LogUtil.d(LogUtil.TAG, mContext == null ? "null" : "not null");
    }

    /**
     * 获得实例
     *
     * @return 碎片实例
     */
    public static AppAllFragment getInstance() {
        if (instance == null)
            instance = new AppAllFragment();
        return instance;
    }

    /**
     * 获得数据列表
     *
     * @return 数据列表
     */
    public List<AppInfo> getAppData() {
        return appData;
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
        getAppsInfo();
        // 设置适配器
        appAdapter = new AppInfoAdapter(mContext, appData);
        listApps.setAdapter(appAdapter);
        listApps.setEmptyView(v.findViewById(R.id.ll_app_list_empty));
    }

    private void getAppsInfo() {
        // 获得包管理器
        PackageManager pm = mContext.getPackageManager();
        // 获得所有已安装的程序包
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        // 创建自定义的数据列表
        appData = new ArrayList<>();
        // 将信息写入自定义类中
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo pack = packs.get(i);
            // 如果是用户自己安装的应用，则显示出来
            AppInfo app = new AppInfo();
            // 设置图标
            app.setAppIcon(pack.applicationInfo.loadIcon(pm));
            // 设置名称
            app.setAppName(pack.applicationInfo.loadLabel(pm).toString());
            // 设置包名
            app.setAppPackageName(pack.packageName);
            // 获得大小
            File apk = new File(pack.applicationInfo.publicSourceDir);
            // 设置大小
            app.setAppSize(apk.length());
            // 设置应用类型是否为用户下载应用
            app.setIsUserApp(filterApp(pack.applicationInfo));
            // 添加到列表
            appData.add(app);
        }
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
