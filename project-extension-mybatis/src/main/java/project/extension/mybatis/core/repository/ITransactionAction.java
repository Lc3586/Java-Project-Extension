package project.extension.mybatis.core.repository;

/**
 * 事务操作
 *
 * @author LCTR
 * @date 2022-04-02
 */
public interface ITransactionAction {
    /**
     * 执行操作
     */
    void invoke()
            throws
            Exception;
}
