package project.extension.action;

/**
 * 有参方法
 *
 * @author LCTR
 * @date 2023-06-25
 */
public interface IAction6Throw<TParameter1, TParameter2, TParameter3, TParameter4, TParameter5, TParameter6> {
    /**
     * 执行
     *
     * @param parameter1 参数1
     * @param parameter2 参数2
     * @param parameter3 参数3
     * @param parameter4 参数4
     * @param parameter5 参数5
     * @param parameter6 参数6
     */
    void invoke(TParameter1 parameter1,
                TParameter2 parameter2,
                TParameter3 parameter3,
                TParameter4 parameter4,
                TParameter5 parameter5,
                TParameter6 parameter6)
            throws
            Exception;
}
