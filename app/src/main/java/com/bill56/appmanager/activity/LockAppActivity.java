package com.bill56.appmanager.activity;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bill56.appmanager.R;
import com.bill56.appmanager.service.WatchAppService;
import com.bill56.appmanager.util.KeyBordUtil;
import com.bill56.appmanager.util.ToastUtil;

/**
 * Created by Bill56 on 2016/6/5.
 */
public class LockAppActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener {

    private ImageView ivLockAppIcon;
    private TextView tvLockAppName;
    private EditText etInputPwd;
    private Button btnConfirm;
    private String packageName;
    private String passWord;
    private SharedPreferences preferences;
    //public static boolean isLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_pwd);
        //WatchAppService.isLock = false;
        ivLockAppIcon = (ImageView) findViewById(R.id.iv_lock_app_icon);
        tvLockAppName = (TextView) findViewById(R.id.tv_lock_app_name);
        etInputPwd = (EditText) findViewById(R.id.et_lock_pwd);
        etInputPwd.setOnTouchListener(this);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        packageName = getIntent().getStringExtra("packageName");

        try {
            //通过包名拿到applicationInfo
            ApplicationInfo appInfo = getPackageManager().getPackageInfo(packageName, 0).applicationInfo;
            //应用图标
            Drawable app_icon = appInfo.loadIcon(getPackageManager());
            //应用的名字
            String app_name = appInfo.loadLabel(getPackageManager()).toString();
            ivLockAppIcon.setImageDrawable(app_icon);
            tvLockAppName.setText(app_name);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    //不让用户按后退键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        //屏蔽后退键
        if(KeyEvent.KEYCODE_BACK == event.getKeyCode())
        {
            return true;//阻止事件继续向下分发
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        String input = etInputPwd.getText().toString().trim();
        preferences = getSharedPreferences("passWord", MODE_PRIVATE);
        passWord = preferences.getString("pwd", "");
        if(TextUtils.isEmpty(input))
        {
            ToastUtil.show(this,R.string.lock_pwd_empty);
        }
        else if(passWord.equals(input))
        {
            //这里赋值，终于解决了反复弹出验证页面的BUG
            WatchAppService.lastRunningApp = WatchAppService.runningApp;
            finish();
        }
        else
        {
            ToastUtil.show(this,R.string.lock_pwd_error);
            etInputPwd.setText("");//置空
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        //这样是在触摸到控件时，软键盘才会显示出来
        int inputback = etInputPwd.getInputType();
        etInputPwd.setInputType(InputType.TYPE_NULL);
        new KeyBordUtil(this, this, etInputPwd).showKeyboard();
        etInputPwd.setInputType(inputback);
        return false;
    }

}

