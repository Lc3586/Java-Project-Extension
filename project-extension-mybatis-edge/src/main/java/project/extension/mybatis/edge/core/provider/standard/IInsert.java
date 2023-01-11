package project.extension.mybatis.edge.core.provider.standard;

import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-03-31
 */
public interface IInsert<T> {
    /**
     * 追加数据
     *
     * @param data 数据
     */
    <T2> IInsert<T> appendData(T2 data);

    /**
     * 追加数据
     *
     * @param data 数据
     */
    <T2> IInsert<T> appendData(Collection<T2> data);

    /**
     * 指定字段
     *
     * @param fieldNames 字段名
     */
    IInsert<T> include(String... fieldNames);

    /**
     * 指定字段
     *
     * @param fieldNames 字段名
     */
    IInsert<T> include(Collection<String> fieldNames);

    /**
     * 忽略字段
     *
     * @param fieldNames 字段名
     */
    IInsert<T> ignore(String... fieldNames);

    /**
     * 忽略字段
     *
     * @param fieldNames 字段名
     */
    IInsert<T> ignore(Collection<String> fieldNames);

    /**
     * 指定表名
     *
     * @param tableName 表名
     */
    IInsert<T> asTable(String tableName);

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
    <T2> IInsert<T> asDto(Class<T2> dtoType);

    /**
     * 指定主标签等级
     *
     * @param level 主标签等级
     */
    IInsert<T> mainTagLevel(Integer level);

    /**
     * 附加其他标签
     *
     * @param tag 标签
     */
    IInsert<T> withAnOtherTag(String... tag);

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
