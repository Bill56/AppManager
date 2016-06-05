package com.bill56.appmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.entity.TimeFieldApp;

import java.util.ArrayList;

/**
 * 加载历史记录详细页的布局适配器
 * Created by Bill56 on 2016/6/5.
 */
public class UsingDetailAdapter extends BaseAdapter {

    // 上下文环境
    private Context mContext;
    // 数据
    private ArrayList<TimeFieldApp> mData;
    // 布局加载服务
    private LayoutInflater inflater;

    public UsingDetailAdapter(Context context, ArrayList<TimeFieldApp> data) {
        this.mContext = context;
        this.mData = data;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 获得数据数量
     *
     * @return 数据列表元素数量
     */
    @Override
    public int getCount() {
        return mData.size();
    }

    /**
     * 获得数据中指定位置的元素
     *
     * @param position 元素索引
     * @return 元素对象
     */
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 获得视图
     *
     * @param position    位置
     * @param convertView 保存视图的布局
     * @param parent      父布局
     * @return 加载的视图项
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        // 加载模板，创建视图项——将布局创建成一个View对象
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_using_detail, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TimeFieldApp appInfo = mData.get(position);
        viewHolder.bindData(appInfo);
        return convertView;
    }

    /**
     * 暂存视图对象的类
     */
    private class ViewHolder {
        TextView textUsingTime;
        TextView textUsingAppNumber;
        TextView textUsingAppStrup;

        /**
         * 构造方法，获得布局中各个视图的引用
         *
         * @param v 视图对象
         */
        public ViewHolder(View v) {
            textUsingTime = (TextView) v.findViewById(R.id.tex_time);
            textUsingAppNumber = (TextView) v.findViewById(R.id.text_app_number);
            textUsingAppStrup = (TextView) v.findViewById(R.id.text_app_strup);
        }

        /**
         * 绑定数据
         *
         * @param appInfo 文件操作对象
         */
        public void bindData(TimeFieldApp appInfo) {
            textUsingTime.setText(appInfo.getTime());
            textUsingAppNumber.setText(String.valueOf(appInfo.getAppNumber()));
            textUsingAppStrup.setText(String.valueOf(appInfo.getAppStrupNumber()));
        }

    }

}
