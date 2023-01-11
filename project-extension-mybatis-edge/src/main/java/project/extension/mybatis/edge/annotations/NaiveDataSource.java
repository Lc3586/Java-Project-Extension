package project.extension.mybatis.edge.annotations;

import org.springframework.core.annotation.AliasFor;
import project.extension.mybatis.edge.core.ado.INaiveDataSourceProvider;

import java.lang.annotation.*;

/**
 * 数据源
 *
 * @author LCTR
 * @date 2023-01-11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,
         ElementType.METHOD})
public @interface NaiveDataSource {
    /**
     * 数据源名称
     */
    @AliasFor("dataSource") String value() default INaiveDataSourceProvider.DEFAULT_DATASOURCE;

    /**
     * 数据源名称
     */
    @AliasFor("value") String dataSource() default INaiveDataSourceProvider.DEFAULT_DATASOURCE;
}