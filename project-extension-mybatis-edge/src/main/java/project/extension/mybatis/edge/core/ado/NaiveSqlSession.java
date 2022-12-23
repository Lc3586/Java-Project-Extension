package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.dbContext.repository.ITransactionAction;

/**
 * 初级Sql会话
 *
 * @author LCTR
 * @date 2022-04-06
 */
@Component("NaiveSqlSession")
@DependsOn("NaiveDataSourceProvider")
public class NaiveSqlSession {
    public NaiveSqlSession(BaseConfig config,
                           DataSourceTransactionManager dataSourceTransactionManager,
                           TransactionDefinition transactionDefinition)
            throws
            Exception {
        NaiveSqlSession.build(config,
                              dataSourceTransactionManager,
                              transactionDefinition);
    }

    /**
     * 事务管理器
     */
    private static DataSourceTransactionManager dataSourceTransactionManager;

    /**
     * 事务定义
     */
    private static TransactionDefinition transactionDefinition;

    /**
     * Sql会话工厂
     */
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 构建Sql会话工厂
     *
     * @param config                       配置
     * @param dataSourceTransactionManager 事务管理器
     * @param transactionDefinition        事务定义
     */
    private static void build(BaseConfig config,
                              DataSourceTransactionManager dataSourceTransactionManager,
                              TransactionDefinition transactionDefinition)
            throws
            Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSourceTransactionManager.getDataSource());
        sessionFactory.setConfigLocation(new DefaultResourceLoader().getResource(config.getConfigLocation()));
        sqlSessionFactory = sessionFactory.getObject();
        NaiveSqlSession.dataSourceTransactionManager = dataSourceTransactionManager;
        NaiveSqlSession.transactionDefinition = transactionDefinition;
    }

    /**
     * 获取当前SQL会话
     * <p>如果不存在会自动创建</p>
     *
     * @return SqlSession
     */
    public static SqlSession current() {
        return SqlSessionUtils.getSqlSession(sqlSessionFactory);
    }

    /**
     * 打开一个新的SQL会话
     *
     * @return SQL会话
     */
    public static SqlSession open()
            throws
            Throwable {
        return open(null);
    }

    /**
     * 打开一个新的SQL会话
     *
     * @return SQL会话
     */
    public static SqlSession open(TransactionIsolationLevel level)
            throws
            Throwable {
        if (level != null) return sqlSessionFactory.openSession(level);
        else return sqlSessionFactory.openSession();
    }

    /**
     * 停止使用
     *
     * @param sqlSession SQL会话
     */
    public static void done(SqlSession sqlSession) {
        done(sqlSession,
             false);
    }

    /**
     * 停止使用
     *
     * @param sqlSession SQL会话
     * @param commit     提交事务
     */
    public static void done(SqlSession sqlSession,
                            boolean commit) {
        if (commit) sqlSession.commit();
        sqlSession.close();
    }

    /**
     * 停止使用
     *
     * @param sqlSession SQL会话
     */
    public static void error(SqlSession sqlSession) {
        error(sqlSession,
              false);
    }

    /**
     * 停止使用
     *
     * @param sqlSession SQL会话
     * @param rollback   回滚事务
     */
    public static void error(SqlSession sqlSession,
                             boolean rollback) {
        if (rollback) sqlSession.rollback();
        sqlSession.close();
    }

    /**
     * 获取配置
     *
     * @return 配置
     */
    public static Configuration getConfiguration() {
        return sqlSessionFactory.getConfiguration();
    }

    /**
     * 运行事务
     *
     * @param action 操作
     */
    public static void runTransaction(ITransactionAction action)
            throws
            Exception {
        runTransaction(null,
                       action);
    }

    /**
     * 运行事务
     *
     * @param level  事务隔离等级
     * @param action 操作
     */
    public static void runTransaction(TransactionIsolationLevel level,
                                      ITransactionAction action)
            throws
            Exception {
        TransactionStatus transactionStatus = beginTransaction(level);
        try {
            action.invoke();
            commitTransaction(transactionStatus);
        } catch (Exception ex) {
            rollbackTransaction(transactionStatus);
            throw ex;
        }
    }

    /**
     * 开启事务
     *
     * @return 事务状态
     */
    public static TransactionStatus beginTransaction() {
        return beginTransaction(null);
    }

    /**
     * 开启事务
     *
     * @param level 事务隔离等级
     */
    public static TransactionStatus beginTransaction(TransactionIsolationLevel level) {
        if (level == null) return dataSourceTransactionManager.getTransaction(transactionDefinition);

        DefaultTransactionDefinition definition = new DefaultTransactionDefinition(transactionDefinition);
        definition.setIsolationLevel(level.getLevel());
        return dataSourceTransactionManager.getTransaction(definition);
    }

    /**
     * 事务提交
     *
     * @param transactionStatus 事务状态
     */
    public static void commitTransaction(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.commit(transactionStatus);
    }

    /**
     * 事务回滚
     *
     * @param transactionStatus 事务状态
     */
    public static void rollbackTransaction(TransactionStatus transactionStatus) {
        dataSourceTransactionManager.rollback(transactionStatus);
    }
}
