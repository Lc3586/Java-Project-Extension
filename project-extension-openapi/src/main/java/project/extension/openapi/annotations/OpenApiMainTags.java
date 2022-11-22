package project.extension.openapi.annotations;

import java.lang.annotation.*;

/**
 * 主标签
 * <p>主要的，用于类</p>
 * <p>和附属标签搭配使用</p>
 *
 * @author LCTR
 * @date 2022-04-03
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.PARAMETER})
public @interface OpenApiMainTags {
    OpenApiMainTag[] value();
}
