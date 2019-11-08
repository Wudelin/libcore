package com.wdl.core.util;

/**
 * Create by: wdl at 2019/11/8 15:44
 * 颜色转换相关
 * 00000000 00000000 00000000 00000000
 * A      R       G        B
 */
public class ColorUtil
{
    private ColorUtil()
    {
    }

    /**
     * int 转 argb
     *
     * @param color color
     * @return ARGB
     */
    public static int intToARGB(final int color)
    {
        final int colorA = (color >> 24) & 0xff;
        final int colorR = (color >> 16) & 0xff;
        final int colorG = (color >> 8) & 0xff;
        final int colorB = color & 0xff;

        return colorA | colorR | colorG | colorB;
    }
}
