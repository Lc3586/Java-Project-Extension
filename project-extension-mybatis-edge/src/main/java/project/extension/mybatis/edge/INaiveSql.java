package project.extension.mybatis.edge;

import org.springframework.stereotype.Repository;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepository_Key;
import project.extension.standard.exception.ModuleException;

/**
 * NaiveSql
 *
 * @author LCTR
 * @date 2022-06-10
 */
@Repository
public interface INaiveSql {
    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @param <T>        数据类型
     * @return 数据仓储
     */
    <T> IBaseRepository<T> getRepository(Class<T> entityType)
            throws
            ModuleException;

    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @param keyType    主键类型
     * @param <T>        数据类型
     * @param <TKey>     主键类型
     * @return 数据仓储
     */
    <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                             Class<TKey> keyType)
            throws
            ModuleException;

    /**
     * 获取数据库访问对象
     */
    INaiveAdo getAdo()
            throws
            ModuleException;

    /**
     * 获取数据库访问对象
     */
    INaiveAop getAop()
            throws
            ModuleException;

    /**
     * 获取DbFirst 开发模式相关功能
     */
    IDbFirst getDbFirst()
            throws
            ModuleException;

    /**
     * 获取DbFirst 开发模式相关功能
     */
    ICodeFirst getCodeFirst()
            throws
            ModuleException;
}
