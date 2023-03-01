package project.extension.mybatis.edge.entityFields;

/**
 * 测试读写长文本数据实体字段
 *
 * @author LCTR
 * @date 2023-03-01
 * @see project.extension.mybatis.edge.entity.TestClobEntity
 */
public class TCE_Fields {
    /**
     * 主键
     */
    public static final String id = "id";

    /**
     * 文本数据
     */
    public static final String text = "text";

    /**
     * 全部字段
     */
    public static final String[] allFields
            = new String[]{
            id,
            text
    };
}
