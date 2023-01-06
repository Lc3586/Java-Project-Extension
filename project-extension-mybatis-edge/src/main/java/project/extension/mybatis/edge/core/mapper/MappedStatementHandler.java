package project.extension.mybatis.edge.core.mapper;

import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.MappedStatementArgs;
import project.extension.mybatis.edge.aop.NaiveAopProvider;
import project.extension.mybatis.edge.model.NameConvertType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * MappedStatement处理类
 *
 * @author LCTR
 * @date 2022-03-29
 */
@Component
public class MappedStatementHandler {
    public MappedStatementHandler(INaiveAop aop) {
        this.aop = (NaiveAopProvider) aop;
    }

    private final NaiveAopProvider aop;

    /**
     * 是否存在
     *
     * @param configuration 配置
     * @param msId          标识
     */
    public boolean exist(Configuration configuration,
                         String msId) {
        return configuration.getMappedStatementNames()
                            .contains(msId);
    }

    /**
     * 获取
     *
     * @param configuration 配置
     * @param msId          标识
     */
    public MappedStatement get(Configuration configuration,
                               String msId) {

        if (exist(configuration,
                  msId)) {
            return this.aop.mappedStatement(new MappedStatementArgs(true,
                                                                    configuration.getMappedStatement(msId)))
                           .getMappedStatement();
        } else return null;
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
     */
    public <TParameter> MappedStatement create(Configuration configuration,
                                               String msId,
                                               String script,
                                               SqlCommandType sqlCommandType,
                                               Class<TParameter> parameterType,
                                               ParameterMap parameterMap,
                                               ResultMap resultMap) {
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

        MappedStatement ms = builder.build();

        aop.mappedStatement(new MappedStatementArgs(false,
                                                    ms));

        // 缓存
        if (!configuration.hasStatement(msId))
            configuration.addMappedStatement(ms);

        return ms;

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

    public <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                             String msId,
                                                             String script,
                                                             SqlCommandType sqlCommandType,
                                                             @Nullable
                                                                     Class<TParameter> parameterType,
                                                             Map<String, Object> parameterHashMap,
                                                             @Nullable
                                                                     Class<TResult> resultType,
                                                             Collection<String> resultFields,
                                                             NameConvertType nameConvertType) {
        MappedStatement ms = get(configuration,
                                 msId);

        if (ms == null) {
            ParameterMap parameterMap = MapBuilder.getHashMapParameterMap(configuration,
                                                                          msId,
                                                                          parameterType,
                                                                          parameterHashMap);

            ResultMap resultMap = MapBuilder.getHashMapResultMap(configuration,
                                                                 msId,
                                                                 resultType,
                                                                 resultFields,
                                                                 nameConvertType);

            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        parameterMap,
                        resultMap);
        }

        return ms;
    }

    public <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                             String msId,
                                                             String script,
                                                             SqlCommandType sqlCommandType,
                                                             @Nullable
                                                                     Class<TParameter> parameterType,
                                                             Map<String, Object> parameterHashMap,
                                                             Class<TResult> resultType,
                                                             @Nullable
                                                                     Integer resultMainTagLevel,
                                                             @Nullable
                                                                     Collection<String> resultCustomTags,
                                                             NameConvertType nameConvertType) {
        MappedStatement ms = get(configuration,
                                 msId);

        if (ms == null) {
            ParameterMap parameterMap = MapBuilder.getHashMapParameterMap(configuration,
                                                                          msId,
                                                                          parameterType,
                                                                          parameterHashMap);

            ResultMap resultMap = MapBuilder.getResultMap(configuration,
                                                          msId,
                                                          resultType,
                                                          resultMainTagLevel,
                                                          resultCustomTags,
                                                          false,
                                                          nameConvertType);

            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        parameterMap,
                        resultMap);
        }

        return ms;
    }

    public <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                             String msId,
                                                             String script,
                                                             SqlCommandType sqlCommandType,
                                                             Class<TParameter> parameterType,
                                                             @Nullable
                                                                     Integer parameterMainTagLevel,
                                                             @Nullable
                                                                     Collection<String> parameterCustomTags,
                                                             Class<TResult> resultType,
                                                             Collection<String> resultFields,
                                                             NameConvertType nameConvertType) {
        MappedStatement ms = get(configuration,
                                 msId);

        if (ms == null) {
            ParameterMap parameterMap = MapBuilder.getParameterMap(configuration,
                                                                   msId,
                                                                   parameterType,
                                                                   parameterMainTagLevel,
                                                                   parameterCustomTags);

            ResultMap resultMap = MapBuilder.getHashMapResultMap(configuration,
                                                                 msId,
                                                                 resultType,
                                                                 resultFields,
                                                                 nameConvertType);

            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        parameterMap,
                        resultMap);
        }

        return ms;
    }

    public <TParameter, TResult> MappedStatement getOrCreate(Configuration configuration,
                                                             String msId,
                                                             String script,
                                                             SqlCommandType sqlCommandType,
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
                                                             NameConvertType nameConvertType) {
        MappedStatement ms = get(configuration,
                                 msId);

        if (ms == null) {
            ParameterMap parameterMap = MapBuilder.getParameterMap(configuration,
                                                                   msId,
                                                                   parameterType,
                                                                   parameterMainTagLevel,
                                                                   parameterCustomTags);

            ResultMap resultMap = MapBuilder.getResultMap(configuration,
                                                          msId,
                                                          resultType,
                                                          resultMainTagLevel,
                                                          resultCustomTags,
                                                          false,
                                                          nameConvertType);

            ms = create(configuration,
                        msId,
                        script,
                        sqlCommandType,
                        parameterType,
                        parameterMap,
                        resultMap);
        }

        return ms;
    }
}
