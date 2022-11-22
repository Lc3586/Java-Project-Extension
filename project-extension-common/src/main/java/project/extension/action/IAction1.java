package project.extension.action;

/**
 * 有参方法
 *
 * @author LCTR
 * @date 2022-04-18
 */
public interface IAction1<TParameter> {
    /**
     * 执行
     *
     * @param parameter 参数
     */
    void invoke(TParameter parameter)
            throws
            Exception;
}
