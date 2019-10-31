package com.wdl.libcore.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘操作相关
 * InputMethodManager.HIDE_IMPLICIT_ONLY - 表示如果用户未显式地显示软键盘窗口，则隐藏窗口
 * InputMethodManager.HIDE_NOT_ALWAYS - 表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示
 * InputMethodManager.RESULT_HIDDEN - 软键盘窗口从显示切换到隐藏时的状态
 * InputMethodManager.RESULT_SHOWN - 软键盘窗口从隐藏切换到显示时的状态
 * InputMethodManager.RESULT_UNCHANGED_HIDDEN - 软键盘窗口不变保持隐藏时的状态
 * InputMethodManager.RESULT_UNCHANGED_SHOWN - 软键盘窗口不变保持显示时的状态
 * InputMethodManager.SHOW_FORCED - 表示用户强制打开输入法（如长按菜单键），一直保持打开直至只有显式关闭
 * InputMethodManager.SHOW_IMPLICIT - 表示隐式显示输入窗口，非用户直接要求。窗口可能不显示。
 */
@SuppressWarnings("unused")
public final class KeyboardUtil extends WLibrary
{
    private static int sDecorViewDelta = 0;

    private KeyboardUtil()
    {
        throw new UnsupportedOperationException("u can not operation me...");
    }

    /**
     * 显示软键盘
     * 判断activity真实可见的范围
     *
     * @param activity Activity
     */
    public static void showSoftKeyboard(final Activity activity)
    {
        if (activity == null) return;
        if (!isInVisible(activity))
        {
            toggleSoftKeyboard();
        }

    }

    /**
     * 判断软键盘是否可显示
     *
     * @param activity Activity
     * @return boolean
     */
    private static boolean isInVisible(Activity activity)
    {
        return getDecorViewInvisibleHeight(activity) > 0;
    }

    /**
     * 判断DecorView的真实高度
     *
     * @param activity Activity
     * @return height
     */
    private static int getDecorViewInvisibleHeight(final Activity activity)
    {
        final View decorView = activity.getWindow().getDecorView();
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= WResUtil.getNavBarHeight() + WResUtil.getStatusBarHeight(activity))
        {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    /**
     * 显示软键盘
     *
     * @param view View
     */
    public static void showSoftKeyboard(final View view)
    {
        if (view == null) return;
        showSoftKeyboard(view, 0);
    }

    /**
     * 显示软键盘
     *
     * @param view View
     */
    public static void showSoftKeyboard(View view, int flag)
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, flag, new ResultReceiver(new Handler())
        {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData)
            {
                if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN
                        || resultCode == InputMethodManager.RESULT_HIDDEN)
                {
                    toggleSoftKeyboard();
                }
            }
        });

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    /**
     * 软键盘显示与否-切换
     * Toggle the soft input display or not.
     */
    public static void toggleSoftKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(0, 0);
    }


    /**
     * 隐藏软键盘
     *
     * @param activity Activity
     */
    public static void hideSoftKeyboard(final Activity activity)
    {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view == null)
        {
            View decorView = activity.getWindow().getDecorView();
            View focusView = decorView.findViewWithTag("keyboardTagView");
            if (focusView == null)
            {
                focusView = new EditText(activity);
                focusView.setTag("keyboardTagView");
                ((ViewGroup) decorView).addView(focusView, 0, 0);
            } else
            {
                view = focusView;
            }
        }
        hideSoftKeyboard(view);
    }

    /**
     * 隐藏软键盘
     *
     * @param view View
     */
    public static void hideSoftKeyboard(final View view)
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
