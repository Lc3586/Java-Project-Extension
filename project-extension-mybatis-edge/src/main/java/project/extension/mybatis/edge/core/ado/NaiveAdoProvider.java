package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionHolder;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import project.extension.func.IFunc0;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.core.mapper.MappedStatementHandler;
import project.extension.mybatis.edge.extention.CommonUtils;
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
    public NaiveAdoProvider(DataSource dataSource)
            throws
            ModuleException {
        this.mappedStatementHandler = IOCExtension.applicationContext.getBean(MappedStatementHandler.class);
        this.dataSource = dataSource;
        final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(CommonUtils.getConfig()
                                                                                               .getConfigLocation()));
        try {
            this.sqlSessionFactory = sqlSessionFactory.getObject();
        } catch (Exception ex) {
            throw new ModuleException(DbContextStrings.getSqlSessionFactoryFailed());
        }
    }

    /**
     * MappedStatement处理类
     */
    private final MappedStatementHandler mappedStatementHandler;

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
    private IFunc0<TransactionStatus> resolveTransaction;

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

    /**
     * 获取解析事务的方法
     *
     * @return 解析事务的方法
     */
    protected IFunc0<TransactionStatus> getResolveTransaction() {
        return this.resolveTransaction;
    }

    /**
     * 设置解析事务的方法
     *
     * @param resolveTransaction 解析事务的方法
     */
    protected void setResolveTransaction(IFunc0<TransactionStatus> resolveTransaction) {
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
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public SqlSession getOrCreateSqlSession()
            throws
            ModuleException {
        return getOrCreateSqlSession(null);
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
    public SqlSession getOrCreateSqlSession(TransactionIsolationLevel level)
            throws
            ModuleException {
        Tuple2<Boolean, SqlSession> currentSqlSession = currentSqlSession();
        if (currentSqlSession.a)
            return currentSqlSession.b;

        //开启事务
        if (getResolveTransaction() != null) {
            try {
                getResolveTransaction().invoke();
            } catch (Exception ex) {
                throw new ModuleException(DbContextStrings.getResolveTransactionFailed(),
                                          ex);
            }
        }

        //创建新的Sql会话
        ExecutorType executorType = getExecutorType();
        SqlSession session = level == null
                             ? sqlSessionFactory.openSession(executorType)
                             : sqlSessionFactory.openSession(executorType,
                                                             level);

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
