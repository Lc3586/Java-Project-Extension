package project.extension.mybatis.edge.dbContext;

import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.mybatis.edge.dbContext.unitOfWork.UnitOfWork;

/**
 * 数据库上下文
 *
 * @author LCTR
 * @date 2023-01-05
 */
public class DbContext {
    protected DbContext() {
        this(null);
    }

    protected DbContext(INaiveSql orm) {
        this.originalOrm = orm;
        this.ormScoped = DbContextScopedNaiveSql.create(orm,
                                                        () -> this,
                                                        this::getUnitOfWork);
        if (this.ormScoped == null) {

        }
    }

    protected DbContextScopedNaiveSql ormScoped;

    private final INaiveSql originalOrm;

    /**
     * 是否创建工作单元
     */
    protected boolean isUseUnitOfWork = true;

    private IUnitOfWork uow;

    /**
     * 原始的Orm对象
     */
    protected INaiveSql getOriginalOrm() {
        return this.originalOrm;
    }

    public void setUnitOfWork(IUnitOfWork unitOfWork) {
        this.uow = unitOfWork;
    }

    public IUnitOfWork getUnitOfWork() {
        if (this.uow != null)
            return this.uow;
        if (!this.isUseUnitOfWork)
            return null;
        setUnitOfWork(new UnitOfWork(this.originalOrm));
        return getUnitOfWork();
    }

    public void flushCommand() {

    }
}
