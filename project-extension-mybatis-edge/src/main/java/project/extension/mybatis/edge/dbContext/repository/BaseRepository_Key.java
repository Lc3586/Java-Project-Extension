package project.extension.mybatis.edge.dbContext.repository;

import project.extension.collections.TupleExtension;
import project.extension.mybatis.edge.INaiveSql;
import project.extension.mybatis.edge.core.mapper.EntityTypeHandler;
import project.extension.mybatis.edge.globalization.Strings;
import project.extension.mybatis.edge.model.FilterCompare;
import project.extension.mybatis.edge.model.NullResultException;
import project.extension.standard.exception.ModuleException;
import project.extension.tuple.ITuple;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据仓储基础实现类
 *
 * @param <TEntity> 实体类型
 * @param <TKey>    主键类型
 * @author LCTR
 * @date 2022-03-29
 */
public class BaseRepository_Key<TEntity, TKey>
        extends BaseRepository<TEntity>
        implements IBaseRepository_Key<TEntity, TKey> {
    private final List<Field> keyFields;
    private final Class<TKey> keyType;
    private final String defaultNullErrorMessage = Strings.getDataUndefined();

    /**
     * @param orm        orm
     * @param entityType 实体类型
     * @param keyType    主键类型
     */
    public BaseRepository_Key(INaiveSql orm,
                              Class<TEntity> entityType,
                              Class<TKey> keyType)
            throws
            ModuleException {
        super(orm,
              entityType);
        this.keyFields = EntityTypeHandler.getPrimaryKeyField(entityType);
        if (this.keyFields.size() == 0) throw new ModuleException(Strings.getEntityPrimaryKeyUndefined());
        if (keyFields.size() > 1 && !ITuple.class.isAssignableFrom(keyType))
            throw new ModuleException(Strings.getEntityCompositePrimaryKeyMustBeTupleType());
        this.keyType = keyType;
    }

    /**
     * 获取主键值集合
     *
     * @param id 主键
     * @return 主键值集合
     */
    private List<Object> getKeyValues(TKey id) {
        List<Object> keyValues = new ArrayList<>();
        if (keyType.isAssignableFrom(ITuple.class)) keyValues.addAll(TupleExtension.tuple2List(id,
                                                                                               keyType));
        else keyValues.add(id);
        return keyValues;
    }

    @Override
    public TEntity getById(TKey id)
            throws
            Exception {
        return getById(id,
                       setting.getEntityType());
    }

    @Override
    public TEntity getByIdAndCheckNull(TKey id)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   defaultNullErrorMessage);
    }

    @Override
    public TEntity getByIdAndCheckNull(TKey id,
                                       String nullErrorMessage)
            throws
            Exception {
        TEntity result = getById(id);
        if (result == null) throw new Exception(nullErrorMessage);
        return result;
    }

    @Override
    public <TDto> TDto getById(TKey id,
                               Class<TDto> dtoType)
            throws
            Exception {
        return getById(id,
                       dtoType,
                       0);
    }

    @Override
    public <TDto> TDto getById(TKey id,
                               Class<TDto> dtoType,
                               int mainTagLevel)
            throws
            Exception {
        List<Object> keyValues = getKeyValues(id);
        return getOrm().select(setting.getEntityType())
                       .as("a")
                       .where(x -> {
                           for (int i = 0; i < keyFields.size(); i++) {
                               x.and(keyFields.get(i)
                                              .getName(),
                                     FilterCompare.Eq,
                                     keyValues.get(i));
                           }
                           return x;
                       })
                       .mainTagLevel(mainTagLevel)
                       .first(dtoType);
    }

    @Override
    public <TDto> TDto getByIdAndCheckNull(TKey id,
                                           Class<TDto> dtoType)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   dtoType,
                                   defaultNullErrorMessage);
    }

    @Override
    public <TDto> TDto getByIdAndCheckNull(TKey id,
                                           Class<TDto> dtoType,
                                           int mainTagLevel)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   dtoType,
                                   mainTagLevel,
                                   defaultNullErrorMessage);
    }

    @Override
    public <TDto> TDto getByIdAndCheckNull(TKey id,
                                           Class<TDto> dtoType,
                                           String nullErrorMessage)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   dtoType,
                                   0,
                                   nullErrorMessage);
    }

    @Override
    public <TDto> TDto getByIdAndCheckNull(TKey id,
                                           Class<TDto> dtoType,
                                           int mainTagLevel,
                                           String nullErrorMessage)
            throws
            Exception {
        TDto result = getById(id,
                              dtoType,
                              mainTagLevel);
        if (result == null) throw new NullResultException(nullErrorMessage);
        return result;
    }

    @Override
    public void deleteById(TKey id)
            throws
            Exception {
        deleteByIds(Collections.singleton(id));
    }

    @Override
    public void deleteByIds(Collection<TKey> ids)
            throws
            Exception {
        if (getOrm().delete(setting.getEntityType())
                    .where(x -> {
                        if (keyFields.size() == 1) {
                            //单个主键 id IN('1', '2')
                            x.and(keyFields.get(0)
                                           .getName(),
                                  FilterCompare.InSet,
                                  ids);
                        } else {
                            //联合主键 (ida='1' AND idb='1') OR (ida='2' AND idb='2')
                            for (TKey id : ids) {
                                List<Object> keyValues = getKeyValues(id);
                                x.and(y -> {
                                    for (int i = 0; i < keyFields.size(); i++) {
                                        y.and(keyFields.get(i)
                                                       .getName(),
                                              FilterCompare.Eq,
                                              keyValues.get(i));
                                    }
                                    return y;
                                });
                            }
                        }
                        return x;
                    })
                    .executeAffrows() < 0) throw new Exception("执行操作失败");
    }
}
