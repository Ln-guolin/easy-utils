package cn.soilove.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 *
 * @author: Chen GuoLin
 * @create: 2019-11-08 15:31
 **/
@Slf4j
public class ThreadPoolUtils {

    /**
     * 线程名称
     */
    private static final String THREAD_NAME = "[ThreadPool]-%s";

    /**
     * 线程池最大排队数量
     */
    private static final int MAX_QUEUE_LEN = 50000;

    /**
     * 线程池的基本大小，即使线程已经闲置，也会保留在池中的线程数
     */
    private static final int CORE_POOL_SIZE = 8;

    /**
     * 最大线程数量，当前线程数超过线程池大小并小于此值是会创建新线程
     */
    private static final int MAX_NUM_POOL_SIZE = 16;

    /**
     * 线程池中空闲线程等待工作的超时时间
     */
    private static final long KEEP_ALIVE_TIME = 0;

    /**
     * 线程工厂
     */
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(THREAD_NAME).build();

    /**
     * 创建线程池实例
     */
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_NUM_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MICROSECONDS,
            new LinkedBlockingDeque<>(MAX_QUEUE_LEN),
            threadFactory);

    /**
     * 直接在公共线程池中执行线程
     * @param runnable 可运行对象
     */
    public static void execute(Runnable runnable) {
        try {
            threadPoolExecutor.execute(runnable);
        } catch (Exception e) {
            log.error("[ThreadPool]线程池执行异常，Exception: " , e);
        }catch (Throwable r){
            log.error("[ThreadPool]线程池执行异常，Throwable: " ,r);
        }
    }

}
