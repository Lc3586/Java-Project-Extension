package project.extension.mybatis.edge.core.provider.oracle;

import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.provider.normal.CodeFirst;
import project.extension.standard.exception.ModuleException;

/**
 * Oracle CodeFirst 开发模式相关功能
 *
 * @author LCTR
 * @date 2023-02-21
 */
public class OracleCodeFirst
        extends CodeFirst {
    protected OracleCodeFirst(DataSourceConfig config,
                              INaiveAdo ado) {
        super(config,
              ado);
    }

    /**
     * 创建数据库long转char方法
     */
    private void createOrReplaceNaiveSqlLongToCharDefault() {
        String sql = "CREATE OR REPLACE FUNCTION NAIVE_SQL_LONG_TO_CHAR_DEFAULT\n"
                + "(\n"
                + "  TABLE_NAME VARCHAR,\n"
                + "  COLUMN     VARCHAR2\n"
                + ")\n"
                + "  RETURN VARCHAR AS\n"
                + "  TEXT_C1 VARCHAR2(32767);\n"
                + "  SQL_CUR VARCHAR2(2000);\n"
                + "BEGIN\n"
                + "  DBMS_OUTPUT.ENABLE(BUFFER_SIZE => NULL);\n"
                + "  SQL_CUR := 'SELECT T.DATA_DEFAULT FROM USER_TAB_COLUMNS T WHERE T.TABLE_NAME = '''||TABLE_NAME||''' AND T.COLUMN_NAME='''||COLUMN||'''';\n"
                + "  DBMS_OUTPUT.PUT_LINE(SQL_CUR);\n"
                + "  EXECUTE IMMEDIATE SQL_CUR\n"
                + "    INTO TEXT_C1;\n"
                + "  TEXT_C1 := SUBSTR(TEXT_C1, 1, 4000);\n"
                + "  RETURN TEXT_C1;\n"
                + "END;";

        this.ado.selectOne(getSqlSession(),
                           getMSId(),
                           sql,
                           null,
                           null,
                           null,
                           null,
                           null,
                           config.getNameConvertType());
    }

    /**
     * 创建数据库long转varchar方法
     */
    private void createOrReplaceNaiveSqlLongToVarchar() {
        String sql = "CREATE OR REPLACE FUNCTION NAIVE_SQL_LONG_TO_VARCHAR (\n"
                + "   p_index_name        IN user_ind_expressions.index_name%TYPE,\n"
                + "   p_table_name        IN user_ind_expressions.table_name%TYPE,\n"
                + "   p_COLUMN_POSITION   IN user_ind_expressions.table_name%TYPE)\n"
                + "   RETURN VARCHAR2\n"
                + "AS\n"
                + "   l_COLUMN_EXPRESSION   LONG;\n"
                + "BEGIN\n"
                + "   SELECT COLUMN_EXPRESSION\n"
                + "     INTO l_COLUMN_EXPRESSION\n"
                + "     FROM user_ind_expressions  \n"
                + "    WHERE     index_name = p_index_name\n"
                + "          AND table_name = p_table_name\n"
                + "          AND COLUMN_POSITION = p_COLUMN_POSITION;\n"
                + "  \n"
                + "   RETURN SUBSTR (l_COLUMN_EXPRESSION, 1, 4000);\n"
                + "END;";

        this.ado.selectOne(getSqlSession(),
                           getMSId(),
                           sql,
                           null,
                           null,
                           null,
                           null,
                           null,
                           config.getNameConvertType());
    }

    @Override
    public void createOrReplaceFunctions()
            throws
            ModuleException {
        createOrReplaceNaiveSqlLongToCharDefault();
        createOrReplaceNaiveSqlLongToVarchar();
    }
}
