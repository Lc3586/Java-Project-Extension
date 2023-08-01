package project.extension.mybatis.edge.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 实体映射
 *
 * @author LCTR
 * @date 2022-04-15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,
         ElementType.FIELD,
         ElementType.PARAMETER})
public @interface EntityMapping {
    /**
     * 实体类型
     */
    @AliasFor("entityType") Class<?> value() default Object.class;

    /**
     * 实体类型
     */
    @AliasFor("value") Class<?> entityType() default Object.class;

    /**
     * 写入查询结果时使用此配置
     */
    boolean enableSetter() default true;

    /**
     * 读取数据时使用此配置
     */
    boolean enableGetter() default true;
}