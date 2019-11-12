package com.wdl.core.executor;

import androidx.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Create by: wdl at 2019/11/12 10:22
 * 线程工具类
 * * 阿里巴巴 Android 开发手册
 * 【强制】线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方
 * 式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 * 说明：
 * Executors 返回的线程池对象的弊端如下：
 * 1) FixedThreadPool 和 SingleThreadPool ： 允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM；
 * 2) CachedThreadPool 和 ScheduledThreadPool ： 允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。
 */
@SuppressWarnings("unused")
public class ExecutorManager<T>
{
    private static volatile ExecutorManager executorManager;
    /**
     * CPU数量
     */
    private static final int NUM_OF_CORES = Runtime.getRuntime().availableProcessors();
    /**
     * 最优性能的线程数
     */
    private static final int BEST_CORES = NUM_OF_CORES * 2 + 1;
    /**
     * 空闲线程保留时长
     */
    private static final int KEEP_ALIVE_TIME = 1000;
    /**
     * 单位
     */
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 磁盘相关、文件IO等
     */
    private Executor mDiskIOExecutor;
    /**
     * 网络相关
     */
    private Executor mNetExecutor;
    /**
     * 其他
     */
    private Executor mOtherExecutor;
    private ScheduledExecutorService mScheduleExecutor;

    public static <T> ExecutorManager getExecutorManager()
    {
        if (null == executorManager)
        {
            synchronized (ExecutorManager.class)
            {
                if (null == executorManager)
                {
                    executorManager = new ExecutorManager<>();
                }
            }
        }
        return executorManager;
    }

    /**
     * int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue 默认为Integer.MAX_VALUE
     */
    private ExecutorManager()
    {
        if (null == mDiskIOExecutor)
        {
            mDiskIOExecutor = new ThreadPoolExecutor(
                    1,
                    1,
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    new LinkedBlockingDeque<Runnable>(10)
            );
        }
        if (null == mNetExecutor)
        {
            mNetExecutor = new ThreadPoolExecutor(
                    5,
                    5,
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    new LinkedBlockingDeque<Runnable>(50)
            );
        }
        if (null == mOtherExecutor)
        {
            mOtherExecutor = new ThreadPoolExecutor(
                    BEST_CORES,
                    BEST_CORES,
                    KEEP_ALIVE_TIME,
                    KEEP_ALIVE_TIME_UNIT,
                    new LinkedBlockingDeque<Runnable>(100)
            );
        }
        if (null == mScheduleExecutor)
        {
            mScheduleExecutor = Executors.newScheduledThreadPool(BEST_CORES);
        }
    }


    /**
     * 执行任务
     *
     * @param type     ExType
     * @param runnable Runnable
     */
    public void execute(ExType type, @NonNull Runnable runnable)
    {
        getThreadPoolExecutor(type).execute(runnable);
    }

    /**
     * 提交一个有返回值的任务
     *
     * @param type     ExType
     * @param callable Callable
     * @return Future
     */
    public Future<T> execute(ExType type, Callable<T> callable)
    {
        return getThreadPoolExecutor(type).submit(callable);
    }

    /**
     * 执行延迟性任务
     *
     * @param delay    delay
     * @param runnable Runnable
     */
    public void execute(long delay, Runnable runnable)
    {
        mScheduleExecutor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行延迟性任务
     *
     * @param period   period
     * @param runnable Runnable
     *                 if(任务执行时间 < period) 每隔指定时间执行一次，不受任务执行时间影响
     *                 if(任务执行时间 > period) 等待原有任务结束马上开始下一个任务，周期被改变
     */
    public void executeAt(long init, long period, Runnable runnable)
    {
        mScheduleExecutor.scheduleAtFixedRate(runnable, init, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行延迟性任务
     *
     * @param period   period
     * @param runnable Runnable
     *                 if(任务执行时间 < period) 每隔指定时间执行一次，不受任务执行时间影响
     *                 if(任务执行时间 > period) 等待原有任务结束马上开始 延时执行时间再次开始执行
     */
    public void executeWith(long init, long period, Runnable runnable)
    {
        mScheduleExecutor.scheduleWithFixedDelay(runnable, init, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 移除任务
     *
     * @param type     ExType
     * @param runnable Runnable
     */
    public void cancel(ExType type, @NonNull Runnable runnable)
    {
        getThreadPoolExecutor(type).getQueue().remove(runnable);
    }

    /**
     * 获取对应的线程池
     *
     * @param exType ExType
     * @return ThreadPoolExecutor
     */
    public ThreadPoolExecutor getThreadPoolExecutor(ExType exType)
    {
        ThreadPoolExecutor executor;
        switch (exType)
        {
            case IO:
                executor = (ThreadPoolExecutor) mDiskIOExecutor;
                break;
            case NETWORK:
                executor = (ThreadPoolExecutor) mNetExecutor;
                break;
            case OTHER:
            default:
                executor = (ThreadPoolExecutor) mOtherExecutor;
                break;
        }
        return executor == null ? (ThreadPoolExecutor) Executors.newFixedThreadPool(BEST_CORES) : executor;
    }

    public Executor getDiskIOExecutor()
    {
        return mDiskIOExecutor;
    }

    public Executor getNetExecutor()
    {
        return mNetExecutor;
    }

    public Executor getOtherExecutor()
    {
        return mOtherExecutor;
    }

    public ExecutorService getScheduleExecutor()
    {
        return mScheduleExecutor;
    }

    public enum ExType
    {
        IO,
        NETWORK,
        OTHER
    }
}
