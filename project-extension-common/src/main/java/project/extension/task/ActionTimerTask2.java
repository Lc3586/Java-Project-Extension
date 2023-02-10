package project.extension.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.extension.action.IAction2;

import java.util.TimerTask;

/**
 * 定时任务
 *
 * @author LCTR
 * @date 2023-02-10
 */
public class ActionTimerTask2<T1, T2>
        extends TimerTask {
    /**
     * @param action     方法
     * @param parameter1 参数1
     * @param parameter2 参数2
     */
    public ActionTimerTask2(IAction2<T1, T2> action,
                            T1 parameter1,
                            T2 parameter2) {
        this.action = action;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
    }

    /**
     * 方法
     */
    private final IAction2<T1, T2> action;

    /**
     * 参数1
     */
    private final T1 parameter1;

    /**
     * 参数2
     */
    private final T2 parameter2;

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
                          parameter2);
        } catch (Exception ex) {
            logger.error("执行定时任务时发生异常",
                         ex);
        }
    }
}
