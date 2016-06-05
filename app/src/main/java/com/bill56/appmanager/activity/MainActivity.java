package com.bill56.appmanager.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.adapter.AppInfoAdapter;
import com.bill56.appmanager.broadcastreciever.ScreenActionReceiver;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.fragment.AppAllFragment;
import com.bill56.appmanager.fragment.AppRunningFragment;
import com.bill56.appmanager.fragment.BaseFragment;
import com.bill56.appmanager.service.LongRunningService;
import com.bill56.appmanager.util.LogUtil;
import com.bill56.appmanager.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主活动，显示当前运行的所有程序，包括每个程序的大小和使用时长，分为全部和运行中
 */
public class MainActivity extends BaseActivity {

    // 导航控件
    private TabLayout tabLayout;
    // 工具栏
    private Toolbar toolbar;
    // 滑动对象
    private ViewPager viewPagerNext;
    // 导航适配器
    private TabAdaper tabAdaper;
    // 当前的碎片
    Fragment currentFragment;
    // 记录按下back键后的毫秒数
    private long lastBackPressed;

    // 设备安装的所有应用程序列表
    private List<AppInfo> apps;
    // 设备安装程序的适配器
    private ListView appList;
    // 监听屏幕锁屏的广播
    private ScreenActionReceiver screenActionReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化导航
        initTab();
        // 启动服务
        startService(new Intent(this, LongRunningService.class));
        // 注册屏幕监听广播
        screenActionReceiver = ScreenActionReceiver.getInstance();
        screenActionReceiver.registerScreenActionReceiver(this);
    }

    /**
     * 初始化导航布局
     */
    private void initTab() {
        // 初始化导航布局
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        // 设置顶部返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 初始化ViewPager
        viewPagerNext = (ViewPager) findViewById(R.id.viewPagerApp);
        tabAdaper = new TabAdaper(getSupportFragmentManager());
        viewPagerNext.setAdapter(tabAdaper);
        // 将标签与ViewPager进行绑定
        tabLayout.setupWithViewPager(viewPagerNext);
    }

    /**
     * 加载选项菜单
     *
     * @param menu 菜单对象
     * @return true表示不需要提交给父布局
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_option, menu);
        // 获得菜单中的某一项
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.activity_main_search_title));
        // 设置输入框的事件监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            /**
             * 提交的时候执行
             * @param query 搜索框中输入的文字
             * @return
             */
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastUtil.show(MainActivity.this, query);
                return true;
            }

            /**
             * 当输入框中的文本变化的时候执行
             * @param newText   变化后的文本
             * @return
             */
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    /**
     * 当选项菜单被选中的时候执行
     *
     * @param item 选中的菜单项
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.group_sort) {
            // 设为选中
            item.setChecked(true);
        }
        // 判断当前是哪个碎片
        if (currentFragment instanceof AppAllFragment) {
            // 获得列表
            apps = AppAllFragment.getInstance().getAppData();
            // 获得应用列表
            appList = AppAllFragment.getInstance().getListApps();
        } else /*if (currentFragment instanceof AppRunningFragment)*/ {
            // 获得列表
            apps = AppRunningFragment.getInstance().getAppData();
            LogUtil.d(LogUtil.TAG, apps == null ? "null" : "not null");
            // 获得应用列表
            appList = AppRunningFragment.getInstance().getListApps();
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_use_history:
                startActivity(new Intent(this, UsingHistoyActivity.class));
                break;
            case R.id.action_sort_all:
                doSortAll();
                break;
            case R.id.action_sort_system:
                doSortSystem();
                break;
            case R.id.action_sort_user:
                doSortUser();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁注册
        screenActionReceiver.unRegisterScreenActionReceiver(this);
    }

    /**
     * 只获得用户app
     */
    private void doSortUser() {
        List<AppInfo> userApp = new ArrayList<>();
        // 遍历所有app
        for (AppInfo app : apps) {
            if (app.isUserApp())
                userApp.add(app);
        }
        appList.setAdapter(new AppInfoAdapter(this, userApp));
    }

    /**
     * 只获得系统app
     */
    private void doSortSystem() {
        List<AppInfo> systemApp = new ArrayList<>();
        // 遍历所有app
        for (AppInfo app : apps) {
            if (!app.isUserApp())
                systemApp.add(app);
        }
        appList.setAdapter(new AppInfoAdapter(this, systemApp));
    }

    /**
     * 获得所有app
     */
    private void doSortAll() {
        appList.setAdapter(new AppInfoAdapter(this, apps));
    }


    /**
     * 自定的导航类
     */
    private class TabAdaper extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        // 标题数组
        int[] titles = {R.string.activity_tab_all, R.string.activity_tab_running};

        public TabAdaper(FragmentManager fm) {
            super(fm);
            // 获得应用列表实例
            AppAllFragment appAllFg = AppAllFragment.getInstance();
            AppRunningFragment runningFragment = AppRunningFragment.getInstance();
            fragmentList.add(appAllFg);
            fragmentList.add(runningFragment);
//            fragmentList.add(new TechFragment());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(titles[position]);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        /**
         * 获得当前的碎片实例
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (BaseFragment) object;
            super.setPrimaryItem(container, position, object);
        }
    }

    /**
     * 当按下返回键的时候执行
     */
    @Override
    public void onBackPressed() {
        // 获取当前时间
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressed < 2000) {
            super.onBackPressed();
        } else {
            ToastUtil.show(this, R.string.activity_toast_quit);
        }
        lastBackPressed = currentTime;
    }
}
