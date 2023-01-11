package project.extension.mybatis.edge.aop;

/**
 * 执行事务操作之后触发的事件的参数
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class TraceAfterEventArgs
        extends TraceBeforeEventArgs {
    public TraceAfterEventArgs(TraceBeforeEventArgs beforeEventData,
                               String remark,
                               Exception exception) {
        super(beforeEventData.getIdentifier(),
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

    /**
     * 耗时（毫秒）
     */
    public Long getCosttime() {
        return costtime;
    }

    /**
     * 操作异常时的错误
     */
    public Exception getException() {
        return exception;
    }

    /**
     * 备注
     */
    public Object getRemark() {
        return remark;
    }
}
