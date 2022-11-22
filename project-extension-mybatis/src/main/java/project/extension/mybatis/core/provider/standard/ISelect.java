package project.extension.mybatis.core.provider.standard;

import project.extension.mybatis.model.*;
import project.extension.tuple.Tuple2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据查询对象接口
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-30
 */
public interface ISelect<T>
        extends IBaseSelect,
                IWhereSource<T>,
                IOrderBySource<T> {
    /**
     * 指定条件
     *
     * @param sql sql语句
     */
    ISelect<T> where(String sql);

    /**
     * 指定条件
     *
     * @param sql       sql语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    ISelect<T> where(String sql,
                     List<Tuple2<String, Object>> parameter);

    /**
     * 指定条件
     *
     * @param sql       sql语句
     * @param parameter 参数
     *                  <p>键: 参数名，值: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    ISelect<T> where(String sql,
                     Map<String, Object> parameter);

    /**
     * 指定条件
     */
    ISelect<T> where(IWhereAction<T, IWhereSource<T>> action);

    /**
     * 排序
     *
     * @param sql sql语句
     */
    ISelect<T> orderBy(String sql);

    /**
     * 排序
     *
     * @param sql       sql语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    ISelect<T> orderBy(String sql,
                       List<Tuple2<String, Object>> parameter);

    /**
     * 排序
     *
     * @param sql       sql语句
     * @param parameter 参数
     *                  <p>键: 参数名，值: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    ISelect<T> orderBy(String sql,
                       Map<String, Object> parameter);

    /**
     * 排序
     */
    ISelect<T> orderBy(IOrderByAction<T, IOrderBySource<T>> action);

    /**
     * 设置子查询
     * <p>实现SELECT * FROM (SELECT * FROM A)的效果</p>
     *
     * @param sql 子查询语句
     */
    ISelect<T> withSql(String sql);

    /**
     * 设置子查询
     * <p>实现SELECT * FROM (SELECT * FROM A WHERE id = @id)的效果</p>
     *
     * @param sql       子查询语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    ISelect<T> withSql(String sql,
                       List<Tuple2<String, Object>> parameter);

    /**
     * 设置子查询
     * <p>实现SELECT * FROM (SELECT * FROM A WHERE id = @id)的效果</p>
     *
     * @param sql       子查询语句
     * @param parameter 参数
     *                  <p>键: 参数名，值: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    ISelect<T> withSql(String sql,
                       Map<String, Object> parameter);

    /**
     * 指定分组列名
     *
     * @param fieldNames 字段名
     */
    ISelect<T> groupBy(String... fieldNames);

    /**
     * 指定分组列名
     *
     * @param fieldNames 字段名
     */
    ISelect<T> groupBy(Collection<String> fieldNames);

    /**
     * 分页
     *
     * @param pagination 分页设置
     */
    ISelect<T> pagination(Pagination pagination);

    /**
     * 左连接
     *
     * @param tableType 实体类型
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> leftJoin(Class<T2> tableType);

    /**
     * 左连接
     *
     * @param tableType 实体类型
     * @param alias     别名
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> leftJoin(Class<T2> tableType,
                             String alias);

    /**
     * 右连接
     *
     * @param tableType 实体类型
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> rightJoin(Class<T2> tableType);

    /**
     * 右连接
     *
     * @param tableType 实体类型
     * @param alias     别名
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> rightJoin(Class<T2> tableType,
                              String alias);

    /**
     * 加载导航属性
     * <p>必须在实体的主键上添加注解@NavigateSetting</p>
     *
     * @param fieldName 字段名
     */
    ISelect<T> include(String fieldName);

    /**
     * 加载导航属性
     * <p>必须在实体的主键上添加注解@NavigateSetting</p>
     *
     * @param fieldName 字段名
     * @param alias     别名
     */
    ISelect<T> include(String fieldName,
                       String alias);

    /**
     * 加载导航属性
     *
     * @param tableType 实体类型
     * @param sql       sql语句
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> include(Class<T2> tableType,
                            String sql);

    /**
     * 加载导航属性
     *
     * @param tableType 实体类型
     * @param alias     别名
     * @param sql       sql语句
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> include(Class<T2> tableType,
                            String sql,
                            String alias);

    /**
     * 指定条件
     *
     * @param tableType 实体类型
     * @param sql       sql语句
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> where(Class<T2> tableType,
                          String sql);

    /**
     * 指定条件
     *
     * @param alias 别名
     * @param sql   sql语句
     */
    ISelect<T> where(String alias,
                     String sql);

    /**
     * 指定条件
     *
     * @param tableType 实体类型
     * @param filter    动态过滤条件
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> where(Class<T2> tableType,
                          DynamicFilter filter);

    /**
     * 指定条件
     *
     * @param tableType 实体类型
     * @param filters   动态过滤条件
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> where(Class<T2> tableType,
                          List<DynamicFilter> filters);

    /**
     * 指定条件
     *
     * @param alias  别名
     * @param filter 动态过滤条件
     */
    ISelect<T> where(String alias,
                     DynamicFilter filter);

    /**
     * 指定条件
     *
     * @param alias   别名
     * @param filters 动态过滤条件
     */
    ISelect<T> where(String alias,
                     Collection<DynamicFilter> filters);

    /**
     * 设置子查询
     * <p>实现SELECT * FROM (SELECT * FROM A)的效果</p>
     *
     * @param tableType 实体类型
     * @param sql       子查询语句
     * @param <T2>      实体类型
     */
    <T2> ISelect<T> withSql(Class<T2> tableType,
                            String sql);

    /**
     * 设置子查询
     * <p>实现SELECT * FROM (SELECT * FROM A)的效果</p>
     *
     * @param alias 别名
     * @param sql   子查询语句
     */
    ISelect<T> withSql(String alias,
                       String sql);

    /**
     * 是否有记录
     *
     * @return 是否存在
     */
    Boolean any()
            throws
            Exception;

    /**
     * 指定查询列名
     *
     * @param fieldNames 字段名
     */
    ISelect<T> columns(String... fieldNames);

    /**
     * 指定查询列名
     *
     * @param fieldNames 字段名
     */
    ISelect<T> columns(Collection<String> fieldNames);

    /**
     * 返回数据集合
     *
     * @return 数据集合
     */
    List<T> toList()
            throws
            Exception;

    /**
     * 返回数据集合
     *
     * @param dtoType 业务模型类型
     * @param <T2>    业务模型类型
     * @return 数据集合
     */
    <T2> List<T2> toList(Class<T2> dtoType)
            throws
            Exception;

    /**
     * 返回键值对映射数据集合
     *
     * @return 列名和值的集合
     */
    List<Map<String, Object>> toMapList()
            throws
            Exception;

    /**
     * 返回首条数据
     *
     * @return 数据
     */
    T first()
            throws
            Exception;

    /**
     * 返回首条数据
     *
     * @param dtoType 业务模型类型
     * @param <T2>    业务模型类型
     * @return 数据
     */
    <T2> T2 first(Class<T2> dtoType)
            throws
            Exception;

    /**
     * 返回首条数据
     *
     * @param fieldName  字段名
     * @param memberType 成员类型
     * @param <C>        成员类型
     * @return 数据
     */
    <C> C first(String fieldName,
                Class<C> memberType)
            throws
            Exception;

    /**
     * 返回首条键值对映射数据
     *
     * @return 列名和值
     */
    Map<String, Object> firstMap()
            throws
            Exception;

    /**
     * 返回总数
     *
     * @return 总数
     */
    Long count()
            throws
            Exception;

    /**
     * 返回指定列的最大值
     *
     * @param fieldName  字段名
     * @param memberType 成员类型
     * @param <C>        列数据类型
     * @return 最大值
     */
    <C> C max(String fieldName,
              Class<C> memberType)
            throws
            Exception;

    /**
     * 返回指定列的最小值
     *
     * @param fieldName  字段名
     * @param memberType 成员类型
     * @param <C>        列数据类型
     * @return 最小值
     */
    <C> C min(String fieldName,
              Class<C> memberType)
            throws
            Exception;

    /**
     * 返回指定列的平均值
     *
     * @param fieldName  字段名
     * @param memberType 成员类型
     * @param <C>        列数据类型
     * @return 平均值
     */
    <C> C avg(String fieldName,
              Class<C> memberType)
            throws
            Exception;

    /**
     * 返回指定列的合计值
     *
     * @param fieldName  字段名
     * @param memberType 成员类型
     * @param <C>        列数据类型
     * @return 合计值
     */
    <C> C sum(String fieldName,
              Class<C> memberType)
            throws
            Exception;

    /**
     * 指定表名
     *
     * @param tableName 表名
     */
    ISelect<T> asTable(String tableName);

    /**
     * 指定别名
     *
     * @param alias 别名
     */
    ISelect<T> as(String alias);

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    Class<T> getEntityType();

    /**
     * 指定主标签等级
     *
     * @param level 主标签等级
     */
    ISelect<T> mainTagLevel(Integer level);

    /**
     * 附加其他标签
     *
     * @param tag 标签
     */
    ISelect<T> withAnOtherTag(String... tag);

    /**
     * 返回将会执行的sql语句
     * <p>禁用参数化</p>
     *
     * @return Sql语句
     */
    String toSqlWithNoParameter()
            throws
            Exception;

    /**
     * 返回将会执行的sql语句
     *
     * @return a: Sql语句, b: 参数
     */
    Tuple2<String, Map<String, Object>> toSql()
            throws
            Exception;
}
