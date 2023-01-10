package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionHolder;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import project.extension.func.IFunc0;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.core.mapper.MappedStatementHandler;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import javax.sql.DataSource;
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
        this.mappedStatementHandler = IOCExtension.applicationContext.getBean(MappedStatementHandler.class);
        final INaiveDataSourceProvider naiveDataSourceProvider = IOCExtension.applicationContext.getBean(INaiveDataSourceProvider.class);
        this.dataSource = naiveDataSourceProvider.getDataSources(dataSource);
        this.sqlSessionFactory = naiveDataSourceProvider.getSqlSessionFactory(dataSource);
    }

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
     * Sql会话工厂
     */
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 解析事务的方法
     */
    private IFunc0<Tuple2<TransactionStatus, TransactionIsolationLevel>> resolveTransaction;

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
                    throw new ModuleException(DbContextStrings.getNoneSpringManagedTransactionFactory());

                //Sql会话未同步事务
            }
        }
        //else 未启用Spring事务管理器
    }

    @Override
    public IFunc0<Tuple2<TransactionStatus, TransactionIsolationLevel>> getResolveTransaction() {
        return this.resolveTransaction;
    }

    @Override
    public void setResolveTransaction(IFunc0<Tuple2<TransactionStatus, TransactionIsolationLevel>> resolveTransaction) {
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
    public SqlSession getOrCreateSqlSession()
            throws
            ModuleException {
        Tuple2<Boolean, SqlSession> currentSqlSession1 = currentSqlSession();
        if (currentSqlSession1.a)
            return currentSqlSession1.b;

        //开启事务
        Tuple2<TransactionStatus, TransactionIsolationLevel> transaction = null;
        if (getResolveTransaction() != null) {
            try {
                transaction = getResolveTransaction().invoke();
            } catch (Exception ex) {
                throw new ModuleException(DbContextStrings.getResolveTransactionFailed(),
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
                                                             true)
                             : sqlSessionFactory.openSession(executorType,
                                                             transaction.b);

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
    public <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                   String msId,
                                                   String script,
                                                   Class<TParameter> parameterType,
                                                   Map<String, Object> parameterHashMap,
                                                   Class<TResult> resultType,
                                                   Integer resultMainTagLevel,
                                                   Collection<String> resultCustomTags,
                                                   NameConvertType nameConvertType) {
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterHashMap,
                                           resultType,
                                           resultMainTagLevel,
                                           resultCustomTags,
                                           nameConvertType);

        return sqlSession.selectOne(msId,
                                    parameterHashMap);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterMainTagLevel,
                                           parameterCustomTags,
                                           resultType,
                                           resultMainTagLevel,
                                           resultCustomTags,
                                           nameConvertType);

        return sqlSession.selectOne(msId,
                                    parameter);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterHashMap,
                                           resultType,
                                           resultMainTagLevel,
                                           resultCustomTags,
                                           nameConvertType);

        return sqlSession.selectList(msId,
                                     parameterHashMap);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterMainTagLevel,
                                           parameterCustomTags,
                                           resultType,
                                           resultMainTagLevel,
                                           resultCustomTags,
                                           nameConvertType);

        return sqlSession.selectList(msId,
                                     parameter);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterHashMap,
                                           resultType,
                                           resultFields,
                                           nameConvertType);

        return sqlSession.selectOne(msId,
                                    parameterHashMap);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterMainTagLevel,
                                           parameterCustomTags,
                                           resultType,
                                           resultFields,
                                           nameConvertType);

        return sqlSession.selectOne(msId,
                                    parameter);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterHashMap,
                                           resultType,
                                           resultFields,
                                           nameConvertType);

        return sqlSession.selectList(msId,
                                     parameterHashMap);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.SELECT,
                                           parameterType,
                                           parameterMainTagLevel,
                                           parameterCustomTags,
                                           resultType,
                                           resultFields,
                                           nameConvertType);

        return sqlSession.selectList(msId,
                                     parameter);
    }

    @Override
    public <TParameter> int insert(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   Class<TParameter> parameterType,
                                   Map<String, Object> parameterHashMap,
                                   NameConvertType nameConvertType) {
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.INSERT,
                                           parameterType,
                                           parameterHashMap,
                                           Integer.class,
                                           null,
                                           null,
                                           nameConvertType);

        return sqlSession.insert(msId,
                                 parameterHashMap);
    }

    @Override
    public <TParameter> int insert(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   TParameter parameter,
                                   Class<TParameter> parameterType,
                                   Integer parameterMainTagLevel,
                                   Collection<String> parameterCustomTags,
                                   NameConvertType nameConvertType) {
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.INSERT,
                                           parameterType,
                                           parameterMainTagLevel,
                                           parameterCustomTags,
                                           Integer.class,
                                           null,
                                           null,
                                           nameConvertType);

        return sqlSession.insert(msId,
                                 parameter);
    }

    @Override
    public <TParameter> int update(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   Class<TParameter> parameterType,
                                   Map<String, Object> parameterHashMap,
                                   NameConvertType nameConvertType) {
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.UPDATE,
                                           parameterType,
                                           parameterHashMap,
                                           Integer.class,
                                           null,
                                           null,
                                           nameConvertType);

        return sqlSession.update(msId,
                                 parameterHashMap);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.UPDATE,
                                           parameterType,
                                           parameterMainTagLevel,
                                           parameterCustomTags,
                                           Integer.class,
                                           null,
                                           null,
                                           nameConvertType);

        return sqlSession.update(msId,
                                 parameter);
    }

    @Override
    public <TParameter> int delete(SqlSession sqlSession,
                                   String msId,
                                   String script,
                                   Class<TParameter> parameterType,
                                   Map<String, Object> parameterHashMap,
                                   NameConvertType nameConvertType) {
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.DELETE,
                                           parameterType,
                                           parameterHashMap,
                                           Integer.class,
                                           null,
                                           null,
                                           nameConvertType);

        return sqlSession.delete(msId,
                                 parameterHashMap);
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
        mappedStatementHandler.getOrCreate(getConfiguration(),
                                           msId,
                                           script,
                                           SqlCommandType.DELETE,
                                           parameterType,
                                           parameterMainTagLevel,
                                           parameterCustomTags,
                                           Integer.class,
                                           null,
                                           null,
                                           nameConvertType);

        return sqlSession.delete(msId,
                                 parameter);
    }
}
