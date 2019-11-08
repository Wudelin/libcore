package com.wdl.libcore;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Create by: wdl at 2019/11/8 16:59
 * 通过注解限定方法的参数
 */
@SuppressWarnings("unused")
public class Test
{
    public static final int GET_META_DATA = 0x00000080;
    public static final int GET_META_DATA_YA = 0x00000090;

    @IntDef(flag = true, value = {
            GET_META_DATA, GET_META_DATA_YA
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface PackageInfoFlags
    {
    }

    public int getActivityInfo(@PackageInfoFlags int flags)
    {
        return flags;
    }


}
