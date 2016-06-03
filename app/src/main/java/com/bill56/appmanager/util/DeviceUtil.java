package com.bill56.appmanager.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**设备管理工具类，包括获取SK卡总大小，剩余大小，机身存储大小，机身存储剩余大小等
 * Created by Bill56 on 2016/5/19.
 */
public class DeviceUtil {

    /**
     * 获得SD卡总大小
     * @return  sd卡总大小
     */
    public static long getSDTotalSize() {
        // 存在外部存储卡才执行
        if (!Environment.isExternalStorageEmulated()) {
            return 1;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }
    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static long getSDAvailableSize() {
        // 存在外部存储卡才执行
        if (!Environment.isExternalStorageEmulated()) {
            return 0;
        }
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }
    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static long getRomTotalSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }
    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static long getRomAvailableSize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

}
