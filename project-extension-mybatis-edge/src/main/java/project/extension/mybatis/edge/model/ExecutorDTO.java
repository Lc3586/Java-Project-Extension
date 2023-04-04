package project.extension.mybatis.edge.model;

import lombok.Data;
import project.extension.mybatis.edge.annotations.ExecutorSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据查询设置
 *
 * @author LCTR
 * @date 2022-04-02
 */
@ExecutorSetting
@Data
public class ExecutorDTO {
    /**
     * 分页设置
     */
    @ExecutorSetting(ExecutorParameter.分页设置)
    private Pagination pagination;

    /**
     * 动态排序条件
     */
    @ExecutorSetting(ExecutorParameter.动态排序条件)
    private DynamicOrder dynamicOrder;

    /**
     * 动态过滤条件
     */
    @ExecutorSetting(ExecutorParameter.动态过滤条件)
    private List<DynamicFilter> dynamicFilters;

    /**
     * 模式名
     */
    @ExecutorSetting(ExecutorParameter.模式名)
    private String schema;

    /**
     * 数据表名
     */
    @ExecutorSetting(ExecutorParameter.数据表名)
    private String tableName;

    /**
     * 数据表别名
     */
    @ExecutorSetting(ExecutorParameter.数据表别名)
    private String alias;

    /**
     * 设置为查询所有列
     */
    private Boolean allColumns = true;

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
     * 自定义列
     */
    @ExecutorSetting(ExecutorParameter.自定义列)
    private final List<String> customFieldNames = new ArrayList<>();

    /**
     * 自定义SQL
     * <p>实现SELECT * FROM (SELECT `id`, `name` FROM `table1`)的效果，这条语句中的子查询为此字段设置的值</p>
     * <p>如果在此sql语句中使用了别名，那么必须使用添加了@ExecutorSetting(ExecutorParameter.数据表别名)注解的字段设置其他别名</p>
     */
    @ExecutorSetting(ExecutorParameter.自定义SQL)
    private final Map<DbType, String> withSQL = new HashMap<>();

    /**
     * 自定义过滤SQL
     * <p>实现SELECT `id`, `name` FROM `table1` WHERE `id` = '1' AND `name` LIKE '%a%'的效果，这条语句中的WHERE条件的内容为此字段设置的值（无需设置WHERE 关键字）</p>
     */
    @ExecutorSetting(ExecutorParameter.自定义过滤SQL)
    private final List<String> withWhereSQLs = new ArrayList<>();

    /**
     * 自定义排序SQL
     * <p>实现SELECT `id`, `name` FROM `table1` ORDER BY `id` ASC, `create_time` DESC的效果，这条语句中的ORDER BY条件的内容为此字段设置的值（无需设置ORDER BY 关键字）</p>
     */
    @ExecutorSetting(ExecutorParameter.自定义排序SQL)
    private String withOrderBySQL;

    /**
     * 分组列
     */
    private final List<String> groupByFieldNames = new ArrayList<>();

    /**
     * 参数
     */
    private final Map<String, Object> parameter = new HashMap<>();

    /**
     * 自定义参数
     */
    private final Map<String, Object> customParameter = new HashMap<>();
}
