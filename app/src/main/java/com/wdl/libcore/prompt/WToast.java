package com.wdl.libcore.prompt;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.wdl.libcore.util.WLibrary;

@SuppressWarnings("unused")
public final class WToast extends WLibrary
{
    private WToast()
    {
        throw new UnsupportedOperationException("u can not operation me...");
    }

    /**
     * show
     *
     * @param charSequence CharSequence
     */
    public static void show(final CharSequence charSequence)
    {
        show(charSequence, Toast.LENGTH_SHORT, 0);
    }

    /**
     * @param charSequence CharSequence
     * @param gravity      显示的位置
     */
    public static void show(final CharSequence charSequence, final int gravity)
    {
        show(charSequence, Toast.LENGTH_SHORT, gravity);
    }

    /**
     * @param charSequence CharSequence
     * @param duration     持续时间
     * @param gravity      显示的位置
     */
    public static void show(final CharSequence charSequence, final int duration, final int gravity)
    {
        // 判断是否在主线程的looper
        if (Looper.myLooper() == Looper.getMainLooper())
        {
            showInternal(charSequence, duration, gravity);
        } else
        {
            // 发送一条显示toast的消息,传入getMainLooper，可直接在子线程中调用
            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    showInternal(charSequence, duration, gravity);
                }
            });
        }
    }

    /**
     * 真实的显示toast
     *
     * @param charSequence CharSequence
     * @param duration     持续时间
     * @param gravity      显示的位置
     */
    private static void showInternal(CharSequence charSequence, int duration, final int gravity)
    {
        if (TextUtils.isEmpty(charSequence)) return;
        Toast toast = Toast.makeText(getContext(), charSequence, duration);
        if (gravity != 0)
        {
            toast.setGravity(gravity, 0, 0);
        }
        if (toast != null)
            toast.show();
    }
}
