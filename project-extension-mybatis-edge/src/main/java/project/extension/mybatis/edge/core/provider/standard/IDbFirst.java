package project.extension.mybatis.edge.core.provider.standard;

import project.extension.mybatis.edge.model.DbColumnInfo;
import project.extension.mybatis.edge.model.DbTableInfo;
import project.extension.standard.exception.ModuleException;

import java.sql.JDBCType;
import java.util.List;

/**
 * DbFirst 开发模式相关功能接口类
 *
 * @author LCTR
 * @date 2022-06-10
 */
public interface IDbFirst {
    /**
     * 所有操作是否在由springframework管理的事务下运行
     * <p>如果是 需要在方法上添加此注解 org.springframework.transaction.annotation.Transactional</p>
     *
     * @param withTransactional 所有操作是否在由springframework管理的事务下运行
     */
    IDbFirst withTransactional(boolean withTransactional);

    /**
     * 获取所有数据库
     *
     * @return 所有的数据库名称
     */
    List<String> getDatabases()
            throws
            ModuleException;

    /**
     * 获取指定数据库的表信息，包括表、列详情、主键、唯一键、索引、外键、备注
     *
     * @param database 指定数据库
     * @return 数据库表信息集合
     */
    List<DbTableInfo> getTablesByDatabase(String... database)
            throws
            ModuleException;

    /**
     * 获取指定单表信息，包括列详情、主键、唯一键、索引、备注
     *
     * @param name       表名，如：dbo.table1
     * @param ignoreCase 是否忽略大小写
     * @return 数据库表信息
     */
    DbTableInfo getTableByName(String name,
                               boolean ignoreCase)
            throws
            ModuleException;

    /**
     * 判断表是否存在
     *
     * @param name       表名，如：dbo.table1
     * @param ignoreCase 是否忽略大小写
     * @return 指定表是否存在
     */
    boolean existsTable(String name,
                        boolean ignoreCase)
            throws
            ModuleException;

    /**
     * 获取列对应的JDBC类型
     *
     * @param column 列信息
     * @return JDBC类型
     */
    JDBCType getJDBCType(DbColumnInfo column);

    /**
     * 获取列对应的java类型强制转换
     * <p>示例返回值：(int), (long)</p>
     *
     * @param column 列信息
     * @return java类型强制转换
     */
    String getJavaTypeConvert(DbColumnInfo column);

    /**
     * 获取列对应的java基本类型
     * <p>示例返回值：int, long</p>
     *
     * @param column 列信息
     * @return java基本类型
     */
    String getJavaType(DbColumnInfo column);

    /**
     * 获取列对应的java包装类型
     * <p>示例返回值：Integer, Long</p>
     *
     * @param column 列信息
     * @return java包装类型
     */
    String getJavaPackageType(DbColumnInfo column);

    /**
     * 获取列对应的java基本类型对象
     * <p>示例返回值：int.class, long.class</p>
     *
     * @param column 列信息
     * @return java类型对象
     */
    Class<?> getJavaTypeInfo(DbColumnInfo column);

    /**
     * 获取列对应的java包装类型对象
     * <p>示例返回值：Integer.class, Long.class</p>
     *
     * @param column 列信息
     * @return java类型对象
     */
    Class<?> getJavaPackageTypeInfo(DbColumnInfo column);

    /**
     * 获取列对应的java字符串化语句
     * <p>示例返回值：Integer.toString(%s), Long.toString(%s)</p>
     *
     * @param column 列信息
     * @return 字符串化语句
     */
    String getJavaStringify(DbColumnInfo column);

    /**
     * 获取列对应的java类型转换语句
     * <p>示例返回值：Integer.parseInt(%s), Long.parseLong(%s)</p>
     *
     * @param column 列信息
     * @return 字符串转换语句
     */
    String getJavaParse(DbColumnInfo column);
}
