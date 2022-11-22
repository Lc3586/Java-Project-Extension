package project.extension.mybatis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表外键信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
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

    /**
     * 所在表
     */
    public DbTableInfo getTable() {
        return table;
    }

    /**
     * 所在表
     */
    public void setTable(DbTableInfo table) {
        this.table = table;
    }

    /**
     * 相关列集合
     */
    public List<DbColumnInfo> getColumns() {
        return columns;
    }

    /**
     * 关联表
     */
    public DbTableInfo getReferencedTable() {
        return referencedTable;
    }

    /**
     * 关联表
     */
    public void setReferencedTable(DbTableInfo referencedTable) {
        this.referencedTable = referencedTable;
    }

    /**
     * 关联列集合
     */
    public List<DbColumnInfo> getReferencedColumns() {
        return referencedColumns;
    }
}
