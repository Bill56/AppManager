package com.bill56.appmanager.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.util.ToastUtil;

/**
 * 使用的历史记录的活动，以日历表的形式给出
 * Created by Bill56 on 2016/6/3.
 */
public class UsingHistoyActivity extends BaseActivity {

    // 应用栏
    private Toolbar toolbar;
    // 日历
    private CalendarView calendarUsing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_history);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        // 用工具栏替代操作栏
        setSupportActionBar(toolbar);
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
                ToastUtil.show(UsingHistoyActivity.this,String.format("%d-%d-%d",year,month+1,dayOfMonth));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
