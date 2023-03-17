package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import project.extension.func.IFunc0;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据库访问对象
 *
 * @author LCTR
 * @date 2022-12-22
 */
@Repository
public interface INaiveAdo {
    /**
     * 获取解析事务的方法
     *
     * @return 解析事务的方法
     */
    IFunc0<TransactionStatus> getResolveTransaction();

    /**
     * 设置解析事务的方法
     *
     * @param resolveTransaction 解析事务的方法
     */
    void setResolveTransaction(IFunc0<TransactionStatus> resolveTransaction);

    /**
     * 获取SqlSession工厂类
     */
    SqlSessionFactory getSqlSessionFactory();

    /**
     * 获取配置
     */
    Configuration getConfiguration();

    /**
     * 获取数据源名称
     *
     * @return 数据源名称
     */
    String getDataSourceName();

    /**
     * 获取数据源
     *
     * @return 数据源
     */
    DataSource getDataSource();

    /**
     * 是否已开启事务
     *
     * @return true：是
     */
    boolean isTransactionAlreadyExisting()
            throws
            ModuleException;

    /**
     * 获取当前事务
     *
     * @return 当前事务，可能为空
     */
    TransactionStatus currentTransaction()
            throws
            ModuleException;

    /**
     * 开启事务
     *
     * @return 事务
     */
    TransactionStatus beginTransaction()
            throws
            ModuleException;

    /**
     * 开启事务
     *
     * @param isolationLevel 事务隔离等级
     * @return 事务
     */
    TransactionStatus beginTransaction(TransactionIsolationLevel isolationLevel)
            throws
            ModuleException;

    /**
     * 开启事务
     *
     * @param transactionDefinition 事务定义
     * @return 事务
     */
    TransactionStatus beginTransaction(TransactionDefinition transactionDefinition)
            throws
            ModuleException;

    /**
     * 提交事务
     */
    void transactionCommit()
            throws
            ModuleException;

    /**
     * 提交事务
     *
     * @param transactionStatus 事务
     */
    void transactionCommit(TransactionStatus transactionStatus)
            throws
            ModuleException;

    /**
     * 回滚事务
     */
    void transactionRollback()
            throws
            ModuleException;

    /**
     * 回滚事务
     *
     * @param transactionStatus 事务
     */
    void transactionRollback(TransactionStatus transactionStatus)
            throws
            ModuleException;

    /**
     * 事务结束后执行
     *
     * @param remark 备注
     * @param ex     异常
     */
    void triggerAfterTransactionAop(String remark,
                                    Exception ex)
            throws
            ModuleException;

    /**
     * 获取或创建Sql会话
     *
     * @return Sql会话
     */
    SqlSession getOrCreateSqlSession()
            throws
            ModuleException;

    /**
     * 获取或创建Sql会话
     *
     * @param isolationLevel 事务隔离等级
     * @return Sql会话
     */
    SqlSession getOrCreateSqlSession(
            @Nullable
                    TransactionIsolationLevel isolationLevel)
            throws
            ModuleException;

    /**
     * 获取当前的Sql会话
     *
     * @return Sql会话
     */
    Tuple2<Boolean, SqlSession> currentSqlSession();

    /**
     * 提交当前的Sql会话
     */
    void commit()
            throws
            ModuleException;

    /**
     * 提交
     *
     * @param sqlSession Sql会话
     */
    void commit(SqlSession sqlSession)
            throws
            ModuleException;

    /**
     * 撤回当前的Sql会话
     */
    void rollback()
            throws
            ModuleException;

    /**
     * 撤回
     *
     * @param sqlSession Sql会话
     */
    void rollback(SqlSession sqlSession)
            throws
            ModuleException;

    /**
     * 关闭当前的Sql会话
     */
    void close()
            throws
            ModuleException;

    /**
     * 关闭
     *
     * @param sqlSession Sql会话
     */
    void close(SqlSession sqlSession)
            throws
            ModuleException;

    /**
     * 查询单条记录
     *
     * @param sqlSession         指定SQL会话
     * @param msId               MappedStatement标识
     * @param script             脚本
     * @param parameterType      参数类型
     * @param parameterHashMap   参数键值对映射表
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     */
    <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                            String msId,
                                            String script,
                                            @Nullable
                                                    Class<TParameter> parameterType,
                                            Map<String, Object> parameterHashMap,
                                            Class<TResult> resultType,
                                            @Nullable
                                                    Integer resultMainTagLevel,
                                            @Nullable
                                                    Collection<String> resultCustomTags,
                                            NameConvertType nameConvertType);

    /**
     * 查询单条记录
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回值类型
     * @param resultMainTagLevel    返回模型主标签等级
     * @param resultCustomTags      返回模型自定义标签
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     */
    <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                            String msId,
                                            String script,
                                            TParameter parameter,
                                            Class<TParameter> parameterType,
                                            @Nullable
                                                    Integer parameterMainTagLevel,
                                            @Nullable
                                                    Collection<String> parameterCustomTags,
                                            Class<TResult> resultType,
                                            @Nullable
                                                    Integer resultMainTagLevel,
                                            @Nullable
                                                    Collection<String> resultCustomTags,
                                            NameConvertType nameConvertType);

    /**
     * 查询记录集合
     *
     * @param sqlSession         指定SQL会话
     * @param msId               MappedStatement标识
     * @param script             脚本
     * @param parameterType      参数类型
     * @param parameterHashMap   参数键值对映射表
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     */
    <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                   String msId,
                                                   String script,
                                                   @Nullable
                                                           Class<TParameter> parameterType,
                                                   Map<String, Object> parameterHashMap,
                                                   Class<TResult> resultType,
                                                   @Nullable
                                                           Integer resultMainTagLevel,
                                                   @Nullable
                                                           Collection<String> resultCustomTags,
                                                   NameConvertType nameConvertType);

    /**
     * 查询记录集合
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回值类型
     * @param resultMainTagLevel    返回模型主标签等级
     * @param resultCustomTags      返回模型自定义标签
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     */
    <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                   String msId,
                                                   String script,
                                                   TParameter parameter,
                                                   Class<TParameter> parameterType,
                                                   @Nullable
                                                           Integer parameterMainTagLevel,
                                                   @Nullable
                                                           Collection<String> parameterCustomTags,
                                                   Class<TResult> resultType,
                                                   @Nullable
                                                           Integer resultMainTagLevel,
                                                   @Nullable
                                                           Collection<String> resultCustomTags,
                                                   NameConvertType nameConvertType);

    /**
     * 查询单个键值对集合
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回数据类型
     * @param resultFields     返回数据字段集合
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     */
    <TParameter, TResult> Map<String, Object> selectMap(SqlSession sqlSession,
                                                        String msId,
                                                        String script,
                                                        @Nullable
                                                                Class<TParameter> parameterType,
                                                        Map<String, Object> parameterHashMap,
                                                        @Nullable
                                                                Class<TResult> resultType,
                                                        @Nullable
                                                                Collection<String> resultFields,
                                                        NameConvertType nameConvertType);

    /**
     * 查询单个键值对集合
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回数据类型
     * @param resultFields          返回数据字段集合
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     */
    <TParameter, TResult> Map<String, Object> selectMap(SqlSession sqlSession,
                                                        String msId,
                                                        String script,
                                                        TParameter parameter,
                                                        Class<TParameter> parameterType,
                                                        @Nullable
                                                                Integer parameterMainTagLevel,
                                                        @Nullable
                                                                Collection<String> parameterCustomTags,
                                                        @Nullable
                                                                Class<TResult> resultType,
                                                        @Nullable
                                                                Collection<String> resultFields,
                                                        NameConvertType nameConvertType);

    /**
     * 查询多个键值对集合
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回数据类型
     * @param resultFields     返回数据字段集合
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     */
    <TParameter, TResult> List<Map<String, Object>> selectMapList(SqlSession sqlSession,
                                                                  String msId,
                                                                  String script,
                                                                  @Nullable
                                                                          Class<TParameter> parameterType,
                                                                  Map<String, Object> parameterHashMap,
                                                                  @Nullable
                                                                          Class<TResult> resultType,
                                                                  @Nullable
                                                                          Collection<String> resultFields,
                                                                  NameConvertType nameConvertType);

    /**
     * 查询多个键值对集合
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回数据类型
     * @param resultFields          返回数据字段集合
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     */
    <TParameter, TResult> List<Map<String, Object>> selectMapList(SqlSession sqlSession,
                                                                  String msId,
                                                                  String script,
                                                                  TParameter parameter,
                                                                  Class<TParameter> parameterType,
                                                                  @Nullable
                                                                          Integer parameterMainTagLevel,
                                                                  @Nullable
                                                                          Collection<String> parameterCustomTags,
                                                                  @Nullable
                                                                          Class<TResult> resultType,
                                                                  @Nullable
                                                                          Collection<String> resultFields,
                                                                  NameConvertType nameConvertType);

    /**
     * 插入记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param useGeneratedKeys 使用自增列
     * @param keyProperty      主键属性
     * @param keyColumn        主键列
     * @param useSelectKey     从数据库获取主键值
     * @param selectKeyScript  获取主键值的脚本
     * @param selectKeyType    主键值的数据类型
     * @param parameterType    参数类型
     * @param parameterHashMap 全部参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    <TParameter> int insert(SqlSession sqlSession,
                            String msId,
                            String script,
                            @Nullable
                                    boolean useGeneratedKeys,
                            @Nullable
                                    String keyProperty,
                            @Nullable
                                    String keyColumn,
                            @Nullable
                                    boolean useSelectKey,
                            @Nullable
                                    String selectKeyScript,
                            @Nullable
                                    Class<?> selectKeyType,
                            @Nullable
                                    Class<TParameter> parameterType,
                            Map<String, Object> parameterHashMap,
                            Map<String, Field> outParameterHashMap,
                            Map<String, Field> inOutParameterHashMap,
                            NameConvertType nameConvertType);

    /**
     * 插入记录
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param useGeneratedKeys      使用自增列
     * @param keyProperty           主键属性
     * @param keyColumn             主键列
     * @param useSelectKey          从数据库获取主键值
     * @param selectKeyScript       获取主键值的脚本
     * @param selectKeyType         主键值的数据类型
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @return 影响行数
     */
    <TParameter> int insert(SqlSession sqlSession,
                            String msId,
                            String script,
                            @Nullable
                                    boolean useGeneratedKeys,
                            @Nullable
                                    String keyProperty,
                            @Nullable
                                    String keyColumn,
                            @Nullable
                                    boolean useSelectKey,
                            @Nullable
                                    String selectKeyScript,
                            @Nullable
                                    Class<?> selectKeyType,
                            TParameter parameter,
                            Class<TParameter> parameterType,
                            @Nullable
                                    Integer parameterMainTagLevel,
                            @Nullable
                                    Collection<String> parameterCustomTags,
                            NameConvertType nameConvertType);

    /**
     * 更新记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    <TParameter> int update(SqlSession sqlSession,
                            String msId,
                            String script,
                            @Nullable
                                    Class<TParameter> parameterType,
                            Map<String, Object> parameterHashMap,
                            NameConvertType nameConvertType);

    /**
     * 更新记录
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @return 影响行数
     */
    <TParameter> int update(SqlSession sqlSession,
                            String msId,
                            String script,
                            TParameter parameter,
                            Class<TParameter> parameterType,
                            @Nullable
                                    Integer parameterMainTagLevel,
                            @Nullable
                                    Collection<String> parameterCustomTags,
                            NameConvertType nameConvertType);

    /**
     * 删除记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    <TParameter> int delete(SqlSession sqlSession,
                            String msId,
                            String script,
                            @Nullable
                                    Class<TParameter> parameterType,
                            Map<String, Object> parameterHashMap,
                            NameConvertType nameConvertType);

    /**
     * 删除记录
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @return 影响行数
     */
    <TParameter> int delete(SqlSession sqlSession,
                            String msId,
                            String script,
                            TParameter parameter,
                            Class<TParameter> parameterType,
                            @Nullable
                                    Integer parameterMainTagLevel,
                            @Nullable
                                    Collection<String> parameterCustomTags,
                            NameConvertType nameConvertType);

    /**
     * 获取Mapper
     *
     * @param mapperType 类型
     * @param <TMapper>  类型
     */
    <TMapper> TMapper getMapper(Class<TMapper> mapperType);
}
