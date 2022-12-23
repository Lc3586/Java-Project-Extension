package project.extension.mybatis.edge.dbContext.unitOfWork;

/**
 * 实体变更类型
 *
 * @author LCTR
 * @date 2022-12-19
 */
public enum EntityChangeType {
    /**
     * 插入
     */
    Insert,
    /**
     * 更新
     */
    Update,
    /**
     * 删除
     */
    Delete,
    /**
     * 直接执行Sql语句
     */
    SqlRaw
}
