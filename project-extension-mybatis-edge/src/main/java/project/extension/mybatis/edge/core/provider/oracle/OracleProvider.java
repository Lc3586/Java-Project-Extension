package project.extension.mybatis.edge.core.provider.oracle;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.oracle.curd.OracleDelete;
import project.extension.mybatis.edge.core.provider.oracle.curd.OracleInsert;
import project.extension.mybatis.edge.core.provider.oracle.curd.OracleSelect;
import project.extension.mybatis.edge.core.provider.oracle.curd.OracleUpdate;
import project.extension.mybatis.edge.core.provider.standard.IBaseDbProvider;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;
import project.extension.mybatis.edge.core.provider.standard.IDbFirst;
import project.extension.mybatis.edge.core.provider.standard.curd.IDelete;
import project.extension.mybatis.edge.core.provider.standard.curd.IInsert;
import project.extension.mybatis.edge.core.provider.standard.curd.ISelect;
import project.extension.mybatis.edge.core.provider.standard.curd.IUpdate;

/**
 * Oracle数据库对象构造器
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class OracleProvider
        implements IBaseDbProvider {
    public OracleProvider(DataSourceConfig config) {
        this.config = config;
    }

    private final DataSourceConfig config;

    @Override
    public <TEntity> ISelect<TEntity> createSelect(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new OracleSelect<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public <TEntity> IInsert<TEntity> createInsert(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new OracleInsert<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public <TEntity> IUpdate<TEntity> createUpdate(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new OracleUpdate<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public <TEntity> IDelete<TEntity> createDelete(Class<TEntity> entityType,
                                                   INaiveAdo ado) {
        return new OracleDelete<>(config,
                                  ado,
                                  entityType);
    }

    @Override
    public IDbFirst createDbFirst(INaiveAdo ado) {
        return new OracleDbFirst(config,
                                 ado,
                                 createCodeFirst(ado));
    }

    @Override
    public ICodeFirst createCodeFirst(INaiveAdo ado) {
        return new OracleCodeFirst(config,
                                   ado);
    }
}
