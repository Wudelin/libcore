package com.wdl.core.util;

import android.text.TextUtils;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Create by: wdl at 2019/11/27 9:48
 * 时间工具类
 * 1.date -> string
 * 2.date -> mills
 * 3.string -> date
 * 4.mills -> date
 * 5.string -> mills
 * 6.mills -> string
 */
@SuppressWarnings("unused")
public final class DateUtil
{

    @IntDef(flag = true, value = {
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DATE,
            Calendar.MINUTE,
            Calendar.HOUR_OF_DAY,
            Calendar.MILLISECOND,
            Calendar.DAY_OF_YEAR,
            Calendar.WEEK_OF_YEAR,
            Calendar.WEEK_OF_MONTH,
            Calendar.DAY_OF_WEEK_IN_MONTH,
            Calendar.HOUR,
            Calendar.SECOND,
            Calendar.DAY_OF_WEEK
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface FieldValue
    {
    }

    /**
     * 时间转换格式
     */
    private static final ThreadLocal<SimpleDateFormat> SDF_TH = new ThreadLocal<>();
    /**
     * Calendar
     */
    private static final ThreadLocal<Calendar> CALENDAR_TH = new ThreadLocal<>();
    /**
     * 默认转换格式
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";


    private DateUtil()
    {
    }

    /**
     * Calendar
     *
     * @return Calendar
     */
    public static Calendar getCalendar()
    {
        Calendar calendar = CALENDAR_TH.get();
        if (calendar == null)
        {
            calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            CALENDAR_TH.set(calendar);
        }
        return calendar;
    }


    /**
     * 获取默认SimpleDateFormat
     *
     * @return SimpleDateFormat
     */
    private static SimpleDateFormat getDefaultSimpleDateFormat()
    {
        return getSimpleDataFormat(DEFAULT_PATTERN);
    }

    /**
     * 获取指定转换格式的SimpleDateFormat
     *
     * @param pattern 转换格式
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getSimpleDataFormat(final String pattern)
    {
        if (TextUtils.isEmpty(pattern))
        {
            throw new NullPointerException("pattern is null ......");
        }
        SimpleDateFormat sdf = SDF_TH.get();
        if (sdf == null)
        {
            sdf = new SimpleDateFormat(pattern, Locale.getDefault());
            SDF_TH.set(sdf);
        } else
        {
            sdf.applyPattern(pattern);
        }
        return sdf;
    }

    /**
     * 时间戳转String
     *
     * @param mills 时间戳
     * @return String
     */
    public static String mills2String(final long mills)
    {
        return mills2String(mills, getDefaultSimpleDateFormat());
    }

    /**
     * 时间戳转String
     *
     * @param mills   时间戳
     * @param pattern 转换格式
     * @return String
     */
    public static String mills2String(final long mills, @NonNull final String pattern)
    {
        return mills2String(mills, getSimpleDataFormat(pattern));
    }

    /**
     * 时间戳转String
     *
     * @param mills  时间戳
     * @param format 转换格式
     * @return String
     */
    private static String mills2String(final long mills, @NonNull final DateFormat format)
    {
        return format.format(new Date(mills));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateTime 时间字符串
     * @return 时间戳
     */
    public static long string2Mills(@NonNull final String dateTime)
    {
        return string2Mills(dateTime, getDefaultSimpleDateFormat());
    }

    /**
     * 字符串转时间戳
     *
     * @param dateTime 时间字符串
     * @param pattern  转换格式
     * @return 时间戳
     */
    public static long string2Mills(@NonNull final String dateTime, @NonNull final String pattern)
    {
        return string2Mills(dateTime, getSimpleDataFormat(pattern));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateTime 时间字符串
     * @param format   转换格式
     * @return 时间戳
     */
    private static long string2Mills(@NonNull final String dateTime, @NonNull final DateFormat format)
    {
        if (TextUtils.isEmpty(dateTime))
            return -1;
        try
        {
            Date date = format.parse(dateTime);
            if (date == null) return -1;
            return date.getTime();
        } catch (ParseException e)
        {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 字符串转Date
     *
     * @param dateTime 时间字符串
     * @return Date
     */
    public static Date string2Date(@NonNull final String dateTime)
    {
        return string2Date(dateTime, getDefaultSimpleDateFormat());
    }

    /**
     * 字符串转Date
     *
     * @param dateTime 时间字符串
     * @param pattern  转换格式
     * @return Date
     */
    public static Date string2Date(@NonNull final String dateTime, @NonNull final String pattern)
    {
        return string2Date(dateTime, getSimpleDataFormat(pattern));
    }

    /**
     * 字符串转Date
     *
     * @param dateTime 时间字符串
     * @param format   转换格式
     * @return Date
     */
    private static Date string2Date(@NonNull final String dateTime, @NonNull final DateFormat format)
    {
        try
        {
            return format.parse(dateTime);
        } catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Date转String
     *
     * @param date Date
     * @return String
     */
    public static String date2String(@NonNull final Date date)
    {
        return date2String(date, getDefaultSimpleDateFormat());
    }

    /**
     * Date转String
     *
     * @param date    Date
     * @param pattern 转换格式
     * @return String
     */
    public static String date2String(@NonNull final Date date, @NonNull final String pattern)
    {
        return date2String(date, getSimpleDataFormat(pattern));
    }

    /**
     * Date转String
     *
     * @param date   Date
     * @param format 转换格式
     * @return String
     */
    private static String date2String(@NonNull final Date date, @NonNull final DateFormat format)
    {
        return format.format(date);
    }

    /**
     * Date转mills
     *
     * @param date Date
     * @return long
     */
    public static long date2Mills(@NonNull final Date date)
    {
        return date.getTime();
    }

    /**
     * mills转Date
     *
     * @param mills mills
     * @return Date
     */
    public static Date mills2Date(final long mills)
    {
        return new Date(mills);
    }

    /**
     * 获取指定的年 月 日 一年的第几天 一周的第几天 周几 时 分 秒等
     *
     * @param field Calendar.YEAR 年
     *              Calendar.MONTH 月 注意：月份从0开始
     *              Calendar.DAY_OF_MONTH 当前月的日期
     *              Calendar.DATE 日
     *              Calendar.MINUTE 分钟
     *              Calendar.HOUR_OF_DAY 24小时制-时
     *              Calendar.HOUR 12小时制
     *              Calendar.SECOND 秒
     *              Calendar.MILLISECOND 毫秒
     *              Calendar.DAY_OF_YEAR 一年的第几天
     *              Calendar.WEEK_OF_YEAR 一年的第几周
     *              Calendar.WEEK_OF_MONTH 一个月的第几周
     *              Calendar.DAY_OF_WEEK 周几
     *              Calendar.DAY_OF_WEEK_IN_MONTH 今天是本月的第几周
     * @return int
     */
    public static int get(@FieldValue final int field)
    {
        return getCalendar().get(field);
    }

    /**
     * 获取本年/月的最大天数
     *
     * @param field DAY_OF_MONTH DAY_OF_YEAR
     * @return 最大天数
     */
    public static int getMax(@FieldValue final int field)
    {
        return getCalendar().getActualMaximum(field);
    }

    /**
     * 获取本年/月的最小天数
     *
     * @param field DAY_OF_MONTH DAY_OF_YEAR
     * @return 最小天数
     */
    public static int getMin(@FieldValue final int field)
    {
        return getCalendar().getActualMinimum(field);
    }


    //TODO 日期加减 - 日期判断

}
