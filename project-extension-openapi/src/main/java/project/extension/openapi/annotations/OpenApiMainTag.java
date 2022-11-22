package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 主标签
 * <p>主要的，用于类</p>
 * <p>和附属标签搭配使用</p>
 *
 * @author LCTR
 * @date 2022-03-18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
public @interface OpenApiMainTag {
    /**
     * 标签名称集合
     */
    @AliasFor("names") String[] value() default "";

    /**
     * 标签名称集合
     */
    @AliasFor("value") String[] names() default "";

    /**
     * 标签级别
     */
    int level() default 0;
}
