package project.extension.task;

import org.slf4j.Logger;
import project.extension.Identity.SnowFlake;
import project.extension.action.IAction0;
import project.extension.action.IAction1;
import project.extension.exception.CommonException;
import project.extension.func.IFunc0;

import java.util.*;
import java.util.concurrent.*;

/**
 * 基础任务队列运行类
 *
 * @author LCTR
 * @date 2022-09-09
 */
public abstract class TaskQueueHandler<Key> {
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
        this.name = name;
        this.state = TaskQueueHandlerState.已停止;
        this.taskQueue = new ConcurrentLinkedDeque<>();
        this.priorityTaskQueue = new LinkedList<>();
        this.concurrentTaskMap = new ConcurrentHashMap<>();
        this.concurrentPriorityTaskMap = new ConcurrentHashMap<>();
        this.scheduleTaskList = new ConcurrentLinkedQueue<>();
        this.cf = new CompletableFuture<>();
        this.cf_priority = new CompletableFuture<>();

        this.keyGenerate = new SnowFlake(1,
                                         1);

        this.mainExecutor = Executors.newSingleThreadExecutor();
        this.otherTaskExecutor = Executors.newSingleThreadExecutor();
        this.taskExecutor = Executors.newSingleThreadExecutor();
        this.priorityTaskExecutor = Executors.newSingleThreadExecutor();

        if (threadPoolSize == -1) {
            this.concurrentTaskLimit = Runtime.getRuntime()
                                              .availableProcessors();
            this.concurrentTaskExecutor = Executors.newWorkStealingPool();
        } else if (threadPoolSize == 0) {
            this.concurrentTaskLimit = 1;
            this.concurrentTaskExecutor = Executors.newSingleThreadScheduledExecutor();
        } else if (threadPoolSize == 1) {
            this.concurrentTaskLimit = 1;
            this.concurrentTaskExecutor = Executors.newSingleThreadExecutor();
        } else {
            this.concurrentTaskLimit = threadPoolSize;
            this.concurrentTaskExecutor = Executors.newFixedThreadPool(threadPoolSize);
        }
        this.logger = logger;
    }

    /**
     * 并发任务上限
     */
    private final int concurrentTaskLimit;

    /**
     * 主任务队列
     * <p>先进先出</p>
     */
    private final Queue<Key> taskQueue;

    /**
     * 高优先级队列
     * <p>后进先出</p>
     * <P>同步执行</P>
     */
    private final Deque<Key> priorityTaskQueue;

    /**
     * 主任务的并发子任务集合
     * <p>key：任意Key，value：异步任务</p>
     */
    private final ConcurrentMap<String, CompletableFuture<Void>> concurrentTaskMap;

    /**
     * 高优先级任务的并发子任务集合
     * <p>key：任意Key，value：异步任务</p>
     */
    private final ConcurrentMap<String, CompletableFuture<Void>> concurrentPriorityTaskMap;

    /**
     * 延时子任务集合
     * <p>item：任意Key</p>
     */
    private final ConcurrentLinkedQueue<UUID> scheduleTaskList;

    /**
     * 主线程管理服务
     */
    private final ExecutorService mainExecutor;

    /**
     * 主任务线程管理服务
     */
    private final ExecutorService taskExecutor;

    /**
     * 高优先级任务线程管理服务
     */
    private final ExecutorService priorityTaskExecutor;

    /**
     * 并发子任务线程管理服务
     */
    private final ExecutorService concurrentTaskExecutor;

    /**
     * 其他任务线程管理服务
     */
    private final ExecutorService otherTaskExecutor;

    /**
     * 用于生成并发任务的key
     */
    private final SnowFlake keyGenerate;

    /**
     * 定时器
     */
    private Timer timer;

    /**
     * 名称
     */
    private final String name;

    /**
     * 启动时间
     */
    private Date startTime;

    /**
     * 当前状态
     */
    private TaskQueueHandlerState state;

    /**
     * 主任务死锁
     * <p>一个不会主动完成的任务</p>
     * <p>返回值 true: 开始处理队列 , false: 停止处理队列 </p>
     */
    private CompletableFuture<Boolean> cf;

    /**
     * 高优先级任务死锁
     * <p>一个不会主动完成的任务</p>
     * <p>返回值 true: 开始处理队列 , false: 停止处理队列 </p>
     */
    private CompletableFuture<Boolean> cf_priority;

    /**
     * 正在处理的任务数量
     */
    private int handlerTaskCount = 0;

    /**
     * 模块名称
     */
    public String getName() {
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
    public int getHandlerTaskCount() {
        return taskQueue.size() + priorityTaskQueue.size();
    }

    /**
     * 高优先级任务数量
     */
    public int getPriorityTaskCount() {
        return priorityTaskQueue.size();
    }

    /**
     * 并发子任务数量
     */
    public int getConcurrentTaskCount() {
        return concurrentTaskMap.size() + concurrentPriorityTaskMap.size();
    }

    /**
     * 延时子任务数量
     */
    public int getScheduleTaskCount() {
        return scheduleTaskList.size();
    }

    /**
     * 模块当前状态
     *
     * @see TaskQueueHandlerState
     */
    public TaskQueueHandlerState getState() {
        return this.state;
    }

    /**
     * 启动
     *
     * @param autoHandler 自动开始处理任务
     */
    public CompletableFuture<Void> start(boolean autoHandler) {
        return CompletableFuture.runAsync(() -> startStart(autoHandler),
                                          this.mainExecutor);
    }

    /**
     * 启动
     *
     * @param autoHandler 自动开始处理任务
     * @param before      启动前要执行的方法，返回值不为true时，中止启动操作
     */
    public CompletableFuture<Void> start(boolean autoHandler,
                                         IFunc0<Boolean> before) {
        return CompletableFuture.runAsync(() -> {
                                              this.state = TaskQueueHandlerState.启动中;
                                              if (!before.invoke()) {
                                                  this.state = TaskQueueHandlerState.已停止;
                                                  return;
                                              }

                                              this.startStart(autoHandler);
                                          },
                                          this.mainExecutor);
    }

    /**
     * 关停
     */
    public CompletableFuture<Void> shutdown() {
        return shutdown(null);
    }

    /**
     * 停止
     *
     * @param before 停止前要执行的方法
     */
    public CompletableFuture<Void> shutdown(IAction0 before) {
        return CompletableFuture.runAsync(() -> {
                                              this.state = TaskQueueHandlerState.停止中;

                                              if (before != null)
                                                  before.invoke();

                                              this.state = TaskQueueHandlerState.停止中;

                                              startTime = null;

                                              cf.complete(false);

                                              cf_priority.complete(false);

                                              //取消定时任务
                                              if (timer != null)
                                                  timer.cancel();

                                              //清理延时任务
                                              if (scheduleTaskList != null)
                                                  scheduleTaskList.clear();

                                              //清理主任务
                                              if (taskQueue != null)
                                                  taskQueue.clear();

                                              //清理高优先级任务
                                              if (priorityTaskQueue != null)
                                                  priorityTaskQueue.clear();

                                              //清理主任务的并发子任务
                                              if (concurrentTaskMap != null)
                                                  concurrentTaskMap.values()
                                                                   .forEach(x -> {
                                                                       try {
                                                                           x.cancel(true);
                                                                       } catch (Exception ignore) {

                                                                       }
                                                                   });

                                              //清理高优先级任务的并发子任务
                                              if (concurrentPriorityTaskMap != null)
                                                  concurrentPriorityTaskMap.values()
                                                                           .forEach(x -> {
                                                                               try {
                                                                                   x.cancel(true);
                                                                               } catch (Exception ignore) {

                                                                               }
                                                                           });

                                              swatch(null);

                                              this.state = TaskQueueHandlerState.已停止;
                                          },
                                          this.mainExecutor);
    }

    /**
     * 新增主任务并立即开始处理
     *
     * @param taskKey 任务标识
     */
    public void addTask(Key taskKey) {
        this.addTask(taskKey,
                     true);
    }

    /**
     * 新增主任务
     *
     * @param taskKey 任务标识
     * @param handler 是否立即开始处理
     */
    public void addTask(Key taskKey,
                        boolean handler) {
        addTask(taskKey,
                false,
                handler);
    }

    /**
     * 新增主任务集合并立即开始处理
     *
     * @param taskKeys 任务标识集合
     */
    public void addTasks(Collection<Key> taskKeys) {
        this.addTasks(taskKeys,
                      true);
    }

    /**
     * 新增主任务集合
     *
     * @param taskKeys 任务标识集合
     * @param handler  是否立即开始处理
     */
    public void addTasks(Collection<Key> taskKeys,
                         boolean handler) {
        addTasks(taskKeys,
                 false,
                 handler);
    }

    /**
     * 新增高优先级任务并立即开始处理
     *
     * @param taskKey 任务标识
     */
    public void addPriorityTask(Key taskKey) {
        this.addPriorityTask(taskKey,
                             true);
    }

    /**
     * 新增高优先级任务
     *
     * @param taskKey 任务标识
     * @param handler 是否立即开始处理
     */
    public void addPriorityTask(Key taskKey,
                                boolean handler) {
        addTask(taskKey,
                true,
                handler);
    }

    /**
     * 新增高优先级任务集合并立即开始处理
     *
     * @param taskKeys 任务标识集合
     */
    public void addPriorityTasks(Collection<Key> taskKeys) {
        this.addPriorityTasks(taskKeys,
                              true);
    }

    /**
     * 新增高优先级任务集合
     *
     * @param taskKeys 任务标识集合
     * @param handler  是否立即开始处理
     */
    public void addPriorityTasks(Collection<Key> taskKeys,
                                 boolean handler) {
        addTasks(taskKeys,
                 true,
                 handler);
    }

    /**
     * 开始处理任务队列
     */
    public void handler() {
        //开始处理主任务
        if (!taskQueue.isEmpty())
            cf.complete(true);

        //开始处理高优先级任务
        if (!priorityTaskQueue.isEmpty())
            cf_priority.complete(true);
    }

    /**
     * 等待启动
     */
    public CompletableFuture<Void> wait2Start() {
        return wait2Start(-1);
    }

    /**
     * 等待启动
     *
     * @param timeout 超时时间(ms)(值为-1时会一直等待下去)
     */
    public CompletableFuture<Void> wait2Start(int timeout) {
        return CompletableFuture.runAsync(() -> {
                                              int _timeout = timeout;
                                              while (true) {
                                                  if (!this.getState()
                                                           .equals(TaskQueueHandlerState.启动中))
                                                      return;

                                                  if (_timeout != -1
                                                          && _timeout <= 0)
                                                      throw new CommonException(String.format("等待%s启动超时",
                                                                                              getName()));

                                                  TaskExtension.delay(50);
                                                  if (_timeout != -1)
                                                      _timeout -= 50;
                                              }
                                          },
                                          this.otherTaskExecutor);
    }

    /**
     * 等待关闭
     */
    public CompletableFuture<Void> wait2Shutdown() {
        return wait2Shutdown(-1);
    }

    /**
     * 等待关闭
     *
     * @param timeout 超时时间(ms)(值为-1时会一直等待下去)
     */
    public CompletableFuture<Void> wait2Shutdown(int timeout) {
        return CompletableFuture.runAsync(() -> {
                                              int _timeout = timeout;
                                              while (true) {
                                                  if (this.getState()
                                                          .equals(TaskQueueHandlerState.已停止))
                                                      return;

                                                  if (_timeout != -1
                                                          && _timeout <= 0)
                                                      throw new CommonException(String.format("等待%s关闭超时",
                                                                                              getName()));

                                                  TaskExtension.delay(50);
                                                  if (_timeout != -1)
                                                      _timeout -= 50;
                                              }
                                          },
                                          this.otherTaskExecutor);
    }

    /**
     * 等待任务执行结束
     */
    public CompletableFuture<Void> wait2Idle() {
        return wait2Idle(0,
                         0,
                         -1);
    }

    /**
     * 等待任务执行结束
     *
     * @param timeout 超时时间(ms)(值为-1时会一直等待下去)
     */
    public CompletableFuture<Void> wait2Idle(int timeout) {
        return wait2Idle(0,
                         0,
                         timeout);
    }

    /**
     * 等待任务执行结束
     *
     * @param offsetConcurrentTaskCount 并发子任务差值
     *                                  <p>如果当总的并发子任务个数减去此差值小于等于0，则判断任务已执行结束</p>
     * @param offsetScheduleTaskCount   延时子任务差值
     *                                  <p>如果当总的延时子任务个数减去此差值小于等于0，则判断任务已执行结束</p>
     */
    public CompletableFuture<Void> wait2Idle(int offsetConcurrentTaskCount,
                                             int offsetScheduleTaskCount) {
        return wait2Idle(offsetConcurrentTaskCount,
                         offsetScheduleTaskCount,
                         -1);
    }

    /**
     * 等待任务执行结束
     *
     * @param offsetConcurrentTaskCount 并发子任务差值
     *                                  <p>如果当总的并发子任务个数减去此差值小于等于0，则判断任务已执行结束</p>
     * @param offsetScheduleTaskCount   延时子任务差值
     *                                  <p>如果当总的延时子任务个数减去此差值小于等于0，则判断任务已执行结束</p>
     * @param timeout                   超时时间(ms)(值为-1时会一直等待下去)
     */
    public CompletableFuture<Void> wait2Idle(int offsetConcurrentTaskCount,
                                             int offsetScheduleTaskCount,
                                             int timeout) {
        return CompletableFuture.runAsync(() -> {
                                              int _timeout = timeout;
                                              while (true) {
                                                  if (!this.getState()
                                                           .equals(TaskQueueHandlerState.运行中)
                                                          || (offsetConcurrentTaskCount != 0 && getConcurrentTaskCount() - offsetConcurrentTaskCount <= 0
                                                          && offsetScheduleTaskCount != 0 && getScheduleTaskCount() - offsetScheduleTaskCount <= 0))
                                                      return;

                                                  if (_timeout != -1
                                                          && _timeout <= 0)
                                                      throw new CommonException(String.format("等待%s任务执行结束超时",
                                                                                              getName()));

                                                  TaskExtension.delay(50);
                                                  if (_timeout != -1)
                                                      _timeout -= 50;
                                              }
                                          },
                                          this.otherTaskExecutor);
    }

    /**
     * 日志组件
     */
    protected final Logger logger;

    /**
     * 启动
     *
     * @param autoHandler 自动开始处理队列
     */
    private void startStart(boolean autoHandler) {
        this.state = TaskQueueHandlerState.启动中;

        timer = new Timer();

        cf = new CompletableFuture<>();

        cf_priority = new CompletableFuture<>();

        startTime = new Date();

        this.state = TaskQueueHandlerState.空闲;

        //异步运行主任务
        CompletableFuture.runAsync(this::runTask,
                                   this.taskExecutor);

        //异步运行优先级任务
        CompletableFuture.runAsync(this::runPriorityTask,
                                   this.priorityTaskExecutor);

        if (autoHandler)
            this.handler();
    }

    /**
     * 切换状态
     */
    private synchronized void swatch(Integer count) {
        handlerTaskCount += count == null
                            ? -handlerTaskCount
                            : count;

        if (handlerTaskCount == 0)
            this.state = TaskQueueHandlerState.空闲;
        else
            this.state = TaskQueueHandlerState.运行中;
    }

    /**
     * 新增任务
     *
     * @param taskKey  任务标识
     * @param priority 高优先级
     * @param handler  是否立即开始处理
     */
    private void addTask(Key taskKey,
                         boolean priority,
                         boolean handler) {
        if (this.state.equals(TaskQueueHandlerState.停止中)
                || this.state.equals(TaskQueueHandlerState.已停止))
            throw new CommonException(String.format("%s已关闭",
                                                    getName()));

        if (priority)
            priorityTaskQueue.addLast(taskKey);
        else
            taskQueue.add(taskKey);

        swatch(1);

        if (handler)
            this.handler();
    }

    /**
     * 新增任务集合
     *
     * @param taskKeys 任务标识集合
     * @param priority 高优先级
     * @param handler  是否立即开始处理
     */
    private void addTasks(Collection<Key> taskKeys,
                          boolean priority,
                          boolean handler) {
        if (this.state.equals(TaskQueueHandlerState.停止中)
                || this.state.equals(TaskQueueHandlerState.已停止))
            throw new CommonException(String.format("%s已关闭",
                                                    getName()));

        if (priority)
            taskKeys.forEach(priorityTaskQueue::addLast);
        else
            taskQueue.addAll(taskKeys);

        swatch(taskKeys.size());

        if (handler)
            this.handler();
    }

    /**
     * 运行主任务
     */
    private void runTask() {
        try {
            while (true) {
                if (!cf.get()) return;

                cf = new CompletableFuture<>();

                try {
                    this.processingTaskQueue();
                } catch (Exception ex) {
                    logger.error(String.format("%s运行主任务时发生异常，已跳过当前任务",
                                               name),
                                 ex);
                }
            }
        } catch (Exception ex) {
            logger.error(String.format("%s运行主任务时发生异常, 程序已停止",
                                       name),
                         ex);
        }
    }

    /**
     * 处理主任务队列
     */
    private void processingTaskQueue() {
        while (!taskQueue.isEmpty()) {
            Key taskKey = taskQueue.poll();

            try {
                //等待并发任务数量少于并发上限
                while (concurrentTaskMap.size() >= concurrentTaskLimit) {
                    try {
                        CompletableFuture.anyOf(concurrentTaskMap.values()
                                                                 .toArray(new CompletableFuture[0]))
                                         .get();
                    } catch (Exception ignore) {

                    }
                }

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
    protected abstract void processingTask(Key taskKey);

    /**
     * 运行高优先级任务
     */
    private void runPriorityTask() {
        try {
            while (true) {
                if (!cf_priority.get()) return;

                cf_priority = new CompletableFuture<>();

                try {
                    this.processingPriorityTaskQueue();
                } catch (Exception ex) {
                    logger.error(String.format("%s运行优先级任务时发生异常，已跳过当前任务",
                                               name),
                                 ex);
                }
            }
        } catch (Exception ex) {
            logger.error(String.format("%s运行优先级任务时发生异常, 程序已停止",
                                       name),
                         ex);
        }
    }

    /**
     * 处理高优先级任务队列
     */
    private void processingPriorityTaskQueue() {
        while (!priorityTaskQueue.isEmpty()) {
            Key taskKey = priorityTaskQueue.pollLast();

            try {
                //等待并发任务数量少于并发上限
                while (concurrentPriorityTaskMap.size() >= concurrentTaskLimit) {
                    try {
                        CompletableFuture.anyOf(concurrentPriorityTaskMap.values()
                                                                         .toArray(new CompletableFuture[0]))
                                         .get();
                    } catch (Exception ignore) {

                    }
                }

                processingPriorityTask(taskKey);
            } catch (Exception ex) {
                logger.error(String.format("%s处理高优先级任务时发生异常，已跳过当前任务",
                                           name),
                             ex);
            }
        }
    }

    /**
     * 处理高优先级任务
     */
    protected abstract void processingPriorityTask(Key taskKey);

    /**
     * 新增并发子任务
     *
     * @param runnable 执行操作
     * @return 任务标识
     */
    protected String addConcurrentTask(Runnable runnable) {
        return addConcurrentTask(runnable,
                                 null);
    }

    /**
     * 新增并发子任务
     *
     * @param runnable     执行操作
     * @param thenRunAsync 操作结束后执行
     * @return 任务标识
     */
    protected String addConcurrentTask(Runnable runnable,
                                       Runnable thenRunAsync) {
        if (this.state.equals(TaskQueueHandlerState.停止中)
                || this.state.equals(TaskQueueHandlerState.已停止))
            throw new CommonException(String.format("%s已关闭",
                                                    getName()));

        String key = keyGenerate.nextId2String();

        concurrentTaskMap.put(key,
                              CompletableFuture.runAsync(runnable,
                                                         concurrentTaskExecutor)
                                               .thenRunAsync(() -> {
                                                   removeConcurrentTask(key);
                                                   if (thenRunAsync != null)
                                                       thenRunAsync.run();
                                               }));

        return key;
    }

    /**
     * 是否存在指定的并发子任务
     *
     * @param key 任务标识
     */
    protected boolean containsConcurrentTask(String key) {
        return concurrentTaskMap.containsKey(key);
    }

    /**
     * 移除指定的并发子任务
     * <p>此方法无需手动调用，只在需要接收结束通知时继承重载</p>
     *
     * @param key 任务标识
     */
    protected synchronized void removeConcurrentTask(String key) {
        concurrentTaskMap.remove(key);
        swatch(-1);
    }

    /**
     * 新增高优先级并发子任务
     *
     * @param runnable 执行操作
     * @return 任务标识
     */
    protected String addPriorityConcurrentTask(Runnable runnable) {
        return addPriorityConcurrentTask(runnable,
                                         null);
    }

    /**
     * 新增高优先级并发子任务
     *
     * @param runnable     执行操作
     * @param thenRunAsync 操作结束后执行
     * @return 任务标识
     */
    protected String addPriorityConcurrentTask(Runnable runnable,
                                               Runnable thenRunAsync) {
        if (this.state.equals(TaskQueueHandlerState.停止中)
                || this.state.equals(TaskQueueHandlerState.已停止))
            throw new CommonException(String.format("%s已关闭",
                                                    getName()));

        String key = keyGenerate.nextId2String();

        concurrentPriorityTaskMap.put(key,
                                      CompletableFuture.runAsync(runnable,
                                                                 concurrentTaskExecutor)
                                                       .thenRunAsync(() -> {
                                                           removePriorityConcurrentTask(key);
                                                           if (thenRunAsync != null)
                                                               thenRunAsync.run();
                                                       }));

        return key;
    }

    /**
     * 是否存在指定的高优先级并发子任务
     *
     * @param key 任务标识
     */
    protected boolean containsPriorityConcurrentTask(String key) {
        return concurrentPriorityTaskMap.containsKey(key);
    }

    /**
     * 移除指定的高优先级并发子任务
     * <p>此方法无需手动调用，只在需要接收结束通知时继承重载</p>
     *
     * @param key 任务标识
     */
    protected synchronized void removePriorityConcurrentTask(String key) {
        concurrentPriorityTaskMap.remove(key);
        swatch(-1);
    }

    /**
     * 添加延时子任务
     *
     * @param action    任务
     * @param parameter 参数
     * @param delay     延时
     */
    protected <T> void addScheduleTask(IAction1<T> action,
                                       T parameter,
                                       long delay) {
        if (this.state.equals(TaskQueueHandlerState.停止中)
                || this.state.equals(TaskQueueHandlerState.已停止))
            throw new CommonException(String.format("%s已关闭",
                                                    getName()));

        UUID key = UUID.randomUUID();
        scheduleTaskList.add(key);
        timer.schedule(new ActionTimerTask<>(state -> {
                           action.invoke(parameter);
                           scheduleTaskList.remove(key);
                       },
                                             parameter),
                       delay);
    }
}
