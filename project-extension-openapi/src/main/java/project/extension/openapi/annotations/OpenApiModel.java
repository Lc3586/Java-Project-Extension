package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;
import project.extension.openapi.model.ExampleType;

import java.lang.annotation.*;

/**
 * 模型
 *
 * @author LCTR
 * @date 2022-04-25
 */
@Documented
@Target({ElementType.METHOD,
         ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiModel {
    /**
     * 类型
     */
    @AliasFor("type") Class<?> value() default Void.class;

    /**
     * 类型
     */
    @AliasFor("value") Class<?> type() default Void.class;

    /**
     * 泛型类型
     */
    OpenApiGenericType[] genericTypes() default {};

    /**
     * 业务模型类型
     */
    OpenApiDtoType[] dtoTypes() default {};

    /**
     * 示例类型
     */
    ExampleType exampleType() default ExampleType.JsonWithComments;

    /**
     * 名称
     */
    String name() default "";

    /**
     * 说明
     */
    String summary() default "";

    /**
     * 注释
     */
    String description() default "";

    /**
     * 默认
     */
    boolean defaultModel() default false;
}