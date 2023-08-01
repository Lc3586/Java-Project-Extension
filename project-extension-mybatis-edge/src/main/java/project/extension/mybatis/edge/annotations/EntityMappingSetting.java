package project.extension.mybatis.edge.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 实体映射设置
 *
 * @author LCTR
 * @date 2022-04-15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,
         ElementType.FIELD,
         ElementType.PARAMETER})
public @interface EntityMappingSetting {
    /**
     * 对应实体类的字段名
     */
    @AliasFor("entityFieldName") String value() default "";

    /**
     * 对应实体类的字段名
     */
    @AliasFor("value") String entityFieldName() default "";

    /**
     * 实体类型
     */
    Class<?> entityType() default Void.class;

    /**
     * 从自身获取列名
     */
    boolean self() default false;

    /**
     * 主键
     */
    boolean isPrimaryKey() default false;

    /**
     * 自增
     */
    boolean isIdentity() default false;

    /**
     * 忽略
     */
    boolean ignore() default false;

    /**
     * 写入查询结果时使用此配置
     */
    boolean enableSetter() default true;

    /**
     * 读取数据时使用此配置
     */
    boolean enableGetter() default true;
}