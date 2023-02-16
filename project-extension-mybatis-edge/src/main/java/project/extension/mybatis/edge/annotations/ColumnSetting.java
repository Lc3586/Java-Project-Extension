package project.extension.mybatis.edge.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 列设置
 *
 * @author LCTR
 * @date 2022-03-31
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnSetting {
    /**
     * 列的别名
     * <p>如果同时设置了finalName，则最后使用的将会是finalName</p>
     */
    @AliasFor("alias") String value() default "";

    /**
     * 列的别名
     * <p>如果同时设置了finalName，则最后使用的将会是finalName</p>
     */
    @AliasFor("value") String alias() default "";

    /**
     * 指定列名
     * <p>此值不受配置中的命名规则影响</p>
     * <p>优先级高于别名</p>
     */
    String finalName() default "";

    /**
     * 主键
     */
    boolean primaryKey() default false;

    /**
     * 忽略
     */
    boolean ignore() default false;

    /**
     * 长度
     * <p>-1 对应JdbcType.LONGNVARCHAR</p>
     * <p>-2 对应JdbcType.LONGVARCHAR</p>
     * <p>-3 对应JdbcType.CLOB</p>
     */
    int length() default 30;

    /**
     * 长度
     */
    int precision() default 0;

    /**
     * 小数位
     */
    int scale() default 0;

    /**
     * 可为空
     */
    boolean isNullable() default true;
}