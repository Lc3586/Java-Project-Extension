package project.extension.mybatis.edge.common;

import org.junit.jupiter.api.Assertions;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.IMultiNaiveSql;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.config.BaseConfig;
import project.extension.mybatis.edge.core.ado.INaiveDataSourceProvider;
import project.extension.mybatis.edge.core.ado.NaiveDataSource;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 获取Orm对象帮助类
 * <p>必须先执行injection方法</p>
 *
 * @author LCTR
 * @date 2023-01-10
 */
public class OrmObjectResolve {
    public static BaseConfig baseConfig;

    public static INaiveSql masterNaiveSql;

    public static IMultiNaiveSql multiNaiveSql;

    public static INaiveDataSourceProvider naiveDataSourceProvider;

    public static Map<Object, DataSource> druidDataSourceMap;

    /**
     * 注入
     */
    public static void injection() {
        baseConfig = IOCExtension.applicationContext.getBean(BaseConfig.class);

        Assertions.assertNotNull(baseConfig,
                                 "未获取到BaseConfig");

        System.out.printf("\r\n%s已注入\r\n",
                          BaseConfig.class.getName());


        masterNaiveSql = IOCExtension.applicationContext.getBean(INaiveSql.class);

        Assertions.assertNotNull(masterNaiveSql,
                                 "未获取到INaiveSql");

        System.out.printf("\r\n%s已注入\r\n",
                          INaiveSql.class.getName());


        multiNaiveSql = IOCExtension.applicationContext.getBean(IMultiNaiveSql.class);

        Assertions.assertNotNull(multiNaiveSql,
                                 "未获取到IMultiNaiveSql");

        System.out.printf("\r\n%s已注入\r\n",
                          IMultiNaiveSql.class.getName());


        naiveDataSourceProvider = IOCExtension.applicationContext.getBean(INaiveDataSourceProvider.class);


        NaiveDataSource naiveDataSource = IOCExtension.applicationContext.getBean(NaiveDataSource.class);

        Assertions.assertNotNull(naiveDataSource,
                                 "未获取到NaiveDataSource");

        System.out.printf("\r\n%s已注入\r\n",
                          NaiveDataSource.class.getName());

        druidDataSourceMap = naiveDataSource.getResolvedDataSources();

        Assertions.assertNotEquals(0,
                                   druidDataSourceMap.size(),
                                   "未获取到任何DruidDataSource");
    }

    /**
     * 获取Orm对象
     *
     * @param dataSource 数据源
     */
    public static INaiveSql getOrm(String dataSource) {
        Assertions.assertTrue(OrmObjectResolve.naiveDataSourceProvider.isExists(dataSource),
                              String.format("%s数据源不存在",
                                            dataSource));

        Assertions.assertTrue(OrmObjectResolve.naiveDataSourceProvider.isEnable(dataSource),
                              String.format("%s数据源未启用",
                                            dataSource));

        Assertions.assertTrue(OrmObjectResolve.druidDataSourceMap.containsKey(dataSource),
                              String.format("未获取到name为%s的DruidDataSource",
                                            dataSource));

        System.out.printf("\r\n已获取%s数据源\r\n",
                          dataSource);

        if (dataSource.equals(baseConfig.getDataSource()))
            return masterNaiveSql;
        else
            return multiNaiveSql.getSlaveOrm(dataSource);
    }

    /**
     * 获取CodeFirst对象
     *
     * @param dataSource 数据源
     */
    public static ICodeFirst getCodeFirst(String dataSource) {
        ICodeFirst codeFirst = getOrm(dataSource).getCodeFirst();
        Assertions.assertNotNull(codeFirst,
                                 String.format("获取到%s数据源的ICodeFirst失败",
                                               dataSource));

        System.out.printf("\r\n已获取%s数据源的ICodeFirst对象\r\n",
                          dataSource);

        return codeFirst;
    }

    /**
     * 获取DbFirst对象
     *
     * @param dataSource 数据源
     */
    public static IDbFirst getDbFirst(String dataSource) {
        IDbFirst dbFirst = getOrm(dataSource).getDbFirst();
        Assertions.assertNotNull(dbFirst,
                                 String.format("获取到%s数据源的IDbFirst失败",
                                               dataSource));

        System.out.printf("\r\n已获取%s数据源的IDbFirst对象\r\n",
                          dataSource);

        return dbFirst;
    }

    /**
     * 获取Orm对象
     *
     * @param dataSource 数据源
     * @param mapperType Mapper类型
     * @param <T>        Mapper类型
     */
    public static <T> T getMapper(String dataSource,
                                  Class<T> mapperType) {
        T mapper = getOrm(dataSource).getMapper(mapperType);
        Assertions.assertNotNull(mapper,
                                 String.format("获取到%s数据源的%s失败",
                                               dataSource,
                                               mapperType.getName()));

        System.out.printf("\r\n已获取%s数据源的%s对象\r\n",
                          dataSource,
                          mapperType.getName());

        return mapper;
    }
}
