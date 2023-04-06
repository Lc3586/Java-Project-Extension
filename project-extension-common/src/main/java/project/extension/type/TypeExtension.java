package project.extension.type;

import project.extension.tuple.Tuple2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类型拓展方法
 *
 * @author LCTR
 * @date 2023-04-06
 */
public class TypeExtension {
    private static Method getGenericSignatureMethod;

    /**
     * 获取泛型签名信息的方法
     */
    public static Method getGenericSignatureMethod()
            throws
            NoSuchMethodException {
        if (getGenericSignatureMethod == null) {
            getGenericSignatureMethod = Field.class.getDeclaredMethod("getGenericSignature");
            getGenericSignatureMethod.setAccessible(true);
        }
        return getGenericSignatureMethod;
    }

    /**
     * 是否为java语言类型
     *
     * @param type 类型
     */
    public static boolean isLangType(Class<?> type) {
        return type.getTypeName()
                   .startsWith("java.lang.")
                || type.equals(byte.class)
                || type.equals(int.class)
                || type.equals(short.class)
                || type.equals(long.class);
    }

    /**
     * 获取父类类型
     *
     * @param type 类型
     * @return 类型的父类类型，如果没有则返回它自己
     */
    public static Class<?> getSuperType(Class<?> type) {
//        //返回数组类型的元素类型
//        if (type.isArray()) return getSuperModelType(type.getComponentType());
//
        //获取继承类
        Class<?> superClazz = type.getSuperclass();
//        if (superClazz == null || superClazz.equals(Object.class)) return type;

        //是否标记为父类类型
        if (superClazz != null && !superClazz.equals(Object.class)) return superClazz;
//        if (superClazz != null && !superClazz.equals(Object.class)
//                && superClazz.getAnnotation(OpenApiSuperModel.class) != null) return superClazz;

        return type;
    }

    /**
     * 获取泛型集合元素类型
     *
     * @param clazz 类型
     * @param type  类型
     * @return 元素类型
     */
    public static Class<?> getGenericType(Class<?> clazz,
                                          Type type) {
        //返回数组类型的元素类型
        if (clazz.isArray()) return getGenericType(clazz.getComponentType());

        //返回泛型类型
        if (type instanceof ParameterizedType && Iterable.class.isAssignableFrom(clazz)) {
            return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
        }

        return clazz;
    }

    /**
     * 获取数组元素类型
     *
     * @param type 类型
     * @return 元素类型
     */
    public static Class<?> getGenericType(Class<?> type) {
        //返回数组类型的元素类型
        if (type.isArray()) return getGenericType(type.getComponentType());

        return null;
    }

    /**
     * 获取数组和泛型集合元素类型
     *
     * @param clazz 类型
     * @param type  泛型类型
     * @return a: 是否为数组或泛型集合,b: 元素类型
     */
    public static Tuple2<Boolean, Class<?>> tryGetCollectionItemType(Class<?> clazz,
                                                                     Type type) {
        Boolean flag = clazz.isArray() || (type instanceof ParameterizedType && Iterable.class.isAssignableFrom(clazz));
        return new Tuple2<>(flag,
                            flag
                            ? getGenericType(clazz,
                                             type)
                            : null);
    }
}
