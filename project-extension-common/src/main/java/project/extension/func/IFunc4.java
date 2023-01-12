package project.extension.func;

/**
 * 有参有返回值方法
 *
 * @author LCTR
 * @date 2022-06-16
 */
public interface IFunc4<TParameter1, TParameter2, TParameter3, TParameter4, TResult> {
    /**
     * 执行
     *
     * @param parameter1 参数1
     * @param parameter2 参数2
     * @param parameter3 参数3
     * @param parameter4 参数4
     */
    TResult invoke(TParameter1 parameter1,
                   TParameter2 parameter2,
                   TParameter3 parameter3,
                   TParameter4 parameter4);
}
