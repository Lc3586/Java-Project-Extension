package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态排序条件
 *
 * @author LCTR
 * @date 2022-03-25
 */
@Data
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

    /**
     * 数据表别名
     */
    private String alias;

    /**
     * 排序字段
     * <p>默认值 createTime</p>
     */
    private String fieldName;

    /**
     * 排序方法
     * <p>默认值 DESC（降序）</p>
     */
    private OrderMethod method;

    /**
     * 高级排序
     */
    private final List<AdvancedOrder> advancedOrder = new ArrayList<>();
}
