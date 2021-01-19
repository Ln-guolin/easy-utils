package cn.soilove.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 线程池
 *
 * @author: Chen GuoLin
 * @create: 2019-11-08 15:31
 **/
@Slf4j
public class ThreadPoolUtils {

    private static final Map<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>();
    private static final Map<String, ScheduledExecutorService> scheduledExecutorServiceMap = new ConcurrentHashMap<>();

    /**
     * 单线程线程池，池子里的线程按照提交的先后顺序执行
     * @param bizName
     * @return
     */
    public static synchronized ExecutorService newSingleThreadExecutor(String bizName){
        ExecutorService executorService = executorServiceMap.get(bizName);
        if(executorService == null || executorService.isShutdown()){
            executorService = Executors.newSingleThreadExecutor();
            executorServiceMap.put(bizName,executorService);
            log.info("[thread_pool][newSingleThreadExecutor]创建新线程池！bizName=" + bizName);
        }
        return executorService;
    }

    /**
     * 可缓存线程池，当有线程任务完成时，下一个新线程会复用已完成的线程，不新建线程，线程无顺序
     * @param bizName
     * @return
     */
    public static synchronized ExecutorService newCachedThreadPool(String bizName){
        ExecutorService executorService = executorServiceMap.get(bizName);
        if(executorService == null || executorService.isShutdown()){
            executorService = Executors.newCachedThreadPool();
            executorServiceMap.put(bizName,executorService);
            log.info("[thread_pool][newCachedThreadPool]创建新线程池！bizName=" + bizName);
        }
        return executorService;
    }

    /**
     * 定长线程池，可以设置最大并发数，超出最大限制的线程排队等待
     * @param bizName
     * @param nThreads
     * @return
     */
    public static synchronized ExecutorService newFixedThreadPool(String bizName,int nThreads){
        ExecutorService executorService = executorServiceMap.get(bizName);
        if(executorService == null || executorService.isShutdown()){
            executorService = Executors.newFixedThreadPool(nThreads);
            executorServiceMap.put(bizName,executorService);
            log.info("[thread_pool][newFixedThreadPool]创建新线程池！bizName=" + bizName);
        }
        return executorService;
    }

    /**
     * 定长线程池，可以设置最大并发数，超出最大限制的线程排队等待 ，另外还可以延迟执行和周期性重复执行
     * <pre>
     *         // 示例
     *         ScheduledExecutorService scheduledExecutorService = ThreadPoolUtils.newScheduledThreadPool("test",2);
     *         // 延迟2秒运行
     *         scheduledExecutorService.schedule(() -> {
     *             System.out.println("执行示例1...");
     *         },2,TimeUnit.SECONDS);
     *         // 延迟2秒执行，后续每5秒运行一次
     *         scheduledExecutorService.scheduleAtFixedRate(() -> {
     *             System.out.println("执行示例2...");
     *         },2,5,TimeUnit.SECONDS);
     * </pre>
     * @param bizName
     * @param nThreads
     * @return
     */
    public static synchronized ScheduledExecutorService newScheduledThreadPool(String bizName,int nThreads){
        ScheduledExecutorService executorService = scheduledExecutorServiceMap.get(bizName);
        if(executorService == null || executorService.isShutdown()){
            executorService = Executors.newScheduledThreadPool(nThreads);
            scheduledExecutorServiceMap.put(bizName,executorService);
            log.info("[thread_pool][newScheduledThreadPool]创建新线程池！bizName=" + bizName);
        }
        return executorService;
    }

    /**
     * 创建自定义参数线程池
     * @param bizName
     * @param corePoolSize 线程池的基本大小，即使线程已经闲置，也会保留在池中的线程数
     * @param maximumPoolSize 最大线程数量，当前线程数超过线程池大小并小于此值是会创建新线程，如：16
     * @param keepAliveTime 线程池中空闲线程等待工作的超时时间，如：0
     * @param keepAliveTimeTimeUnit 设置keepAliveTime参数的单位，如： TimeUnit.SECONDS
     * @param max_queue_len 线程池最大排队数量
     * @param nameFormat 如：ThreadPool-Order-%s
     * @return
     */
    public static synchronized ExecutorService newThreadPoolExecutor(String bizName,int corePoolSize,int maximumPoolSize,
                                                          long keepAliveTime,TimeUnit keepAliveTimeTimeUnit,int max_queue_len,String nameFormat){
        ExecutorService executorService = executorServiceMap.get(bizName);
        if(executorService == null || executorService.isShutdown()){
            executorService = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    keepAliveTimeTimeUnit,
                    new LinkedBlockingDeque<>(max_queue_len),
                    new ThreadFactoryBuilder().setNameFormat(nameFormat).build());
            executorServiceMap.put(bizName,executorService);
            log.info("[thread_pool][newThreadPoolExecutor]创建新线程池！bizName=" + bizName);
        }
        return executorService;
    }

    /**
     * 创建自定义参数线程池
     * @param bizName
     * @param corePoolSize 线程池的基本大小，即使线程已经闲置，也会保留在池中的线程数
     * @param maximumPoolSize 最大线程数量，当前线程数超过线程池大小并小于此值是会创建新线程，如：16
     * @param keepAliveTime 线程池中空闲线程等待工作的超时时间，如：0
     * @param keepAliveTimeTimeUnit 设置keepAliveTime参数的单位，如： TimeUnit.SECONDS
     * @param max_queue_len 线程池最大排队数量
     * @param nameFormat 如：ThreadPool-Order-%s
     * @param rejectedExecutionHandler 拒绝策略，可以自己去实现RejectedExecutionHandler接口
     * @return
     */
    public static synchronized ExecutorService newThreadPoolExecutor(String bizName,int corePoolSize,int maximumPoolSize,
                                                        long keepAliveTime,TimeUnit keepAliveTimeTimeUnit,int max_queue_len,
                                                        String nameFormat,RejectedExecutionHandler rejectedExecutionHandler){
        ExecutorService executorService = executorServiceMap.get(bizName);
        if(executorService == null || executorService.isShutdown()){
            executorService = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    keepAliveTimeTimeUnit,
                    new LinkedBlockingDeque<>(max_queue_len),
                    new ThreadFactoryBuilder().setNameFormat(nameFormat).build(),
                    rejectedExecutionHandler);
            executorServiceMap.put(bizName,executorService);
            log.info("[thread_pool][newThreadPoolExecutor]创建新线程池！bizName=" + bizName);
        }
        return executorService;
    }
    /**
     * 创建自定义参数线程池
     * <pre>
     *         // 示例
     *         ThreadPoolUtils.newThreadPoolExecutor("test", 2, 2, 60, TimeUnit.SECONDS, 3, "[ssss]-sss-%s",() -> System.out.println("警告：线程池排队数量超限，任务添加失败！"))
     *         .execute(() -> System.out.println("执行示例..."));
     * </pre>
     * @param bizName
     * @param corePoolSize 线程池的基本大小，即使线程已经闲置，也会保留在池中的线程数
     * @param maximumPoolSize 最大线程数量，当前线程数超过线程池大小并小于此值是会创建新线程，如：16
     * @param keepAliveTime 线程池中空闲线程等待工作的超时时间，如：0
     * @param keepAliveTimeTimeUnit 设置keepAliveTime参数的单位，如： TimeUnit.SECONDS
     * @param max_queue_len 线程池最大排队数量
     * @param nameFormat 如：ThreadPool-Order-%s
     * @param rejected 拒绝策略自定义处理
     * @return
     */
    public static synchronized ExecutorService newThreadPoolExecutor(String bizName,int corePoolSize,int maximumPoolSize,
                                                        long keepAliveTime,TimeUnit keepAliveTimeTimeUnit,int max_queue_len,
                                                        String nameFormat,Runnable rejected){
        return newThreadPoolExecutor(bizName, corePoolSize, maximumPoolSize, keepAliveTime, keepAliveTimeTimeUnit, max_queue_len, nameFormat, new MyRejectPolicy(rejected));
    }

    static class MyRejectPolicy implements RejectedExecutionHandler{
        private Runnable rejected;
        public MyRejectPolicy(Runnable rejected) {
            this.rejected = rejected;
        }
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            this.rejected.run();
        }
    }

    public static void main(String[] args) {
        IntStream.range(1,20).parallel().forEach(n -> {
            ThreadPoolUtils.newFixedThreadPool("test",2).execute(() -> {
                System.out.println("print="+System.currentTimeMillis());
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
