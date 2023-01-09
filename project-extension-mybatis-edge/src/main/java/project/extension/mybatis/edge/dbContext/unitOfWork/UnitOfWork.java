package project.extension.mybatis.edge.dbContext.unitOfWork;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.Operation;
import project.extension.mybatis.edge.aop.TraceAfterEventArgs;
import project.extension.mybatis.edge.aop.TraceBeforeEventArgs;
import project.extension.mybatis.edge.aop.NaiveAopProvider;
import project.extension.mybatis.edge.dbContext.DbContextScopedNaiveSql;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.standard.exception.ModuleException;

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
            throw new ModuleException(DbContextStrings.getInstanceParamsUndefined("project.extension.mybatis.edge.dbContext.unitOfWork.UnitOfWork",


                                                                                  "INaiveSql orm"));
        this.aop = (NaiveAopProvider) IOCExtension.applicationContext.getBean(INaiveAop.class);
        this.dataSourceTransactionManager = IOCExtension.applicationContext.getBean(DataSourceTransactionManager.class);
        this.transactionDefinition = IOCExtension.applicationContext.getBean(TransactionDefinition.class);
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
     * 事务管理器
     */
    private final DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 事务定义
     */
    private TransactionDefinition transactionDefinition;

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
            throw new ModuleException(DbContextStrings.getTransactionHasBeenStarted());

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
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition(this.transactionDefinition);
        definition.setIsolationLevel(this.isolationLevel.getLevel());
        this.transactionDefinition = definition;
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

        this.tranBefore = new TraceBeforeEventArgs(Operation.BeginTransaction,
                                                   this.isolationLevel);
        this.aop.traceBefore(this.tranBefore);

        try {
            this.transactionStatus = this.dataSourceTransactionManager.getTransaction(this.transactionDefinition);

            this.id = String.format("%s_%s",
                                    new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                                    seed.incrementAndGet());
            debugBeingUsed.putIfAbsent(this.getId(),
                                       this);
        } catch (Exception ex) {
            this.returnObject();
            this.aop.traceAfter(new TraceAfterEventArgs(this.tranBefore,
                                                        "失败",
                                                        ex));
            throw new ModuleException(DbContextStrings.getTransactionBeginFailed(),
                                      ex);
        }

        return this.transactionStatus;
    }

    @Override
    public void commit() {
        try {
            if (this.transactionStatus != null) {
                if (!this.transactionStatus.isCompleted())
                    this.dataSourceTransactionManager.commit(this.transactionStatus);
                this.aop.traceAfter(new TraceAfterEventArgs(this.tranBefore,
                                                            "提交",
                                                            null));
            }
        } catch (Exception ex) {
            this.aop.traceAfter(new TraceAfterEventArgs(this.tranBefore,
                                                        "提交失败",
                                                        ex));
            throw new ModuleException(DbContextStrings.getTransactionCommitFailed(),
                                      ex);
        } finally {
            this.returnObject();
            this.tranBefore = null;
        }
    }

    @Override
    public void rollback() {
        try {
            if (this.transactionStatus != null) {
                if (!this.transactionStatus.isCompleted())
                    this.dataSourceTransactionManager.rollback(this.transactionStatus);
                this.aop.traceAfter(new TraceAfterEventArgs(this.tranBefore,
                                                            "回滚",
                                                            null));
            }
        } catch (Exception ex) {
            this.aop.traceAfter(new TraceAfterEventArgs(this.tranBefore,
                                                        "回滚失败",
                                                        ex));
            throw new ModuleException(DbContextStrings.getTransactionRollbackFailed(),
                                      ex);
        } finally {
            this.returnObject();
            this.tranBefore = null;
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
