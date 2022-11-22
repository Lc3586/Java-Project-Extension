package project.extension.openapi.annotations;

import java.lang.annotation.*;

/**
 * 多个泛型参数
 *
 * @author LCTR
 * @date 2022-05-05
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface OpenApiGenericTypeArguments {
    OpenApiGenericTypeArgument[] value();
}
