package project.extension.mybatis.edge.core.provider.oracle.curd;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.curd.Insert;
import project.extension.mybatis.edge.core.provider.oracle.OracleSqlProvider;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.Tuple3;

import java.lang.reflect.Field;

/**
 * Dameng数据插入对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-05-27
 */
public class OracleInsert<T>
        extends Insert<T> {
    public OracleInsert(DataSourceConfig config,
                        INaiveAdo ado,
                        Class<T> entityType) {
        super(config,
              new OracleSqlProvider(config),
              ado,
              entityType);
    }

    /**
     * 返回当前的获取序列值的脚本
     *
     * @return 脚本
     */
    private Tuple3<Boolean, String, Class<?>> currentSelectKeyScript()
            throws
            ModuleException {
        return sqlProvider.selectKey2Script(inserter);
    }

    @Override
    protected int insert(String msId,
                         String script)
            throws
            ModuleException {
        Tuple3<Boolean, Field, String> identityFieldAndColumn = super.getIdentityFieldAndColumn();

        if (identityFieldAndColumn.a)
            super.inserter.getOutParameter()
                          .put(identityFieldAndColumn.b.getName(),
                               identityFieldAndColumn.b);

        //oracle自增列使用序列获取主键值
        Tuple3<Boolean, String, Class<?>> useSelectKey = this.currentSelectKeyScript();

        return super.ado.insert(getSqlSession(),
                                msId,
                                script,
                                identityFieldAndColumn.a,
                                identityFieldAndColumn.a
                                ? identityFieldAndColumn.b.getName()
                                : null,
                                identityFieldAndColumn.c,
                                useSelectKey.a,
                                useSelectKey.b,
                                useSelectKey.c,
                                null,
                                super.inserter.getParameter(),
                                super.inserter.getOutParameter(),
                                super.inserter.getInOutParameter(),
                                super.config.getNameConvertType());
    }
}
