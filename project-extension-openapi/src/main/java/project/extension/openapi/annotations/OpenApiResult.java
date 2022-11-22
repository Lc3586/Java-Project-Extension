package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 返回值
 *
 * @author LCTR
 * @date 2022-04-25
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiResult {
    /**
     * 业务模型
     */
    @AliasFor("dtoType") Class<?> value() default Object.class;

    /**
     * 业务模型
     */
    @AliasFor("value") Class<?> dtoType() default Object.class;

    /**
     * 数组
     */
    boolean array() default false;

    /**
     * 说明
     */
    String description() default "";
}