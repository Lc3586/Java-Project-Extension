package project.extension.mybatis.edge.aop;

import lombok.Data;
import org.apache.commons.lang3.time.StopWatch;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行事务操作之前触发的事件的参数
 *
 * @author LCTR
 * @date 2022-12-19
 */
@Data
public class BeforeTraceEventArgs {
    public BeforeTraceEventArgs(String transactionDefinitionName,
                                Operation operation,
                                Object value) {
        this(transactionDefinitionName,
             new StopWatch(),
             operation,
             value,
             new HashMap<>());
    }

    protected BeforeTraceEventArgs(String transactionDefinitionName,
                                   StopWatch watch,
                                   Operation operation,
                                   Object value,
                                   Map<String, Object> states) {
        this.transactionDefinitionName = transactionDefinitionName;
        this.watch = watch;
        if (!this.watch.isStarted() && !this.watch.isStopped())
            this.watch.start();
        this.operation = operation;
        this.value = value;
        this.states = states;
    }

    /**
     * 事务定义名称
     */
    private final String transactionDefinitionName;

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
}
