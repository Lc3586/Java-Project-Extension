package project.extension.mybatis.edge.core.provider.dameng;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.CodeFirst;
import project.extension.standard.exception.ModuleException;

/**
 * Dameng CodeFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2022-06-10
 */
public class DamengCodeFirst
        extends CodeFirst {
    protected DamengCodeFirst(DataSourceConfig config,
                              INaiveAdo ado) {
        super(config,
              ado,
              "DamengCodeFirst");
    }

    @Override
    public void createOrReplaceFunctions()
            throws
            ModuleException {

    }
}
