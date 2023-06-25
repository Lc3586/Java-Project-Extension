package project.extension.action;

/**
 * 有参方法
 *
 * @author LCTR
 * @date 2023-06-25
 */
public interface IAction1Throw<TParameter> {
    /**
     * 执行
     *
     * @param parameter 参数
     */
    void invoke(TParameter parameter)
            throws
            Exception;
}
