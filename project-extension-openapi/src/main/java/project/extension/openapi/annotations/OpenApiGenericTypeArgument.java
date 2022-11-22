package project.extension.openapi.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 泛型类型参数
 *
 * @author LCTR
 * @date 2022-05-05
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiGenericTypeArgument {
    /**
     * 参数类型
     */
    @AliasFor("type") Class<?> value() default Void.class;

    /**
     * 参数类型
     */
    @AliasFor("value") Class<?> type() default Void.class;

    /**
     * 参数名称
     * <p>1、默认值为 T</p>
     * <p>2、当类型只有一个泛型参数时无需设置此值</p>
     * <p>3、泛型参数名称区分大小写</p>
     */
    String name() default "T";
}