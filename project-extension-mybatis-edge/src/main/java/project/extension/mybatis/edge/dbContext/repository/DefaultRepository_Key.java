package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.core.provider.standard.IBaseDbProvider;
import project.extension.standard.exception.ApplicationException;

/**
 * 默认数据仓储
 *
 * @param <T>    数据类型
 * @param <TKey> 主键类型
 * @author LCTR
 * @date 2022-03-29
 */
public class DefaultRepository_Key<T, TKey>
        extends BaseRepository_Key<T, TKey> {
    public DefaultRepository_Key(Class<T> type,
                                 Class<TKey> keyType,
                                 IBaseDbProvider<T> dbProvider)
            throws
            ApplicationException {
        super(type,
              keyType,
              dbProvider);
    }
}
