package com.wdl.core;

import com.wdl.core.util.WActivityStack;
import com.wdl.core.util.WLogger;

public class App extends WApp
{
    public static App mContext;

    public static App get()
    {
        return mContext;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = this;
    }

    @Override
    protected void runOnMainProcess()
    {
        WLogger.setIsDebug(true);
        WActivityStack.getInstance().setDebug(true);
    }


}
