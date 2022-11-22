package project.extension.openapi.annotations;

import java.lang.annotation.*;

/**
 * 接口架构严格模式
 * <p>1：未指定OpenApiSchema注解的属性将不会输出</p>
 * <p>2：当存在MainTag时，未指定SubTag的属性将不会处理</p>
 *
 * @author LCTR
 * @date 2022-03-18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
public @interface OpenApiSchemaStrictMode {
}
