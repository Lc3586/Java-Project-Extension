package project.extension.mybatis.edge.common;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import project.extension.mybatis.edge.annotations.NaiveDataSource;
import project.extension.mybatis.edge.core.ado.INaiveDataSourceProvider;
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
