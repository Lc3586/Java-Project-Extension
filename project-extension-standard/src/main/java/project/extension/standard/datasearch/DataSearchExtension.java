package project.extension.standard.datasearch;

import org.springframework.util.StringUtils;
import project.extension.mybatis.edge.model.AdvancedOrder;
import project.extension.mybatis.edge.model.DynamicFilter;
import project.extension.mybatis.edge.model.DynamicOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据搜索拓展方法
 *
 * @author LCTR
 * @date 2022-12-08
 */
public class DataSearchExtension {
    /**
     * 数据搜索条件转为动态搜索条件
     *
     * @param filters          数据搜索条件
     * @param tableKeyAliasMap 数据表别名映射
     * @return 动态搜索条件
     */
    public static List<DynamicFilter> toDynamicFilter(List<DataSearchFilter> filters,
                                                      Map<String, String> tableKeyAliasMap) {
        if (filters == null) return null;

        List<DynamicFilter> dynamicFilters = new ArrayList<>();
        filters.forEach(f -> dynamicFilters.add(toDynamicFilter(f,
                                                                tableKeyAliasMap)));

        return dynamicFilters;
    }

    /**
     * 数据搜索条件转为动态搜索条件
     *
     * @param filter           数据搜索条件
     * @param tableKeyAliasMap 数据表标识数据表别名映射数据
     * @return 动态搜索条件
     */
    public static DynamicFilter toDynamicFilter(DataSearchFilter filter,
                                                Map<String, String> tableKeyAliasMap) {
        if (filter == null) return null;

        return new DynamicFilter(
                filter.getField(),
                filter.getCompare(),
                filter.getValueIsField()
                ? filter.getValue() != null
                  ? filter.getValue()
                          .toString()
                  : null
                : filter.getValue(),
                filter.getValueIsField(),
                filter.getRelation(),
                tableKeyAliasMap.get(filter.getTableKey()),
                toDynamicFilter(filter.getFilters(),
                                tableKeyAliasMap)
        );
    }

    /**
     * 数据排序条件转为动态排序条件
     *
     * @param order            数据排序条件
     * @param tableKeyAliasMap 数据表标识数据表别名映射数据
     * @return 动态排序条件
     */
    public static DynamicOrder toDynamicOrder(DataSearchOrder order,
                                              Map<String, String> tableKeyAliasMap) {
        if (order == null || !StringUtils.hasText((order.getField()))) return null;

        return new DynamicOrder(
                order.getField(),
                order.getMethod(),
                tableKeyAliasMap.get(order.getTableKey()),
                toAdvancedOrder(order.getAdvancedOrder(),
                                tableKeyAliasMap));
    }

    /**
     * 数据高级排序条件转为高级排序条件
     *
     * @param orders           数据高级排序条件
     * @param tableKeyAliasMap 数据表标识数据表别名映射数据
     * @return 高级排序条件
     */
    public static List<AdvancedOrder> toAdvancedOrder(List<DataSearchAdvancedOrder> orders,
                                                      Map<String, String> tableKeyAliasMap) {
        if (orders == null || orders.size() == 0) return null;

        return orders.stream()
                     .map(order -> new AdvancedOrder(
                             order.getField(),
                             order.getMethod(),
                             tableKeyAliasMap.get(order.getTableKey())))
                     .collect(Collectors.toList());
    }
}
