package project.extension.mybatis.model;

/**
 * 数据库表索引相关的列信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
public class DbIndexColumnInfo {
    public DbIndexColumnInfo() {

    }

    public DbIndexColumnInfo(DbColumnInfo column,
                             boolean isDesc) {
        this.column = column;
        this.isDesc = isDesc;
    }

    /**
     * 列信息
     */
    private DbColumnInfo column;

    /**
     * 是否降序
     */
    private boolean isDesc;

    /**
     * 列信息
     */
    public DbColumnInfo getColumn() {
        return column;
    }

    /**
     * 列信息
     */
    public void setColumn(DbColumnInfo column) {
        this.column = column;
    }

    /**
     * 是否降序
     */
    public boolean getIsDesc() {
        return isDesc;
    }

    /**
     * 是否降序
     */
    public void setIsDesc(boolean desc) {
        isDesc = desc;
    }
}
