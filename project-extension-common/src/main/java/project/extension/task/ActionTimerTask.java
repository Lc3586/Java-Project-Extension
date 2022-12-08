package project.extension.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.extension.action.IAction1;

import java.util.TimerTask;

/**
 * 定时任务
 *
 * @author LCTR
 * @date 2022-12-08
 */
public class ActionTimerTask<T>
        extends TimerTask {
    /**
     * @param action    方法
     * @param parameter 参数
     */
    public ActionTimerTask(IAction1<T> action,
                           T parameter) {
        this.action = action;
        this.parameter = parameter;
    }

    /**
     * 方法
     */
    private final IAction1<T> action;

    /**
     * 参数
     */
    private final T parameter;

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
            action.invoke(parameter);
        } catch (Exception ex) {
            logger.error("执行定时任务时发生异常",
                         ex);
        }
    }
}
