package project.extension.mybatis.edge.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表索引信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
public class DbIndexInfo {
    public DbIndexInfo() {

    }

    public DbIndexInfo(String name,
                       boolean isUnique) {
        this.name = name;
        this.isUnique = isUnique;
    }

    /**
     * 名称
     */
    private String name;

    /**
     * 列信息集合
     */
    private final List<DbIndexColumnInfo> columns = new ArrayList<>();

    /**
     * 是否唯一
     */
    private boolean isUnique;

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 列信息集合
     */
    public List<DbIndexColumnInfo> getColumns() {
        return columns;
    }

    /**
     * 是否唯一
     */
    public boolean getIsUnique() {
        return isUnique;
    }

    /**
     * 是否唯一
     */
    public void setIsUnique(boolean unique) {
        isUnique = unique;
    }
}
