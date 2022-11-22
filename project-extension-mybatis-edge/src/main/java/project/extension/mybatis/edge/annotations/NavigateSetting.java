package project.extension.mybatis.edge.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 导航属性设置
 *
 * @author LCTR
 * @date 2022-03-31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
public @interface NavigateSetting {
    /**
     * 绑定外键
     *
     * @return
     */
    @AliasFor("bind") String value() default "";

    /**
     * 绑定外键
     *
     * @return
     */
    @AliasFor("value") String bind() default "";

    /**
     * 绑定一对多的外键表实体类型
     *
     * @return
     */
    Class<?> one2Many() default Object.class;

    /**
     * 指定多对多的关系表实体类型
     *
     * @return
     */
    Class<?> many2Many() default Object.class;
}