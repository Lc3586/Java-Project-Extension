package project.extension.mybatis;

import project.extension.mybatis.core.provider.standard.ICodeFirst;
import project.extension.mybatis.core.provider.standard.IDbFirst;
import project.extension.mybatis.core.repository.IBaseRepository;
import project.extension.mybatis.core.repository.IBaseRepository_Key;

/**
 * NaiveSql
 *
 * @author LCTR
 * @date 2022-06-10
 */
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
            Exception;

    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @param dataSource 数据源名称
     * @param <T>        数据类型
     * @return 数据仓储
     */
    <T> IBaseRepository<T> getRepository(Class<T> entityType,
                                         String dataSource)
            throws
            Exception;

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
            Exception;

    /**
     * 获取数据仓储
     *
     * @param entityType 实体类型
     * @param keyType    主键类型
     * @param dataSource 数据源名称
     * @param <T>        数据类型
     * @param <TKey>     主键类型
     * @return 数据仓储
     */
    <T, TKey> IBaseRepository_Key<T, TKey> getRepository_Key(Class<T> entityType,
                                                             Class<TKey> keyType,
                                                             String dataSource)
            throws
            Exception;

    /**
     * 获取DbFirst 开发模式相关功能
     */
    IDbFirst getDbFirst()
            throws
            Exception;

    /**
     * 获取DbFirst 开发模式相关功能
     *
     * @param dataSource 数据源名称
     */
    IDbFirst getDbFirst(String dataSource)
            throws
            Exception;

    /**
     * 获取DbFirst 开发模式相关功能
     */
    ICodeFirst getCodeFirst()
            throws
            Exception;

    /**
     * 获取DbFirst 开发模式相关功能
     *
     * @param dataSource 数据源名称
     */
    ICodeFirst getCodeFirst(String dataSource)
            throws
            Exception;
}
