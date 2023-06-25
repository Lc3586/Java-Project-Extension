package project.extension.func;

/**
 * 无参有返回值方法
 *
 * @author LCTR
 * @date 2023-06-25
 */
public interface IFunc0Throw<TResult> {
    /**
     * 执行
     */
    TResult invoke()
            throws
            Exception;
}
