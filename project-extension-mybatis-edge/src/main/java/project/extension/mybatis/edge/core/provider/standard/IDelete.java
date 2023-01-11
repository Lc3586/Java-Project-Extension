package project.extension.mybatis.edge.core.provider.standard;

import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据删除对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public interface IDelete<T>
        extends IWhereSource<T> {
    /**
     * 追加数据
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param data 数据
     */
    <T2> IDelete<T> setSource(T2 data);

    /**
     * 追加数据
     * <p>必须在实体的主键上添加注解@ColumnSetting(primaryKey = true)</p>
     *
     * @param data 数据
     */
    <T2> IDelete<T> setSource(Collection<T2> data);

    /**
     * 设置临时主键
     *
     * @param fieldNames 用作临时主键的字段名
     */
    IDelete<T> whitTempKey(String... fieldNames);

    /**
     * 设置临时主键
     *
     * @param fieldNames 用作临时主键的字段名
     */
    IDelete<T> whitTempKey(Collection<String> fieldNames);

    /**
     * 指定条件
     *
     * @param sql sql语句
     */
    IDelete<T> where(String sql);

    /**
     * 指定条件
     *
     * @param sql       sql语句
     * @param parameter 参数
     *                  <p>a: 参数名，b: 值</p>
     *                  <p>用法示例一：@id，在sql语句中使用@前缀加名称作为参数（且参数名不区分大小写）</p>
     *                  <p>用法示例二：#{id,javaType=String,jdbcType=VARCHAR}，sql语句中直接使用mybatis语法作为参数</p>
     */
    IDelete<T> where(String sql,
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
    IDelete<T> where(String sql,
                     Map<String, Object> parameter);

    /**
     * 指定条件
     */
    IDelete<T> where(IWhereAction<T, IWhereSource<T>> action);

    /**
     * 指定表名
     *
     * @param tableName 表名
     */
    IDelete<T> asTable(String tableName);

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
    <T2> IDelete<T> asDto(Class<T2> dtoType);

    /**
     * 返回将会执行的sql语句
     * <p>禁用参数化</p>
     *
     * @return Sql语句
     */
    List<String> toSqlWithNoParameter()
            throws
            ModuleException;

    /**
     * 返回将会执行的sql语句
     *
     * @return a: Sql语句, b: 参数
     */
    List<Tuple2<String, Map<String, Object>>> toSql()
            throws
            ModuleException;

    /**
     * 执行sql语句，并返回影响的行数
     *
     * @return 影响的行数
     */
    int executeAffrows()
            throws
            ModuleException;
}
