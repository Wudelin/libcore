package com.wdl.libcore;

import android.app.Application;

import com.wdl.libcore.util.WActivityStack;
import com.wdl.libcore.util.WProcessUtil;

@SuppressWarnings("unused")
public abstract class WApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        // 属于当前进程下
        final String processName = WProcessUtil.getProcessName(this);
        if (getPackageName().equals(processName))
        {
            // 初始化WActivityStack
            WActivityStack.getInstance().init(this);
            runOnMainProcess();
        }
    }

    /**
     * 运行在此进程中
     */
    protected abstract void runOnMainProcess();
}
