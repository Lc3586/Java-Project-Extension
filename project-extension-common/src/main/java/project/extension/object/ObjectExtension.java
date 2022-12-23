package project.extension.object;

/**
 * Object拓展方法
 *
 * @author LCTR
 * @date 2022-04-15
 */
public class ObjectExtension {
    /**
     * 尝试将指定类型S的对象转换为目标类型T
     *
     * @param object 指定对象
     * @param target 目标类型
     * @param <S>    指定类型S
     * @param <T>    目标类型T
     * @return 如果转换失败则返回null
     */
    public static <S, T> T as(S object,
                              Class<T> target) {
        return target.isInstance(object)
               ? target.cast(object)
               : null;
    }
}
