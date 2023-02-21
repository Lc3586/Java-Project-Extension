package project.extension.mybatis.edge.core.provider.oracle;

import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.CodeFirst;

/**
 * Oracle CodeFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class OracleCodeFirst
        extends CodeFirst {
    protected OracleCodeFirst(INaiveAdo ado) {
        super(ado);
    }
}
