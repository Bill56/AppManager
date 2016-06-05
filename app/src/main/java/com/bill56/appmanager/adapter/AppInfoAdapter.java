package com.bill56.appmanager.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.dao.AppManagerDB;
import com.bill56.appmanager.entity.AppInfo;
import com.bill56.appmanager.service.LongRunningService;
import com.bill56.appmanager.service.WatchAppService;
import com.bill56.appmanager.util.AppUtil;
import com.bill56.appmanager.util.DateTimeUtil;
import com.bill56.appmanager.util.LogUtil;
import com.bill56.appmanager.util.ToastUtil;

import java.lang.reflect.Field;
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
    // 内部存储
    private SharedPreferences preferencesPwd;
    // 创建数据库操作对象
    private AppManagerDB appManagerDB;
    // 加锁需要的控件
    private EditText etPwd1;
    private EditText etPwd2;
    private EditText etOldPwd;
    private EditText etNewPwd1;
    private EditText etNewPwd2;
    private View viewSetPwd;
    private View viewChangePwd;
    private SharedPreferences.Editor editor;

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param data    数据
     */
    public AppInfoAdapter(Context context, List<AppInfo> data) {
        this.mContext = context;
        this.mData = data;
        // 加载布局的服务对象
        inflater = LayoutInflater.from(context);
        appManagerDB = LongRunningService.appManagerDB;
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
        ImageButton imgBtnMore;
        // 弹出菜单的选项监听器
        PopupMenuClickListener popupMenuClickListener;

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
            imgBtnMore = (ImageButton) v.findViewById(R.id.imgBtn_more);
            popupMenuClickListener = new PopupMenuClickListener();
            // 设置按钮的点击监听器
            imgBtnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 弹出菜单
                    PopupMenu popupMenu = new PopupMenu(mContext, v);
                    // 加载菜单布局文件
                    popupMenu.inflate(R.menu.item_fragment_apps_more_pop);
                    // 为菜单注册监听器
                    popupMenu.setOnMenuItemClickListener(popupMenuClickListener);
                    // 显示菜单
                    popupMenu.show();
                }
            });
        }

        /**
         * 绑定数据
         *
         * @param appInfo 应用操作对象
         */
        public void bindData(AppInfo appInfo) {
            // 设置图标，如果是文件则显示文件图标，否则显示文件夹图标
            imageAppIcon.setImageDrawable(appInfo.getAppIcon());
            textAppName.setText(appInfo.getAppName());
            // 将文件大小进行字符串格式化
            textAppSize.setText(AppUtil.longSizeToStrSize(mContext, appInfo.getAppSize()));
            // 获得当前时间
            long currentTime = System.currentTimeMillis();
            String subTime = DateTimeUtil.caculateSubTime(mContext, appInfo.getAppInstalTime(), currentTime);
            if (subTime == null) {
                subTime = mContext.getString(R.string.frag_adapter_error);
            }
            textAppUseTime.setText(subTime);
            // 设置要操作的应用
            popupMenuClickListener.setAppInfo(appInfo);
        }

    }

    /**
     * 弹出菜单的选项监听器
     */
    class PopupMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        AppInfo appInfo;

        /**
         * 设置要操作的应用
         *
         * @param appInfo 要操作的应用引用
         */
        public void setAppInfo(AppInfo appInfo) {
            this.appInfo = appInfo;
        }

        /**
         * 菜单项点击后执行
         *
         * @param item 菜单项，即时间源头
         * @return true表示该控件已经处理完该事件，不用交给上层控件处理，否则需交给上层控件处理
         */
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // 根据点击的菜单项的id进行不同的处理
            switch (item.getItemId()) {
                case R.id.action_run:
                    doRun();
                    break;
                case R.id.action_lock:
                    doLock();
                    break;
                case R.id.action_share:
                    doShare();
                    break;
                case R.id.action_unload:
                    doUnload();
                    break;
                default:
                    break;
            }
            return true;
        }


        /**
         * 处理点击运行菜单后的方法
         */
        private void doRun() {
            try {
                PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(appInfo.getAppPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
                LogUtil.i(LogUtil.TAG, packageInfo.packageName);
                Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
                resolveIntent.setPackage(packageInfo.packageName);
                PackageManager pManager = mContext.getPackageManager();
                List apps = pManager.queryIntentActivities(
                        resolveIntent, 0);

                ResolveInfo ri = (ResolveInfo) apps.iterator().next();
                if (ri != null) {
                    String packageName = ri.activityInfo.packageName;
                    String className = ri.activityInfo.name;
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cn = new ComponentName(packageName, className);
                    intent.setComponent(cn);
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.show(mContext, R.string.frag_adapter_no_run);
                }
                //扫描出来的所以activity节点的信息
                /*ActivityInfo[] activityInfos = packageInfo.activities;
                if (activityInfos == null) {
                    LogUtil.i(LogUtil.TAG,"null");
                    return ;
                }
                if (activityInfos.length == 0 ) {
                    LogUtil.i(LogUtil.TAG,"== 0");
                    return;
                }
                if(activityInfos != null && activityInfos.length > 0 ) {
                    //在扫描出来的应用里面，第一个是具有启动意义的
                    ActivityInfo startActivity = activityInfos[0];
                    //设置Intent，启动activity,这里使用异常处理，对启动结果进行处理
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClassName(appInfo.getAppPackageName(), startActivity.name);
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.show(mContext,R.string.frag_adapter_no_run);
                }*/
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                ToastUtil.show(mContext, R.string.frag_adapter_run_fail);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.show(mContext, R.string.frag_adapter_run_fail);
            }
        }

        /**
         * 处理点击加锁菜单后的方法
         */
        private void doLock() {
            //设置密码页面控件
            viewSetPwd = inflater.inflate(R.layout.set_pwd,null);
            etPwd1 = (EditText) viewSetPwd.findViewById(R.id.et_pwd1);
            etPwd2 = (EditText) viewSetPwd.findViewById(R.id.et_pwd2);
            viewChangePwd = inflater.inflate(R.layout.change_pwd, null);
            etOldPwd = (EditText) viewChangePwd.findViewById(R.id.et_old_pwd);
            etNewPwd1 = (EditText) viewChangePwd.findViewById(R.id.et_new_pwd1);
            etNewPwd2 = (EditText) viewChangePwd.findViewById(R.id.et_new_pwd2);
            preferencesPwd = mContext.getSharedPreferences("passWord", mContext.MODE_PRIVATE);
            editor = preferencesPwd.edit();
            String itemPackageName = appInfo.getAppPackageName();
            String pwd = preferencesPwd.getString("pwd", "");
            if (/*tv_app_lock.getText().equals("加锁")*/true) {
                if (TextUtils.isEmpty(pwd)) {
                    setPassWord(itemPackageName);
                } else {
                    appManagerDB.add(itemPackageName);
                    ToastUtil.show(mContext, R.string.frag_adapter_lock_success);
                }
            } else if (/*tv_app_lock.getText().equals("解锁")*/false) {
                unLock(itemPackageName);
            }
            notifyDataSetChanged();
        }

        /**
         * 处理点击分享菜单后的方法
         */
        private void doShare() {
            Intent shareintent = new Intent();
            //设置Intent的action
            shareintent.setAction(Intent.ACTION_SEND);
            //设定分享的类型是纯文本的
            shareintent.setType("text/plain");
            //设置分享主题
            shareintent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.frag_adapter_share_title));
            //设置分享的文本
            shareintent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.frag_adapter_share_front) + appInfo.getAppName() + "\n" + mContext.getString(R.string.frag_adapter_share_behind));
            mContext.startActivity(shareintent);
        }

        /**
         * 处理点击卸载菜单后的方法
         */
        private void doUnload() {
            if (!appInfo.isUserApp()) {
                ToastUtil.show(mContext, R.string.frag_adapter_system_unload_fail);
            } else {
                String strUri = "package:" + appInfo.getAppPackageName();
                //通过uri去访问你要卸载的包名
                Uri uri = Uri.parse(strUri);
                Intent delectIntent = new Intent();
                delectIntent.setAction(Intent.ACTION_DELETE);
                delectIntent.setData(uri);
                mContext.startActivity(delectIntent);
                mData.remove(appInfo);
                notifyDataSetChanged();
            }
        }

        /**
         * 解锁的方法
         *
         * @param packageName 应用程序包名
         */
        public void unLock(final String packageName) {
            final EditText pwd = new EditText(mContext);
            pwd.setHint(R.string.frag_input_pwd);
            pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            new AlertDialog.Builder(mContext).setTitle(R.string.frag_input_pwd_avlidate).setIcon(
                    R.drawable.ic_setting_pwd).setView(
                    pwd).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    String stringPwd = preferencesPwd.getString("pwd", "");
                    if (TextUtils.isEmpty(pwd.getText().toString())) {
                        boolean isVisible = false;
                        dialogView(dialog, isVisible);
                        ToastUtil.show(mContext, R.string.frag_input_pwd);
                    } else if ((pwd.getText().toString()).equals(stringPwd)) {
                        appManagerDB.delete(packageName);
                        boolean isVisible = true;
                        dialogView(dialog, isVisible);
                        ToastUtil.show(mContext, R.string.frag_adapter_unlock_success);
                    } else {
                        boolean isVisible = false;
                        dialogView(dialog, isVisible);
                        ToastUtil.show(mContext, R.string.lock_pwd_error);
                    }
                }
            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            boolean isVisible = true;
                            dialogView(dialog, isVisible);
                        }
                    }).show();
        }

        /**
         * 功能：第一次加锁设置密码
         *
         * @param itemPackageName
         */
        public void setPassWord(final String itemPackageName) {
            new AlertDialog.Builder(mContext).setTitle("设置安全锁密钥").setIcon(R.drawable.ic_setting_pwd)
                    .setView(viewSetPwd)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            if ((etPwd1.getText().toString()).equals(etPwd2.getText().toString())) {
                                if (TextUtils.isEmpty(etPwd1.getText().toString()) | TextUtils.isEmpty(etPwd2.getText().toString())) {
                                    ToastUtil.show(mContext,R.string.lock_pwd_empty);
                                    boolean isVisible = false;
                                    dialogView(dialog, isVisible);
                                } else {
                                    editor.putString("pwd", etPwd1.getText().toString());
                                    editor.commit();
                                    appManagerDB.add(itemPackageName);
                                    ToastUtil.show(mContext, "密码设置成功");
                                    ToastUtil.show(mContext,R.string.frag_adapter_lock_success);
                                    boolean isVisible = true;
                                    dialogView(dialog, isVisible);
                                }
                            } else {
                                etPwd1.setText("");
                                etPwd2.setText("");
                                ToastUtil.show(mContext,R.string.frag_input_pwd_nomatch);
                                boolean isVisible = false;
                                dialogView(dialog, isVisible);
                            }
                        }
                    })
                    .setNeutralButton(R.string.input_button_reset, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            etPwd1.setText("");
                            etPwd2.setText("");
                            boolean isVisible = false;
                            dialogView(dialog, isVisible);
                        }
                    })
                    .setNegativeButton(R.string.input_button_cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            boolean isVisible = true;
                            dialogView(dialog, isVisible);
                        }
                    })
                    .create().show();
        }

        /**
         * 功能：控制popupwindow对话框消失与显示
         *
         * @param dialog
         * @param isVisible
         */
        public void dialogView(DialogInterface dialog, boolean isVisible) {
            try {
                //实现点击重置对话框不消失
                Field field;
                field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                field.setAccessible(true);
                //设置mShowing值，欺骗android系统
                field.set(dialog, isVisible);  //需要关闭的时候将这个参数设置为true 他就会自动关闭了
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /**
         * 功能：重置密码
         */
        public void changePwd() {
            new AlertDialog.Builder(mContext).setTitle("密码设置").setView(viewChangePwd).
                    setIcon(R.drawable.ic_setting_pwd).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    String stringPwd = preferencesPwd.getString("pwd", "");
                    if ((etOldPwd.getText().toString()).equals(stringPwd)) {
                        if ((etNewPwd1.getText().toString()).equals(etNewPwd2.getText().toString())) {
                            editor.putString("pwd", etNewPwd1.getText().toString());
                            editor.commit();
                            ToastUtil.show(mContext,"密码重置成功");
                            boolean isVisible = true;
                            dialogView(dialog, isVisible);
                        } else {
                            etNewPwd1.setText("");
                            etNewPwd2.setText("");
                            boolean isVisible = false;
                            dialogView(dialog, isVisible);
                            ToastUtil.show(mContext,R.string.frag_input_pwd_nomatch);
                        }
                    } else {
                        boolean isVisible = false;
                        dialogView(dialog, isVisible);
                        ToastUtil.show(mContext,"原始密码错误");
                    }
                }
            })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            boolean isVisible = true;
                            dialogView(dialog, isVisible);
                        }
                    }).show();
        }

    }

}
