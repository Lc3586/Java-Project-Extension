package project.extension.func;

/**
 * 有参有返回值方法
 *
 * @author LCTR
 * @date 2023-06-25
 */
public interface IFunc2Throw<TParameter1, TParameter2, TResult> {
    /**
     * 执行
     *
     * @param parameter1 参数1
     * @param parameter2 参数2
     */
    TResult invoke(TParameter1 parameter1,
                   TParameter2 parameter2)
            throws
            Exception;
}
