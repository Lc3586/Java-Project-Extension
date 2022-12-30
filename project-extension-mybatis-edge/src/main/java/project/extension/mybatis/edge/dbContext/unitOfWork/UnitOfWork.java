package project.extension.mybatis.edge.dbContext.unitOfWork;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.Operation;
import project.extension.mybatis.edge.aop.TraceBeforeEventArgs;
import project.extension.mybatis.edge.core.provider.normal.NaiveAopProvider;
import project.extension.mybatis.edge.dbContext.DbContextScopedNaiveSql;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.standard.exception.ApplicationException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 工作单元
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class UnitOfWork
        implements IUnitOfWork {
    public UnitOfWork(INaiveSql orm,
                      INaiveAop aop) {
        this.orm = orm;
        if (orm == null)
            throw new ApplicationException(DbContextStrings.getInstanceParamsUndefined("project.extension.mybatis.edge.dbContext.unitOfWork.UnitOfWork",


                                                                                       "INaiveSql orm"));
        this.aop = (NaiveAopProvider) aop;
        this.uowBefore = new TraceBeforeEventArgs(Operation.UnitOfWork,
                                                  null);
        this.aop.traceBefore(this.uowBefore);
        this.entityChangeRecord = new EntityChangeRecord();
        this.enable = true;
    }

    /**
     * 唯一标识
     * <p>开启事务后有值，是 UnitOfWork 的唯一标识</p>
     * <p>格式：yyyyMMdd_HHmmss_种子id</p>
     * <p>例如：20191121_214504_1</p>
     */
    private String id;

    /**
     * 种子
     */
    private static int seed;

    /**
     * 正在使用中的工作单元
     * <p>用于调试</p>
     */
    private static final ConcurrentMap<String, UnitOfWork> debugBeingUsed = new ConcurrentHashMap<>();

    /**
     * 实体变更记录
     */
    private final EntityChangeRecord entityChangeRecord;

    /**
     * 启用状态
     */
    private boolean enable;

    /**
     * 原始的Orm对象
     */
    protected INaiveSql orm;

    /**
     * 有生命周期的Orm对象
     */
    private DbContextScopedNaiveSql ormScoped;

    /**
     * 事务隔离等级
     */
    private TransactionIsolationLevel isolationLevel;

    /**
     * AOP编程对象
     */
    protected final NaiveAopProvider aop;

    protected final DataSourceTransactionManager dataSourceTransactionManager;

    protected final TransactionDefinition transactionDefinition;

    /**
     * Sql会话
     */
    protected SqlSession sqlSession;

    /**
     * 事务对象
     */
    protected TransactionStatus transactionStatus;

    /**
     * 执行事务前
     */
    protected TraceBeforeEventArgs tranBefore;

    /**
     * 执行工作单元前
     */
    protected TraceBeforeEventArgs uowBefore;

    /**
     * 使用完毕后归还对象
     */
    private void returnObject() {
        if (StringUtils.hasText(this.id) && debugBeingUsed.remove(this.id) != null)
            this.id = null;

        this.transactionStatus = null;
        this.entityChangeRecord.getRecordList()
                               .clear();
    }

    /**
     * 获取唯一标识
     * <p>开启事务后有值，是 UnitOfWork 的唯一标识</p>
     * <p>格式：yyyyMMdd_HHmmss_种子id</p>
     * <p>例如：20191121_214504_1</p>
     *
     * @return 唯一标识
     */
    public String getId() {
        return this.id;
    }

    /**
     * 实体变更记录
     *
     * @return 实体变更记录
     */
    public EntityChangeRecord getEntityChangeRecord() {
        return this.entityChangeRecord;
    }

    /**
     * 获取正在使用中的工作单元
     * <p>用于调试</p>
     *
     * @return 正在使用中的工作单元
     */
    public static ConcurrentMap<String, UnitOfWork> getDebugBeingUsed() {
        return debugBeingUsed;
    }

    /**
     * 启用状态
     *
     * @return 启用状态
     */
    public boolean getEnable() {
        return this.enable;
    }

    /**
     * 关闭工作单元
     */
    public void close() {
        if (this.transactionStatus != null)
            throw new ApplicationException(DbContextStrings.transactionHasBeenStarted());

        this.enable = false;
    }

    /**
     * 打开工作单元
     */
    public void open() {
        this.enable = true;
    }

    /**
     * 获取Orm对象
     *
     * @return Orm对象
     */
    @Override
    public INaiveSql getOrm() {
        if (this.ormScoped == null)
            this.ormScoped = DbContextScopedNaiveSql.create(this.orm,
                                                            null,
                                                            () -> this);
        return this.ormScoped;
    }

    /**
     * 获取事务隔离等级
     *
     * @return 事务隔离等级
     */
    @Override
    public TransactionIsolationLevel getIsolationLevel() {
        return this.isolationLevel;
    }

    /**
     * 设置事务隔离等级
     *
     * @param isolationLevel 事务隔离等级
     */
    @Override
    public void setIsolationLevel(TransactionIsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    /**
     * 提交事务
     */
    @Override
    public void commit() {

    }

    /**
     * 回滚事务
     */
    @Override
    public void rollback() {

    }

    /**
     * 获取工作单元内的实体变化跟踪
     *
     * @return 工作单元内的实体变化跟踪
     */
    @Override
    public EntityChangeRecord getEntityChangeReport() {
        return null;
    }

    /**
     * 获取用户自定义的状态数据，便于扩展
     *
     * @return 用户自定义的状态数据
     */
    @Override
    public Map<String, Object> getStates() {
        return null;
    }

    @Override
    public SqlSession getOrBeginTransaction() {
        return this.getOrBeginTransaction(true);
    }

    @Override
    public SqlSession getOrBeginTransaction(boolean isCreate) {
        if (this.sqlSession != null)
            return sqlSession;

        return this.sqlSession;
    }
}
