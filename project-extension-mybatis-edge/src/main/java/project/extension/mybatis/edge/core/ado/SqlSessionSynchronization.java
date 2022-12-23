package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.springframework.util.Assert.notNull;

/**
 * Sql会话事务同步
 *
 * @author LCTR
 * @date 2022-12-22
 */
public class SqlSessionSynchronization
        implements TransactionSynchronization {

    private final SqlSessionHolder holder;

    private final SqlSessionFactory sessionFactory;

    private boolean holderActive = true;

    public SqlSessionSynchronization(SqlSessionHolder holder,
                                     SqlSessionFactory sessionFactory) {
        notNull(holder,
                "Parameter 'holder' must be not null");
        notNull(sessionFactory,
                "Parameter 'sessionFactory' must be not null");

        this.holder = holder;
        this.sessionFactory = sessionFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        // order right before any Connection synchronization
        return DataSourceUtils.CONNECTION_SYNCHRONIZATION_ORDER - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void suspend() {
        if (this.holderActive) {
            TransactionSynchronizationManager.unbindResource(this.sessionFactory);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
        if (this.holderActive) {
            TransactionSynchronizationManager.bindResource(this.sessionFactory,
                                                           this.holder);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeCommit(boolean readOnly) {
        // Connection commit or rollback will be handled by ConnectionSynchronization or
        // DataSourceTransactionManager.
        // But, do cleanup the SqlSession / Executor, including flushing BATCH statements so
        // they are actually executed.
        // SpringManagedTransaction will no-op the commit over the jdbc connection
        // TODO This updates 2nd level caches but the tx may be rolledback later on!
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            try {
                this.holder.getSqlSession()
                           .commit();
            } catch (PersistenceException p) {
                if (this.holder.getPersistenceExceptionTranslator() != null) {
                    DataAccessException translated = this.holder.getPersistenceExceptionTranslator()
                                                                .translateExceptionIfPossible(p);
                    if (translated != null) {
                        throw translated;
                    }
                }
                throw p;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeCompletion() {
        // Issue #18 Close SqlSession and deregister it now
        // because afterCompletion may be called from a different thread
        if (!this.holder.isOpen()) {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            this.holderActive = false;
            this.holder.getSqlSession()
                       .close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(int status) {
        if (this.holderActive) {
            // afterCompletion may have been called from a different thread
            // so avoid failing if there is nothing in this one
            TransactionSynchronizationManager.unbindResourceIfPossible(sessionFactory);
            this.holderActive = false;
            this.holder.getSqlSession()
                       .close();
        }
        this.holder.reset();
    }
}