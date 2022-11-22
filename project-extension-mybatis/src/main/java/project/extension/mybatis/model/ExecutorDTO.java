package project.extension.mybatis.model;

import project.extension.mybatis.annotations.ExecutorSetting;

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
public class ExecutorDTO {
    @ExecutorSetting(ExecutorParameter.分页设置)
    private Pagination pagination;

    @ExecutorSetting(ExecutorParameter.动态排序条件)
    private DynamicOrder dynamicOrder;

    @ExecutorSetting(ExecutorParameter.动态过滤条件)
    private List<DynamicFilter> dynamicFilters;

    @ExecutorSetting(ExecutorParameter.模式名)
    private String schema;

    @ExecutorSetting(ExecutorParameter.数据表名)
    private String tableName;

    @ExecutorSetting(ExecutorParameter.数据表别名)
    private String alias;

    private Boolean allColumns = true;

    private Class<?> entityType;

    private Class<?> dtoType;

    private Integer mainTagLevel = 0;

    private final List<String> customTags = new ArrayList<>();

    @ExecutorSetting(ExecutorParameter.自定义列)
    private final List<String> customFieldNames = new ArrayList<>();

    @ExecutorSetting(ExecutorParameter.自定义SQL)
    private String withSQL;

    @ExecutorSetting(ExecutorParameter.自定义过滤SQL)
    private final List<String> withWhereSQLs = new ArrayList<>();

    @ExecutorSetting(ExecutorParameter.自定义排序SQL)
    private String withOrderBySQL;

    private final List<String> groupByFieldNames = new ArrayList<>();

    private final Map<String, Object> parameter = new HashMap<>();

    private final Map<String, Object> customParameter = new HashMap<>();

    /**
     * 分页设置
     */
    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * 动态过滤条件
     */
    public List<DynamicFilter> getDynamicFilters() {
        return dynamicFilters;
    }

    public void setDynamicFilters(List<DynamicFilter> dynamicFilters) {
        this.dynamicFilters = dynamicFilters;
    }

    /**
     * 模式名
     */
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * 数据表名
     */
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 数据表别名
     */
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 设置为查询所有列
     */
    public Boolean getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(Boolean allColumns) {
        this.allColumns = allColumns;
    }

    /**
     * 实体类型
     */
    public Class<?> getEntityType() {
        return entityType;
    }

    public void setEntityType(Class<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * 业务模型类型
     */
    public Class<?> getDtoType() {
        return dtoType;
    }

    public void setDtoType(Class<?> dtoType) {
        this.dtoType = dtoType;
    }

    /**
     * 主标签等级
     */
    public Integer getMainTagLevel() {
        return mainTagLevel;
    }

    public void setMainTagLevel(Integer mainTagLevel) {
        this.mainTagLevel = mainTagLevel;
    }

    /**
     * 自定义标签
     */
    public List<String> getCustomTags() {
        return customTags;
    }

    /**
     * 自定义列
     */
    public List<String> getCustomFieldNames() {
        return customFieldNames;
    }

    /**
     * 自定义SQL
     * <p>实现SELECT * FROM (SELECT `id`, `name` FROM `table1`)的效果，这条语句中的子查询为此字段设置的值</p>
     * <p>如果在此sql语句中使用了别名，那么必须使用添加了@ExecutorSetting(ExecutorParameter.数据表别名)注解的字段设置其他别名</p>
     */
    public String getWithSQL() {
        return withSQL;
    }

    public void setWithSQL(String withSQL) {
        this.withSQL = withSQL;
    }

    /**
     * 自定义过滤SQL
     * <p>实现SELECT `id`, `name` FROM `table1` WHERE `id` = '1' AND `name` LIKE '%a%'的效果，这条语句中的WHERE条件的内容为此字段设置的值（无需设置WHERE 关键字）</p>
     */
    public List<String> getWithWhereSQLs() {
        return withWhereSQLs;
    }

    /**
     * 自定义排序SQL
     * <p>实现SELECT `id`, `name` FROM `table1` ORDER BY `id` ASC, `create_time` DESC的效果，这条语句中的ORDER BY条件的内容为此字段设置的值（无需设置ORDER BY 关键字）</p>
     */
    public String getWithOrderBySQL() {
        return withOrderBySQL;
    }

    public void setWithOrderBySQL(String withOrderBySQL) {
        this.withOrderBySQL = withOrderBySQL;
    }

    /**
     * 动态排序条件
     */
    public DynamicOrder getDynamicOrder() {
        return dynamicOrder;
    }

    public void setDynamicOrder(DynamicOrder dynamicOrder) {
        this.dynamicOrder = dynamicOrder;
    }

    /**
     * 分组列
     */
    public List<String> getGroupByFieldNames() {
        return groupByFieldNames;
    }

    /**
     * 参数
     */
    public Map<String, Object> getParameter() {
        return parameter;
    }

    /**
     * 自定义参数
     */
    public Map<String, Object> getCustomParameter() {
        return customParameter;
    }
}
