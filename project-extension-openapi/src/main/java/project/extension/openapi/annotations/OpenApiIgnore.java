package project.extension.openapi.annotations;

import java.lang.annotation.*;

/**
 * 处理接口架构时忽略带有此注解的对象
 *
 * @author LCTR
 * @date 2022-03-18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface OpenApiIgnore {

}
