package project.extension.task;

import org.slf4j.Logger;
import org.springframework.lang.Nullable;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * 基础任务队列运行类
 *
 * @author LCTR
 * @date 2022-09-09
 */
public abstract class TaskQueueHandler {
    /**
     * <p>threadPoolSize 默认值为 -1，将会使用所有可用的处理器</p>
     *
     * @param name   名称
     * @param logger 日志组件
     */
    public TaskQueueHandler(String name,
                            Logger logger) {
        this(name,
             -1,
             logger);
    }

    /**
     * @param name           名称
     * @param threadPoolSize 并发子任务线程池大小
     *                       <p>-1 将会使用所有可用的处理器</p>
     *                       <p>0 延迟执行的单线程执行器</p>
     *                       <p>1 单线程执行器</p>
     *                       <p>大于1 拥有固定数量线程的线程池</p>
     * @param logger         日志组件
     */
    public TaskQueueHandler(String name,
                            int threadPoolSize,
                            Logger logger) {
        TaskQueueHandler.name = name;
        this.taskQueue = new ConcurrentLinkedDeque<>();
        this.ConcurrentTaskMap = new ConcurrentHashMap<>();
        if (threadPoolSize == -1)
            this.concurrentTaskExecutor = Executors.newWorkStealingPool();
        else if (threadPoolSize == 0)
            this.concurrentTaskExecutor = Executors.newSingleThreadScheduledExecutor();
        else if (threadPoolSize == 1)
            this.concurrentTaskExecutor = Executors.newSingleThreadExecutor();
        else
            this.concurrentTaskExecutor = Executors.newFixedThreadPool(threadPoolSize);
        this.logger = logger;
    }

    /**
     * 主任务队列
     */
    private final Queue<Object> taskQueue;

    /**
     * 并发子任务集合
     * <p>key：任意Key，value：异步任务</p>
     */
    private final ConcurrentMap<Object, CompletableFuture<Void>> ConcurrentTaskMap;

    /**
     * 并发子任务线程管理服务
     */
    private final ExecutorService concurrentTaskExecutor;

    /**
     * 定时器
     */
    private Timer timer;

    /**
     * 名称
     */
    private static String name;

    /**
     * 启动时间
     */
    private Date startTime;

    /**
     * 死锁
     * <p>一个不会主动完成的任务</p>
     * <p>返回值 true: 开始处理队列 , false: 停止处理队列 </p>
     */
    private CompletableFuture<Boolean> cf;

    /**
     * 日志组件
     */
    protected final Logger logger;

    /**
     * 模块名称
     */
    public static String getName() {
        return name;
    }

    /**
     * 模块启动时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 待处理任务数量
     */
    public int getTaskCount() {
        return taskQueue.size();
    }

    /**
     * 并发子任务数量
     */
    public int getConcurrentTaskCount() {
        return ConcurrentTaskMap.size();
    }

    /**
     * 模块当前状态
     *
     * @return 0: 空闲, 1: 运行中, 2: 已停止
     */
    public int getState()
            throws
            Exception {
        return cf == null
               ? 0
               : cf.isDone()
                 ? cf.get()
                   ? 1
                   : 2
                 : 1;
    }

    /**
     * 启动
     */
    public void start() {
        timer = new Timer();

        cf = new CompletableFuture<>();

        startTime = new Date();

        //异步执行
        CompletableFuture.runAsync(this::run,
                                   Executors.newSingleThreadExecutor());
    }

    /**
     * 关停
     */
    public void shutDown() {
        if (cf == null) cf = new CompletableFuture<>();

        startTime = null;

        if (!cf.isDone()) cf.complete(false);

        //取消定时任务
        timer.cancel();

        //清理主任务
        taskQueue.clear();

        //清理并发子任务
        ConcurrentTaskMap.values()
                         .forEach(x -> {
                             try {
                                 x.cancel(true);
                             } catch (Exception ignore) {

                             }
                         });
    }

    /**
     * 新增主任务并立即开始处理
     *
     * @param taskKey 任务标识
     */
    public void addTask(Object taskKey) {
        addTask(taskKey,
                true);
    }

    /**
     * 新增主任务
     *
     * @param taskKey 任务标识
     * @param handler 是否立即开始处理
     */
    public void addTask(Object taskKey,
                        boolean handler) {
        taskQueue.add(taskKey);

        if (handler)
            handler();
    }

    /**
     * 新增主任务集合并立即开始处理
     *
     * @param taskKeys 任务标识集合
     */
    public void addTasks(Collection<Object> taskKeys) {
        addTasks(taskKeys,
                 true);
    }

    /**
     * 新增主任务集合
     *
     * @param taskKeys 任务标识集合
     * @param handler  是否立即开始处理
     */
    public void addTasks(Collection<Object> taskKeys,
                         boolean handler) {
        taskQueue.addAll(taskKeys);

        if (handler)
            handler();
    }

    /**
     * 开始处理主任务队列
     */
    public void handler() {
        if (cf != null) cf.complete(true);
    }

    /**
     * 运行
     */
    private void run() {
        try {
            while (true) {
                if (cf != null) {
                    if (!cf.get()) return;
                    cf = null;
                }

                if (taskQueue.isEmpty()) {
                    cf = new CompletableFuture<>();
                    continue;
                }

                try {
                    processingQueue();
                } catch (Exception ex) {
                    logger.error(String.format("%s运行时发生异常，已跳过当前任务",
                                               name),
                                 ex);
                }
            }
        } catch (Exception ex) {
            logger.error(String.format("%s运行时发生异常, 程序已停止",
                                       name),
                         ex);
        }
    }

    /**
     * 处理主任务队列
     */
    private void processingQueue() {
        int count = taskQueue.size();

        for (int i = 0; i < count; i++) {
            Object taskKey = taskQueue.poll();

            try {
                processingTask(taskKey);
            } catch (Exception ex) {
                logger.error(String.format("%s处理主任务时发生异常，已跳过当前任务",
                                           name),
                             ex);
            }
        }
    }

    /**
     * 处理主任务
     */
    protected abstract void processingTask(Object taskKey);

    /**
     * 新增并发子任务
     *
     * @param key        任务标识
     * @param runnable   执行操作
     * @param thenAccept 操作结束后执行（可选）
     */
    protected void putConcurrentTask(Object key,
                                     Runnable runnable,
                                     @Nullable
                                             Consumer<Void> thenAccept) {
        ConcurrentTaskMap.put(key,
                              thenAccept == null
                              ? CompletableFuture.runAsync(runnable,
                                                           concurrentTaskExecutor)
                              : CompletableFuture.runAsync(runnable,
                                                           concurrentTaskExecutor)
                                                 .thenAcceptAsync(thenAccept));
    }

    /**
     * 是否存在指定的并发子任务
     *
     * @param key 任务标识
     */
    protected boolean containsConcurrentTask(Object key) {
        return ConcurrentTaskMap.containsKey(key);
    }

    /**
     * 移除指定的并发子任务
     *
     * @param key 任务标识
     */
    protected void removeConcurrentTask(Object key) {
        ConcurrentTaskMap.remove(key);
    }

    /**
     * 添加延时任务
     *
     * @param task  任务
     * @param delay 延时
     */
    protected void addScheduleTask(TimerTask task,
                                   long delay) {
        timer.schedule(task,
                       delay);
    }
}
