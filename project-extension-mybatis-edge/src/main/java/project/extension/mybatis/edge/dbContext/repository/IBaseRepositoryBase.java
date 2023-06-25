package project.extension.mybatis.edge.dbContext.repository;

import org.apache.ibatis.session.TransactionIsolationLevel;
import project.extension.action.IAction0Throw;
import project.extension.func.IFunc1;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.dbContext.unitOfWork.IUnitOfWork;
import project.extension.tuple.Tuple2;

/**
 * 数据仓储基础接口
 *
 * @author LCTR
 * @date 2023-01-06
 */
public interface IBaseRepositoryBase {
    /**
     * 实体类型
     */
    Class<?> getEntityType();

    /**
     * 获取工作单元
     */
    IUnitOfWork getUnitOfWork();

    /**
     * 设置工作单元
     */
    void setUnitOfWork(IUnitOfWork unitOfWork);

    /**
     * 获取ORM
     */
    INaiveSql getOrm();

    /**
     * 切换实体类型
     *
     * @param entityType 实体类型
     */
    void asType(Class<?> entityType);

    /**
     * 设置表名
     *
     * @param change (旧表名)-> 新表名
     */
    void asTable(IFunc1<String, String> change);

    /**
     * 开启事务
     *
     * @param handler 执行函数
     * @return a：true：已提交，false：已回滚，b：异常信息
     */
    Tuple2<Boolean, Exception> transaction(IAction0Throw handler);

    /**
     * 开启事务
     * <p>使用springframework管理事务</p>
     * <p>兼容声明式事务（方法上添加了注解 org.springframework.transaction.annotation.Transactional）</p>
     *
     * @param isolationLevel 事务隔离等级
     * @param handler        执行函数
     * @return a：true：已提交，false：已回滚，b：异常信息
     */
    Tuple2<Boolean, Exception> transaction(TransactionIsolationLevel isolationLevel,
                                           IAction0Throw handler);
}
