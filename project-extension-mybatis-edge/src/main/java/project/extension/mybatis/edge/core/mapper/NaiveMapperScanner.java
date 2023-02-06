package project.extension.mybatis.edge.core.mapper;

import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import project.extension.resource.ScanExtension;

/**
 * Mapper接口类扫描器
 *
 * @author LCTR
 * @date 2023-02-06
 */
public class NaiveMapperScanner
        extends ClassPathMapperScanner {
    /**
     * @param sqlSessionFactoryBeanName 数据源名称
     * @param registry                  注册处理类
     */
    public NaiveMapperScanner(String sqlSessionFactoryBeanName,
                              BeanDefinitionRegistry registry) {
        super(registry);
        // this check is needed in Spring 3.1
        super.setResourceLoader(ScanExtension.RESOURCE_PATTERN_RESOLVER);
        super.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);
        super.setMapperFactoryBeanClass(NaiveMapperFactoryBean.class);
        super.registerFilters();
    }
}
