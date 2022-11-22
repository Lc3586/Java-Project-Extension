package project.extension.action;

/**
 * 有参方法
 *
 * @author LCTR
 * @date 2022-04-18
 */
public interface IAction2<TParameter1, TParameter2> {
    /**
     * 执行
     *
     * @param parameter1 参数1
     * @param parameter2 参数2
     */
    void invoke(TParameter1 parameter1,
                TParameter2 parameter2)
            throws
            Exception;
}
