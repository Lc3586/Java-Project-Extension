package project.extension.mybatis.edge.core.provider.sqlserver;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.CodeFirst;
import project.extension.standard.exception.ModuleException;

/**
 * SqlServer CodeFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerCodeFirst
        extends CodeFirst {
    protected SqlServerCodeFirst(DataSourceConfig config,
                                 INaiveAdo ado) {
        super(config,
              ado);
    }

    @Override
    public void createOrReplaceFunctions()
            throws
            ModuleException {

    }
}
