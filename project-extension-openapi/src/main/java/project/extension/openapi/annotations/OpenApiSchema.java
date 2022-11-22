package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 接口架构属性
 *
 * @author LCTR
 * @date 2022-03-18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,
         ElementType.PARAMETER})
public @interface OpenApiSchema {
    /**
     * 示例值
     */
    @AliasFor("valueExample" ) String value() default "";

    /**
     * 类型
     *
     * @see project.extension.openapi.model.OpenApiSchemaType
     */
    String type() default "";

    /**
     * 格式
     *
     * @see project.extension.openapi.model.OpenApiSchemaFormat
     */
    String format() default "";

    /**
     * 示例值
     */
    @AliasFor("value" ) String valueExample() default "";

    /**
     * 排序值
     * <p>从1开始</p>
     */
    int sort() default 1;
}
