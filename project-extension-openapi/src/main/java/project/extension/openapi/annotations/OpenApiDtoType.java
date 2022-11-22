package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 泛型参数
 *
 * @author LCTR
 * @date 2022-05-05
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiDtoType {
    /**
     * 类型
     */
    @AliasFor("type") Class<?> value() default Void.class;

    /**
     * 类型
     */
    @AliasFor("value") Class<?> type() default Void.class;

    /**
     * 主标签等级
     */
    int mainTagLevel() default 0;

    /**
     * 其他标签
     */
    String[] customTags() default "";
}