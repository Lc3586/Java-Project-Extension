package project.extension.mybatis.edge.core.ado;

import project.extension.ioc.IOCExtension;
import project.extension.mybatis.edge.aop.DataSourceChangedEventArgs;
import project.extension.mybatis.edge.aop.INaiveAop;
import project.extension.mybatis.edge.aop.NaiveAopProvider;

/**
 * 各线程的数据源
 *
 * @author LCTR
 * @date 2023-07-07
 */
public class NaiveDataSourceContextHolder {
    private static NaiveAopProvider aop;

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private static NaiveAopProvider getAop() {
        if (aop == null)
            aop = (NaiveAopProvider) IOCExtension.applicationContext.getBean(INaiveAop.class);
        return aop;
    }

    /**
     * 设置
     *
     * @param dataSource 数据源
     */
    public static void setDataSource(String dataSource) {
        getAop().dataSourceChanged(new DataSourceChangedEventArgs(Thread.currentThread()
                                                                        .getId(),
                                                                  null,
                                                                  dataSource));
        threadLocal.set(dataSource);
    }

    /**
     * 获取
     *
     * @return 数据源
     */
    public static String getDataSource() {
        return threadLocal.get();
    }

    /**
     * 清理
     */
    public static void clearDataSource() {
        getAop().dataSourceChanged(new DataSourceChangedEventArgs(Thread.currentThread()
                                                                        .getId(),
                                                                  getDataSource(),
                                                                  null));
        threadLocal.remove();
    }
}
