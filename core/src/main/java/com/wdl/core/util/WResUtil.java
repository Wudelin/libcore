package com.wdl.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

/**
 * 单位换算
 * 获取statusBar高度、状态栏是否可见
 * 获取图片名对应的资源ID
 */
@SuppressWarnings({"unused"})
public final class WResUtil extends WLibrary
{
    private WResUtil()
    {
    }


    public static Resources getResource()
    {
        return getContext().getResources();
    }

    public static DisplayMetrics getDisplayMetrics()
    {
        return getResource().getDisplayMetrics();
    }

    /**
     * dp转px
     *
     * @param dpValue dpValue
     * @return px
     */
    public static int dp2px(float dpValue)
    {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getDisplayMetrics()) + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue pxValue
     * @return dp
     */
    public static int px2dp(float pxValue)
    {
        final float scale = getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue spValue
     * @return px
     */
    public static int sp2px(float spValue)
    {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getDisplayMetrics()) + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue pxValue
     * @return sp
     */
    public static int px2sp(float pxValue)
    {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽
     *
     * @return widthPixels
     */
    public static int getScreenWidth()
    {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @return heightPixels
     */
    public static int getScreenHeight()
    {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 根据资源名获取资源ID,路径完整时后2个参数可为空
     * 注意：getIdentifier(packageName+":drawable/ic_launcher_background", null, null);
     *
     * @param drawableName 资源名称
     * @return 资源ID
     */
    public static int getIdentifierForDrawable(String drawableName)
    {
        try
        {
            return getResource()
                    .getIdentifier(drawableName, "drawable", getContext().getPackageName());
        } catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }


    private static int STATUS_BAR_HEIGHT = -1;

    /**
     * 获取状态栏高度
     *
     * @param activity activity
     * @return STATUS_BAR_HEIGHT
     */
    public static int getStatusBarHeight(Activity activity)
    {
        // 如果状态栏可见
        if (isStatusBarVisible(activity))
        {
            // 如果未计算过并且版本大于4.4
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && STATUS_BAR_HEIGHT == -1)
            {
                try
                {
                    final Resources res = activity.getResources();
                    // 尝试获取status_bar_height这个属性的Id对应的资源int值
                    int resId = res
                            .getIdentifier("status_bar_height", "dimen", "android");
                    if (resId <= 0)
                    {
                        @SuppressLint("PrivateApi")
                        Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                        Object object = clazz.newInstance();
                        resId = Integer.parseInt(Objects.requireNonNull(clazz.getField("status_bar_height")
                                .get(object)).toString());
                    }
                    // 如果拿到了,直接获取
                    if (resId > 0)
                    {
                        STATUS_BAR_HEIGHT = res.getDimensionPixelSize(resId);
                    }
                    // 未拿到,通过window获取
                    if (resId <= 0)
                    {
                        Rect rect = new Rect();
                        Window window = activity.getWindow();
                        window.getDecorView().getWindowVisibleDisplayFrame(rect);
                        STATUS_BAR_HEIGHT = rect.top;
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return STATUS_BAR_HEIGHT;
        } else
        {
            return STATUS_BAR_HEIGHT;
        }
    }

    /**
     * 获取底部导航栏高度
     *
     * @return NavBarHeight
     */
    public static int getNavBarHeight()
    {
        Resources res = getResource();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0)
        {
            return res.getDimensionPixelSize(resourceId);
        } else
        {
            return 0;
        }
    }


    /**
     * 判断状态栏是否可见
     *
     * @param activity Activity
     * @return true 可见，false 不可见
     */
    public static boolean isStatusBarVisible(Activity activity)
    {
        if (activity == null) return false;
        return (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
    }

}
