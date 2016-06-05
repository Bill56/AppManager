package com.bill56.appmanager.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.dao.AppManagerDB;
import com.bill56.appmanager.entity.DatetimeApp;
import com.bill56.appmanager.service.LongRunningService;
import com.bill56.appmanager.util.LogUtil;
import com.bill56.appmanager.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用的历史记录的活动，以日历表的形式给出
 * Created by Bill56 on 2016/6/3.
 */
public class UsingHistoyActivity extends BaseActivity {

    // 应用栏
    private Toolbar toolbar;
    // 日历
    private CalendarView calendarUsing;
    // 数据库操作类
    private AppManagerDB appManagerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_history);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        // 用工具栏替代操作栏
        setSupportActionBar(toolbar);
        // 创建数据库操作对象
        appManagerDB = LongRunningService.appManagerDB;
        // 设置顶部返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 初始化日历的一些属性
        initCalendar();
    }

    /**
     * 初始化日历的一些属性设置
     */
    private void initCalendar() {
        // 绑定日历控件
        calendarUsing = (CalendarView) findViewById(R.id.calendar_using);
        calendarUsing.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // 查询数据库
                String date = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                ToastUtil.show(UsingHistoyActivity.this, date);
                ArrayList<DatetimeApp> usingApps = appManagerDB.loadDatetimeAppByDate(date);
                // 查询成功
                // 将数据传递给详情页
                Intent i = new Intent(UsingHistoyActivity.this,UsingDetailActivity.class);
                i.putExtra("USING_APPS",usingApps);
                i.putExtra("USING_DATE",date);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.using_history_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_recent_days:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:*#*#4636#*#*"));
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}
