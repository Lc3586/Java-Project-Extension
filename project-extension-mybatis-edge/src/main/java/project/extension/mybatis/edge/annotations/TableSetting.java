package project.extension.mybatis.edge.annotations;

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
     * 表的别名
     * <p>如果同时设置了finalName，则最后使用的将会是finalName</p>
     */
    @AliasFor("alias") String value() default "";

    /**
     * 表的别名
     * <p>如果同时设置了finalName，则最后使用的将会是finalName</p>
     */
    @AliasFor("value") String alias() default "";

    /**
     * 指定表名
     * <p>此值不受配置中的命名规则影响</p>
     * <p>优先级高于别名</p>
     */
    String finalName() default "";

    /**
     * 模式名
     * <p>此值不受配置中的命名规则影响</p>
     */
    String schema() default "";
}