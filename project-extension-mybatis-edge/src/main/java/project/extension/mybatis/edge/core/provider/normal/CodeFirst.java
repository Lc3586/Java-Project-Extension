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
                        INaiveAdo ado) {
        this.config = config;
        this.ado = ado;
    }

    protected final DataSourceConfig config;

    protected final INaiveAdo ado;

    /**
     * 获取Sql会话
     */
    protected SqlSession getSqlSession() {
        return ado.getOrCreateSqlSession();
    }

    /**
     * 获取标识
     *
     * @return 标识
     */
    protected String getMSId() {
        return ado.getCurrentMSId();
    }
}
