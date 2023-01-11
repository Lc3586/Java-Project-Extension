package project.extension.mybatis.edge.aop;

/**
 * 执行增删改查操作的命令之后触发的事件的参数
 *
 * @author LCTR
 * @date 2022-07-14
 */
public class CurdAfterEventArgs
        extends CurdBeforeEventArgs {
    public CurdAfterEventArgs(CurdBeforeEventArgs beforeEventData,
                              Exception exception,
                              Object executeResult) {
        super(beforeEventData.getIdentifier(),
              beforeEventData.getWatch(),
              beforeEventData.getCurdType(),
              beforeEventData.getEntityType(),
              beforeEventData.getDtoType(),
              beforeEventData.getDataSource(),
              beforeEventData.getSql(),
              beforeEventData.getParameter(),
              beforeEventData.getStates());
        if (super.getWatch()
                 .isStarted())
            super.getWatch()
                 .stop();
        this.costtime = super.getWatch()
                             .getTime();
        this.exception = exception;
        this.executeResult = executeResult;
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
     * 执行SQL语句后返回的结果
     */
    private final Object executeResult;

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
     * 执行SQL语句后返回的结果
     */
    public Object getExecuteResult() {
        return executeResult;
    }
}
