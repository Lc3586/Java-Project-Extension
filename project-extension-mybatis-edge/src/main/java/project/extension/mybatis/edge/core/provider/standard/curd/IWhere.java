package project.extension.mybatis.edge.core.provider.standard.curd;

import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.mybatis.edge.model.DynamicFilter;
import project.extension.tuple.Tuple2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 条件对象
 *
 * @param <T>       实体类型
 * @param <TSource> 源对象类型
 * @author LCTR
 * @date 2022-04-04
 */
public interface IWhere<T, TSource> {
    /**
     * 并且
     *
     * @param fieldName 字段名
     * @param compare   比较方式
     * @param value     值
     */
    IWhere<T, TSource> and(String fieldName,
                           FilterCompare compare,
                           Object value);

    /**
     * 并且
     *
     * @param fieldName       字段名
     * @param compare         比较方式
     * @param targetFieldName 要比较的目标字段名
     */
    IWhere<T, TSource> andWithTarget(String fieldName,
                                     FilterCompare compare,
                                     String targetFieldName);

    /**
     * 并且
     *
     * @param filter 动态过滤条件
     */
    IWhere<T, TSource> and(DynamicFilter filter);

    /**
     * 并且
     *
     * @param filters 动态过滤条件
     */
    IWhere<T, TSource> and(Collection<DynamicFilter> filters);

    /**
     * 并且
     *
     * @param action 子条件
     */
    IWhere<T, TSource> and(IWhereAction<T, TSource> action);

    /**
     * 并且
     * <p>实现id = '123'的效果</p>
     *
     * @param sql 子查询语句
     */
    IWhere<T, TSource> and(String sql);

    /**
     * 并且
     * <p>实现id = @id的效果
     *
     * @param sql       条件语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IWhere<T, TSource> and(String sql,
                           List<Tuple2<String, Object>> parameter);

    /**
     * 并且
     * <p>实现id = @id的效果
     *
     * @param sql       条件语句
     * @param parameter 参数
     *                  <p>键: 参数名，值: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IWhere<T, TSource> and(String sql,
                           Map<String, Object> parameter);

    /**
     * 并且
     *
     * @param where 子条件
     * @param <T2>  另一个表的实体类型
     */
    <T2, TSource2> IWhere<T, TSource> andOther(IWhere<T2, TSource2> where)
            throws
            Exception;

    /**
     * 或
     *
     * @param fieldName 字段名
     * @param compare   比较方式
     * @param value     值
     */
    IWhere<T, TSource> or(String fieldName,
                          FilterCompare compare,
                          Object value);

    /**
     * 并且
     *
     * @param fieldName       字段名
     * @param compare         比较方式
     * @param targetFieldName 要比较的目标字段名
     */
    IWhere<T, TSource> orWithTarget(String fieldName,
                                    FilterCompare compare,
                                    String targetFieldName);

    /**
     * 或
     *
     * @param filter 动态过滤条件
     */
    IWhere<T, TSource> or(DynamicFilter filter);

    /**
     * 或
     *
     * @param filters 动态过滤条件
     */
    IWhere<T, TSource> or(Collection<DynamicFilter> filters);

    /**
     * 或
     *
     * @param action 子条件
     */
    IWhere<T, TSource> or(IWhereAction<T, IWhereSource<T>> action);

    /**
     * 或
     * <p>实现id = '123'的效果</p>
     *
     * @param sql 子查询语句
     */
    IWhere<T, TSource> or(String sql);

    /**
     * 或
     * <p>实现id = @id的效果
     *
     * @param sql       条件语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IWhere<T, TSource> or(String sql,
                          List<Tuple2<String, Object>> parameter);

    /**
     * 或
     * <p>实现id = @id的效果
     *
     * @param sql       条件语句
     * @param parameter 参数
     *                  <p>键: 参数名，值: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IWhere<T, TSource> or(String sql,
                          Map<String, Object> parameter);

    /**
     * 或
     *
     * @param where 子条件
     * @param <T2>  另一个表的实体类型
     */
    <T2, TSource2> IWhere<T, TSource> orOther(IWhere<T2, TSource2> where)
            throws
            Exception;

    /**
     * 指定别名
     *
     * @param alias 别名
     */
    IWhere<T, TSource> setAlias(String alias);

    /**
     * 是否设置了别名
     */
    boolean hasAlias();

    /**
     * 获取动态条件
     *
     * @return 动态条件
     */
    List<DynamicFilter> getDynamicFilters();

    /**
     * 获取参数
     *
     * @return 参数
     */
    Map<String, Object> getParameters();
}
