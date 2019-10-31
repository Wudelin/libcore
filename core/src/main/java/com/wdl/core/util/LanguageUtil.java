package com.wdl.core.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.wdl.core.App;

import java.util.List;
import java.util.Locale;

/**
 * Create by: wdl at 2019/10/31 14:32
 * 语言管理
 */
@SuppressWarnings("unused")
public final class LanguageUtil
{
    private LanguageUtil()
    {
    }

    /**
     * 更新语言
     * 注意：在获取Resources对象时，所传的Context在Android8.0以下可以是Activity或者是Application；
     * 但是在8.0以上，只能是Activity，否则切换无效
     *
     * @param locale Locale
     */
    public static void applyLang(Context context, @NonNull final Locale locale)
    {
        final String lang = locale.getLanguage();
        final String country = locale.getCountry();
        // TODO 存入SP中
        updateLang(context, locale);
    }

    /**
     * 更新语言，重启
     *
     * @param context Context
     * @param locale  语言
     */
    private static void updateLang(Context context, Locale locale)
    {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
//        Locale current = getLocal(config);
//        if (equal(current,locale))return;

        DisplayMetrics dm = resources.getDisplayMetrics();
        // 设置语言
        setLocale(context, config, locale);
        resources.updateConfiguration(config, dm);

        // 重启
        reLauncher();
    }

    private static Locale getLocal(Configuration config)
    {
        // 获取系统当前语言
        Locale sysLocal;
        if (Build.VERSION.SDK_INT >= 24)
        {
            sysLocal = config.getLocales().get(0);
        } else
        {
            sysLocal = config.locale;
        }
        return sysLocal;
    }

    /**
     * 重启
     */
    private static void reLauncher()
    {
        Intent intent = new Intent();
        String acName = getLauncherActivity();
        if (TextUtils.isEmpty(acName))
        {
            return;
        }
        intent.setComponent(new ComponentName(App.get(), acName));
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        App.get().startActivity(intent);
    }


    /**
     * 获取LauncherActivity
     * 匹配启动页Activity
     *
     * @return String
     */
    private static String getLauncherActivity()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(App.get().getPackageName());
        PackageManager pm = App.get().getPackageManager();
        List<ResolveInfo> info = pm.queryIntentActivities(intent, 0);
        ResolveInfo next = info.iterator().next();
        if (next != null)
        {
            return next.activityInfo.name;
        }
        return "no launcher activity";
    }


    /**
     * 设置语言
     *
     * @param context       Context
     * @param configuration Configuration
     * @param locale        Locale
     */
    private static void setLocale(Context context, Configuration configuration, Locale locale)
    {
        if (Build.VERSION.SDK_INT >= 17)
        {
            configuration.setLocale(locale);
            context.createConfigurationContext(configuration);
        } else
        {
            configuration.locale = locale;
        }
    }

    private static boolean equal(Locale sysLocal, Locale locale)
    {
        if (sysLocal == null || locale == null)
        {
            return false;
        }
        return sysLocal.getCountry().equals(locale.getCountry()) &&
                sysLocal.getLanguage().equals(locale.getLanguage());
    }
}
