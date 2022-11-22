package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 附属标签
 * <p>标明所属，用于属性和字段</p>
 * <p>和主标签搭配使用</p>
 *
 * @author LCTR
 * @date 2022-03-18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface OpenApiSubTag {
    /**
     * 标签名称集合
     *
     * @return
     */
    @AliasFor("names") String[] value() default "";

    /**
     * 标签名称集合
     *
     * @return
     */
    @AliasFor("value") String[] names() default "";
}
