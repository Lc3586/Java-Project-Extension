package project.extension.mybatis.edge.core.provider.postgresql;

import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.CodeFirst;

/**
 * PostgreSQL CodeFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class PostgreSQLCodeFirst
        extends CodeFirst {
    protected PostgreSQLCodeFirst(INaiveAdo ado) {
        super(ado);
    }
}
