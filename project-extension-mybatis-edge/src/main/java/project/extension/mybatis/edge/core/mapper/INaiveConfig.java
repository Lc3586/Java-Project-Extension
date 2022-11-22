package project.extension.mybatis.edge.core.mapper;

import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.Select;
import project.extension.mybatis.edge.core.driver.NaiveMixedLanguageDriver;
import project.extension.mybatis.edge.model.DynamicMethod;

/**
 * 配置程序
 *
 * @author LCTR
 * @date 2022-04-06
 * @deprecated 请使用RepositoryProvider来操作数据库
 */
@Deprecated
public interface INaiveConfig {
    @Select(DynamicMethod.Configuration)
    @Lang(NaiveMixedLanguageDriver.class)
    void configuration();
}
