package project.extension.mybatis.core.driver;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.extention.ExecutorExtension;
import project.extension.mybatis.extention.MappedStatementExtension;
import project.extension.mybatis.model.DynamicSqlSetting;
import project.extension.mybatis.model.ExecutorParameter;
import project.extension.mybatis.model.NameConvertType;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 初级插件
 *
 * @author LCTR
 * @date 2022-03-26
 * @deprecated 请使用RepositoryProvider来操作数据库
 */
@Deprecated
@Intercepts({
        @Signature(type = Executor.class,
                   method = "update",
                   args = {MappedStatement.class,
                           Object.class}),
        @Signature(type = Executor.class,
                   method = "query",
                   args = {MappedStatement.class,
                           Object.class,
                           RowBounds.class,
                           ResultHandler.class}),
        @Signature(type = Executor.class,
                   method = "query",
                   args = {MappedStatement.class,
                           Object.class,
                           RowBounds.class,
                           ResultHandler.class,
                           CacheKey.class,
                           BoundSql.class}),
        @Signature(type = ParameterHandler.class,
                   method = "getParameterObject",
                   args = {}),
        @Signature(type = ParameterHandler.class,
                   method = "setParameters",
                   args = {PreparedStatement.class})
})
public class NaivePlugin
        implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object intercept(Invocation invocation)
            throws
            Throwable {
        Object target = invocation.getTarget();
        if (target.getClass()
                  .isAssignableFrom(Executor.class)) {
            Object[] args = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) args[0];
            Object parameter = args[1];

            String msId = mappedStatement.getId();
//            System.out.println(msId);

            //是否为动态映射器
            boolean dynamic = false;
            Class<?> resultType = null;
            if (parameter == null) {
                if (msId.startsWith("project.extension.mybatis.core.mapper.INaiveMapper.")) {
                    //无参数
                    List<ResultMap> resultMaps = mappedStatement.getResultMaps();
                    if (resultMaps.size() > 0) resultType = resultMaps.get(0)
                                                                      .getType();
                    else throw new Exception(String.format("%s返回数据为空",
                                                           msId));
                    dynamic = true;
                }
            } else if (parameter.getClass()
                                .equals(ParamMap.class)) {
                //多参数
                ParamMap<Object> parameterMap = (ParamMap<Object>) parameter;
                Object arg0 = parameterMap.getOrDefault("arg0",
                                                        null);
                if (arg0 == null)
                    arg0 = parameterMap.getOrDefault("param1",
                                                     null);
                if (arg0 == null)
                    arg0 = parameterMap.get(new ArrayList<>(parameterMap.keySet()).get(0));

                if (arg0 != null && (msId.startsWith("project.extension.mybatis.core.mapper.INaiveMapper.")
                        || arg0.getClass()
                               .equals(DynamicSqlSetting.class))) {
                    DynamicSqlSetting setting = (DynamicSqlSetting) arg0;
                    resultType = setting.getResultType();
                    dynamic = true;
                }
            } else {
                //单参数
//                    System.out.println(parameter.getClass().getTypeName());
                resultType = (Class<?>) CollectionsExtension.tryGet(ExecutorExtension.getParameterValues(parameter,
                                                                                                         ExecutorParameter.返回值类型),
                                                                    ExecutorParameter.返回值类型).b;

                if (resultType != null) dynamic = true;
            }

            if (dynamic) {
                //获取或创建对应的MappedStatement进行操作
                Configuration configuration = mappedStatement.getConfiguration();
                String specialMSId = String.format("%s+p:%s+r:%s",
                                                   msId,
                                                   parameter.getClass()
                                                            .getTypeName(),
                                                   resultType == null
                                                   ? "void"
                                                   : resultType.getTypeName());
//                System.out.println(specialMSId);
                MappedStatement specialMS = MappedStatementExtension.get(configuration,
                                                                         specialMSId);
                if (specialMS == null) {
                    specialMS = MappedStatementExtension.create(configuration,
                                                                specialMSId,
                                                                mappedStatement.getSqlSource()
                                                                               .getBoundSql(parameter)
                                                                               .getSql(),
                                                                SqlCommandType.SELECT,
                                                                parameter.getClass(),
                                                                resultType,
                                                                true,
                                                                NameConvertType.None);
                    // 缓存
                    configuration.addMappedStatement(specialMS);
                }

                return invok(invocation,
                             parameter,
                             specialMS,
                             args);
            }
        } else if (target.getClass()
                         .isAssignableFrom(ParameterHandler.class)) {
            Object[] args = invocation.getArgs();

        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,
                           this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 调用方法
     *
     * @param invocation
     * @param parameter
     * @param mappedStatement
     * @param args
     * @return
     * @throws Throwable
     */
    private Object invok(Invocation invocation,
                         Object parameter,
                         MappedStatement mappedStatement,
                         Object... args)
            throws
            Throwable {
        Executor executor = (Executor) invocation.getTarget();

        switch (invocation.getMethod()
                          .getName()) {
            case "update":
                return executor.update(mappedStatement,
                                       parameter);
            default:
            case "query":
                if (args.length == 4)
                    return executor.query(mappedStatement,
                                          parameter,
                                          (RowBounds) args[2],
                                          (ResultHandler) args[3]);
                else
                    return executor.query(mappedStatement,
                                          parameter,
                                          (RowBounds) args[2],
                                          (ResultHandler) args[3],
                                          (CacheKey) args[4],
                                          (BoundSql) args[5]);
        }
    }
}
