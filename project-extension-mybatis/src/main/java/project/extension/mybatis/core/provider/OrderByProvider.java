package project.extension.mybatis.core.provider;

import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.core.provider.standard.IOrderBy;
import project.extension.mybatis.core.provider.standard.IOrderBySource;
import project.extension.mybatis.model.AdvancedOrder;
import project.extension.mybatis.model.DynamicOrder;
import project.extension.mybatis.model.OrderMethod;

import java.util.Collection;

/**
 * 排序构造器
 *
 * @param <T>       实体类型
 * @param <TSource> 源对象类型
 * @author LCTR
 * @date 2022-04-04
 */
public class OrderByProvider<T, TSource>
        implements IOrderBy<T, IOrderBySource<T>> {
    private final IOrderBySource<T> source;
    private final DynamicOrder dynamicOrder;
    /**
     * 历史别名
     */
    private String aliasHistory;

    public OrderByProvider(IOrderBySource<T> source) {
        this.source = source;
        this.dynamicOrder = new DynamicOrder();
    }

    /**
     * 设置别名
     *
     * @param dynamicOrder 排序
     * @param alias        别名
     */
    private void setAlias(DynamicOrder dynamicOrder,
                          String alias) {
        if (!StringUtils.hasText(dynamicOrder.getAlias()) || (StringUtils.hasText(aliasHistory)
                && dynamicOrder.getAlias()
                               .equals(aliasHistory)))
            dynamicOrder.setAlias(alias);

        if (CollectionsExtension.anyPlus(dynamicOrder.getAdvancedOrder()))
            dynamicOrder.getAdvancedOrder()
                        .forEach(x -> setAlias(x,
                                               alias));
    }

    /**
     * 设置别名
     *
     * @param advancedOrder 排序
     * @param alias         别名
     */
    private void setAlias(AdvancedOrder advancedOrder,
                          String alias) {
        if (!StringUtils.hasText(advancedOrder.getAlias()) || (StringUtils.hasText(aliasHistory)
                && advancedOrder.getAlias()
                                .equals(aliasHistory)))
            advancedOrder.setAlias(alias);
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> orderBy(String fieldName) {
        dynamicOrder.setFieldName(fieldName);
        dynamicOrder.setMethod(OrderMethod.ASC);
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> orderByDescending(String fieldName) {
        dynamicOrder.setFieldName(fieldName);
        dynamicOrder.setMethod(OrderMethod.DESC);
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> thenOrderBy(String fieldName) {
        dynamicOrder.getAdvancedOrder()
                    .add(new AdvancedOrder(fieldName,
                                           OrderMethod.ASC));
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> thenOrderByDescending(String fieldName) {
        dynamicOrder.getAdvancedOrder()
                    .add(new AdvancedOrder(fieldName,
                                           OrderMethod.DESC));
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> orderBy(DynamicOrder order) {
        if (order == null)
            return this;

        if (order.getMethod()
                 .equals(OrderMethod.ASC))
            orderBy(order.getFieldName());
        else
            orderByDescending(order.getFieldName());
        orderBy(order.getAdvancedOrder());
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> orderBy(AdvancedOrder order) {
        if (order == null)
            return this;

        dynamicOrder.getAdvancedOrder()
                    .add(order);
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> orderBy(Collection<AdvancedOrder> orders) {
        if (orders != null && orders.size() > 0)
            dynamicOrder.getAdvancedOrder()
                        .addAll(orders);
        return this;
    }

    @Override
    public <T2, TSource2> IOrderBy<T, IOrderBySource<T>> orderByOther(IOrderBy<T2, TSource2> order)
            throws
            Exception {
        if (!order.hasAlias())
            throw new Exception("子排序必须指定别名");

        DynamicOrder otherDynamicOrder = order.getDynamicOrder();
        if (StringUtils.hasText(otherDynamicOrder.getFieldName())) {
            if (StringUtils.hasText(dynamicOrder.getFieldName()))
                dynamicOrder.getAdvancedOrder()
                            .add(new AdvancedOrder(otherDynamicOrder.getFieldName(),
                                                   otherDynamicOrder.getMethod(),
                                                   otherDynamicOrder.getAlias()));
            else {
                dynamicOrder.setFieldName(otherDynamicOrder.getFieldName());
                dynamicOrder.setMethod(otherDynamicOrder.getMethod());
                dynamicOrder.setAlias(otherDynamicOrder.getAlias());
            }
        }

        if (otherDynamicOrder.getAdvancedOrder()
                             .size() > 0)
            dynamicOrder.getAdvancedOrder()
                        .addAll(otherDynamicOrder.getAdvancedOrder());
        return this;
    }

    @Override
    public IOrderBy<T, IOrderBySource<T>> setAlias(String alias) {
        setAlias(dynamicOrder,
                 alias);
        aliasHistory = alias;
        return this;
    }

    @Override
    public boolean hasAlias() {
        return StringUtils.hasText(aliasHistory);
    }

    @Override
    public DynamicOrder getDynamicOrder() {
        return dynamicOrder;
    }
}
