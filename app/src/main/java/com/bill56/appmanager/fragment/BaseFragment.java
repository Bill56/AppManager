package com.bill56.appmanager.fragment;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.support.v4.app.Fragment;

import com.bill56.appmanager.R;

/**
 * Created by Bill56 on 2016/6/3.
 */
public class BaseFragment extends Fragment {

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
