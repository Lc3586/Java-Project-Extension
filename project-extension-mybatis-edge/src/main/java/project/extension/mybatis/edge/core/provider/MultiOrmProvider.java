package project.extension.mybatis.edge.core.provider;

import org.springframework.stereotype.Component;
import project.extension.mybatis.edge.IMultiNaiveSql;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.core.ado.INaiveDataSourceProvider;
import project.extension.mybatis.edge.extention.CommonUtils;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.standard.exception.ModuleException;

import java.util.HashMap;
import java.util.Map;

/**
 * 多库ORM构造器
 *
 * @author LCTR
 * @date 2023-01-10
 */
@Component
public class MultiOrmProvider
        implements IMultiNaiveSql {
    public MultiOrmProvider(INaiveSql orm,
                            INaiveDataSourceProvider dataSourceProvider) {
        this.masterOrm = orm;
    }

    /**
     * 主库Orm
     */
    private final INaiveSql masterOrm;

    /**
     * 从库Orm
     */
    private final Map<String, INaiveSql> slaveOrmMap = new HashMap<>();

    @Override
    public INaiveSql getMasterOrm() {
        return masterOrm;
    }

    @Override
    public INaiveSql getSlaveOrm(String dataSource) {
        if (dataSource.equals(CommonUtils.getConfig()
                                         .getDataSource()))
            throw new ModuleException(Strings.getUseMasterOrmMethod());

        if (!slaveOrmMap.containsKey(dataSource))
            slaveOrmMap.put(dataSource,
                            new OrmProvider(dataSource));

        return slaveOrmMap.get(dataSource);
    }
}
