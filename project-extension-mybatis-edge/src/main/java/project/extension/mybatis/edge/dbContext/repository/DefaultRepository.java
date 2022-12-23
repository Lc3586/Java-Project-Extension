package project.extension.mybatis.edge.dbContext.repository;

import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.provider.standard.IBaseDbProvider;

/**
 * 默认数据仓储
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-29
 */
public class DefaultRepository<T>
        extends BaseRepository<T> {
    public DefaultRepository(BaseConfig config,
                             Class<T> entityType,
                             IBaseDbProvider<T> dbProvider) {
        super(config,
              entityType,
              dbProvider);
    }

}
