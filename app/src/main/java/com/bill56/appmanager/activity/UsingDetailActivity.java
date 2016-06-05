package com.bill56.appmanager.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.adapter.UsingDetailAdapter;
import com.bill56.appmanager.entity.DatetimeApp;
import com.bill56.appmanager.entity.DatetimeScreen;
import com.bill56.appmanager.entity.TimeFieldApp;
import com.bill56.appmanager.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * 使用情况的详细活动
 * Created by Bill56 on 2016/6/5.
 */
public class UsingDetailActivity extends BaseActivity {

    // 应用栏
    private Toolbar toolbar;
    // 列表视图
    private ListView listUsingApps;
    // 传递的数据：app使用记录
    private ArrayList<DatetimeApp> appsData;
    // 传递的数据：手机使用记录
    private ArrayList<DatetimeScreen> screensData;
    // 实际应该装入适配器的数据
    private ArrayList<TimeFieldApp> data;
    // 适配器
    UsingDetailAdapter detailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_detail);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        // 用工具栏替代操作栏
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 当前日期为标题
        getSupportActionBar().setTitle(getIntent().getStringExtra("USING_DATE"));
        initList();
    }

    /**
     * 初始化数据
     */
    private void initList() {
        listUsingApps = (ListView) findViewById(R.id.list_using_apps);
        // 获取intent中的序列化对象
        appsData = (ArrayList<DatetimeApp>) getIntent().getSerializableExtra("USING_APPS");
        screensData = (ArrayList<DatetimeScreen>) getIntent().getSerializableExtra("USING_SCREENS");
        // 对获取到的数据进行分析，将其按时间段进行分类放入到时间段app使用对象中
        analysisAppsData();
        // 装载适配器
        detailAdapter = new UsingDetailAdapter(this, data);
        listUsingApps.setAdapter(detailAdapter);
        listUsingApps.setEmptyView(findViewById(R.id.ll_using_app_list_empty));
    }

    /**
     * 分析数据
     */
    private void analysisAppsData() {
        // 装载统计信息的map
        HashMap<String,TimeFieldApp> map = new HashMap<>();
        // 遍历应用数据
        for (int i=0;i<appsData.size();i++) {
            DatetimeApp app = appsData.get(i);
            // 如果Map中已经有对应的时间段字符串
            if (map.containsKey(app.getTime())) {
                // 统计app个数，更新对应的值，将其加1
                TimeFieldApp tf = map.get(app.getTime());
                int old = tf.getAppNumber();
                tf.setAppNumber(old + 1);
                // 统计启动次数
                int oldTime = tf.getAppStrupNumber();
                tf.setAppStrupNumber(oldTime + app.getNumber());
            } else {
                //　不存在，则创建
                TimeFieldApp tf = new TimeFieldApp();
                tf.setTime(app.getTime());
                tf.setAppNumber(1);
                tf.setAppStrupNumber(app.getNumber());
                map.put(app.getTime(),tf);
            }
        }
        // 遍历屏幕使用数据
        for(int i = 0;i<screensData.size();i++) {
            DatetimeScreen screen = screensData.get(i);
            // 如果map中已经有对应的时间段字符串
            if (map.containsKey(screen.getTime())) {
                // 将屏幕使用时长和锁屏次数写入
                TimeFieldApp tf = map.get(screen.getTime());
                tf.setScreenOff(screen.getScreenTime());
                tf.setUseTime(screen.getUseTime());
            } else {
                // 不存在不管
            }
        }
        // 处理完毕后，将map中的键和对应的值进行封装
        Set<String> set = map.keySet();
        data = new ArrayList<>();
        for (String keyTime : set) {
            TimeFieldApp tfapp = map.get(keyTime);
            data.add(tfapp);
            LogUtil.i(LogUtil.TAG, tfapp.toString());
        }

        /*// 临时装载统计信息的map
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Integer> mapTime = new HashMap<>();
        // 遍历app数据
        for (int i = 0; i < appsData.size(); i++) {
            DatetimeApp app = appsData.get(i);
            // 如果包含了时间段字符串
            if (map.containsKey(app.getTime())) {
                // 统计app个数，更新对应的值，将其加1
                int old = map.get(app.getTime());
                int n = old + 1;
                map.put(app.getTime(), n);
                // 统计启动次数
                int oldTime = mapTime.get(app.getTime());
                int nTime = oldTime + app.getNumber();
                mapTime.put(app.getTime(), nTime);
            } else {
                // 不存在，则直接放入,值为1
                map.put(app.getTime(), 1);
                mapTime.put(app.getTime(), app.getNumber());
            }
        }
        // 遍历屏幕数据

        // 处理完毕后，将map中的键和对应的值进行封装
        Set<String> set = map.keySet();
        data = new ArrayList<>();
        for (String keyTime : set) {
            TimeFieldApp tfapp = new TimeFieldApp();
            tfapp.setTime(keyTime);
            tfapp.setAppNumber(map.get(keyTime));
            tfapp.setAppStrupNumber(mapTime.get(keyTime));
            data.add(tfapp);
            LogUtil.i(LogUtil.TAG, tfapp.toString());
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }


}
