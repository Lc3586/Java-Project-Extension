package project.extension.mybatis.edge.aop;

import lombok.Data;

/**
 * 执行事务操作之后触发的事件的参数
 *
 * @author LCTR
 * @date 2022-12-19
 */
@Data
public class AfterTraceEventArgs
        extends BeforeTraceEventArgs {
    public AfterTraceEventArgs(BeforeTraceEventArgs beforeEventData,
                               String remark,
                               Exception exception) {
        super(beforeEventData.getTransactionDefinitionName(),
              beforeEventData.getWatch(),
              beforeEventData.getOperation(),
              beforeEventData.getValue(),
              beforeEventData.getStates());
        if (super.getWatch()
                 .isStarted())
            super.getWatch()
                 .stop();
        this.costtime = super.getWatch()
                             .getTime();
        this.remark = remark;
        this.exception = exception;
    }

    /**
     * 耗时（毫秒）
     */
    private final Long costtime;

    /**
     * 操作异常时的错误
     */
    private final Exception exception;

    /**
     * 备注
     */
    private final String remark;
}
