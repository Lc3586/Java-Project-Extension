package project.extension.task;

import java.util.Arrays;
import java.util.Optional;

/**
 * 处理类状态
 *
 * @author LCTR
 * @date 2022-03-24
 */
public enum TaskQueueHandlerState {
    /**
     * 启动中
     * <p>可能正在处理启动前需要执行的方法</p>
     */
    启动中(0,
        "启动中"),
    /**
     * 空闲
     * <p>队列为空</p>
     * <p>异步子任务全部执行完毕</p>
     * <p>但是可能存在要处理的延时子任务</p>
     */
    空闲(1,
       "空闲"),
    /**
     * 运行中
     * <p>正在处理队列</p>
     * <p>正在处理异步子任务</p>
     */
    运行中(2,
        "运行中"),
    /**
     * 停止中
     * <p>可能正在处理停止前需要执行的方法</p>
     */
    停止中(3,
        "停止中"),
    /**
     * 已停止
     * <p>不再接收新任务</p>
     * <p>添加新任务将会抛出异常</p>
     */
    已停止(4,
        "已停止");

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