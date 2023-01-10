package project.extension.mybatis.edge.common;

import org.junit.jupiter.api.Assertions;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.IMultiNaiveSql;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.config.BaseConfig;

/**
 * 注入
 *
 * @author LCTR
 * @date 2023-01-10
 */
public class OrmInjection {
    public static BaseConfig baseConfig;

    public static INaiveSql masterNaiveSql;

    public static IMultiNaiveSql multiNaiveSql;

    /**
     * 注入
     */
    public static void injection() {
        baseConfig = IOCExtension.applicationContext.getBean(BaseConfig.class);

        Assertions.assertNotEquals(null,
                                   baseConfig,
                                   "未获取到BaseConfig");

        System.out.printf("\r\n%s已注入\r\n",
                          BaseConfig.class.getName());

        masterNaiveSql = IOCExtension.applicationContext.getBean(INaiveSql.class);

        Assertions.assertNotEquals(null,
                                   masterNaiveSql,
                                   "未获取到INaiveSql");

        System.out.printf("\r\n%s已注入\r\n",
                          INaiveSql.class.getName());

        multiNaiveSql = IOCExtension.applicationContext.getBean(IMultiNaiveSql.class);

        Assertions.assertNotEquals(null,
                                   multiNaiveSql,
                                   "未获取到IMultiNaiveSql");

        System.out.printf("\r\n%s已注入\r\n",
                          IMultiNaiveSql.class.getName());
    }
}
