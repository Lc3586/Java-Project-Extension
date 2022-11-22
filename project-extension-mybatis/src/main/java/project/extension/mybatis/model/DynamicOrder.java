package project.extension.mybatis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态排序条件
 *
 * @author LCTR
 * @date 2022-03-25
 */
public class DynamicOrder {
    public DynamicOrder() {

    }

    /**
     * @param fieldName 字段名
     * @param method   排序方法
     */
    public DynamicOrder(String fieldName, OrderMethod method) {
        this(fieldName, method, null);
    }

    /**
     * @param fieldName 字段名
     * @param method   排序方法
     * @param alias    数据表别名
     */
    public DynamicOrder(String fieldName, OrderMethod method, String alias) {
        this.fieldName = fieldName;
        this.method = method;
        this.alias = alias;
    }

    /**
     * @param advancedOrder 高级排序
     */
    public DynamicOrder(List<AdvancedOrder> advancedOrder) {
        if (advancedOrder != null)
            this.advancedOrder.addAll(advancedOrder);
    }

    /**
     * @param fieldName      字段名
     * @param method        排序方法
     * @param alias         数据表别名
     * @param advancedOrder 高级排序
     */
    public DynamicOrder(String fieldName, OrderMethod method, String alias, List<AdvancedOrder> advancedOrder) {
        this.fieldName = fieldName;
        this.method = method;
        this.alias = alias;
        if (advancedOrder != null)
            this.advancedOrder.addAll(advancedOrder);
    }

    private String alias;

    private String fieldName;

    private OrderMethod method;

    private final List<AdvancedOrder> advancedOrder = new ArrayList<>();

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
     * 排序字段
     * <p>默认值 createTime</p>
     */
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 排序方法
     * <p>默认值 DESC（降序）</p>
     */
    public OrderMethod getMethod() {
        return method;
    }

    public void setMethod(OrderMethod method) {
        this.method = method;
    }

    /**
     * 高级排序
     */
    public List<AdvancedOrder> getAdvancedOrder() {
        return advancedOrder;
    }
}
