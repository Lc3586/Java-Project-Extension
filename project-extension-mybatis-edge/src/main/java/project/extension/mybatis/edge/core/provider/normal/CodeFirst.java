package project.extension.mybatis.edge.core.provider.normal;

import org.apache.ibatis.session.SqlSession;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.standard.ICodeFirst;

/**
 * CodeFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2022-06-10
 */
public abstract class CodeFirst
        implements ICodeFirst {
    protected CodeFirst(DataSourceConfig config,
                        INaiveAdo ado,
                        String msIdPrefix) {
        this.config = config;
        this.ado = ado;
        this.msIdPrefix = msIdPrefix;
    }

    protected final DataSourceConfig config;

    protected final INaiveAdo ado;

    private final String msIdPrefix;

    /**
     * 获取Sql会话
     */
    protected SqlSession getSqlSession() {
        return ado.getOrCreateSqlSession();
    }

    /**
     * 获取标识
     *
     * @param values 附加值
     * @return 标识
     */
    protected String getMSId(String... values) {
        return String.format("%s:%s",
                             msIdPrefix,
                             String.join("-",
                                         values));
    }
}
