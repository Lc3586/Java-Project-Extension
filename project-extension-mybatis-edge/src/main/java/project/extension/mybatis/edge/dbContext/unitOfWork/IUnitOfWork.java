package project.extension.mybatis.edge.dbContext.unitOfWork;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.transaction.TransactionStatus;
import project.extension.mybatis.edge.INaiveSql;

import java.util.Map;

/**
 * 工作单元接口类
 *
 * @author LCTR
 * @date 2022-12-19
 */
public interface IUnitOfWork {
    /**
     * 获取Orm
     * <p>此对象下的select/delete/insert/update 与工作单元事务保持一致，无需再设置 WithTransaction</p>
     *
     * @return Orm对象
     */
    INaiveSql getOrm();

    /**
     * 开启事务，或者返回已开启的事务
     *
     * @return 事务对象
     */
    TransactionStatus getOrBeginTransaction();

    /**
     * 开启事务，或者返回已开启的事务
     *
     * @param isCreate 若未开启事务，则开启
     * @return 事务对象
     */
    TransactionStatus getOrBeginTransaction(boolean isCreate);

    /**
     * 获取事务隔离等级
     *
     * @return 事务隔离等级
     */
    TransactionIsolationLevel getIsolationLevel();

    /**
     * 设置事务隔离等级
     *
     * @param isolationLevel 事务隔离等级
     */
    void setIsolationLevel(TransactionIsolationLevel isolationLevel);

    /**
     * 提交事务
     */
    void commit();

    /**
     * 回滚事务
     */
    void rollback();

    /**
     * 获取工作单元内的实体变化跟踪
     *
     * @return 工作单元内的实体变化跟踪
     */
    EntityChangeRecord getEntityChangeReport();

    /**
     * 获取用户自定义的状态数据，便于扩展
     *
     * @return 用户自定义的状态数据
     */
    Map<String, Object> getStates();
}
