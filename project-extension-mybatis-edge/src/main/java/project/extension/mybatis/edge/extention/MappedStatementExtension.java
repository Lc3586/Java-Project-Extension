package project.extension.mybatis.edge.extention;

import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import project.extension.mybatis.edge.core.provider.normal.NaiveAopProvider;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.MappedStatementArgs;
import project.extension.mybatis.edge.model.NameConvertType;

import java.util.*;

/**
 * MappedStatement对象拓展方法
 *
 * @author LCTR
 * @date 2022-03-29
 */
@org.springframework.context.annotation.Configuration
public class MappedStatementExtension {
    public MappedStatementExtension(INaiveAop aop) {
        MappedStatementExtension.aop = (NaiveAopProvider) aop;
    }

    private static NaiveAopProvider aop;

    /**
     * 是否存在
     *
     * @param configuration 配置
     * @param msId          标识
     */
    public static boolean exist(Configuration configuration,
                                String msId) {
        return configuration.getMappedStatementNames()
                            .contains(msId);
    }

    /**
     * 获取
     *
     * @param configuration 配置
     * @param msId          标识
     * @return MappedStatement
     */
    public static MappedStatement get(Configuration configuration,
                                      String msId) {

        if (exist(configuration,
                  msId)) {
            return aop.mappedStatement(new MappedStatementArgs(true,
                                                               configuration.getMappedStatement(msId)))
                      .getMappedStatement();
        } else return null;
    }

    /**
     * 创建
     *
     * @param configuration   配置
     * @param msId            标识
     * @param script          脚本
     * @param sqlCommandType  查询语句类型
     * @param parameterType   参数类型
     * @param resultType      返回数据类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement create(Configuration configuration,
                                                               String msId,
                                                               String script,
                                                               SqlCommandType sqlCommandType,
                                                               Class<TParameter> parameterType,
                                                               Class<TResult> resultType,
                                                               boolean noParameterMap,
                                                               NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          noParameterMap
                          ? null
                          : MapBuilderExtension.getParameterMap(configuration,
                                                                msId,
                                                                parameterType,
                                                                0,
                                                                null),
                          MapBuilderExtension.getResultMap(configuration,
                                                           msId,
                                                           resultType,
                                                           0,
                                                           null,
                                                           false,
                                                           nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration         配置
     * @param msId                  标识
     * @param script                脚本
     * @param sqlCommandType        查询语句类型
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回数据类型
     * @param resultMainTagLevel    返回模型主标签等级
     * @param resultCustomTags      返回模型自定义标签
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement create(Configuration configuration,
                                                               String msId,
                                                               String script,
                                                               SqlCommandType sqlCommandType,
                                                               Class<TParameter> parameterType,
                                                               int parameterMainTagLevel,
                                                               Collection<String> parameterCustomTags,
                                                               Class<TResult> resultType,
                                                               int resultMainTagLevel,
                                                               Collection<String> resultCustomTags,
                                                               NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          MapBuilderExtension.getParameterMap(configuration,
                                                              msId,
                                                              parameterType,
                                                              parameterMainTagLevel,
                                                              parameterCustomTags),
                          MapBuilderExtension.getResultMap(configuration,
                                                           msId,
                                                           resultType,
                                                           resultMainTagLevel,
                                                           resultCustomTags,
                                                           false,
                                                           nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回数据类型
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, VParameter, VResult> MappedStatement create(Configuration configuration,
                                                                                    String msId,
                                                                                    String script,
                                                                                    SqlCommandType sqlCommandType,
                                                                                    Map<String, VParameter> parameterHashMap,
                                                                                    Class<TResult> resultType,
                                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterHashMap.getClass(),
                          MapBuilderExtension.getHashMapParameterMap(configuration,
                                                                     msId,
                                                                     null,
                                                                     parameterHashMap),
                          MapBuilderExtension.getResultMap(configuration,
                                                           msId,
                                                           resultType,
                                                           0,
                                                           null,
                                                           false,
                                                           nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回数据类型
     * @param mainTagLevel     主标签等级
     * @param customTags       自定义标签
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, VParameter> MappedStatement create(Configuration configuration,
                                                                           String msId,
                                                                           String script,
                                                                           SqlCommandType sqlCommandType,
                                                                           Map<String, VParameter> parameterHashMap,
                                                                           Class<TResult> resultType,
                                                                           int mainTagLevel,
                                                                           Collection<String> customTags,
                                                                           NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterHashMap.getClass(),
                          MapBuilderExtension.getHashMapParameterMap(configuration,
                                                                     msId,
                                                                     null,
                                                                     parameterHashMap),
                          MapBuilderExtension.getResultMap(configuration,
                                                           msId,
                                                           resultType,
                                                           mainTagLevel,
                                                           customTags,
                                                           false,
                                                           nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回数据类型
     * @param resultHashMap    返回数据键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, VParameter, VResult> MappedStatement create(Configuration configuration,
                                                                                    String msId,
                                                                                    String script,
                                                                                    SqlCommandType sqlCommandType,
                                                                                    Class<TParameter> parameterType,
                                                                                    Map<String, VParameter> parameterHashMap,
                                                                                    Class<TResult> resultType,
                                                                                    Map<String, VResult> resultHashMap,
                                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          MapBuilderExtension.getHashMapParameterMap(configuration,
                                                                     msId,
                                                                     parameterType,
                                                                     parameterHashMap),
                          MapBuilderExtension.getHashMapResultMap(configuration,
                                                                  msId,
                                                                  resultType,
                                                                  resultHashMap,
                                                                  nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration   配置
     * @param msId            标识
     * @param script          脚本
     * @param sqlCommandType  查询语句类型
     * @param parameterType   参数类型
     * @param resultType      返回数据类型
     * @param mainTagLevel    主标签等级
     * @param customTags      自定义标签
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement create(Configuration configuration,
                                                               String msId,
                                                               String script,
                                                               SqlCommandType sqlCommandType,
                                                               Class<TParameter> parameterType,
                                                               Class<TResult> resultType,
                                                               int mainTagLevel,
                                                               Collection<String> customTags,
                                                               boolean noParameterMap,
                                                               NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          noParameterMap
                          ? null
                          : MapBuilderExtension.getParameterMap(configuration,
                                                                msId,
                                                                parameterType,
                                                                0,
                                                                null),
                          MapBuilderExtension.getResultMap(configuration,
                                                           msId,
                                                           resultType,
                                                           mainTagLevel,
                                                           customTags,
                                                           false,
                                                           nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration   配置
     * @param msId            标识
     * @param script          脚本
     * @param sqlCommandType  查询语句类型
     * @param parameterType   参数类型
     * @param resultType      返回数据类型
     * @param resultHashMap   返回数据键值对映射表
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, V> MappedStatement create(Configuration configuration,
                                                                  String msId,
                                                                  String script,
                                                                  SqlCommandType sqlCommandType,
                                                                  Class<TParameter> parameterType,
                                                                  Class<TResult> resultType,
                                                                  Map<String, V> resultHashMap,
                                                                  boolean noParameterMap,
                                                                  NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          noParameterMap
                          ? null
                          : MapBuilderExtension.getParameterMap(configuration,
                                                                msId,
                                                                parameterType,
                                                                0,
                                                                null),
                          MapBuilderExtension.getHashMapResultMap(configuration,
                                                                  msId,
                                                                  resultType,
                                                                  resultHashMap,
                                                                  nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration   配置
     * @param msId            标识
     * @param script          脚本
     * @param sqlCommandType  查询语句类型
     * @param parameterType   参数类型
     * @param parameterMap    参数映射表
     * @param resultType      返回数据类型
     * @param mainTagLevel    主标签等级
     * @param customTags      自定义标签
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement create(Configuration configuration,
                                                               String msId,
                                                               String script,
                                                               SqlCommandType sqlCommandType,
                                                               Class<TParameter> parameterType,
                                                               ParameterMap parameterMap,
                                                               Class<TResult> resultType,
                                                               int mainTagLevel,
                                                               Collection<String> customTags,
                                                               NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          parameterMap,
                          MapBuilderExtension.getResultMap(configuration,
                                                           msId,
                                                           resultType,
                                                           mainTagLevel,
                                                           customTags,
                                                           false,
                                                           nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration   配置
     * @param msId            标识
     * @param script          脚本
     * @param sqlCommandType  查询语句类型
     * @param parameterType   参数类型
     * @param parameterMap    参数映射表
     * @param resultType      返回数据类型
     * @param resultHashMap   返回数据键值对映射表
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回数据类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, V> MappedStatement create(Configuration configuration,
                                                                  String msId,
                                                                  String script,
                                                                  SqlCommandType sqlCommandType,
                                                                  Class<TParameter> parameterType,
                                                                  ParameterMap parameterMap,
                                                                  Class<TResult> resultType,
                                                                  Map<String, V> resultHashMap,
                                                                  NameConvertType nameConvertType)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          parameterMap,
                          MapBuilderExtension.getHashMapResultMap(configuration,
                                                                  msId,
                                                                  resultType,
                                                                  resultHashMap,
                                                                  nameConvertType));
    }

    /**
     * 创建
     *
     * @param configuration  配置
     * @param msId           标识
     * @param script         脚本
     * @param sqlCommandType 查询语句类型
     * @param parameterType  参数类型
     * @param mainTagLevel   主标签等级
     * @param customTags     自定义标签
     * @param resultMap      返回结果映射表
     * @param <TParameter>   参数类型
     * @return MappedStatement
     */
    public static <TParameter> MappedStatement create(Configuration configuration,
                                                      String msId,
                                                      String script,
                                                      SqlCommandType sqlCommandType,
                                                      Class<TParameter> parameterType,
                                                      int mainTagLevel,
                                                      Collection<String> customTags,
                                                      ResultMap resultMap)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          MapBuilderExtension.getParameterMap(configuration,
                                                              msId,
                                                              parameterType,
                                                              mainTagLevel,
                                                              customTags),
                          resultMap);
    }

    /**
     * 创建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultMap        返回数据映射表
     * @param <TParameter>     参数类型
     * @return MappedStatement
     */
    public static <TParameter, V> MappedStatement create(Configuration configuration,
                                                         String msId,
                                                         String script,
                                                         SqlCommandType sqlCommandType,
                                                         Class<TParameter> parameterType,
                                                         Map<String, V> parameterHashMap,
                                                         ResultMap resultMap)
            throws
            Exception {
        return createBase(configuration,
                          msId,
                          script,
                          sqlCommandType,
                          parameterType,
                          MapBuilderExtension.getHashMapParameterMap(configuration,
                                                                     msId,
                                                                     parameterType,
                                                                     parameterHashMap),
                          resultMap);
    }

    /**
     * 创建
     *
     * @param configuration  配置
     * @param msId           标识
     * @param script         脚本
     * @param sqlCommandType 查询语句类型
     * @param parameterType  参数类型
     * @param parameterMap   参数映射表
     * @param resultMap      返回数据映射表
     * @param <TParameter>   参数类型
     * @return MappedStatement
     */
    public static <TParameter> MappedStatement createBase(Configuration configuration,
                                                          String msId,
                                                          String script,
                                                          SqlCommandType sqlCommandType,
                                                          Class<TParameter> parameterType,
                                                          ParameterMap parameterMap,
                                                          ResultMap resultMap)
            throws
            Exception {
        if (exist(configuration,
                  msId))
            return get(configuration,
                       msId);
//            throw new Exception(String.format("createMappedStatement 失败，id:%s已注册过MappedStatement了",
//                                              msId));

        LanguageDriver languageDriver = configuration.getLanguageRegistry()
                                                     .getDefaultDriver();

        SqlSource sqlSource = languageDriver.createSqlSource(configuration,
                                                             script,
                                                             parameterType);

        MappedStatement.Builder builder = new MappedStatement.Builder(configuration,
                                                                      msId,
                                                                      sqlSource,
                                                                      sqlCommandType);
        //参数映射
        if (parameterMap != null) builder.parameterMap(parameterMap);

        //返回结果映射
        if (resultMap != null) builder.resultMaps(new ArrayList<>(Arrays.asList(resultMap)));

        return aop.mappedStatement(new MappedStatementArgs(false,
                                                           builder.build()))
                  .getMappedStatement();

//        MappedStatement.Builder builder = new MappedStatement.Builder(configuration, newMSId, sqlSource, sqlCommandType);
//        builder.resource(currentMS.getResource());
//        builder.fetchSize(currentMS.getFetchSize());
//        builder.statementType(currentMS.getStatementType());
//        builder.keyGenerator(currentMS.getKeyGenerator());
//        //属性集合
//        if (currentMS.getKeyProperties() != null && currentMS.getKeyProperties().length != 0)
//            builder.keyProperty(String.join(",", currentMS.getKeyProperties()));
//        builder.timeout(currentMS.getTimeout());
//        builder.parameterMap(currentMS.getParameterMap());
//        //查询返回值
//        List<ResultMap> resultMaps = new ArrayList<ResultMap>() {{
//            add(ResultMapExtension.getResultMap(resultType, configuration));
//        }};
//        builder.resultMaps(resultMaps);
//        builder.resultSetType(currentMS.getResultSetType());
//        builder.cache(currentMS.getCache());
//        builder.flushCacheRequired(currentMS.isFlushCacheRequired());
//        builder.useCache(currentMS.isUseCache());
//
//        return builder.build();
    }

    /**
     * 获取或新建
     *
     * @param configuration      配置
     * @param msId               标识
     * @param script             脚本
     * @param sqlCommandType     查询语句类型
     * @param parameterHashMap   参数键值对映射表
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, V> MappedStatement getOrCreate(Configuration configuration,
                                                                       String msId,
                                                                       String script,
                                                                       SqlCommandType sqlCommandType,
                                                                       Map<String, V> parameterHashMap,
                                                                       Class<TResult> resultType,
                                                                       int resultMainTagLevel,
                                                                       Collection<String> resultCustomTags,
                                                                       NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterHashMap,
                        resultType,
                        resultMainTagLevel,
                        resultCustomTags,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }

    /**
     * 获取或新建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, VParameter, VResult> MappedStatement getOrCreate(Configuration configuration,
                                                                                         String msId,
                                                                                         String script,
                                                                                         SqlCommandType sqlCommandType,
                                                                                         Map<String, VParameter> parameterHashMap,
                                                                                         Class<TResult> resultType,
                                                                                         NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterHashMap,
                        resultType,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }

    /**
     * 获取或新建
     *
     * @param configuration   配置
     * @param msId            标识
     * @param script          脚本
     * @param sqlCommandType  查询语句类型
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                                    String msId,
                                                                    String script,
                                                                    SqlCommandType sqlCommandType,
                                                                    Class<TParameter> parameterType,
                                                                    Class<TResult> resultType,
                                                                    boolean noParameterMap,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        resultType,
                        noParameterMap,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }

    /**
     * 获取或新建
     *
     * @param configuration         配置
     * @param msId                  标识
     * @param script                脚本
     * @param sqlCommandType        查询语句类型
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回值类型
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                                    String msId,
                                                                    String script,
                                                                    SqlCommandType sqlCommandType,
                                                                    Class<TParameter> parameterType,
                                                                    int parameterMainTagLevel,
                                                                    Collection<String> parameterCustomTags,
                                                                    Class<TResult> resultType,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        parameterMainTagLevel,
                        parameterCustomTags,
                        resultType,
                        0,
                        null,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }

    /**
     * 获取或新建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, V> MappedStatement getOrCreate(Configuration configuration,
                                                                       String msId,
                                                                       String script,
                                                                       SqlCommandType sqlCommandType,
                                                                       Class<TParameter> parameterType,
                                                                       Map<String, V> parameterHashMap,
                                                                       Class<TResult> resultType,
                                                                       NameConvertType nameConvertType)
            throws
            Exception {
        return getOrCreate(configuration,
                           msId,
                           script,
                           sqlCommandType,
                           parameterType,
                           parameterHashMap,
                           resultType,
                           null,
                           nameConvertType);
    }

    /**
     * 获取或新建
     *
     * @param configuration      配置
     * @param msId               标识
     * @param script             脚本
     * @param sqlCommandType     查询语句类型
     * @param parameterType      参数类型
     * @param resultType         返回值类型
     * @param resultMainTagLevel 返回模型主标签等级
     * @param resultCustomTags   返回模型自定义标签
     * @param noParameterMap     无参数映射
     * @param nameConvertType    命名规则
     * @param <TParameter>       参数类型
     * @param <TResult>          返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                                    String msId,
                                                                    String script,
                                                                    SqlCommandType sqlCommandType,
                                                                    Class<TParameter> parameterType,
                                                                    Class<TResult> resultType,
                                                                    int resultMainTagLevel,
                                                                    Collection<String> resultCustomTags,
                                                                    boolean noParameterMap,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        resultType,
                        resultMainTagLevel,
                        resultCustomTags,
                        noParameterMap,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }

    /**
     * 获取或新建
     *
     * @param configuration   配置
     * @param msId            标识
     * @param script          脚本
     * @param sqlCommandType  查询语句类型
     * @param parameterType   参数类型
     * @param resultType      返回值类型
     * @param resultHashMap   返回数据键值对映射表
     * @param noParameterMap  无参数映射
     * @param nameConvertType 命名规则
     * @param <TParameter>    参数类型
     * @param <TResult>       返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, V> MappedStatement getOrCreate(Configuration configuration,
                                                                       String msId,
                                                                       String script,
                                                                       SqlCommandType sqlCommandType,
                                                                       Class<TParameter> parameterType,
                                                                       Class<TResult> resultType,
                                                                       Map<String, V> resultHashMap,
                                                                       boolean noParameterMap,
                                                                       NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        resultType,
                        resultHashMap,
                        noParameterMap,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }

    /**
     * 获取或新建
     *
     * @param configuration         配置
     * @param msId                  标识
     * @param script                脚本
     * @param sqlCommandType        查询语句类型
     * @param parameterType         参数类型
     * @param parameterMainTagLevel 参数模型主标签等级
     * @param parameterCustomTags   参数模型自定义标签
     * @param resultType            返回值类型
     * @param resultMainTagLevel    返回模型主标签等级
     * @param resultCustomTags      返回模型自定义标签
     * @param nameConvertType       命名规则
     * @param <TParameter>          参数类型
     * @param <TResult>             返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                                    String msId,
                                                                    String script,
                                                                    SqlCommandType sqlCommandType,
                                                                    Class<TParameter> parameterType,
                                                                    int parameterMainTagLevel,
                                                                    Collection<String> parameterCustomTags,
                                                                    Class<TResult> resultType,
                                                                    int resultMainTagLevel,
                                                                    Collection<String> resultCustomTags,
                                                                    NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        parameterMainTagLevel,
                        parameterCustomTags,
                        resultType,
                        resultMainTagLevel,
                        resultCustomTags,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }

    /**
     * 获取或新建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param parameterType    参数类型
     * @param parameterHashMap 参数键值对映射表
     * @param resultType       返回值类型
     * @param resultHashMap    返回数据键值对映射表
     * @param nameConvertType  命名规则
     * @param <TParameter>     参数类型
     * @param <TResult>        返回值类型
     * @return MappedStatement
     */
    public static <TParameter, TResult, VParameter, VResult> MappedStatement getOrCreate(Configuration configuration,
                                                                                         String msId,
                                                                                         String script,
                                                                                         SqlCommandType sqlCommandType,
                                                                                         Class<TParameter> parameterType,
                                                                                         Map<String, VParameter> parameterHashMap,
                                                                                         Class<TResult> resultType,
                                                                                         Map<String, VResult> resultHashMap,
                                                                                         NameConvertType nameConvertType)
            throws
            Exception {
        MappedStatement ms = MappedStatementExtension.get(configuration,
                                                          msId);
        if (ms == null) {
            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        parameterHashMap,
                        resultType,
                        resultHashMap,
                        nameConvertType);
            // 缓存
            if (!configuration.hasStatement(msId))
                configuration.addMappedStatement(ms);
        }
        return ms;
    }
}
