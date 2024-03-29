package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表外键信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
@Data
public class DbForeignInfo {
    public DbForeignInfo() {

    }

    public DbForeignInfo(DbTableInfo table,
                         DbTableInfo referencedTable) {
        this.table = table;
        this.referencedTable = referencedTable;
    }

    /**
     * 所在表
     */
    private DbTableInfo table;

    /**
     * 相关列集合
     */
    private final List<DbColumnInfo> columns = new ArrayList<>();

    /**
     * 关联表
     */
    private DbTableInfo referencedTable;

    /**
     * 关联列集合
     */
    private final List<DbColumnInfo> referencedColumns = new ArrayList<>();
}
