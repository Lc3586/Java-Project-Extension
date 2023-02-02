package project.extension.task;

import java.util.Arrays;
import java.util.Optional;

/**
 * 数据库类型
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum TaskQueueHandlerState {
    /**
     * 启动中
     */
    STARTING(0,
             "STARTING"),
    /**
     * 空闲
     * <p>队列为空</p>
     * <p>但是可能存在要处理的异步子任务和延时子任务</p>
     */
    IDLING(1,
           "IDLING"),
    /**
     * 运行中
     * <p>正在处理队列</p>
     */
    RUNNING(2,
            "RUNNING"),
    /**
     * 停止中
     */
    STOPPING(3,
             "STOPPING"),
    /**
     * 已停止
     */
    STOPPED(4,
            "STOPPED");

    /**
     * @param index 索引
     * @param value 值
     */
    TaskQueueHandlerState(int index,
                          String value) {
        this.index = index;
        this.value = value;
    }

    /**
     * 索引
     */
    final int index;

    /**
     * 值
     */
    final String value;

    /**
     * 获取索引
     *
     * @return 索引
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * 获取字符串
     *
     * @return 值
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static TaskQueueHandlerState toEnum(String value)
            throws
            IllegalArgumentException {
        Optional<TaskQueueHandlerState> find = Arrays.stream(TaskQueueHandlerState.values())
                                                     .filter(x -> x.value.equals(value))
                                                     .findFirst();
        if (!find.isPresent())
            throw new IllegalArgumentException(String.format("未找到符合%s此值的TaskQueueHandlerState枚举",
                                                             value));
        return find.get();
    }

    /**
     * 获取枚举
     *
     * @param index 索引
     * @return 枚举
     */
    public static TaskQueueHandlerState toEnum(int index)
            throws
            IllegalArgumentException {
        for (TaskQueueHandlerState value : TaskQueueHandlerState.values()) {
            if (value.getIndex() == index)
                return value;
        }

        throw new IllegalArgumentException(String.format("指定索引%s无效",
                                                         index));
    }
}