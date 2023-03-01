package project.extension.mybatis.edge.entityFields;

/**
 * 测试读写文件数据实体字段
 *
 * @author LCTR
 * @date 2023-03-01
 * @see project.extension.mybatis.edge.entity.TestBlobEntity
 */
public class TBE_Fields {
    /**
     * 自增主键
     */
    public static final String id = "id";

    /**
     * 文件数据
     */
    public static final String bytes = "bytes";

    /**
     * 全部字段
     */
    public static final String[] allFields
            = new String[]{
            id,
            bytes
    };
}
