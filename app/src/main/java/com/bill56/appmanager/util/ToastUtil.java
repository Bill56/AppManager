package com.bill56.appmanager.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Bill56 on 2016/5/30.
 */
public class ToastUtil {

    public static void show(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

}
