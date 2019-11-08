package com.wdl.core.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * Create by: wdl at 2019/11/8 16:22
 * 获取xml文件中的MetaData
 */
@SuppressWarnings("unused")
public class MetadataUtil
{
    private MetadataUtil()
    {
    }

    /**
     * 获取AndroidManifest.xml 中Application的meta-data对应Key的值
     *
     * @param key key
     * @return value
     */
    public static String getApplicationMetadata(@NonNull final String key)
    {
        PackageManager pm = WLibrary.getContext().getPackageManager();
        String packageName = WLibrary.getContext().getPackageName();
        if (TextUtils.isEmpty(packageName)) return null;
        try
        {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            return ai.metaData == null ? null : String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getActivityMetadata(@NonNull final Activity activity, @NonNull final String key)
    {
        return getActivityMetadata(activity.getClass(), key);
    }

    /**
     * 获取Activity标签中的MetaData
     *
     * @param clazz clazz
     * @param key   key
     * @return String
     */
    public static String getActivityMetadata(@NonNull final Class<? extends Activity> clazz, @NonNull final String key)
    {
        PackageManager pm = WLibrary.getContext().getPackageManager();
        ComponentName cn = new ComponentName(WLibrary.getContext(), clazz);
        try
        {
            ActivityInfo ai = pm.getActivityInfo(cn, PackageManager.GET_META_DATA);
            return ai.metaData == null ? null : String.valueOf(ai.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getServiceMetadata(@NonNull final Service service, @NonNull final String key)
    {
        return getServiceMetadata(service.getClass(), key);
    }

    /**
     * 获取Service标签中的MetaData
     *
     * @param clazz clazz
     * @param key   key
     * @return String
     */
    public static String getServiceMetadata(@NonNull final Class<? extends Service> clazz, @NonNull final String key)
    {
        PackageManager pm = WLibrary.getContext().getPackageManager();
        ComponentName cn = new ComponentName(WLibrary.getContext(), clazz);
        try
        {
            ServiceInfo si = pm.getServiceInfo(cn, PackageManager.GET_META_DATA);
            return si.metaData == null ? null : String.valueOf(si.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getReceiverMetadata(@NonNull final BroadcastReceiver receiver, @NonNull final String key)
    {
        return getReceiverMetadata(receiver.getClass(), key);
    }

    /**
     * 获取BroadcastReceiver标签中的MetaData
     *
     * @param clazz clazz
     * @param key   key
     * @return String
     */
    public static String getReceiverMetadata(@NonNull final Class<? extends BroadcastReceiver> clazz, @NonNull final String key)
    {
        PackageManager pm = WLibrary.getContext().getPackageManager();
        ComponentName cn = new ComponentName(WLibrary.getContext(), clazz);
        try
        {
            ActivityInfo si = pm.getReceiverInfo(cn, PackageManager.GET_META_DATA);
            return si.metaData == null ? null : String.valueOf(si.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProviderMetadata(@NonNull final ContentProvider provider, @NonNull final String key)
    {
        return getProviderMetadata(provider.getClass(), key);
    }

    /**
     * 获取ContentProvider标签中的MetaData
     *
     * @param clazz clazz
     * @param key   key
     * @return String
     */
    public static String getProviderMetadata(@NonNull final Class<? extends ContentProvider> clazz, @NonNull final String key)
    {
        PackageManager pm = WLibrary.getContext().getPackageManager();
        ComponentName cn = new ComponentName(WLibrary.getContext(), clazz);
        try
        {
            ProviderInfo si = pm.getProviderInfo(cn, PackageManager.GET_META_DATA);
            return si.metaData == null ? null : String.valueOf(si.metaData.get(key));
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
