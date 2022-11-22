package project.extension.mybatis.core.repository;

import project.extension.collections.TupleExtension;
import project.extension.mybatis.config.BaseConfig;
import project.extension.mybatis.core.provider.standard.IBaseDbProvider;
import project.extension.mybatis.extention.RepositoryExtension;
import project.extension.mybatis.model.FilterCompare;
import project.extension.mybatis.model.NullResultException;
import project.extension.tuple.ITuple;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据仓储基础实现类
 *
 * @param <T>    数据类型
 * @param <TKey> 主键类型
 * @author LCTR
 * @date 2022-03-29
 */
public class BaseRepository_Key<T, TKey>
        extends BaseRepository<T>
        implements IBaseRepository_Key<T, TKey> {
    private final List<Field> keyFields;
    private final Class<TKey> keyType;
    private final String defaultNullErrorMessage = "数据不存在或已被移除";

    /**
     * @param entityType 实体类型
     * @param keyType    主键类型
     * @param dbProvider 基础构造器
     */
    public BaseRepository_Key(BaseConfig config,
                              Class<T> entityType,
                              Class<TKey> keyType,
                              IBaseDbProvider<T> dbProvider)
            throws
            Exception {
        super(config,
              entityType,
              dbProvider);
        this.keyFields = RepositoryExtension.getPrimaryKeyField(entityType);
        if (this.keyFields.size() == 0) throw new Exception("请在实体里设置主键@ColumnSetting(primaryKey = true)");
        if (keyFields.size() > 1 && !ITuple.class.isAssignableFrom(keyType))
            throw new Exception("实体存在联合主键时，主键类型必须为Tuple元组类型");
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
    public BaseRepository_Key<T, TKey> withTransactional(boolean withTransactional) {
        this.useTransactional = withTransactional;
        return this;
    }

    @Override
    public T getById(TKey id)
            throws
            Exception {
        return getById(id,
                       setting.getEntityType());
    }

    @Override
    public T getByIdAndCheckNull(TKey id)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   defaultNullErrorMessage);
    }

    @Override
    public T getByIdAndCheckNull(TKey id,
                                 String nullErrorMessage)
            throws
            Exception {
        T result = getById(id);
        if (result == null) throw new Exception(nullErrorMessage);
        return result;
    }

    @Override
    public <T2> T2 getById(TKey id,
                           Class<T2> dtoType)
            throws
            Exception {
        return getById(id,
                       dtoType,
                       0);
    }

    @Override
    public <T2> T2 getById(TKey id,
                           Class<T2> dtoType,
                           int mainTagLevel)
            throws
            Exception {
        List<Object> keyValues = getKeyValues(id);
        return dbProvider.createSelect(setting.getEntityType(),
                                       useTransactional)
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
    public <T2> T2 getByIdAndCheckNull(TKey id,
                                       Class<T2> dtoType)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   dtoType,
                                   defaultNullErrorMessage);
    }

    @Override
    public <T2> T2 getByIdAndCheckNull(TKey id,
                                       Class<T2> dtoType,
                                       int mainTagLevel)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   dtoType,
                                   mainTagLevel,
                                   defaultNullErrorMessage);
    }

    @Override
    public <T2> T2 getByIdAndCheckNull(TKey id,
                                       Class<T2> dtoType,
                                       String nullErrorMessage)
            throws
            Exception {
        return getByIdAndCheckNull(id,
                                   dtoType,
                                   0,
                                   nullErrorMessage);
    }

    @Override
    public <T2> T2 getByIdAndCheckNull(TKey id,
                                       Class<T2> dtoType,
                                       int mainTagLevel,
                                       String nullErrorMessage)
            throws
            Exception {
        T2 result = getById(id,
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
        if (dbProvider.createDelete(setting.getEntityType(),
                                    useTransactional)
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
