package project.extension.func;

/**
 * 有参有返回值方法
 *
 * @author LCTR
 * @date 2022-06-16
 */
public interface IFunc1<TParameter, TResult> {
    /**
     * 执行
     *
     * @param parameter 参数
     */
    TResult invoke(TParameter parameter)
            throws
            Exception;
}
