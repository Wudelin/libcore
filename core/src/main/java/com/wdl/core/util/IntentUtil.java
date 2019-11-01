package com.wdl.core.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.File;

/**
 * Create by: wdl at 2019/11/1 9:22
 * 常用意图
 */
@SuppressWarnings("unused")
public final class IntentUtil
{
    private static final String SMS_URI = "smsto:";
    private static final String SMS_BODY = "sms_body";

    private IntentUtil()
    {
    }

    /**
     * 获取打开外链的意图
     *
     * @param url url路径
     * @return Intent
     */
    public static Intent getBroseIntent(String url)
    {
        if (TextUtils.isEmpty(url)) return null;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

    /**
     * 跳转APP设置界面意图
     *
     * @return Intent
     */
    public static Intent getSettingIntent()
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", WLibrary.getContext().getPackageName(), null));
        return intent;
    }

    /**
     * 跳转wifi设置意图
     *
     * @return Intent
     */
    public static Intent getWifiSettingIntent()
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_WIFI_SETTINGS);
        return intent;
    }

    /**
     * 跳转网络设置意图
     *
     * @return Intent
     */
    public static Intent getWirelessSettingIntent()
    {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
        return intent;
    }

    /**
     * 获取跳转意图
     *
     * @param packageName 包名
     * @param className   类名
     * @param bundle      bundle
     * @param isNewTask   是否为新栈
     * @return Intent
     */
    public static Intent getComponentIntent(String packageName, String className, Bundle bundle, boolean isNewTask)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) intent.putExtras(bundle);
        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);
        return getIsNewTaskIntent(intent, isNewTask);
    }

    /**
     * 获取发送短信意图
     *
     * @param phoneNum 电话号码
     * @param content  短信内容
     * @return Intent
     */
    public static Intent getSmsIntent(String phoneNum, String content)
    {
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(content))
            return null;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(SMS_URI + phoneNum));
        intent.putExtra(SMS_BODY, content);
        return intent;
    }

    /**
     * 获取打电话意图,需要<uses-permission android:name="android.permission.CALL_PHONE"/>权限
     *
     * @param phoneNum 电话号码
     * @return Intent
     */
    public static Intent getCallIntent(String phoneNum)
    {
        if (TextUtils.isEmpty(phoneNum)) return null;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        return intent;
    }

    /**
     * 获取跳转拨号界面意图
     *
     * @param phoneNum 电话号码
     * @return Intent
     */
    public static Intent getDialIntent(String phoneNum)
    {
        if (TextUtils.isEmpty(phoneNum)) return null;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        return intent;
    }

    /**
     * 获取发送邮件意图
     *
     * @param email   String
     * @param subject String
     * @return Intent
     */
    public static Intent getSendEmailIntent(String email, String subject)
    {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(subject)) return null;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        return intent;

    }

    /**
     * 图库选取照片意图
     *
     * @return Intent
     */
    public static Intent getSelectImageFromAlbumIntent()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /**
     * 拍照意图
     *
     * @param file    File
     * @param context Context
     * @return Intent
     */
    public static Intent getCaptureIntent(Context context, File file)
    {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, file));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    /**
     * 安装APK的意图，兼容8.0
     * <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
     *
     * @param context Context
     * @param file    File
     * @return Intent
     */
    public static Intent getInstallAPPIntent(Context context, File file)
    {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        FileProvider.setIntentDataAndType(context, intent, type, file, true);
        return intent;
    }

    /**
     * 卸载APP意图
     *
     * @param packageName packageName
     * @return Intent
     */
    public static Intent getUninstallAppIntent(String packageName)
    {
        if (TextUtils.isEmpty(packageName)) return null;
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:"+packageName));
        return intent;
    }

    /**
     * 是否开启新栈
     *
     * @param intent    Intent
     * @param isNewTask 是否开启新栈
     * @return Intent
     */
    private static Intent getIsNewTaskIntent(Intent intent, boolean isNewTask)
    {
        return isNewTask ? intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) : intent;
    }

    /**
     * 判断意图是否有效
     *
     * @param context Context
     * @param intent  Intent
     * @return true有效
     */
    public static boolean isIntentAvailable(Context context, Intent intent)
    {
        if (intent == null) return false;
        return context.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }
}
