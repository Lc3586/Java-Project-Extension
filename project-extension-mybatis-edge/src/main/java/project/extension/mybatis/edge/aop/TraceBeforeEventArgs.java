package project.extension.mybatis.edge.aop;

import org.apache.commons.lang3.time.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 执行事务操作之前触发的事件的参数
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class TraceBeforeEventArgs {
    public TraceBeforeEventArgs(Operation operation,
                                Object value) {
        this(UUID.randomUUID(),
             new StopWatch(),
             operation,
             value,
             new HashMap<>());
    }

    protected TraceBeforeEventArgs(UUID identifier,
                                   StopWatch watch,
                                   Operation operation,
                                   Object value,
                                   Map<String, Object> states) {
        this.identifier = identifier;
        this.watch = watch;
        if (!this.watch.isStarted())
            this.watch.start();
        this.operation = operation;
        this.value = value;
        this.states = states;
    }

    /**
     * 标识
     * <p>可将 TraceBeforeEventArgs 与 TraceAfterEventArgs 进行匹配</p>
     */
    private final UUID identifier;

    /**
     * 计时器
     */
    private final StopWatch watch;

    /**
     * 操作
     */
    public final Operation operation;

    /**
     * 值
     */
    public final Object value;

    /**
     * 状态数据
     */
    public Map<String, Object> states;

    /**
     * 标识
     * <p>可将 TraceBeforeEventArgs 与 TraceAfterEventArgs 进行匹配</p>
     *
     * @return 标识
     */
    public UUID getIdentifier() {
        return this.identifier;
    }

    /**
     * 计时器
     */
    protected StopWatch getWatch() {
        return watch;
    }

    /**
     * 操作
     *
     * @return 操作
     */
    public Operation getOperation() {
        return this.operation;
    }

    /**
     * 值
     *
     * @return 值
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * 状态数据
     *
     * @return 状态数据
     */
    public Map<String, Object> getStates() {
        return this.states;
    }
}
