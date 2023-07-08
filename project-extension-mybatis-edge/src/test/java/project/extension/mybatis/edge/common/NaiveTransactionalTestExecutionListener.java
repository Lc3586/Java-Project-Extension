package project.extension.mybatis.edge.common;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.annotations.NaiveDataSource;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.core.ado.INaiveDataSourceProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听测试方法的事务
 *
 * @author LCTR
 * @date 2023-01-11
 */
public class NaiveTransactionalTestExecutionListener
        extends TransactionalTestExecutionListener {
    static {
        names = TestDataSourceConfigure.getMultiTestDataSourceName();
    }

    /**
     * 方法所接受的数据源
     */
    public static final ConcurrentHashMap<String, List<String>> testName4Method = new ConcurrentHashMap<>();

    private static final String[] names;

    private static INaiveDataSourceProvider naiveDataSourceProvider;

    private static INaiveDataSourceProvider getNaiveDataSourceProvider() {
        if (naiveDataSourceProvider == null)
            naiveDataSourceProvider = IOCExtension.applicationContext.getBean(INaiveDataSourceProvider.class);
        return naiveDataSourceProvider;
    }

    /**
     * 获取事务管理器
     */
    @Override
    protected PlatformTransactionManager getTransactionManager(TestContext testContext,
                                                               @Nullable
                                                                       String qualifier) {
        Method testMethod = testContext.getTestMethod();

        NaiveDataSource naiveDataSource = AnnotationUtils.getAnnotation(testMethod,
                                                                        NaiveDataSource.class);
        if (naiveDataSource == null) {
            Class<?> testClass = testContext.getTestClass();
            naiveDataSource = AnnotationUtils.getAnnotation(testClass,
                                                            NaiveDataSource.class);
        }

        if (naiveDataSource != null)
            return getNaiveDataSourceProvider().getTransactionManager(naiveDataSource.dataSource());
//            return super.getTransactionManager(testContext,
//                                               NaiveDataSourceProvider.getTransactionManagerBeanName(naiveDataSource.dataSource()));

        String key = testContext.getTestClass()
                                .getName() + testMethod.getName();

        if (!testName4Method.containsKey(key))
            testName4Method.put(key,
                                new ArrayList<>());

        List<String> nameList = testName4Method.get(key);
        for (int i = 0; i < names.length; i++) {
            if (nameList.contains(names[i])) {
                if (i == names.length - 1) {
                    nameList.clear();
                    nameList.add(names[0]);
                }
            } else {
                nameList.add(names[i]);
                break;
            }
        }

        if (nameList.size() > 0) {
            String dataSource = TestDataSourceConfigure.getTestDataSource(nameList.get(nameList.size() - 1));
            return getNaiveDataSourceProvider().getTransactionManager(dataSource);
//            return super.getTransactionManager(testContext,
//                                               NaiveDataSourceProvider.getTransactionManagerBeanName(dataSource));
        }

        return super.getTransactionManager(testContext,
                                           qualifier);
    }
}
