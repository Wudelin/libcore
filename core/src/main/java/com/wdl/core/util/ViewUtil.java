package com.wdl.core.util;


import android.graphics.Bitmap;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;


/**
 * Create by: wdl at 2019/11/26 14:28
 */
@SuppressWarnings("unused")
public final class ViewUtil
{
    private ViewUtil()
    {

    }

    /**
     * view是否添加到window
     *
     * @param view View
     * @return true-是
     */
    public static boolean isAttached(final View view)
    {
        if (view == null)
        {
            return false;
        }

        if (Build.VERSION.SDK_INT >= 19)
        {
            return view.isAttachedToWindow();
        } else
        {
            return view.getWindowToken() != null;
        }
    }

    /**
     * 获取view在屏幕上的坐标
     *
     * @param view View
     * @return 坐标数组
     */
    public static int[] getLocationOnScreen(final View view)
    {
        int[] location = new int[]{0, 0};
        if (view == null)
        {
            return location;
        }
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * 判断View是否在触摸点下方
     *
     * @param view  View
     * @param event MotionEvent
     * @return boolean
     */
    public static boolean isViewUnder(final View view, MotionEvent event)
    {
        if (view == null)
        {
            return false;
        }
        if (!isAttached(view))
        {
            return false;
        }
        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();
        return isViewUnder(view, x, y);
    }

    private static boolean isViewUnder(final View view, final int x, final int y)
    {
        if (view == null)
        {
            return false;
        }
        if (!isAttached(view))
        {
            return false;
        }

        final int[] location = getLocationOnScreen(view);
        final int left = location[0];
        final int top = location[1];
        final int right = left + view.getWidth();
        final int bottom = top + view.getHeight();

        return x >= left && x < right && y >= top && y < bottom;
    }

    /**
     * 手动测量
     *
     * @param view View
     */
    public static void measure(final View view)
    {
        if (view == null)
        {
            return;
        }

        final int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(width, height);
    }

    /**
     * 获取View截图
     *
     * @param view View
     * @return Bitmap
     */
    public static Bitmap getViewBitmap(final View view)
    {
        if (view == null)
        {
            return null;
        }
        // 开启
        view.setDrawingCacheEnabled(true);
        final Bitmap drawCache = view.getDrawingCache();
        if (drawCache == null)
        {
            return null;
        }

        final Bitmap result = Bitmap.createBitmap(drawCache);
        view.destroyDrawingCache();
        return result;
    }
}
