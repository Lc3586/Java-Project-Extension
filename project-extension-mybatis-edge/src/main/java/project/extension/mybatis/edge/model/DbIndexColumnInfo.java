package project.extension.mybatis.edge.model;

import lombok.Data;

/**
 * 数据库表索引相关的列信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
@Data
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
}
