package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionHolder;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import project.extension.func.IFunc2;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.standard.exception.ApplicationException;
import project.extension.tuple.Tuple2;

import javax.sql.DataSource;

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
            ApplicationException {
        this.dataSource = dataSource;
        final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(CommonUtils.getConfig()
                                                                                               .getConfigLocation()));
        try {
            this.sqlSessionFactory = sqlSessionFactory.getObject();
        } catch (Exception ex) {
            throw new ApplicationException(DbContextStrings.getSqlSessionFactoryFailed());
        }
    }

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
    private IFunc2<SqlSessionFactory, ExecutorType, TransactionStatus> resolveTransaction;

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
            ApplicationException {
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
                    throw new ApplicationException(DbContextStrings.getNoneSpringManagedTransactionFactory());

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
    protected IFunc2<SqlSessionFactory, ExecutorType, TransactionStatus> getResolveTransaction() {
        return this.resolveTransaction;
    }

    /**
     * 设置解析事务的方法
     *
     * @param resolveTransaction 解析事务的方法
     */
    protected void setResolveTransaction(IFunc2<SqlSessionFactory, ExecutorType, TransactionStatus> resolveTransaction) {
        this.resolveTransaction = resolveTransaction;
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public SqlSession getOrCreateSqlSession()
            throws
            ApplicationException {
        Tuple2<Boolean, SqlSession> currentSqlSession = currentSqlSession();
        if (currentSqlSession.a)
            return currentSqlSession.b;

        //创建新的Sql会话
        ExecutorType executorType = getExecutorType();
        SqlSession session;
        if (getResolveTransaction() == null) {
            session = sqlSessionFactory.openSession();
        } else {
            try {
                session = getResolveTransaction().invoke().;
            } catch (Exception ex) {
                throw new ApplicationException(DbContextStrings.getResolveTransactionFailed(),
                                               ex);
            }
        }
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
}
