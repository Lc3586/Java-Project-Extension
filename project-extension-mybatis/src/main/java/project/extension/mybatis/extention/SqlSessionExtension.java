package project.extension.mybatis.extention;

import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.SqlSession;
import project.extension.mybatis.core.driver.NaiveSqlSession;
import project.extension.mybatis.model.NameConvertType;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sql会话拓展方法
 *
 * @author LCTR
 * @date 2022-03-26
 */
public class SqlSessionExtension {
    /**
     * 查询单条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> TResult selectOne(SqlSession sqlSession,
                                                             String msId,
                                                             String script,
                                                             Map<String, V> parameterHashMap,
                                                             Class<TResult> resultType,
                                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterHashMap,
                                             resultType,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameterHashMap);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession         指定SQL会话
     * @param msId               MappedStatement标识
     * @param script             脚本
     * @param parameterHashMap   参数键值对映射表
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> TResult selectOne(SqlSession sqlSession,
                                                             String msId,
                                                             String script,
                                                             Map<String, V> parameterHashMap,
                                                             Class<TResult> resultType,
                                                             int resultMainTagLevel,
                                                             Collection<String> resultCustomTags,
                                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterHashMap,
                                             resultType,
                                             resultMainTagLevel,
                                             resultCustomTags,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameterHashMap);
    }

    /**
     * 查询单条记录
     *
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return 数据
     */
    public static <TParameter, TResult> TResult selectOne(String msId,
                                                          String script,
                                                          TParameter parameter,
                                                          Class<TParameter> parameterType,
                                                          Class<TResult> resultType,
                                                          boolean noParameterMap,
                                                          NameConvertType nameConvertType)
            throws
            Exception {
        return selectOne(null,
                         msId,
                         script,
                         parameter,
                         parameterType,
                         resultType,
                         noParameterMap,
                         nameConvertType);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return 数据
     */
    public static <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                          String msId,
                                                          String script,
                                                          TParameter parameter,
                                                          Class<TParameter> parameterType,
                                                          Class<TResult> resultType,
                                                          boolean noParameterMap,
                                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             resultType,
                                             noParameterMap,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

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
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     * @return 数据
     */
    public static <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                          String msId,
                                                          String script,
                                                          TParameter parameter,
                                                          Class<TParameter> parameterType,
                                                          int parameterMainTagLevel,
                                                          Collection<String> parameterCustomTags,
                                                          Class<TResult> resultType,
                                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterMainTagLevel,
                                             parameterCustomTags,
                                             resultType,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession         指定SQL会话
     * @param msId               MappedStatement标识
     * @param script             脚本
     * @param parameter          参数
     * @param parameterType      参数类型
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param noParameterMap     无参数映射
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     * @return 数据
     */
    public static <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                          String msId,
                                                          String script,
                                                          TParameter parameter,
                                                          Class<TParameter> parameterType,
                                                          Class<TResult> resultType,
                                                          int resultMainTagLevel,
                                                          Collection<String> resultCustomTags,
                                                          boolean noParameterMap,
                                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             resultType,
                                             resultMainTagLevel,
                                             resultCustomTags,
                                             noParameterMap,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

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
     * @return 数据
     */
    public static <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                          String msId,
                                                          String script,
                                                          TParameter parameter,
                                                          Class<TParameter> parameterType,
                                                          int parameterMainTagLevel,
                                                          Collection<String> parameterCustomTags,
                                                          Class<TResult> resultType,
                                                          int resultMainTagLevel,
                                                          Collection<String> resultCustomTags,
                                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterMainTagLevel,
                                             parameterCustomTags,
                                             resultType,
                                             resultMainTagLevel,
                                             resultCustomTags,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> TResult selectOne(SqlSession sqlSession,
                                                             String msId,
                                                             String script,
                                                             TParameter parameter,
                                                             Class<TParameter> parameterType,
                                                             Map<String, V> parameterHashMap,
                                                             Class<TResult> resultType,
                                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterHashMap,
                                             resultType,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param resultHashMap   返回数据键值对映射表
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> TResult selectOne(SqlSession sqlSession,
                                                             String msId,
                                                             String script,
                                                             TParameter parameter,
                                                             Class<TParameter> parameterType,
                                                             Class<TResult> resultType,
                                                             Map<String, V> resultHashMap,
                                                             boolean noParameterMap,
                                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             resultType,
                                             resultHashMap,
                                             noParameterMap,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param resultHashMap    返回数据键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, VParameter, VResult> TResult selectOne(SqlSession sqlSession,
                                                                               String msId,
                                                                               String script,
                                                                               TParameter parameter,
                                                                               Class<TParameter> parameterType,
                                                                               Map<String, VParameter> parameterHashMap,
                                                                               Class<TResult> resultType,
                                                                               Map<String, VResult> resultHashMap,
                                                                               NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterHashMap,
                                             resultType,
                                             resultHashMap,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession   指定SQL会话
     * @param msId         MappedStatement标识
     * @param parameter    参数
     * @param <TParameter> 参数类型
     * @param <TResult>    返回值类型
     * @return 数据
     */
    public static <TParameter, TResult> TResult selectOne(SqlSession sqlSession,
                                                          String msId,
                                                          TParameter parameter) {
        SqlSession useSqlSession = sqlSession == null
                                   ? NaiveSqlSession.current()
                                   : sqlSession;
        try {
            TResult result = useSqlSession.selectOne(msId,
                                                     parameter);
            if (sqlSession == null) NaiveSqlSession.done(useSqlSession);
            return result;
        } catch (Exception ex) {
            if (sqlSession == null) NaiveSqlSession.error(useSqlSession);
            throw ex;
        }
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 数据
     */
    public static <TParameter, V> Map<String, Object> selectMap(SqlSession sqlSession,
                                                                String msId,
                                                                String script,
                                                                Map<String, V> parameterHashMap,
                                                                NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterHashMap,
                                             HashMap.class,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameterHashMap);
    }

    /**
     * 查询单条记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @return 数据
     */
    public static <TParameter, V> Map<String, Object> selectMap(SqlSession sqlSession,
                                                                String msId,
                                                                String script,
                                                                TParameter parameter,
                                                                Class<TParameter> parameterType,
                                                                boolean noParameterMap,
                                                                NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             HashMap.class,
                                             noParameterMap,
                                             nameConvertType);
        return selectOne(sqlSession,
                         msId,
                         parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 数据
     */
    public static <TParameter, V> List<Map<String, Object>> selectMapList(SqlSession sqlSession,
                                                                          String msId,
                                                                          String script,
                                                                          Map<String, V> parameterHashMap,
                                                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterHashMap,
                                             HashMap.class,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameterHashMap);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @return 数据集合
     */
    public static <TParameter> List<Map<String, Object>> selectMapList(SqlSession sqlSession,
                                                                       String msId,
                                                                       String script,
                                                                       TParameter parameter,
                                                                       Class<TParameter> parameterType,
                                                                       boolean noParameterMap,
                                                                       NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             HashMap.class,
                                             noParameterMap,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> List<TResult> selectList(SqlSession sqlSession,
                                                                    String msId,
                                                                    String script,
                                                                    Map<String, V> parameterHashMap,
                                                                    Class<TResult> resultType,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterHashMap,
                                             resultType,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameterHashMap);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession         指定SQL会话
     * @param msId               MappedStatement标识
     * @param script             脚本
     * @param parameterHashMap   参数键值对映射表
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> List<TResult> selectList(SqlSession sqlSession,
                                                                    String msId,
                                                                    String script,
                                                                    Map<String, V> parameterHashMap,
                                                                    Class<TResult> resultType,
                                                                    int resultMainTagLevel,
                                                                    Collection<String> resultCustomTags,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterHashMap,
                                             resultType,
                                             resultMainTagLevel,
                                             resultCustomTags,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameterHashMap);
    }

    /**
     * 查询多条记录
     *
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return 数据集合
     */
    public static <TParameter, TResult> List<TResult> selectList(String msId,
                                                                 String script,
                                                                 TParameter parameter,
                                                                 Class<TParameter> parameterType,
                                                                 Class<TResult> resultType,
                                                                 boolean noParameterMap,
                                                                 NameConvertType nameConvertType)
            throws
            Exception {
        return selectList(null,
                          msId,
                          script,
                          parameter,
                          parameterType,
                          resultType,
                          noParameterMap,
                          nameConvertType);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return 数据集合
     */
    public static <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                                 String msId,
                                                                 String script,
                                                                 TParameter parameter,
                                                                 Class<TParameter> parameterType,
                                                                 Class<TResult> resultType,
                                                                 boolean noParameterMap,
                                                                 NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             resultType,
                                             noParameterMap,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession            指定SQL会话
     * @param msId                  MappedStatement标识
     * @param script                脚本
     * @param parameter             参数
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回值类型
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     * @return 数据
     */
    public static <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                                 String msId,
                                                                 String script,
                                                                 TParameter parameter,
                                                                 Class<TParameter> parameterType,
                                                                 int parameterMainTagLevel,
                                                                 Collection<String> parameterCustomTags,
                                                                 Class<TResult> resultType,
                                                                 NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterMainTagLevel,
                                             parameterCustomTags,
                                             resultType,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession         指定SQL会话
     * @param msId               MappedStatement标识
     * @param script             脚本
     * @param parameter          参数
     * @param parameterType      参数类型
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param noParameterMap     无参数映射
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     * @return 数据
     */
    public static <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                                 String msId,
                                                                 String script,
                                                                 TParameter parameter,
                                                                 Class<TParameter> parameterType,
                                                                 Class<TResult> resultType,
                                                                 int resultMainTagLevel,
                                                                 Collection<String> resultCustomTags,
                                                                 boolean noParameterMap,
                                                                 NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             resultType,
                                             resultMainTagLevel,
                                             resultCustomTags,
                                             noParameterMap,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
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
     * @return 数据
     */
    public static <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                                 String msId,
                                                                 String script,
                                                                 TParameter parameter,
                                                                 Class<TParameter> parameterType,
                                                                 int parameterMainTagLevel,
                                                                 Collection<String> parameterCustomTags,
                                                                 Class<TResult> resultType,
                                                                 int resultMainTagLevel,
                                                                 Collection<String> resultCustomTags,
                                                                 NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterMainTagLevel,
                                             parameterCustomTags,
                                             resultType,
                                             resultMainTagLevel,
                                             resultCustomTags,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> List<TResult> selectList(SqlSession sqlSession,
                                                                    String msId,
                                                                    String script,
                                                                    TParameter parameter,
                                                                    Class<TParameter> parameterType,
                                                                    Map<String, V> parameterHashMap,
                                                                    Class<TResult> resultType,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterHashMap,
                                             resultType,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param resultHashMap   返回数据键值对映射表
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, V> List<TResult> selectList(SqlSession sqlSession,
                                                                    String msId,
                                                                    String script,
                                                                    TParameter parameter,
                                                                    Class<TParameter> parameterType,
                                                                    Class<TResult> resultType,
                                                                    Map<String, V> resultHashMap,
                                                                    boolean noParameterMap,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             resultType,
                                             resultHashMap,
                                             noParameterMap,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param resultHashMap    返回数据键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return 数据
     */
    public static <TParameter, TResult, VParameter, VResult> List<TResult> selectList(SqlSession sqlSession,
                                                                                      String msId,
                                                                                      String script,
                                                                                      TParameter parameter,
                                                                                      Class<TParameter> parameterType,
                                                                                      Map<String, VParameter> parameterHashMap,
                                                                                      Class<TResult> resultType,
                                                                                      Map<String, VResult> resultHashMap,
                                                                                      NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.SELECT,
                                             parameterType,
                                             parameterHashMap,
                                             resultType,
                                             resultHashMap,
                                             nameConvertType);
        return selectList(sqlSession,
                          msId,
                          parameter);
    }

    /**
     * 查询多条记录
     *
     * @param sqlSession   指定SQL会话
     * @param msId         MappedStatement标识
     * @param parameter    参数
     * @param <TParameter> 参数类型
     * @param <TResult>    返回值类型
     * @return 数据集合
     */
    public static <TParameter, TResult> List<TResult> selectList(SqlSession sqlSession,
                                                                 String msId,
                                                                 TParameter parameter) {
        SqlSession useSqlSession = sqlSession == null
                                   ? NaiveSqlSession.current()
                                   : sqlSession;
        try {
            List<TResult> list = useSqlSession.selectList(msId,
                                                          parameter);
            if (sqlSession == null) NaiveSqlSession.done(useSqlSession);
            return list;
        } catch (Exception ex) {
            if (sqlSession == null) NaiveSqlSession.error(useSqlSession);
            throw ex;
        }
    }

    /**
     * 插入记录
     *
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int insert(String msId,
                                             String script,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        return insert(null,
                      msId,
                      script,
                      parameterHashMap,
                      nameConvertType);
    }

    /**
     * 插入记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int insert(SqlSession sqlSession,
                                             String msId,
                                             String script,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.INSERT,
                                             parameterHashMap,
                                             null,
                                             nameConvertType);
        return insert(sqlSession,
                      msId,
                      parameterHashMap);
    }

    /**
     * 插入多条记录
     *
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @return 影响行数
     */
    public static <TParameter> int insert(String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          boolean noParameterMap,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.INSERT,
                                             parameterType,
                                             null,
                                             noParameterMap,
                                             nameConvertType);
        return insert(null,
                      msId,
                      parameter);
    }

    /**
     * 插入多条记录
     *
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
    public static <TParameter> int insert(String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          int parameterMainTagLevel,
                                          Collection<String> parameterCustomTags,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        return insert(null,
                      msId,
                      script,
                      parameter,
                      parameterType,
                      parameterMainTagLevel,
                      parameterCustomTags,
                      nameConvertType);
    }

    /**
     * 插入多条记录
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
    public static <TParameter> int insert(SqlSession sqlSession,
                                          String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          int parameterMainTagLevel,
                                          Collection<String> parameterCustomTags,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.INSERT,
                                             parameterType,
                                             parameterMainTagLevel,
                                             parameterCustomTags,
                                             null,
                                             nameConvertType);
        return insert(sqlSession,
                      msId,
                      parameter);
    }

    /**
     * 插入多条记录
     *
     * @param sqlSession 指定SQL会话
     * @param msId       MappedStatement标识
     * @param parameter  参数
     * @return 影响行数
     */
    public static int insert(SqlSession sqlSession,
                             String msId,
                             Object parameter) {
        SqlSession useSqlSession = sqlSession == null
                                   ? NaiveSqlSession.current()
                                   : sqlSession;
        try {
            int rows = useSqlSession.insert(msId,
                                            parameter);
            if (sqlSession == null) NaiveSqlSession.done(useSqlSession,
                                                         true);
            return rows;
        } catch (Exception ex) {
            if (sqlSession == null) NaiveSqlSession.error(useSqlSession);
            throw ex;
        }
    }

    /**
     * 更新记录
     *
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int update(String msId,
                                             String script,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        return update(null,
                      msId,
                      script,
                      parameterHashMap,
                      nameConvertType);
    }

    /**
     * 更新记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int update(SqlSession sqlSession,
                                             String msId,
                                             String script,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.UPDATE,
                                             parameterHashMap,
                                             null,
                                             nameConvertType);
        return update(sqlSession,
                      msId,
                      parameterHashMap);
    }

    /**
     * 更新记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @return 影响行数
     */
    public static <TParameter> int update(SqlSession sqlSession,
                                          String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          boolean noParameterMap,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.UPDATE,
                                             parameterType,
                                             null,
                                             noParameterMap,
                                             nameConvertType);
        return update(sqlSession,
                      msId,
                      parameter);
    }

    /**
     * 更新记录
     *
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
    public static <TParameter> int update(String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          int parameterMainTagLevel,
                                          Collection<String> parameterCustomTags,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        return update(null,
                      msId,
                      script,
                      parameter,
                      parameterType,
                      parameterMainTagLevel,
                      parameterCustomTags,
                      nameConvertType);
    }

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
    public static <TParameter> int update(SqlSession sqlSession,
                                          String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          int parameterMainTagLevel,
                                          Collection<String> parameterCustomTags,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.UPDATE,
                                             parameterType,
                                             parameterMainTagLevel,
                                             parameterCustomTags,
                                             null,
                                             nameConvertType);
        return update(sqlSession,
                      msId,
                      parameter);
    }

    /**
     * 更新记录
     *
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int update(String msId,
                                             String script,
                                             Object parameter,
                                             Class<TParameter> parameterType,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        return update(null,
                      msId,
                      script,
                      parameter,
                      parameterType,
                      parameterHashMap,
                      nameConvertType);
    }

    /**
     * 更新记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int update(SqlSession sqlSession,
                                             String msId,
                                             String script,
                                             Object parameter,
                                             Class<TParameter> parameterType,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.UPDATE,
                                             parameterType,
                                             parameterHashMap,
                                             null,
                                             nameConvertType);
        return update(sqlSession,
                      msId,
                      parameter);
    }

    /**
     * 更新记录
     *
     * @param sqlSession 指定SQL会话
     * @param msId       MappedStatement标识
     * @param parameter  参数
     * @return 影响行数
     */
    public static int update(SqlSession sqlSession,
                             String msId,
                             Object parameter) {
        SqlSession useSqlSession = sqlSession == null
                                   ? NaiveSqlSession.current()
                                   : sqlSession;
        try {
            int rows = useSqlSession.update(msId,
                                            parameter);
            if (sqlSession == null) NaiveSqlSession.done(useSqlSession,
                                                         true);
            return rows;
        } catch (Exception ex) {
            if (sqlSession == null) NaiveSqlSession.error(useSqlSession);
            throw ex;
        }
    }

    /**
     * 删除记录
     *
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int delete(String msId,
                                             String script,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        return delete(null,
                      msId,
                      script,
                      parameterHashMap,
                      nameConvertType);
    }

    /**
     * 删除记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int delete(SqlSession sqlSession,
                                             String msId,
                                             String script,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.DELETE,
                                             parameterHashMap,
                                             null,
                                             nameConvertType);
        return delete(sqlSession,
                      msId,
                      parameterHashMap);
    }

    /**
     * 删除记录
     *
     * @param sqlSession      指定SQL会话
     * @param msId            MappedStatement标识
     * @param script          脚本
     * @param parameter       参数
     * @param parameterType   参数类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @return 影响行数
     */
    public static <TParameter> int delete(SqlSession sqlSession,
                                          String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          boolean noParameterMap,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.DELETE,
                                             parameterType,
                                             null,
                                             noParameterMap,
                                             nameConvertType);
        return delete(sqlSession,
                      msId,
                      parameter);
    }

    /**
     * 删除记录
     *
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
    public static <TParameter> int delete(String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          int parameterMainTagLevel,
                                          Collection<String> parameterCustomTags,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        return delete(null,
                      msId,
                      script,
                      parameter,
                      parameterType,
                      parameterMainTagLevel,
                      parameterCustomTags,
                      nameConvertType);
    }

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
    public static <TParameter> int delete(SqlSession sqlSession,
                                          String msId,
                                          String script,
                                          Object parameter,
                                          Class<TParameter> parameterType,
                                          int parameterMainTagLevel,
                                          Collection<String> parameterCustomTags,
                                          NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.DELETE,
                                             parameterType,
                                             parameterMainTagLevel,
                                             parameterCustomTags,
                                             null,
                                             nameConvertType);
        return delete(sqlSession,
                      msId,
                      parameter);
    }

    /**
     * 删除记录
     *
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int delete(String msId,
                                             String script,
                                             Object parameter,
                                             Class<TParameter> parameterType,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        return delete(null,
                      msId,
                      script,
                      parameter,
                      parameterType,
                      parameterHashMap,
                      nameConvertType);
    }

    /**
     * 删除记录
     *
     * @param sqlSession       指定SQL会话
     * @param msId             MappedStatement标识
     * @param script           脚本
     * @param parameter        参数
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @return 影响行数
     */
    public static <TParameter, V> int delete(SqlSession sqlSession,
                                             String msId,
                                             String script,
                                             Object parameter,
                                             Class<TParameter> parameterType,
                                             Map<String, V> parameterHashMap,
                                             NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatementExtension.getOrCreate(NaiveSqlSession.getConfiguration(),
                                             msId,
                                             script,
                                             SqlCommandType.DELETE,
                                             parameterType,
                                             parameterHashMap,
                                             null,
                                             nameConvertType);
        return delete(sqlSession,
                      msId,
                      parameter);
    }

    /**
     * 删除记录
     *
     * @param sqlSession 指定SQL会话
     * @param msId       MappedStatement标识
     * @param parameter  参数
     * @return 影响行数
     */
    public static int delete(SqlSession sqlSession,
                             String msId,
                             Object parameter) {
        SqlSession useSqlSession = sqlSession == null
                                   ? NaiveSqlSession.current()
                                   : sqlSession;
        try {
            int rows = useSqlSession.delete(msId,
                                            parameter);
            if (sqlSession == null) NaiveSqlSession.done(useSqlSession,
                                                         true);
            return rows;
        } catch (Exception ex) {
            if (sqlSession == null) NaiveSqlSession.error(useSqlSession);
            throw ex;
        }
    }
}
