package project.extension.mybatis.edge.entityFields;

/**
 * 测试自增主键实体字段
 *
 * @author LCTR
 * @date 2023-03-01
 * @see project.extension.mybatis.edge.entity.TestIdentityEntity
 */
public class TIE_Fields {
    /**
     * 自增主键
     */
    public static final String id = "id";

    /**
     * 编号
     */
    public static final String no = "no";

    /**
     * 全部字段
     */
    public static final String[] allFields
            = new String[]{
            id,
            no
    };
}
