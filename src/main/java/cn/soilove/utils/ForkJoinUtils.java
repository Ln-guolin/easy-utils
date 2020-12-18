package cn.soilove.utils;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * fork/join并行工具类
 * <pre>
 *     提供最场景的2种任务的快捷使用方法：
 *     - RecursiveTask
 *     - RecursiveAction
 * </pre>
 *
 * @author: Chen GuoLin
 * @create: 2020-12-18 14:08
 **/
public class ForkJoinUtils {

    /**
     * 创建ForkJoinPool
     * @return
     */
    public static ForkJoinPool newForkJoinPool(){
        return new ForkJoinPool();
    }

    /**
     * 创建ForkJoinPool
     * @param parallelism
     * @return
     */
    public static ForkJoinPool newForkJoinPool(int parallelism){
        return new ForkJoinPool(parallelism);
    }

    /**
     * 创建ForkJoinPool并执行任务
     * @param runnables
     */
    public static void execute(Runnable ... runnables){
        ForkJoinPool forkJoinPool = ForkJoinUtils.newForkJoinPool();
        for(Runnable runnable : runnables){
            forkJoinPool.execute(runnable);
        }
        forkJoinPool.shutdown();
    }

    /**
     * 添加任务 - RecursiveTask
     * <pre>
     *         ForkJoinPool forkJoinPool = ForkJoinUtils.newForkJoinPool();
     *         ForkJoinTask task = ForkJoinUtils.submit(forkJoinPool,() -> "执行了-1:" + System.currentTimeMillis());
     *         ForkJoinTask task2 = ForkJoinUtils.submit(forkJoinPool,() -> "执行了-2:" + System.currentTimeMillis());
     *         System.out.println(task.join() + "\n" +  task2.join());
     *         forkJoinPool.shutdown();
     * </pre>
     * @param forkJoinPool
     * @param supplier
     * @return
     */
    public static ForkJoinTask submit(ForkJoinPool forkJoinPool,Supplier supplier){
        return forkJoinPool.submit(new MyRecursiveTask(supplier));
    }

    /**
     * 添加任务 - RecursiveAction
     * @param forkJoinPool
     * @param runnable
     * @return
     */
    public static ForkJoinTask submit(ForkJoinPool forkJoinPool,Runnable runnable){
        return forkJoinPool.submit(new MyRecursiveAction(runnable));
    }

    static class MyRecursiveTask extends RecursiveTask {
        private Supplier supplier;

        public MyRecursiveTask(Supplier supplier) {
            this.supplier = supplier;
        }

        @Override
        protected Object compute() {
            return supplier.get();
        }
    }

    static class MyRecursiveAction extends RecursiveAction {
        private Runnable runnable;

        public MyRecursiveAction(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        protected void compute() {
            runnable.run();
        }
    }
}



