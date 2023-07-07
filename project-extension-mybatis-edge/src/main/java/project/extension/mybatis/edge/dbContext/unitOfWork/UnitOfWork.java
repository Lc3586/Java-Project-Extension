package project.extension.mybatis.edge.dbContext.unitOfWork;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.Operation;
import project.extension.mybatis.edge.aop.BeforeTraceEventArgs;
import project.extension.mybatis.edge.aop.NaiveAopProvider;
import project.extension.mybatis.edge.dbContext.DbContextScopedNaiveSql;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.standard.exception.ModuleException;

import java.sql.Connection;
import java.sql.Savepoint;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工作单元
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class UnitOfWork
        implements IUnitOfWork {
    public UnitOfWork(INaiveSql orm) {
        this.orm = orm;
        if (orm == null)
            throw new ModuleException(Strings.getInstanceParamsUndefined("project.extension.mybatis.edge.dbContext.unitOfWork.UnitOfWork",


                                                                         "INaiveSql orm"));
        this.aop = (NaiveAopProvider) IOCExtension.applicationContext.getBean(INaiveAop.class);
        this.uowBefore = new BeforeTraceEventArgs("UOW",
                                                  Operation.UnitOfWork,
                                                  null);
        this.aop.beforeTrace(this.uowBefore);
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
    private static final AtomicInteger seed = new AtomicInteger();

    /**
     * 正在使用中的工作单元
     * <p>用于调试</p>
     */
    private static final ConcurrentMap<String, UnitOfWork> debugBeingUsed = new ConcurrentHashMap<>();

    /**
     * 工作单元内的实体变化跟踪
     */
    private final EntityChangeRecord entityChangeRecord;

    /**
     * 用户自定义的状态数据
     */
    private final Map<String, Object> states = new HashMap<>();

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

    /**
     * 事务对象
     */
    protected TransactionStatus transactionStatus;

    /**
     * 执行工作单元前
     */
    protected BeforeTraceEventArgs uowBefore;

    /**
     * 还原点
     */
    private Savepoint savepoint;

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
     * 创建还原点
     */
    private void createSavepoint()
            throws
            Exception {
        //创建新连接并禁用自动提交
        Connection connection = getOrm().getAdo()
                                        .getOrCreateSqlSession(this.isolationLevel)
                                        .getConnection();
        connection.setAutoCommit(false);
        //创建还原点
        this.savepoint = connection.setSavepoint();
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
            throw new ModuleException(Strings.getTransactionHasBeenStarted());

        this.enable = false;
    }

    /**
     * 打开工作单元
     */
    public void open() {
        this.enable = true;
    }

    @Override
    public INaiveSql getOrm() {
        if (this.ormScoped == null)
            this.ormScoped = DbContextScopedNaiveSql.create(this.orm,
                                                            null,
                                                            () -> this);
        return this.ormScoped;
    }

    @Override
    public TransactionIsolationLevel getIsolationLevel() {
        return this.isolationLevel;
    }

    @Override
    public void setIsolationLevel(TransactionIsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    @Override
    public TransactionStatus getOrBeginTransaction() {
        return this.getOrBeginTransaction(true);
    }

    @Override
    public TransactionStatus getOrBeginTransaction(boolean isCreate) {
        if (this.transactionStatus != null)
            return this.transactionStatus;

        if (!isCreate)
            return null;

        if (!this.enable)
            return null;

        try {
            if (getOrm().getAdo()
                        .isTransactionAlreadyExisting())
                throw new ModuleException(Strings.getTransactionAlreadyStarted());

            this.transactionStatus = this.isolationLevel == null
                                     ? getOrm().getAdo()
                                               .beginTransaction()
                                     : getOrm().getAdo()
                                               .beginTransaction(this.isolationLevel);

            this.id = String.format("%s_%s",
                                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                                    seed.incrementAndGet());
            debugBeingUsed.putIfAbsent(this.getId(),
                                       this);

//            createSavepoint();
        } catch (Exception ex) {
            this.returnObject();
            getOrm().getAdo()
                    .triggerAfterTransactionAop(Strings.getTransactionBeginFailed(),
                                                ex);
            throw new ModuleException(Strings.getTransactionBeginFailed(),
                                      ex);
        }

        return this.transactionStatus;
    }

    @Override
    public void commit() {
        try {
            if (this.transactionStatus != null) {
                if (!this.transactionStatus.isCompleted())
                    getOrm().getAdo()
                            .transactionCommit(this.transactionStatus);
                getOrm().getAdo()
                        .triggerAfterTransactionAop(Strings.getTransactionCommit(),
                                                    null);
            }

//            if (this.sqlSession != null) {
//                this.sqlSession.commit();
//                this.sqlSession.close();
//            }
        } catch (Exception ex) {
            getOrm().getAdo()
                    .triggerAfterTransactionAop(Strings.getTransactionCommitFailed(),
                                                ex);
            throw new ModuleException(Strings.getTransactionCommitFailed(),
                                      ex);
        } finally {
            this.returnObject();
        }
    }

    @Override
    public void rollback() {
        try {
            if (this.transactionStatus != null) {
                if (!this.transactionStatus.isCompleted())
                    getOrm().getAdo()
                            .transactionRollback(this.transactionStatus);
                getOrm().getAdo()
                        .triggerAfterTransactionAop(Strings.getTransactionRollback(),
                                                    null);
            }

//            if (this.sqlSession != null) {
//                if (this.savepoint != null) {
//                    this.sqlSession.getConnection()
//                                   .rollback(this.savepoint);
////                    this.sqlSession.rollback();
//                } else
//                    this.sqlSession.rollback();
//                this.sqlSession.close();
//            }
        } catch (Exception ex) {
            getOrm().getAdo()
                    .triggerAfterTransactionAop(Strings.getTransactionRollbackFailed(),
                                                ex);
            throw new ModuleException(Strings.getTransactionRollbackFailed(),
                                      ex);
        } finally {
            this.returnObject();
        }
    }

    @Override
    public EntityChangeRecord getEntityChangeReport() {
        return this.entityChangeRecord;
    }

    @Override
    public Map<String, Object> getStates() {
        return this.states;
    }
}
