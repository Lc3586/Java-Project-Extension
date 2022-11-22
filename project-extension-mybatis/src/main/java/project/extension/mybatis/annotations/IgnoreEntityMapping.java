package project.extension.mybatis.annotations;

import java.lang.annotation.*;

/**
 * 忽略实体映射
 *
 * @author LCTR
 * @date 2022-04-15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface IgnoreEntityMapping {

}