package com.wdl.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.wdl.core.R;


@SuppressWarnings("unused")
public class WLibrary
{
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    protected WLibrary()
    {
    }


    public static void set(Context context)
    {
        mContext = context.getApplicationContext();
        init(mContext);
    }

    private static void init(Context mContext)
    {
        final String className = mContext.getResources().getString(R.string.lib_context_init_class);
        if (!TextUtils.isEmpty(className))
        {
            try
            {
                Class.forName(className).newInstance();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }


    public static Context getContext()
    {
        return mContext;
    }

}
