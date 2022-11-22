package project.extension.mybatis.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 表设置
 *
 * @author LCTR
 * @date 2022-04-01
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableSetting {
    /**
     * 表名
     * <p>此值不受配置中的命名规则影响</p>
     */
    @AliasFor("name") String value() default "";

    /**
     * 表名
     * <p>此值不受配置中的命名规则影响</p>
     */
    @AliasFor("value") String name() default "";

    /**
     * 模式名
     * <p>此值不受配置中的命名规则影响</p>
     */
    String schema() default "";
}