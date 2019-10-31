package com.wdl.core.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

@SuppressWarnings("unused")
public final class WProcessUtil
{
    private WProcessUtil()
    {

    }

    /**
     * 获取当前进程名
     *
     * @param context Context
     * @return 当前APP进程名
     */
    public static String getProcessName(Context context)
    {
        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert am != null;
        final List<ActivityManager.RunningAppProcessInfo> process = am.getRunningAppProcesses();
        if (process == null || process.isEmpty()) return null;
        final int pId = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo item : process)
        {
            if (item.pid == pId) return item.processName;
        }
        return null;
    }
}
