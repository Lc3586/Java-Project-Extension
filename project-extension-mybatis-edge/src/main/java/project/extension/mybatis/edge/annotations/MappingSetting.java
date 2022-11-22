package project.extension.mybatis.edge.annotations;

import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.type.JdbcType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 映射设置
 *
 * @author LCTR
 * @date 2022-04-11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,
         ElementType.FIELD,
         ElementType.PARAMETER})
public @interface MappingSetting {
    /**
     * JdbcType
     */
    @AliasFor("jdbcType") JdbcType value() default JdbcType.UNDEFINED;

    /**
     * JdbcType
     */
    @AliasFor("value") JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * org.apache.ibatis.type.TypeHandler
     */
    Class<?> typeHandler() default Void.class;

    /**
     * 忽略
     */
    boolean ignore() default false;

    /**
     * 参数模式
     *
     * @see ParameterMode
     * <p>默认值 入参</p>
     */
    ParameterMode parameterMode() default ParameterMode.IN;
}