package project.extension.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.extension.action.IAction3;

import java.util.TimerTask;

/**
 * 定时任务
 *
 * @author LCTR
 * @date 2023-02-10
 */
public class ActionTimerTask3<T1, T2, T3>
        extends TimerTask {
    /**
     * @param action     方法
     * @param parameter1 参数1
     * @param parameter2 参数2
     * @param parameter3 参数3
     */
    public ActionTimerTask3(IAction3<T1, T2, T3> action,
                            T1 parameter1,
                            T2 parameter2,
                            T3 parameter3) {
        this.action = action;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
        this.parameter3 = parameter3;
    }

    /**
     * 方法
     */
    private final IAction3<T1, T2, T3> action;

    /**
     * 参数1
     */
    private final T1 parameter1;

    /**
     * 参数2
     */
    private final T2 parameter2;

    /**
     * 参数3
     */
    private final T3 parameter3;

    /**
     * 日志组件
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 运行
     */
    @Override
    public void run() {
        try {
            action.invoke(parameter1,
                          parameter2,
                          parameter3);
        } catch (Exception ex) {
            logger.error("执行定时任务时发生异常",
                         ex);
        }
    }
}
