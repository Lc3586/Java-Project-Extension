package project.extension.mybatis.edge.model;

import lombok.Data;

import java.util.*;

/**
 * 数据库表结构信息
 *
 * @author LCTR
 * @date 2022-06-10
 */
@Data
public class DbTableInfo {
    public DbTableInfo() {

    }

    public DbTableInfo(String id,
                       String schema,
                       String name,
                       String comment,
                       DbTableType type) {
        this.id = id;
        this.schema = schema;
        this.name = name;
        this.comment = comment;
        this.type = type;
    }

    /**
     * 唯一标识
     */
    private String id;

    /**
     * SqlServer下是Owner、PostgreSQL下是Schema、MySql下是数据库名
     */
    private String schema;

    /**
     * 表名
     */
    private String name;

    /**
     * 表备注，SqlServer下是扩展属性 MS_Description
     */
    private String comment;

    /**
     * 表/视图
     */
    private DbTableType type;

    /**
     * 列集合
     */
    private final List<DbColumnInfo> columns = new ArrayList<>();

    /**
     * 自增列集合
     */
    private final List<DbColumnInfo> identities = new ArrayList<>();

    /**
     * 主键/联合主键列集合
     */
    private final List<DbColumnInfo> primaries = new ArrayList<>();

    /**
     * 唯一键/组合集合
     * <p>key: 名称</p>
     */
    private final Map<String, DbIndexInfo> uniquesDict = new HashMap<>();

    /**
     * 索引/组合集合
     * <p>key: 名称</p>
     */
    private final Map<String, DbIndexInfo> indexesDict = new HashMap<>();

    /**
     * 外键集合
     * <p>key: 名称</p>
     */
    private final Map<String, DbForeignInfo> foreignsDict = new HashMap<>();

    /**
     * 唯一键/组合集合
     */
    private final List<DbIndexInfo> uniques = new ArrayList<>(getUniquesDict().values());

    /**
     * 索引/组合集合
     */
    private final List<DbIndexInfo> indexes = new ArrayList<>(getIndexesDict().values());

    /**
     * 外键集合
     */
    private final List<DbForeignInfo> foreigns = new ArrayList<>(getForeignsDict().values());
}
