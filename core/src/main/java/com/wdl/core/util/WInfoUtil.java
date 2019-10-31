package com.wdl.core.util;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.wdl.core.App;

/**
 * 获取APK版本号、版本名
 */
@SuppressWarnings("unused")
public final class WInfoUtil extends WLibrary{
    private WInfoUtil() {
    }

    /**
     * 获取版本号
     *
     * @return 版本号
     */
    public static int getVersionCode() {
        int versionCode = 0;
        PackageManager pm = getContext().getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取版本名
     *
     * @return 版本名
     */
    public static String getVersionName() {
        String versionName = "";
        PackageManager pm = App.get().getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(getContext().getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
