package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 泛型类型
 * <p>1、存在嵌套的泛型类型时，需要使用层级来指定当前的泛型类型是哪一个类型</p>
 *
 * @author LCTR
 * @date 2022-05-06
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiGenericType {
    /**
     * 类型
     */
    @AliasFor("type") Class<?> value() default Void.class;

    /**
     * 类型
     */
    @AliasFor("value") Class<?> type() default Void.class;

    /**
     * 层级
     */
    int level() default 0;

    /**
     * 类型参数
     */
    OpenApiGenericTypeArgument[] arguments() default {};
}