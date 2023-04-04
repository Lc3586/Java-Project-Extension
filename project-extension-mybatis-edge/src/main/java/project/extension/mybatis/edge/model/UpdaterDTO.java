package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据更新设置
 *
 * @author LCTR
 * @date 2022-04-02
 */
@Data
public class UpdaterDTO {
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
     * <p>实现UPDATE `table1` SET `enable` = 0 WHERE `id` = '1' AND `name` LIKE '%a%'的效果，这条语句中的WHERE条件的内容为此字段设置的值（无需设置WHERE 关键字）</p>
     */
    private final List<String> withWhereSQLs = new ArrayList<>();

    /**
     * 包括的字段
     */
    private final List<String> exceptionFieldNames = new ArrayList<>();

    /**
     * 忽略的字段
     */
    private final List<String> ignoreFieldNames = new ArrayList<>();

    /**
     * 用作临时主键的字段
     */
    private final List<String> tempKeyFieldNames = new ArrayList<>();

    /**
     * 实体类型
     */
    private Class<?> entityType;

    /**
     * 业务模型类型
     */
    private Class<?> dtoType;

    /**
     * 主标签等级
     */
    private Integer mainTagLevel = 0;

    /**
     * 自定义标签
     */
    private final List<String> customTags = new ArrayList<>();

    /**
     * 更改指定字段的值
     */
    private final Map<String, Object> customSetByFieldName = new HashMap<>();

    /**
     * 指定sql语句更改指定列的值
     */
    private final Map<String, String> customSetByFieldNameWithSql = new HashMap<>();

    /**
     * 动态更新
     */
    private final List<DynamicSetter> dynamicSetters = new ArrayList<>();

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
