package project.extension.openapi.annotations;

import java.lang.annotation.*;

/**
 * 分组
 *
 * @author LCTR
 * @date 2022-04-25
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiGroup {
    /**
     * 分组名称集合
     *
     * @return
     */
    String[] value();
}