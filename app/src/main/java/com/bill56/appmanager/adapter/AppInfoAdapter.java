package com.bill56.appmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.util.AppUtil;

import java.util.List;

/**
 * App显示列表的适配器
 * Created by Bill56 on 2016/5/30.
 */
public class AppInfoAdapter extends BaseAdapter {

    // 上下文
    private Context mContext;
    // 数据列表
    private List<AppInfo> mData;
    // 加载布局的对象
    private LayoutInflater inflater;

    /**
     * 构造方法
     *
     * @param context   上下文
     * @param data  数据
     */
    public AppInfoAdapter(Context context, List<AppInfo> data) {
        this.mContext = context;
        this.mData = data;
        // 加载布局的服务对象
        inflater = LayoutInflater.from(context);
    }

    /**
     * 获得列表数目
     *
     * @return
     */
    @Override
    public int getCount() {
        return mData.size();
    }

    /**
     * 根据位置获得列表中某一项的条目
     *
     * @param position 位置
     * @return 列表项
     */
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据位置获得列表中某一项的id
     *
     * @param position 位置
     * @return 某一项的id
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 获得每一项布局的时候加载
     *
     * @param position    位置
     * @param convertView 封装视图的对象
     * @param parent      父布局
     * @return 视图对象
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        // 加载模板，创建视图项——将布局创建成一个View对象
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_fragment_apps, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AppInfo appInfo = mData.get(position);
        viewHolder.bindData(appInfo);
        return convertView;
    }

    /**
     * 暂存视图对象的类
     */
    private class ViewHolder {
        ImageView imageAppIcon;
        TextView textAppName;
        TextView textAppSize;
        TextView textAppUseTime;

        /**
         * 构造方法，获得布局中各个视图的引用
         *
         * @param v 视图对象
         */
        public ViewHolder(View v) {
            imageAppIcon = (ImageView) v.findViewById(R.id.image_app_icon);
            textAppName = (TextView) v.findViewById(R.id.text_app_name);
            textAppSize = (TextView) v.findViewById(R.id.text_app_size);
            textAppUseTime = (TextView) v.findViewById(R.id.text_app_use_time);
        }

        /**
         * 绑定数据
         *
         * @param appInfo 文件操作对象
         */
        public void bindData(AppInfo appInfo) {
            // 设置图标，如果是文件则显示文件图标，否则显示文件夹图标
            imageAppIcon.setImageDrawable(appInfo.getAppIcon());
            textAppName.setText(appInfo.getAppName());
            // 将文件大小进行字符串格式化
            textAppSize.setText(AppUtil.longSizeToStrSize(mContext,appInfo.getAppSize()));
            textAppUseTime.setText("01:02:33");
        }

    }

}
