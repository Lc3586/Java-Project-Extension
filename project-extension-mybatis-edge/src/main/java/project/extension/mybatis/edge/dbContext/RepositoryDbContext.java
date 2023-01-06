package project.extension.mybatis.edge.dbContext;

import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.dbContext.repository.IBaseRepositoryBase;

/**
 * 数据源数据库上下文
 *
 * @author LCTR
 * @date 2023-01-06
 */
public class RepositoryDbContext
        extends DbContext {
    public RepositoryDbContext(INaiveSql orm,
                               IBaseRepositoryBase repository) {
        super();
        super.ormScoped = DbContextScopedNaiveSql.create(orm,
                                                         () -> this,
                                                         repository::getUnitOfWork);
        super.isUseUnitOfWork = false;
        super.setUnitOfWork(repository.getUnitOfWork());
        this.repository = repository;
    }

    protected final IBaseRepositoryBase repository;
}
