package project.extension.mybatis.edge.dbContext;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.transaction.TransactionStatus;
import project.extension.func.IFunc0;
import project.extension.mybatis.edge.core.ado.NaiveAdoProvider;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

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
    protected ScopeTransactionAdo(String dataSource,
                                  IFunc0<Tuple2<TransactionStatus, TransactionIsolationLevel>> resolveTransaction)
            throws
            ModuleException {
        super(dataSource);
        super.setResolveTransaction(resolveTransaction);
    }
}
