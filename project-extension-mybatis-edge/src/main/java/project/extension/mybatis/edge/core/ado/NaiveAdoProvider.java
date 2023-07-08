package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionHolder;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import project.extension.func.IFunc0;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.aop.*;
import project.extension.mybatis.edge.core.mapper.MappedStatementHandler;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据库访问对象构造器
 *
 * @author LCTR
 * @date 2022-12-22
 */
public class NaiveAdoProvider
        implements INaiveAdo {
    /**
     * @param dataSource 数据源
     */
    public NaiveAdoProvider(String dataSource)
            throws
            ModuleException {
        this.dataSourceName = dataSource;
        this.aop = (NaiveAopProvider) IOCExtension.applicationContext.getBean(INaiveAop.class);
        this.mappedStatementHandler = IOCExtension.applicationContext.getBean(MappedStatementHandler.class);
        final INaiveDataSourceProvider naiveDataSourceProvider = IOCExtension.applicationContext.getBean(INaiveDataSourceProvider.class);
        this.dataSource = naiveDataSourceProvider.getDataSources(dataSource);
        this.sqlSessionFactory = naiveDataSourceProvider.getSqlSessionFactory(dataSource);
        this.dataSourceTransactionManager = naiveDataSourceProvider.getTransactionManager(dataSource);
        this.transactionDefinition = IOCExtension.tryGetBean(TransactionDefinition.class);
    }

    /**
     * AOP编程对象
     */
    protected final NaiveAopProvider aop;

    private static final ThreadLocal<Tuple2<BeforeTraceEventArgs, TransactionStatus>> transactionalResources =
            new NamedThreadLocal<>("AOP Transactional Before resources");

    /**
     * MappedStatement处理类
     */
    private final MappedStatementHandler mappedStatementHandler;

    /**
     * 数据源名称
     */
    private final String dataSourceName;

    /**
     * 数据源
     */
    private final DataSource dataSource;

    /**
     * 事务管理器
     */
    private final DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * Sql会话工厂
     */
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 解析事务的方法
     */
    private IFunc0<TransactionStatus> resolveTransaction;

    /**
     * 事务定义
     */
    private final TransactionDefinition transactionDefinition;

    /**
     * 结束Sql查询后执行
     */
    private void afterExecute(SqlSession sqlSession,
                              boolean success,
                              String msId) {
        mappedStatementHandler.returnId(msId,
                                        sqlSession.getConfiguration());

        if (isTransactionAlreadyExisting())
            return;

        try {
            if (sqlSession.getConnection()
                          .getAutoCommit()) {
                if (success)
                    sqlSession.commit();
                else
                    sqlSession.rollback();
                sqlSession.close();
            }
        } catch (Exception ex) {
            throw new ModuleException(Strings.getSqlSessionCheckClosedFailed());
        }
    }

    /**
     * 开启事务
     *
     * @param transactionDefinition 事务定义
     * @return 事务
     */
    private TransactionStatus beginTransactionPrivate(TransactionDefinition transactionDefinition) {
        if (isTransactionAlreadyExisting())
            throw new ModuleException(Strings.getTransactionAlreadyStarted());

        BeforeTraceEventArgs tranBefore = new BeforeTraceEventArgs(transactionDefinition == null
                                                                   ? ""
                                                                   : transactionDefinition.getName(),
                                                                   Operation.BeginTransaction,
                                                                   transactionDefinition == null
                                                                   ? null
                                                                   : transactionDefinition.getIsolationLevel());
        this.aop.beforeTrace(tranBefore);

        TransactionStatus transactionStatus = this.dataSourceTransactionManager.getTransaction(transactionDefinition);

        transactionalResources.set(new Tuple2<>(tranBefore,
                                                transactionStatus));

        return transactionStatus;
    }

    /**
     * 获取执行器类型
     *
     * @return 执行器类型
     */
    private ExecutorType getExecutorType() {
        return this.sqlSessionFactory.getConfiguration()
                                     .getDefaultExecutorType();
    }

    /**
     * 获取当前的Sql会话
     *
     * @param executorType 执行类型
     * @param holder       Sql会话持有者
     * @return Sql会话
     */
    private static SqlSession sessionHolder(ExecutorType executorType,
                                            SqlSessionHolder holder) {
        SqlSession session = null;
        if (holder != null && holder.isSynchronizedWithTransaction()) {
            if (holder.getExecutorType() != executorType) {
                throw new TransientDataAccessResourceException(
                        "Cannot change the ExecutorType when there is an existing transaction");
            }

            holder.requested();

            //获取当前事务下的Sql会话
            session = holder.getSqlSession();
        }
        return session;
    }

    /**
     * 注册Sql会话
     *
     * @param sessionFactory 会话工厂
     * @param executorType   执行器类型
     * @param session        Sql会话
     */
    private static void registerSessionHolder(SqlSessionFactory sessionFactory,
                                              ExecutorType executorType,
                                              SqlSession session)
            throws
            ModuleException {
        SqlSessionHolder holder;
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            Environment environment = sessionFactory.getConfiguration()
                                                    .getEnvironment();

            if (environment.getTransactionFactory() instanceof SpringManagedTransactionFactory) {
                //Sql会话同步事务
                holder = new SqlSessionHolder(session,
                                              executorType,
                                              null);
                TransactionSynchronizationManager.bindResource(sessionFactory,
                                                               holder);
                TransactionSynchronizationManager
                        .registerSynchronization(new SqlSessionSynchronization(holder,
                                                                               sessionFactory));
                holder.setSynchronizedWithTransaction(true);
                holder.requested();
            } else {
                if (TransactionSynchronizationManager.getResource(environment.getDataSource()) != null)
                    throw new ModuleException(Strings.getNoneSpringManagedTransactionFactory());

                //Sql会话未同步事务
            }
        }
        //else 未启用Spring事务管理器
    }

    @Override
    public IFunc0<TransactionStatus> getResolveTransaction() {
        return this.resolveTransaction;
    }

    @Override
    public void setResolveTransaction(IFunc0<TransactionStatus> resolveTransaction) {
        this.resolveTransaction = resolveTransaction;
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }

    @Override
    public Configuration getConfiguration() {
        return this.sqlSessionFactory.getConfiguration();
    }

    @Override
    public String getDataSourceName() {
        return this.dataSourceName;
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public boolean isTransactionAlreadyExisting()
            throws
            ModuleException {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

    @Override
    public TransactionStatus currentTransaction()
            throws
            ModuleException {
        if (!isTransactionAlreadyExisting())
            return null;
        Tuple2<BeforeTraceEventArgs, TransactionStatus> transactionalResource = transactionalResources.get();
        return transactionalResource == null
               ? null
               : transactionalResource.b;
    }

    @Override
    public TransactionStatus beginTransaction()
            throws
            ModuleException {
        return beginTransactionPrivate(null);
    }

    @Override
    public TransactionStatus beginTransaction(TransactionIsolationLevel isolationLevel)
            throws
            ModuleException {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(this.transactionDefinition);
        transactionDefinition.setIsolationLevel(isolationLevel.getLevel());
        return beginTransactionPrivate(transactionDefinition);
    }

    @Override
    public TransactionStatus beginTransaction(TransactionDefinition transactionDefinition)
            throws
            ModuleException {
        return beginTransactionPrivate(transactionDefinition);
    }

    @Override
    public void transactionCommit()
            throws
            ModuleException {
        if (!isTransactionAlreadyExisting())
            return;
        transactionCommit(currentTransaction());
    }

    @Override
    public void transactionCommit(TransactionStatus transactionStatus)
            throws
            ModuleException {
        this.dataSourceTransactionManager.commit(transactionStatus);
        triggerAfterTransactionAop(Strings.getTransactionCommit(),
                                   null);
        transactionalResources.remove();
    }

    @Override
    public void transactionRollback()
            throws
            ModuleException {
        if (!isTransactionAlreadyExisting())
            return;
        transactionRollback(currentTransaction());
    }

    @Override
    public void transactionRollback(TransactionStatus transactionStatus)
            throws
            ModuleException {
        this.dataSourceTransactionManager.rollback(transactionStatus);
        triggerAfterTransactionAop(Strings.getTransactionRollback(),
                                   null);
        transactionalResources.remove();
    }

    @Override
    public void triggerAfterTransactionAop(String remark,
                                           Exception ex)
            throws
            ModuleException {
        Tuple2<BeforeTraceEventArgs, TransactionStatus> tranResource = transactionalResources.get();
        if (tranResource == null)
            return;

        this.aop.afterTrace(new AfterTraceEventArgs(tranResource.a,
                                                    remark,
                                                    ex));
    }

    @Override
    public SqlSession getOrCreateSqlSession() {
        return getOrCreateSqlSession(null);
    }

    @Override
    public SqlSession getOrCreateSqlSession(TransactionIsolationLevel isolationLevel)
            throws
            ModuleException {
        Tuple2<Boolean, SqlSession> currentSqlSession1 = currentSqlSession();
        if (currentSqlSession1.a)
            return currentSqlSession1.b;

        //事务
        boolean isActualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        TransactionStatus transaction = null;
        if (getResolveTransaction() != null) {
            try {
                transaction = getResolveTransaction().invoke();
            } catch (Exception ex) {
                throw new ModuleException(Strings.getResolveTransactionFailed(),
                                          ex);
            }
        }

        Tuple2<Boolean, SqlSession> currentSqlSession2 = currentSqlSession();
        if (currentSqlSession2.a)
            return currentSqlSession2.b;

        //创建新的Sql会话
        ExecutorType executorType = getExecutorType();
        SqlSession session = transaction == null
                             ? sqlSessionFactory.openSession(executorType,
                                                             !isActualTransactionActive)
                             : sqlSessionFactory.openSession(executorType,
                                                             isolationLevel);

        registerSessionHolder(this.sqlSessionFactory,
                              executorType,
                              session);
        return session;
    }

    @Override
    public Tuple2<Boolean, SqlSession> currentSqlSession() {
        ExecutorType executorType = getExecutorType();
        SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(this.sqlSessionFactory);

        SqlSession session = sessionHolder(executorType,
                                           holder);
        return new Tuple2<>(session != null,
                            session);
    }

    @Override
    public void commit()
            throws
            ModuleException {
        Tuple2<Boolean, SqlSession> currentSqlSession = currentSqlSession();
        if (!currentSqlSession.a)
            return;
        commit(currentSqlSession.b);
    }

    @Override
    public void commit(SqlSession sqlSession)
            throws
            ModuleException {
        sqlSession.commit();
    }

    @Override
    public void rollback()
            throws
            ModuleException {
        Tuple2<Boolean, SqlSession> currentSqlSession = currentSqlSession();
        if (!currentSqlSession.a)
            return;
        rollback(currentSqlSession.b);
    }

    @Override
    public void rollback(SqlSession sqlSession)
            throws
            ModuleException {
        sqlSession.rollback();
    }

    @Override
    public void close()
            throws
            ModuleException {
        Tuple2<Boolean, SqlSession> currentSqlSession = currentSqlSession();
        if (!currentSqlSession.a)
            return;
        close(currentSqlSession.b);
    }

    @Override
    public void close(SqlSession sqlSession)
            throws
            ModuleException {
        sqlSession.close();
    }

    @Override
    public String getMSId() {
        return mappedStatementHandler.applyId();
    }

    @Override
    public <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                   String msId,
                                                   String script,
                                                   Class<TParameter> parameterType,
                                                   Map<String, Object> parameterHashMap,
                                                   Class<TResult> resultType,
                                                   Integer resultMainTagLevel,
                                                   Collection<String> resultCustomTags,
                                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterHashMap,
                                          null,
                                          null,
                                          resultType,
                                          resultMainTagLevel,
                                          resultCustomTags,
                                          nameConvertType);

            TResult result = sqlSession.selectOne(msId,
                                                  parameterHashMap);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                   String msId,
                                                   String script,
                                                   TParameter parameter,
                                                   Class<TParameter> parameterType,
                                                   Integer parameterMainTagLevel,
                                                   Collection<String> parameterCustomTags,
                                                   Class<TResult> resultType,
                                                   Integer resultMainTagLevel,
                                                   Collection<String> resultCustomTags,
                                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterMainTagLevel,
                                          parameterCustomTags,
                                          resultType,
                                          resultMainTagLevel,
                                          resultCustomTags,
                                          nameConvertType);

            TResult result = sqlSession.selectOne(msId,
                                                  parameter);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                          String msId,
                                                          String script,
                                                          Class<TParameter> parameterType,
                                                          Map<String, Object> parameterHashMap,
                                                          Class<TResult> resultType,
                                                          Integer resultMainTagLevel,
                                                          Collection<String> resultCustomTags,
                                                          NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterHashMap,
                                          null,
                                          null,
                                          resultType,
                                          resultMainTagLevel,
                                          resultCustomTags,
                                          nameConvertType);

            List<TResult> result = sqlSession.selectList(msId,
                                                         parameterHashMap);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                          String msId,
                                                          String script,
                                                          TParameter parameter,
                                                          Class<TParameter> parameterType,
                                                          Integer parameterMainTagLevel,
                                                          Collection<String> parameterCustomTags,
                                                          Class<TResult> resultType,
                                                          Integer resultMainTagLevel,
                                                          Collection<String> resultCustomTags,
                                                          NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterMainTagLevel,
                                          parameterCustomTags,
                                          resultType,
                                          resultMainTagLevel,
                                          resultCustomTags,
                                          nameConvertType);

            List<TResult> result = sqlSession.selectList(msId,
                                                         parameter);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter, TResult> Map<String, Object> selectMap(SqlSession sqlSession,
                                                               String msId,
                                                               String script,
                                                               Class<TParameter> parameterType,
                                                               Map<String, Object> parameterHashMap,
                                                               Class<TResult> resultType,
                                                               Collection<String> resultFields,
                                                               NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          parameterType,
                                          parameterHashMap,
                                          null,
                                          null,
                                          resultType,
                                          resultFields,
                                          nameConvertType);

            Map<String, Object> result = sqlSession.selectOne(msId,
                                                              parameterHashMap);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter, TResult> Map<String, Object> selectMap(SqlSession sqlSession,
                                                               String msId,
                                                               String script,
                                                               TParameter parameter,
                                                               Class<TParameter> parameterType,
                                                               Integer parameterMainTagLevel,
                                                               Collection<String> parameterCustomTags,
                                                               Class<TResult> resultType,
                                                               Collection<String> resultFields,
                                                               NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          parameterType,
                                          parameterMainTagLevel,
                                          parameterCustomTags,
                                          resultType,
                                          resultFields,
                                          nameConvertType);

            Map<String, Object> result = sqlSession.selectOne(msId,
                                                              parameter);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter, TResult> List<Map<String, Object>> selectMapList(SqlSession sqlSession,
                                                                         String msId,
                                                                         String script,
                                                                         Class<TParameter> parameterType,
                                                                         Map<String, Object> parameterHashMap,
                                                                         Class<TResult> resultType,
                                                                         Collection<String> resultFields,
                                                                         NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          parameterType,
                                          parameterHashMap,
                                          null,
                                          null,
                                          resultType,
                                          resultFields,
                                          nameConvertType);

            List<Map<String, Object>> result = sqlSession.selectList(msId,
                                                                     parameterHashMap);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter, TResult> List<Map<String, Object>> selectMapList(SqlSession sqlSession,
                                                                         String msId,
                                                                         String script,
                                                                         TParameter parameter,
                                                                         Class<TParameter> parameterType,
                                                                         Integer parameterMainTagLevel,
                                                                         Collection<String> parameterCustomTags,
                                                                         Class<TResult> resultType,
                                                                         Collection<String> resultFields,
                                                                         NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.SELECT,
                                          parameterType,
                                          parameterMainTagLevel,
                                          parameterCustomTags,
                                          resultType,
                                          resultFields,
                                          nameConvertType);

            List<Map<String, Object>> result = sqlSession.selectList(msId,
                                                                     parameter);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter> int insert(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   @Nullable
                                           boolean useGeneratedKeys,
                                   @Nullable
                                           String keyProperty,
                                   @Nullable
                                           String keyColumn,
                                   @Nullable
                                           boolean useSelectKey,
                                   @Nullable
                                           String selectKeyScript,
                                   @Nullable
                                           Class<?> selectKeyType,
                                   Class<TParameter> parameterType,
                                   Map<String, Object> parameterHashMap,
                                   Map<String, Field> outParameterHashMap,
                                   Map<String, Field> inOutParameterHashMap,
                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.INSERT,
                                          useGeneratedKeys,
                                          keyProperty,
                                          keyColumn,
                                          useSelectKey,
                                          selectKeyScript,
                                          selectKeyType,
                                          parameterType,
                                          parameterHashMap,
                                          outParameterHashMap,
                                          inOutParameterHashMap,
                                          Integer.class,
                                          null,
                                          null,
                                          nameConvertType);

            int result = sqlSession.insert(msId,
                                           parameterHashMap);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter> int insert(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   @Nullable
                                           boolean useGeneratedKeys,
                                   @Nullable
                                           String keyProperty,
                                   @Nullable
                                           String keyColumn,
                                   @Nullable
                                           boolean useSelectKey,
                                   @Nullable
                                           String selectKeyScript,
                                   @Nullable
                                           Class<?> selectKeyType,
                                   TParameter parameter,
                                   Class<TParameter> parameterType,
                                   Integer parameterMainTagLevel,
                                   Collection<String> parameterCustomTags,
                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.INSERT,
                                          useGeneratedKeys,
                                          keyProperty,
                                          keyColumn,
                                          useSelectKey,
                                          selectKeyScript,
                                          selectKeyType,
                                          parameterType,
                                          parameterMainTagLevel,
                                          parameterCustomTags,
                                          Integer.class,
                                          null,
                                          null,
                                          nameConvertType);

            int result = sqlSession.insert(msId,
                                           parameter);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter> int update(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   Class<TParameter> parameterType,
                                   Map<String, Object> parameterHashMap,
                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.UPDATE,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterHashMap,
                                          null,
                                          null,
                                          Integer.class,
                                          null,
                                          null,
                                          nameConvertType);

            int result = sqlSession.update(msId,
                                           parameterHashMap);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter> int update(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   TParameter parameter,
                                   Class<TParameter> parameterType,
                                   Integer parameterMainTagLevel,
                                   Collection<String> parameterCustomTags,
                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.UPDATE,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterMainTagLevel,
                                          parameterCustomTags,
                                          Integer.class,
                                          null,
                                          null,
                                          nameConvertType);

            int result = sqlSession.update(msId,
                                           parameter);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter> int delete(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   Class<TParameter> parameterType,
                                   Map<String, Object> parameterHashMap,
                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.DELETE,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterHashMap,
                                          null,
                                          null,
                                          Integer.class,
                                          null,
                                          null,
                                          nameConvertType);

            int result = sqlSession.delete(msId,
                                           parameterHashMap);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TParameter> int delete(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   TParameter parameter,
                                   Class<TParameter> parameterType,
                                   Integer parameterMainTagLevel,
                                   Collection<String> parameterCustomTags,
                                   NameConvertType nameConvertType) {
        boolean success = false;

        try {
            mappedStatementHandler.create(getConfiguration(),
                                          msId,
                                          script,
                                          SqlCommandType.DELETE,
                                          false,
                                          null,
                                          null,
                                          false,
                                          null,
                                          null,
                                          parameterType,
                                          parameterMainTagLevel,
                                          parameterCustomTags,
                                          Integer.class,
                                          null,
                                          null,
                                          nameConvertType);

            int result = sqlSession.delete(msId,
                                           parameter);
            success = true;
            return result;
        } finally {
            afterExecute(sqlSession,
                         success,
                         msId);
        }
    }

    @Override
    public <TMapper> TMapper getMapper(Class<TMapper> mapperType) {
        return getOrCreateSqlSession().getMapper(mapperType);
    }
}
