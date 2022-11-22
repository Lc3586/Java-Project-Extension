package project.extension.mybatis.model;

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
public class UpdaterDTO {
    private String schema;

    private String tableName;

    private String alias;

    private List<DynamicFilter> dynamicFilter;

    private final List<String> withWhereSQLs = new ArrayList<>();

    private final List<String> exceptionFieldNames = new ArrayList<>();

    private final List<String> ignoreFieldNames = new ArrayList<>();

    private final List<String> tempKeyFieldNames = new ArrayList<>();

    private Class<?> entityType;

    private Class<?> dtoType;

    private Integer mainTagLevel = 0;

    private final List<String> customTags = new ArrayList<>();

    private final Map<String, Object> customSetByFieldName = new HashMap<>();

    private final Map<String, String> customSetByFieldNameWithSql = new HashMap<>();

    private final List<DynamicSetter> dynamicSetters = new ArrayList<>();

    private final List<Object> dataList = new ArrayList<>();

    private final Map<String, Object> parameter = new HashMap<>();

    private final Map<String, Object> customParameter = new HashMap<>();

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
     * 自定义过滤SQL
     * <p>实现UPDATE `table1` SET `enable` = 0 WHERE `id` = '1' AND `name` LIKE '%a%'的效果，这条语句中的WHERE条件的内容为此字段设置的值（无需设置WHERE 关键字）</p>
     */
    public List<String> getWithWhereSQLs() {
        return withWhereSQLs;
    }

    /**
     * 动态过滤条件
     */
    public List<DynamicFilter> getDynamicFilter() {
        return dynamicFilter;
    }

    public void setDynamicFilter(List<DynamicFilter> dynamicFilter) {
        this.dynamicFilter = dynamicFilter;
    }

    /**
     * 包括的字段
     */
    public List<String> getExceptionFieldNames() {
        return exceptionFieldNames;
    }

    /**
     * 忽略的字段
     */
    public List<String> getIgnoreFieldNames() {
        return ignoreFieldNames;
    }

    /**
     * 用作临时主键的字段
     */
    public List<String> getTempKeyFieldNames() {
        return tempKeyFieldNames;
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
     * 更改指定字段的值
     */
    public Map<String, Object> getCustomSetByFieldName() {
        return customSetByFieldName;
    }

    /**
     * 指定sql语句更改指定列的值
     */
    public Map<String, String> getCustomSetByFieldNameWithSql() {
        return customSetByFieldNameWithSql;
    }

    /**
     * 动态更新
     */
    public List<DynamicSetter> getDynamicSetters() {
        return dynamicSetters;
    }

    /**
     * 数据集合
     */
    public List<Object> getDataList() {
        return dataList;
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
