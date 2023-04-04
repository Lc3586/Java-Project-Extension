package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态过滤条件
 *
 * @author LCTR
 * @date 2022-03-31
 */
@Data
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

    /**
     * 数据表别名
     */
    private String alias;

    /**
     * 要比较的字段
     */
    private String fieldName;

    /**
     * 用于比较的值
     */
    private Object value;

    /**
     * Value值是另一个用来比较的字段
     */
    private Boolean valueIsFieldName = false;

    /**
     * 比较类型
     * <p>默认值 Eq</p>
     */
    private FilterCompare compare = FilterCompare.Eq;

    /**
     * 自定义sql语句
     */
    private String customSql;

    /**
     * 组内关系
     * <p>默认值 AND</p>
     */
    private FilterGroupRelation relation = FilterGroupRelation.AND;

    /**
     * 子条件
     */
    private final List<DynamicFilter> dynamicFilter = new ArrayList<>();
}
