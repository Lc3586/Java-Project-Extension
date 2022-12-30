package project.extension.mybatis.edge.dbContext;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.TransactionStatus;
import project.extension.func.IFunc2;
import project.extension.mybatis.edge.core.ado.NaiveAdoProvider;
import project.extension.standard.exception.ApplicationException;

import javax.sql.DataSource;

/**
 * 数据访问对象构造器
 *
 * @author LCTR
 * @date 2022-12-22
 */
public class ScopeTransactionAdo
        extends NaiveAdoProvider {
    /**
     * @param dataSource 数据源
     */
    protected ScopeTransactionAdo(DataSource dataSource,
                                  IFunc2<SqlSessionFactory, ExecutorType, TransactionStatus> resolveTransaction)
            throws
            ApplicationException {
        super(dataSource);
        super.setResolveTransaction(resolveTransaction);
    }
}
