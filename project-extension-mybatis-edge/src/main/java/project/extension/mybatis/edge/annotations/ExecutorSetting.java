package project.extension.mybatis.edge.annotations;

import org.springframework.core.annotation.AliasFor;
import project.extension.mybatis.edge.model.ExecutorParameter;

import java.lang.annotation.*;

/**
 * 查询设置
 *
 * @author LCTR
 * @date 2022-03-22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
public @interface ExecutorSetting {
    /**
     * 参数类型
     * <p>project.extension.mybatis.model.PaginationParameter</p>
     */
    @AliasFor("parameter") String value() default ExecutorParameter.查询设置;

    /**
     * 参数类型
     * <p>project.extension.mybatis.model.PaginationParameter</p>
     */
    @AliasFor("value") String parameter() default ExecutorParameter.查询设置;
}
