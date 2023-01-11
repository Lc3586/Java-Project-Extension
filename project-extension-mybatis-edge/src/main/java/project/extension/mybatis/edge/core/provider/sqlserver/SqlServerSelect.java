package project.extension.mybatis.edge.core.provider.sqlserver;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.config.DataSourceConfig;
import project.extension.mybatis.edge.core.ado.INaiveAdo;
import project.extension.mybatis.edge.core.mapper.EntityTypeHandler;
import project.extension.mybatis.edge.core.provider.normal.Select;
import project.extension.mybatis.edge.model.OrderMethod;
import project.extension.standard.exception.ModuleException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * SqlServer数据查询对象
 *
 * @param <T> 实体类型
 * @author LCTR
 * @date 2022-07-15
 */
public class SqlServerSelect<T>
        extends Select<T> {
    public SqlServerSelect(DataSourceConfig config,
                           INaiveAdo ado,
                           Class<T> entityType) {
        super(config,
              new SqlServerSqlProvider(config),
              ado,
              entityType);
        this.config = config;
    }

    private final DataSourceConfig config;

    /**
     * 如果没有设置排序条件则设置个默认的排序条件
     */
    private void checkAndSetDefaultOrderBy(
            @Nullable
                    String fieldName) {
        if (!StringUtils.hasText(orderBy.getDynamicOrder()
                                        .getFieldName())
                && orderBy.getDynamicOrder()
                          .getAdvancedOrder()
                          .size() == 0) {

            if (fieldName != null) {
                //使用自定义查询字段作为默认的排序条件
                orderBy.getDynamicOrder()
                       .setFieldName(fieldName);
                orderBy.getDynamicOrder()
                       .setMethod(OrderMethod.ASC);
            } else if (executor.getCustomFieldNames()
                               .size() > 0) {
                //使用自定义查询字段作为默认的排序条件
                orderBy.getDynamicOrder()
                       .setFieldName(executor.getCustomFieldNames()
                                             .get(0));
                orderBy.getDynamicOrder()
                       .setMethod(OrderMethod.ASC);
            } else {
                Map<String, String> primaryKey = EntityTypeHandler.getPrimaryKeyFieldNameWithColumns(entityType,
                                                                                                     config.getNameConvertType());
                if (primaryKey.size() > 0) {
                    //使用主键作为默认的排序条件
                    orderBy.getDynamicOrder()
                           .setFieldName(CollectionsExtension.firstKey(primaryKey));
                    orderBy.getDynamicOrder()
                           .setMethod(OrderMethod.ASC);
                } else {
                    //使用任意一个列作为默认的排序条件
                    List<Field> fields = EntityTypeHandler.getColumnFieldsByEntityType(entityType,
                                                                                       false);
                    orderBy.getDynamicOrder()
                           .setFieldName(fields.get(0)
                                               .getName());
                    orderBy.getDynamicOrder()
                           .setMethod(OrderMethod.ASC);
                }
            }
        }
    }

    @Override
    public List<T> toList()
            throws
            ModuleException {
        checkAndSetDefaultOrderBy(null);

        return super.toList();
    }

    @Override
    public <T2> List<T2> toList(Class<T2> dtoType)
            throws
            ModuleException {
        checkAndSetDefaultOrderBy(null);

        return super.toList(dtoType);
    }

    @Override
    public List<Map<String, Object>> toMapList()
            throws
            ModuleException {
        checkAndSetDefaultOrderBy(null);

        return super.toMapList();
    }

    @Override
    public T first()
            throws
            ModuleException {
        checkAndSetDefaultOrderBy(null);

        return super.first();
    }

    @Override
    public <T2> T2 first(Class<T2> dtoType)
            throws
            ModuleException {
        checkAndSetDefaultOrderBy(null);

        return super.first(dtoType);
    }

    @Override
    public <C> C first(String fieldName,
                       Class<C> memberType)
            throws
            ModuleException {
        checkAndSetDefaultOrderBy(fieldName);

        return super.first(fieldName,
                           memberType);
    }

    @Override
    public Map<String, Object> firstMap()
            throws
            ModuleException {
        checkAndSetDefaultOrderBy(null);

        return super.firstMap();
    }
}