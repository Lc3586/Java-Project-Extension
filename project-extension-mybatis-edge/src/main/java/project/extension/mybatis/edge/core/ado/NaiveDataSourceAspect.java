package project.extension.mybatis.edge.core.ado;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import project.extension.mybatis.edge.annotations.NaiveDataSource;

/**
 * 处理数据源注解
 *
 * @author LCTR
 * @date 2023-07-07
 * @see project.extension.mybatis.edge.annotations.NaiveDataSource 将当前线程所使用的数据源切换至此注解所配置的数据源
 */
@Aspect
@Order(Integer.MAX_VALUE - 10)
@Component
public class NaiveDataSourceAspect {
    @Pointcut("@annotation(project.extension.mybatis.edge.annotations.NaiveDataSource)"
            + "|| @within(project.extension.mybatis.edge.annotations.NaiveDataSource)")
    public void rPointCut() {

    }

    @Around("rPointCut()")
    public Object around(ProceedingJoinPoint point)
            throws
            Throwable {
        NaiveDataSource dataSource = getDataSource(point);

        //切换至目标数据源
        if (dataSource != null)
            NaiveDataSourceContextHolder.setDataSource(dataSource.dataSource());

        try {
            //执行方法
            return point.proceed();
        } finally {
            //还原数据源
            NaiveDataSourceContextHolder.clearDataSource();
        }
    }

    /**
     * 获取目标数据源
     */
    public NaiveDataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        //优先获取方法上的注解
        NaiveDataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(),
                                                                    NaiveDataSource.class);
        if (dataSource != null)
            return dataSource;

        //其次再获取类上的注解
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(),
                                              NaiveDataSource.class);
    }
}
