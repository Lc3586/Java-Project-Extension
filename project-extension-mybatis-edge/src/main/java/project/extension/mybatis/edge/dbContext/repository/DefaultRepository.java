package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.core.provider.standard.INaiveSql;

/**
 * 默认数据仓储
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-29
 */
public class DefaultRepository<T>
        extends BaseRepository<T> {
    public DefaultRepository(INaiveSql orm,
                             Class<T> entityType) {
        super(orm,
              entityType);
    }

}
