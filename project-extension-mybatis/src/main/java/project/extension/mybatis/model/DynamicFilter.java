package project.extension.mybatis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态过滤条件
 *
 * @author LCTR
 * @date 2022-03-31
 */
public class DynamicFilter {
    public DynamicFilter() {

    }

    /**
     * @param fieldName 字段名
     * @param compare   比较方式
     * @param value     值
     */
    public DynamicFilter(String fieldName,
                         FilterCompare compare,
                         Object value) {
        this.fieldName = fieldName;
        this.compare = compare;
        this.value = value;
    }

    /**
     * @param fieldName 字段名
     * @param compare   比较方式
     * @param value     值
     * @param relation  组内关系
     */
    public DynamicFilter(String fieldName,
                         FilterCompare compare,
                         Object value,
                         FilterGroupRelation relation) {
        this.fieldName = fieldName;
        this.compare = compare;
        this.value = value;
        this.relation = relation;
    }

    /**
     * @param fieldName        字段名
     * @param compare          比较方式
     * @param value            值
     * @param valueIsFieldName Value值是另一个用来比较的字段
     */
    public DynamicFilter(String fieldName,
                         FilterCompare compare,
                         Object value,
                         Boolean valueIsFieldName) {
        this.fieldName = fieldName;
        this.compare = compare;
        this.value = value;
        this.valueIsFieldName = valueIsFieldName;
    }

    /**
     * @param fieldName        字段名
     * @param compare          比较方式
     * @param value            值
     * @param valueIsFieldName Value值是另一个用来比较的字段
     * @param relation         组内关系
     */
    public DynamicFilter(String fieldName,
                         FilterCompare compare,
                         Object value,
                         Boolean valueIsFieldName,
                         FilterGroupRelation relation) {
        this(fieldName,
             compare,
             value,
             valueIsFieldName,
             relation,
             null);
    }

    /**
     * @param fieldName        字段名
     * @param compare          比较方式
     * @param value            值
     * @param valueIsFieldName Value值是另一个用来比较的字段
     * @param relation         组内关系
     * @param alias            数据表别名
     */
    public DynamicFilter(String fieldName,
                         FilterCompare compare,
                         Object value,
                         Boolean valueIsFieldName,
                         FilterGroupRelation relation,
                         String alias) {
        this(fieldName,
             compare,
             value,
             valueIsFieldName,
             relation,
             alias,
             null);
    }

    /**
     * @param fieldName        字段名
     * @param compare          比较方式
     * @param value            值
     * @param valueIsFieldName Value值是另一个用来比较的字段
     * @param relation         组内关系
     * @param alias            数据表别名
     */
    public DynamicFilter(String fieldName,
                         FilterCompare compare,
                         Object value,
                         Boolean valueIsFieldName,
                         FilterGroupRelation relation,
                         String alias,
                         List<DynamicFilter> dynamicFilters) {
        this.fieldName = fieldName;
        this.compare = compare;
        this.value = value;
        this.valueIsFieldName = valueIsFieldName;
        this.relation = relation;
        this.alias = alias;
        if (dynamicFilters != null)
            this.dynamicFilter.addAll(dynamicFilters);
    }

    public DynamicFilter(String customSql) {
        this.customSql = customSql;
    }

    public DynamicFilter(String customSql,
                         FilterGroupRelation relation) {
        this.customSql = customSql;
        this.relation = relation;
    }

    /**
     * @param dynamicFilters 子条件
     */
    public DynamicFilter(List<DynamicFilter> dynamicFilters) {
        this.dynamicFilter.addAll(dynamicFilters);
    }

    /**
     * @param dynamicFilters 子条件
     */
    public DynamicFilter(List<DynamicFilter> dynamicFilters,
                         FilterGroupRelation relation) {
        this.dynamicFilter.addAll(dynamicFilters);
        this.relation = relation;
    }

    private String alias;

    private String fieldName;

    private Object value;

    private Boolean valueIsFieldName = false;

    private FilterCompare compare = FilterCompare.Eq;

    private String customSql;

    private FilterGroupRelation relation = FilterGroupRelation.AND;

    private final List<DynamicFilter> dynamicFilter = new ArrayList<>();

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
     * 要比较的字段
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 用于比较的值
     */
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Value值是另一个用来比较的字段
     */
    public Boolean getValueIsFieldName() {
        return valueIsFieldName;
    }

    public void setValueIsFieldName(Boolean valueIsFieldName) {
        this.valueIsFieldName = valueIsFieldName;
    }

    /**
     * 比较类型
     * <p>默认值 Eq</p>
     */
    public FilterCompare getCompare() {
        return compare;
    }

    public void setCompare(FilterCompare compare) {
        this.compare = compare;
    }

    /**
     * 自定义sql语句
     */
    public String getCustomSql() {
        return customSql;
    }

    public void setCustomSql(String customSql) {
        this.customSql = customSql;
    }

    /**
     * 组内关系
     * <p>默认值 AND</p>
     */
    public FilterGroupRelation getRelation() {
        return relation;
    }

    public void setRelation(FilterGroupRelation relation) {
        this.relation = relation;
    }

    /**
     * 子条件
     */
    public List<DynamicFilter> getDynamicFilter() {
        return dynamicFilter;
    }
}
