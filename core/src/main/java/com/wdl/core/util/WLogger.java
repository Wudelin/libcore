package com.wdl.core.util;

import android.text.TextUtils;
import android.util.Log;

@SuppressWarnings("unused")
public final class WLogger
{
    private static boolean isDebug = false;
    private static final String TAG = WLogger.class.getSimpleName();

    private static String className;
    private static String methodName;
    private static int lineNumber;
    private static final int STACK_TRACE_ELEMENT = 0;

    private WLogger()
    {
    }

    public static void setIsDebug(boolean isDebug)
    {
        WLogger.isDebug = isDebug;
    }

    private static void getMethodInfo(StackTraceElement[] stackTraces)
    {
        className = stackTraces[STACK_TRACE_ELEMENT].getClassName();
        methodName = stackTraces[STACK_TRACE_ELEMENT].getMethodName();
        lineNumber = stackTraces[STACK_TRACE_ELEMENT].getLineNumber();
    }

    private static String createLog(String msg)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("[")
                .append(className)
                .append(" : ")
                .append(methodName)
                .append(" : ")
                .append(lineNumber)
                .append("]")
                .append("-msg:")
                .append(msg);
        return sb.toString();
    }


    public static void i(String msg)
    {
        if (isDebug && isNull(msg))
        {
            getMethodInfo(new Throwable().getStackTrace());
            Log.i(TAG, createLog(msg));
        }
    }


    public static void e(String msg)
    {
        if (isDebug && isNull(msg))
        {
            getMethodInfo(new Throwable().getStackTrace());
            Log.e(TAG, createLog(msg));
        }
    }

    public static void d(String msg)
    {
        if (isDebug && isNull(msg))
        {
            getMethodInfo(new Throwable().getStackTrace());
            Log.d(TAG, createLog(msg));
        }
    }

    public static void w(String msg)
    {
        if (isDebug && isNull(msg))
        {
            getMethodInfo(new Throwable().getStackTrace());
            Log.w(TAG, createLog(msg));
        }
    }

    private static boolean isNull(String msg)
    {
        return !TextUtils.isEmpty(msg);
    }
}
