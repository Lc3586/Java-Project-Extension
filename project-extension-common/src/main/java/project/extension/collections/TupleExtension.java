package project.extension.collections;

import project.extension.tuple.*;

import java.util.Arrays;
import java.util.List;

/**
 * 元组拓展方法
 *
 * @author LCTR
 * @date 2022-03-31
 */
public class TupleExtension {
    /**
     * 元组值转集合
     *
     * @param tuple     元组数据
     * @param tupleType 元组类型
     * @return 值集合
     * @throws UnsupportedOperationException
     */
    public static List<Object> tuple2List(Object tuple, Class<?> tupleType) throws UnsupportedOperationException {
        if (tupleType.equals(Tuple2.class)) {
            Tuple2 tuple2 = (Tuple2) tuple;
            return Arrays.asList(tuple2.a, tuple2.b);
        } else if (tupleType.equals(Tuple3.class)) {
            Tuple3 tuple3 = (Tuple3) tuple;
            return Arrays.asList(tuple3.a, tuple3.b, tuple3.c);
        } else if (tupleType.equals(Tuple4.class)) {
            Tuple4 tuple4 = (Tuple4) tuple;
            return Arrays.asList(tuple4.a, tuple4.b, tuple4.c, tuple4.d);
        } else if (tupleType.equals(Tuple5.class)) {
            Tuple5 tuple5 = (Tuple5) tuple;
            return Arrays.asList(tuple5.a, tuple5.b, tuple5.c, tuple5.d, tuple5.e);
        } else if (tupleType.equals(Tuple6.class)) {
            Tuple6 tuple6 = (Tuple6) tuple;
            return Arrays.asList(tuple6.a, tuple6.b, tuple6.c, tuple6.d, tuple6.e, tuple6.f);
        } else
            throw new UnsupportedOperationException(String.format("暂不支持此元组类型%s", tupleType.getTypeName()));
    }
}
