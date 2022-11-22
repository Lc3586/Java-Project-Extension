package project.extension.mybatis.edge.core.provider.standard;

import project.extension.tuple.Tuple2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据更新对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public interface IUpdate<T>
        extends IWhereSource<T>,
                ISetSource<T> {
    /**
     * 设置源数据
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param data 数据
     */
    <T2> IUpdate<T> setSource(T2 data);

    /**
     * 设置源数据
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param data 数据
     */
    <T2> IUpdate<T> setSource(Collection<T2> data);

    /**
     * 更改指定列的值
     *
     * @param fieldName 字段名
     * @param sql       SQL语句
     */
    IUpdate<T> setWithSql(String fieldName,
                          String sql);

    /**
     * 更改指定列的值
     *
     * @param fieldName 字段名
     * @param sql       SQL语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IUpdate<T> setWithSql(String fieldName,
                          String sql,
                          List<Tuple2<String, Object>> parameter);

    /**
     * 更改指定列的值
     *
     * @param fieldName 字段名
     * @param sql       SQL语句
     * @param parameter 参数
     *                  <p>键: 参数名，值: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IUpdate<T> setWithSql(String fieldName,
                          String sql,
                          Map<String, Object> parameter);

    /**
     * 更改指定列的值
     *
     * @param fieldName 字段名
     * @param value     值
     */
    IUpdate<T> set(String fieldName,
                   Object value);

    /**
     * 更改指定列的值
     *
     * @param fieldName 字段名
     * @param action    操作
     */
    <TMember> IUpdate<T> set(String fieldName,
                             Class<TMember> memberType,
                             ISetAction<T, TMember> action);

    /**
     * 设置临时主键
     *
     * @param fieldNames 用作临时主键的字段名
     */
    IUpdate<T> whitTempKey(String... fieldNames);

    /**
     * 设置临时主键
     *
     * @param fieldNames 用作临时主键的字段名
     */
    IUpdate<T> whitTempKey(Collection<String> fieldNames);

    /**
     * 指定条件
     *
     * @param sql sql语句
     */
    IUpdate<T> where(String sql);

    /**
     * 指定条件
     *
     * @param sql       sql语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IUpdate<T> where(String sql,
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
    IUpdate<T> where(String sql,
                     Map<String, Object> parameter);

    /**
     * 指定条件
     */
    IUpdate<T> where(IWhereAction<T, IWhereSource<T>> action);

    /**
     * 指定字段
     *
     * @param fieldNames 字段名
     */
    IUpdate<T> include(String... fieldNames);

    /**
     * 指定字段
     *
     * @param fieldNames 字段名
     */
    IUpdate<T> include(Collection<String> fieldNames);

    /**
     * 忽略字段
     *
     * @param fieldNames 字段名
     */
    IUpdate<T> ignore(String... fieldNames);

    /**
     * 忽略字段
     *
     * @param fieldNames 字段名
     */
    IUpdate<T> ignore(Collection<String> fieldNames);


    /**
     * 指定表名
     *
     * @param tableName 表名
     */
    IUpdate<T> asTable(String tableName);

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    Class<T> getEntityType();

    /**
     * 指定业务模型
     *
     * @param dtoType 业务模型类型
     * @param <T2>    业务模型类型
     */
    <T2> IUpdate<T> asDto(Class<T2> dtoType);

    /**
     * 指定主标签等级
     *
     * @param level 主标签等级
     */
    IUpdate<T> mainTagLevel(Integer level);

    /**
     * 附加其他标签
     *
     * @param tag 标签
     */
    IUpdate<T> withAnOtherTag(String... tag);

    /**
     * 返回将会执行的sql语句
     * <p>禁用参数化</p>
     *
     * @return Sql语句
     */
    List<String> toSqlWithNoParameter()
            throws
            Exception;

    /**
     * 返回将会执行的sql语句
     *
     * @return a: Sql语句, b: 参数
     */
    List<Tuple2<String, Map<String, Object>>> toSql()
            throws
            Exception;

    /**
     * 执行sql语句，并返回影响的行数
     *
     * @return 影响的行数
     */
    int executeAffrows()
            throws
            Exception;
}
