package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 接口架构父类模型
 *
 * @author LCTR
 * @date 2022-03-22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface OpenApiSuperModel {
    /**
     * 是否还有父类
     *
     * @return
     */
    @AliasFor("superSuper") boolean value() default true;

    /**
     * 是否还有父类
     *
     * @return
     */
    @AliasFor("value") boolean superSuper() default true;
}
