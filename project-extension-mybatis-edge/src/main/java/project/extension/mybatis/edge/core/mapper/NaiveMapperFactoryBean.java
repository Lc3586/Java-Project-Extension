package project.extension.mybatis.edge.core.mapper;

import org.mybatis.spring.mapper.MapperFactoryBean;

/**
 * Mapper对象构造工厂
 *
 * @author LCTR
 * @date 2023-02-06
 */
public class NaiveMapperFactoryBean<T>
        extends MapperFactoryBean<T> {
    public NaiveMapperFactoryBean() {

    }

    public NaiveMapperFactoryBean(Class<T> mapperInterface) {
        super(mapperInterface);
    }
}
