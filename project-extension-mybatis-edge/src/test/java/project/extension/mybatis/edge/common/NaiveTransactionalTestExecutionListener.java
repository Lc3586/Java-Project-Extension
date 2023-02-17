package project.extension.mybatis.edge.common;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import project.extension.mybatis.edge.annotations.NaiveDataSource;
import project.extension.mybatis.edge.config.TestDataSourceConfig;
import project.extension.mybatis.edge.core.ado.NaiveDataSourceProvider;

import java.lang.reflect.Method;

/**
 * 监听测试方法的事务
 *
 * @author LCTR
 * @date 2023-01-11
 */
public class NaiveTransactionalTestExecutionListener
        extends TransactionalTestExecutionListener {
    /**
     * 获取事务管理器
     */
    @Override
    protected PlatformTransactionManager getTransactionManager(TestContext testContext,
                                                               @Nullable
                                                                       String qualifier) {
        Method testMethod = testContext.getTestMethod();

        //TODO 当前方法无法确认在创建事务前使用的是哪个数据源
        String name = TempDataExtension.getThreadTransaction(Thread.currentThread()
                                                                   .getId());

        if (name != null) {
            String dataSource = TestDataSourceConfig.getTestDataSource(name);
            return super.getTransactionManager(testContext,
                                               NaiveDataSourceProvider.getTransactionManagerBeanName(dataSource));
        }

        NaiveDataSource naiveDataSource = AnnotationUtils.getAnnotation(testMethod,
                                                                        NaiveDataSource.class);
        if (naiveDataSource == null) {
            Class<?> testClass = testContext.getTestClass();
            naiveDataSource = AnnotationUtils.getAnnotation(testClass,
                                                            NaiveDataSource.class);
        }

        if (naiveDataSource != null)
            return super.getTransactionManager(testContext,
                                               NaiveDataSourceProvider.getTransactionManagerBeanName(naiveDataSource.dataSource()));

        return super.getTransactionManager(testContext,
                                           qualifier);
    }
}
