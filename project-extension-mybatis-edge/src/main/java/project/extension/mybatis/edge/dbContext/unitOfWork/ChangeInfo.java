package project.extension.mybatis.edge.dbContext.unitOfWork;

/**
 * 实体变更信息
 *
 * @author LCTR
 * @date 2022-12-19
 */
public class ChangeInfo {
    /**
     * 对象
     */
    public Object Object;

    /**
     * Type = Update 的时候，获取更新之前的对象
     */
    public Object BeforeObject;

    /**
     * 变更类型
     */
    public EntityChangeType Type;

    /**
     * 实体类型
     */
    public Class<?> entityType;
}
