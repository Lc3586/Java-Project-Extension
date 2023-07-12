package project.extension.mybatis.edge.core.mapper;

import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.MappedStatementArgs;
import project.extension.mybatis.edge.aop.NaiveAopProvider;
import project.extension.mybatis.edge.model.NameConvertType;

import java.lang.reflect.Field;
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

    public String applyId() {
        return MappedStatementIdManager.applyId();
    }

    public void returnId(String msId,
                         Configuration configuration) {
        MappedStatementIdManager.returnId(msId,
                                          configuration);
    }

    /**
     * 创建
     *
     * @param configuration    配置
     * @param msId             标识
     * @param script           脚本
     * @param sqlCommandType   查询语句类型
     * @param useGeneratedKeys 使用自增列
     * @param keyProperty      主键属性
     * @param keyColumn        主键列
     * @param useSelectKey     从数据库获取主键值
     * @param selectKeyScript  获取主键值的脚本
     * @param selectKeyType    主键值的数据类型
     * @param parameterType    参数类型
     * @param parameterMap     参数映射表
     * @param resultMap        返回数据映射表
     * @param <TParameter>     参数类型
     */
    @SuppressWarnings({"GrazieInspection",
                       "CommentedOutCode"})
    public <TParameter> MappedStatement create(Configuration configuration,
                                               String msId,
                                               String script,
                                               SqlCommandType sqlCommandType,
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
                                               Class<TParameter> parameterType,
                                               ParameterMap parameterMap,
                                               ResultMap resultMap) {
        LanguageDriver languageDriver = configuration.getLanguageRegistry()
                                                     .getDefaultDriver();

        SqlSource sqlSource = languageDriver.createSqlSource(configuration,
                                                             script,
                                                             parameterType);

        //支持自增列
        if (useGeneratedKeys) configuration.setUseGeneratedKeys(true);

        MappedStatement.Builder builder = new MappedStatement.Builder(configuration,
                                                                      msId,
                                                                      sqlSource,
                                                                      sqlCommandType);

        if (useSelectKey) {
            SelectKeyGenerator selectKeyGenerator = new SelectKeyGenerator(createSelectKey(configuration,
                                                                                           MappedStatementIdManager.applySubId(msId),
                                                                                           selectKeyScript,
                                                                                           keyProperty,
                                                                                           keyColumn,
                                                                                           selectKeyType),
                                                                           true);

            builder.keyGenerator(selectKeyGenerator);

            keyProperty = null;
            keyColumn = null;
        }

        //指定自增主键
        if (keyProperty != null) builder.keyProperty(keyProperty);
        if (keyColumn != null) builder.keyColumn(keyColumn);

        //参数映射
        if (parameterMap != null) builder.parameterMap(parameterMap);

        //返回结果映射
        if (resultMap != null) builder.resultMaps(new ArrayList<>(Arrays.asList(resultMap)));

        MappedStatement ms = builder.build();

        aop.mappedStatement(new MappedStatementArgs(false,
                                                    ms));

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

    public <TParameter, TResult> MappedStatement create(Configuration configuration,
                                                        String msId,
                                                        String script,
                                                        SqlCommandType sqlCommandType,
                                                        @Nullable
                                                                Class<TParameter> inParameterType,
                                                        Map<String, Object> inParameterHashMap,
                                                        Map<String, Field> outParameterHashMap,
                                                        Map<String, Field> inOutParameterHashMap,
                                                        @Nullable
                                                                Class<TResult> resultType,
                                                        Collection<String> resultFields,
                                                        NameConvertType nameConvertType) {
        ParameterMap parameterMap = MapBuilder.getHashMapParameterMap(configuration,
                                                                      msId,
                                                                      inParameterType,
                                                                      inParameterHashMap,
                                                                      outParameterHashMap,
                                                                      inOutParameterHashMap);

        ResultMap resultMap = MapBuilder.getHashMapResultMap(configuration,
                                                             msId,
                                                             resultType,
                                                             resultFields,
                                                             nameConvertType);

        return create(configuration,
                      msId,
                      script,
                      sqlCommandType,
                      false,
                      null,
                      null,
                      false,
                      null,
                      null,
                      inParameterType,
                      parameterMap,
                      resultMap);
    }

    public <TParameter, TResult> MappedStatement create(Configuration configuration,
                                                        String msId,
                                                        String script,
                                                        SqlCommandType sqlCommandType,
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
                                                        Class<TResult> resultType,
                                                        @Nullable
                                                                Integer resultMainTagLevel,
                                                        @Nullable
                                                                Collection<String> resultCustomTags,
                                                        NameConvertType nameConvertType) {
        ParameterMap parameterMap = MapBuilder.getHashMapParameterMap(configuration,
                                                                      msId,
                                                                      parameterType,
                                                                      parameterHashMap,
                                                                      outParameterHashMap,
                                                                      inOutParameterHashMap);

        ResultMap resultMap = MapBuilder.getResultMap(configuration,
                                                      msId,
                                                      resultType,
                                                      resultMainTagLevel,
                                                      resultCustomTags,
                                                      false,
                                                      false,
                                                      nameConvertType);

        return create(configuration,
                      msId,
                      script,
                      sqlCommandType,
                      useGeneratedKeys,
                      keyProperty,
                      keyColumn,
                      useSelectKey,
                      selectKeyScript,
                      selectKeyType,
                      parameterType,
                      parameterMap,
                      resultMap);
    }

    public <TParameter, TResult> MappedStatement create(Configuration configuration,
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

        return create(configuration,
                      msId,
                      script,
                      sqlCommandType,
                      false,
                      null,
                      null,
                      false,
                      null,
                      null,
                      parameterType,
                      parameterMap,
                      resultMap);
    }

    public <TParameter, TResult> MappedStatement create(Configuration configuration,
                                                        String msId,
                                                        String script,
                                                        SqlCommandType sqlCommandType,
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
                                                      false,
                                                      nameConvertType);

        return create(configuration,
                      msId,
                      script,
                      sqlCommandType,
                      useGeneratedKeys,
                      keyProperty,
                      keyColumn,
                      useSelectKey,
                      selectKeyScript,
                      selectKeyType,
                      parameterType,
                      parameterMap,
                      resultMap);
    }

    public MappedStatement createSelectKey(Configuration configuration,
                                           String msId,
                                           String script,
                                           String keyProperty,
                                           String keyColumn,
                                           Class<?> keyType) {
        ResultMap resultMap = MapBuilder.getResultMap(configuration,
                                                      msId,
                                                      keyType,
                                                      null,
                                                      null,
                                                      false,
                                                      false,
                                                      null);

        return create(configuration,
                      msId,
                      script,
                      SqlCommandType.SELECT,
                      false,
                      keyProperty,
                      keyColumn,
                      false,
                      null,
                      null,
                      null,
                      null,
                      resultMap);
    }
}
