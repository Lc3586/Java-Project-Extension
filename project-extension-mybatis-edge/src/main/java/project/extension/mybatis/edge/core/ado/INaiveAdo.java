package project.extension.mybatis.edge.core.ado;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.springframework.lang.Nullable;
import project.extension.mybatis.edge.model.NameConvertType;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple2;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 数据库访问对象
 *
 * @author LCTR
 * @date 2022-12-22
 */
public interface INaiveAdo {
    /**
     * 获取SqlSession工厂类
     */
    SqlSessionFactory getSqlSessionFactory();

    /**
     * 获取配置
     */
    Configuration getConfiguration();

    /**
     * 获取数据源
     *
     * @return 数据源
     */
    DataSource getDataSource();

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
     * @param level 事务隔离等级
     * @return Sql会话
     */
    SqlSession getOrCreateSqlSession(TransactionIsolationLevel level)
            throws
            ModuleException;

    /**
     * 获取当前的Sql会话
     *
     * @return Sql会话
     */
    Tuple2<Boolean, SqlSession> currentSqlSession();

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
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    <TParameter> int insert(SqlSession sqlSession,
                            String msId,
                            String script,
                            @Nullable
                                    Class<TParameter> parameterType,
                            Map<String, Object> parameterHashMap,
                            NameConvertType nameConvertType);

    /**
     * 插入记录
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
    <TParameter> int insert(SqlSession sqlSession,
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
}
