package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.INaiveSql;
import project.extension.standard.exception.ModuleException;

/**
 * 默认数据仓储
 *
 * @param <TEntity> 实体类型
 * @param <TKey>    主键类型
 * @author LCTR
 * @date 2022-03-29
 */
public class DefaultRepository_Key<TEntity, TKey>
        extends BaseRepository_Key<TEntity, TKey> {
    public DefaultRepository_Key(INaiveSql orm,
                                 Class<TEntity> entityType,
                                 Class<TKey> keyType)
            throws
            ModuleException {
        super(orm,
              entityType,
              keyType);
    }
}
