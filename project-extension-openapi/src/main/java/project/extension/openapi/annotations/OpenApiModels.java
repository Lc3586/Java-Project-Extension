package project.extension.openapi.annotations;

import project.extension.openapi.model.ExampleType;

import java.lang.annotation.*;

/**
 * 多个模型
 *
 * @author LCTR
 * @date 2022-04-28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OpenApiModels {
    OpenApiModel[] value();

    /**
     * 未特别设置时默认使用的类型
     */
    Class<?> defaultType() default Void.class;

    /**
     * 未特别设置时默认使用的业务模型类型
     */
    OpenApiDtoType[] defaultDtoTypes() default {};

    /**
     * 未特别设置时默认使用的泛型类型
     */
    OpenApiGenericType[] defaultGenericTypes() default {};

    /**
     * 未特别设置时默认使用的其他标签
     */
    String[] defaultCustomTags() default "";

    /**
     * 未特别设置时默认使用的说明
     */
    String defaultSummary() default "";

    /**
     * 未特别设置时默认使用的注释
     */
    String defaultDescription() default "";
}
