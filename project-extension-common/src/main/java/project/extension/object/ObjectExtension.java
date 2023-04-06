package project.extension.object;

import com.alibaba.fastjson.JSONObject;
import project.extension.tuple.Tuple2;

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

    /**
     * 转换类型
     *
     * @param value        数据
     * @param defaultValue 默认值
     * @param type         目标数据类型
     * @param <T>          目标数据类型
     * @return 已转为目标类型的数据，失败时为默认值
     */
    public static <T> Tuple2<Boolean, T> tryCast(Object value,
                                                 T defaultValue,
                                                 Class<T> type) {
        try {
            return new Tuple2<>(true,
                                JSONObject.parseObject(JSONObject.toJSONString(value),
                                                       type));
        } catch (Exception ex) {
            return new Tuple2<>(false,
                                defaultValue);
        }
    }
}
