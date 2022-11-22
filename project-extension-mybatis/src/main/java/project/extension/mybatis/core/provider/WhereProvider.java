package project.extension.mybatis.core.provider;

import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.core.provider.standard.IWhere;
import project.extension.mybatis.core.provider.standard.IWhereAction;
import project.extension.mybatis.core.provider.standard.IWhereSource;
import project.extension.mybatis.model.DynamicFilter;
import project.extension.mybatis.model.FilterCompare;
import project.extension.mybatis.model.FilterGroupRelation;
import project.extension.tuple.Tuple2;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 条件构造器
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-04-04
 */
public class WhereProvider<T>
        implements IWhere<T, IWhereSource<T>> {
    private final IWhereSource<T> source;
    private final List<DynamicFilter> dynamicFilters = new ArrayList<>();
    private final Map<String, Object> parameters = new HashMap<>();

    /**
     * 历史别名
     */
    private String aliasHistory;

    public WhereProvider(IWhereSource<T> source) {
        this.source = source;
    }

    /**
     * 设置别名
     *
     * @param dynamicFilters 条件
     * @param alias          别名
     */
    private void setAlias(List<DynamicFilter> dynamicFilters,
                          String alias) {
        dynamicFilters.forEach(x -> setAlias(x,
                                             alias));
    }

    /**
     * 设置别名
     *
     * @param dynamicFilter 条件
     * @param alias         别名
     */
    private void setAlias(DynamicFilter dynamicFilter,
                          String alias) {
        if (!StringUtils.hasText(dynamicFilter.getAlias()) || (StringUtils.hasText(aliasHistory)
                && dynamicFilter.getAlias()
                                .equals(aliasHistory)))
            dynamicFilter.setAlias(alias);

        if (CollectionsExtension.anyPlus(dynamicFilter.getDynamicFilter()))
            setAlias(dynamicFilter.getDynamicFilter(),
                     alias);
    }

    @Override
    public IWhere<T, IWhereSource<T>> and(String fieldName,
                                          FilterCompare compare,
                                          Object value) {
        dynamicFilters.add(new DynamicFilter(fieldName,
                                             compare,
                                             value));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> andWithTarget(String fieldName,
                                                    FilterCompare compare,
                                                    String targetFieldName) {
        dynamicFilters.add(new DynamicFilter(fieldName,
                                             compare,
                                             targetFieldName,
                                             true));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> and(DynamicFilter filter) {
        filter.setRelation(FilterGroupRelation.AND);
        dynamicFilters.add(filter);
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> and(Collection<DynamicFilter> filters) {
        if (filters.size() > 0)
            dynamicFilters.add(new DynamicFilter(new ArrayList<>(filters)));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> and(IWhereAction<T, IWhereSource<T>> action) {
        IWhere<T, IWhereSource<T>> where = source.newWhere(action);
        if (hasAlias())
            where.setAlias(aliasHistory);
        if (where.getDynamicFilters()
                 .size() > 0)
            dynamicFilters.add(new DynamicFilter(where.getDynamicFilters()));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> and(String sql) {
        dynamicFilters.add(new DynamicFilter(sql));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> and(String sql,
                                          List<Tuple2<String, Object>> parameters) {
        and(sql);
        if (parameters != null && parameters.size() > 0)
            this.parameters.putAll(new HashMap<>(parameters.stream()
                                                           .collect(Collectors.toMap(k -> k.a,
                                                                                     v -> v.b))));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> and(String sql,
                                          Map<String, Object> parameters) {
        and(sql);
        if (parameters != null && parameters.size() > 0)
            this.parameters.putAll(parameters);
        return this;
    }

    @Override
    public <T2, TSource2> IWhere<T, IWhereSource<T>> andOther(IWhere<T2, TSource2> where)
            throws
            Exception {
        if (!where.hasAlias())
            throw new Exception("子条件必须指定别名");
        dynamicFilters.add(new DynamicFilter(where.getDynamicFilters()));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> or(String fieldName,
                                         FilterCompare compare,
                                         Object value) {
        dynamicFilters.add(new DynamicFilter(fieldName,
                                             compare,
                                             value,
                                             FilterGroupRelation.OR));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> orWithTarget(String fieldName,
                                                   FilterCompare compare,
                                                   String targetFieldName) {
        dynamicFilters.add(new DynamicFilter(fieldName,
                                             compare,
                                             targetFieldName,
                                             true,
                                             FilterGroupRelation.OR));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> or(DynamicFilter filter) {
        filter.setRelation(FilterGroupRelation.OR);
        dynamicFilters.add(filter);
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> or(Collection<DynamicFilter> filters) {
        if (filters.size() > 0)
            dynamicFilters.add(new DynamicFilter(new ArrayList<>(filters),
                                                 FilterGroupRelation.OR));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> or(IWhereAction<T, IWhereSource<T>> action) {
        IWhere<T, IWhereSource<T>> where = source.newWhere(action);
        if (hasAlias())
            where.setAlias(aliasHistory);
        if (where.getDynamicFilters()
                 .size() > 0)
            dynamicFilters.add(new DynamicFilter(where.getDynamicFilters(),
                                                 FilterGroupRelation.OR));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> or(String sql) {
        dynamicFilters.add(new DynamicFilter(sql,
                                             FilterGroupRelation.OR));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> or(String sql,
                                         List<Tuple2<String, Object>> parameters) {
        or(sql);
        if (parameters != null && parameters.size() > 0)
            this.parameters.putAll(new HashMap<>(parameters.stream()
                                                           .collect(Collectors.toMap(k -> k.a,
                                                                                     v -> v.b))));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> or(String sql,
                                         Map<String, Object> parameters) {
        or(sql);
        if (parameters != null && parameters.size() > 0)
            this.parameters.putAll(parameters);
        return this;
    }

    @Override
    public <T2, TSource2> IWhere<T, IWhereSource<T>> orOther(IWhere<T2, TSource2> where)
            throws
            Exception {
        if (!where.hasAlias())
            throw new Exception("子条件必须指定别名");
        dynamicFilters.add(new DynamicFilter(where.getDynamicFilters(),
                                             FilterGroupRelation.OR));
        return this;
    }

    @Override
    public IWhere<T, IWhereSource<T>> setAlias(String alias) {
        setAlias(dynamicFilters,
                 alias);
        aliasHistory = alias;
        return this;
    }

    @Override
    public boolean hasAlias() {
        return StringUtils.hasText(aliasHistory);
    }

    @Override
    public List<DynamicFilter> getDynamicFilters() {
        return dynamicFilters;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }
}
