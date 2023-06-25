package project.extension.action;

/**
 * 有参方法
 *
 * @author LCTR
 * @date 2023-06-25
 */
public interface IAction3Throw<TParameter1, TParameter2, TParameter3> {
    /**
     * 执行
     *
     * @param parameter1 参数1
     * @param parameter2 参数2
     * @param parameter3 参数3
     */
    void invoke(TParameter1 parameter1,
                TParameter2 parameter2,
                TParameter3 parameter3)
            throws
            Exception;
}
