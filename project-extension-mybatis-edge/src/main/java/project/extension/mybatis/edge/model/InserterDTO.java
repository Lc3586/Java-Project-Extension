package project.extension.mybatis.edge.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据插入设置
 *
 * @author LCTR
 * @date 2022-04-02
 */
public class InserterDTO {
    private String schema;

    private String tableName;

    private String alias;

    private final List<String> exceptionFieldNames = new ArrayList<>();

    private final List<String> ignoreFieldNames = new ArrayList<>();

    private Class<?> entityType;

    private Class<?> dtoType;

    private Integer mainTagLevel = 0;

    private final List<String> customTags = new ArrayList<>();

    private final List<Object> dataList = new ArrayList<>();

    private final Map<String, Object> parameter = new HashMap<>();

    private final Map<String, Class<?>> outParameter = new HashMap<>();

    private final Map<String, Class<?>> inOutParameter = new HashMap<>();

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
     * 数据集合
     */
    public List<Object> getDataList() {
        return dataList;
    }

    /**
     * 全部参数
     * <p>不可手动设置null值</p>
     */
    public Map<String, Object> getParameter() {
        return parameter;
    }

    /**
     * 输出参数
     */
    public Map<String, Class<?>> getOutParameter() {
        return outParameter;
    }

    /**
     * 输入输出参数
     */
    public Map<String, Class<?>> getInOutParameter() {
        return inOutParameter;
    }
}
