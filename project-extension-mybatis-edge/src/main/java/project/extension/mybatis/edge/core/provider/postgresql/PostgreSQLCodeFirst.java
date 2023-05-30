package project.extension.mybatis.edge.core.provider.postgresql;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.CodeFirst;
import project.extension.standard.exception.ModuleException;

/**
 * PostgreSQL CodeFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class PostgreSQLCodeFirst
        extends CodeFirst {
    protected PostgreSQLCodeFirst(DataSourceConfig config,
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
