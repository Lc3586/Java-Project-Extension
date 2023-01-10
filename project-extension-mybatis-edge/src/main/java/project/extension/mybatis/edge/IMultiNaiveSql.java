package project.extension.mybatis.edge;

import org.springframework.stereotype.Repository;

/**
 * 多库ORM
 *
 * @author LCTR
 * @date 2023-01-10
 */
@Repository
public interface IMultiNaiveSql {
    /**
     * 获取主库ORM
     */
    INaiveSql getMasterOrm();

    /**
     * 获取从库ORM
     *
     * @param dataSource 数据源名称
     */
    INaiveSql getSlaveOrm(String dataSource);
}
