package project.extension.mybatis.edge.model;

import lombok.Data;

/**
 * 数据库类型转换至java类型相关信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
@Data
public class DbTypeToJavaType {
    public DbTypeToJavaType(String javaTypeConvert,
                            String javaPackageTypeConvert,
                            String javaParse,
                            String javaStringify,
                            String javaType,
                            String javaPackageType,
                            Class<?> javaTypeInfo,
                            Class<?> javaPackageTypeInfo) {
        this.javaTypeConvert = javaTypeConvert;
        this.javaPackageTypeConvert = javaPackageTypeConvert;
        this.javaParse = javaParse;
        this.javaStringify = javaStringify;
        this.javaPackageType = javaPackageType;
        this.javaType = javaType;
        this.javaTypeInfo = javaTypeInfo;
        this.javaPackageTypeInfo = javaPackageTypeInfo;
    }

    /**
     * 基本类型强制转换语句
     */
    private String javaTypeConvert;

    /**
     * 包装类型强制转换语句
     */
    private String javaPackageTypeConvert;

    /**
     * 反序列化语句
     */
    private String javaParse;

    /**
     * 序列化语句
     */
    private String javaStringify;

    /**
     * 基本类型
     */
    private String javaType;

    /**
     * 包装类型
     */
    private String javaPackageType;

    /**
     * 基本类型对象
     */
    private Class<?> javaTypeInfo;

    /**
     * 包装类型对象
     */
    private Class<?> javaPackageTypeInfo;
}
