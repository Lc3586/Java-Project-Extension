package project.extension.openapi.annotations;

import java.lang.annotation.*;

/**
 * 允许匿名访问
 *
 * @author LCTR
 * @date 2022-04-25
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiAllowAnonymous {

}