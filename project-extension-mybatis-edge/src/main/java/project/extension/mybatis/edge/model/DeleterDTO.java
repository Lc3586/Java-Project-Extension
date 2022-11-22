package project.extension.mybatis.edge.model;

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
public class DeleterDTO {
    private String schema;

    private String tableName;

    private String alias;

    private List<DynamicFilter> dynamicFilter;

    private final List<String> withWhereSQLs = new ArrayList<>();

    private final List<String> tempKeyFieldNames = new ArrayList<>();

    private final List<String> tempKeyColums = new ArrayList<>();

    private Class<?> entityType;

    private Class<?> dtoType;

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
     * <p>实现DELETE `table1` WHERE `id` = '1' AND `name` LIKE '%a%'的效果，这条语句中的WHERE条件的内容为此字段设置的值（无需设置WHERE 关键字）</p>
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
     * 用作临时主键的字段
     */
    public List<String> getTempKeyFieldNames() {
        return tempKeyFieldNames;
    }

    /**
     * 用作临时主键的列
     */
    public List<String> getTempKeyColums() {
        return tempKeyColums;
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
