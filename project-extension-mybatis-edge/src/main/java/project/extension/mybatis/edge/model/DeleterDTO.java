package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据删除设置
 *
 * @author LCTR
 * @date 2022-04-02
 */
@Data
public class DeleterDTO {
    /**
     * 模式名
     */
    private String schema;

    /**
     * 数据表名
     */
    private String tableName;

    /**
     * 数据表别名
     */
    private String alias;

    /**
     * 动态过滤条件
     */
    private List<DynamicFilter> dynamicFilter;

    /**
     * 自定义过滤SQL
     * <p>实现DELETE `table1` WHERE `id` = '1' AND `name` LIKE '%a%'的效果，这条语句中的WHERE条件的内容为此字段设置的值（无需设置WHERE 关键字）</p>
     */
    private final List<String> withWhereSQLs = new ArrayList<>();

    /**
     * 用作临时主键的字段
     */
    private final List<String> tempKeyFieldNames = new ArrayList<>();

    /**
     * 用作临时主键的列
     */
    private final List<String> tempKeyColums = new ArrayList<>();

    /**
     * 实体类型
     */
    private Class<?> entityType;

    /**
     * 业务模型类型
     */
    private Class<?> dtoType;

    /**
     * 数据集合
     */
    private final List<Object> dataList = new ArrayList<>();

    /**
     * 参数
     */
    private final Map<String, Object> parameter = new HashMap<>();

    /**
     * 自定义参数
     */
    private final Map<String, Object> customParameter = new HashMap<>();
}
