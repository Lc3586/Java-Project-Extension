package project.extension.mybatis.edge.extention;

import org.springframework.core.annotation.AnnotationUtils;
import project.extension.Identity.SnowFlake;
import project.extension.date.DateExtension;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.globalization.DbContextStrings;
import project.extension.standard.authentication.IAuthenticationService;
import project.extension.standard.entity.Base_Fields;
import project.extension.standard.entity.IEntityExtension;
import project.extension.standard.exception.ApplicationException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * 实体类拓展方法
 *
 * @author LCTR
 * @date 2022-12-08
 */
public class EntityExtension
        implements IEntityExtension {
    public EntityExtension(IAuthenticationService authenticationService) {
        this(authenticationService,
             new String[]{Base_Fields.createBy},
             new String[]{Base_Fields.createTime},
             new String[]{Base_Fields.updateBy},
             new String[]{Base_Fields.updateTime});
    }

    /**
     * @param createByFields   创建者字段
     * @param createTimeFields 创建时间字段
     * @param updateByFields   修改者字段
     * @param updateTimeFields 修改时间字段
     */
    public EntityExtension(IAuthenticationService authenticationService,
                           String[] createByFields,
                           String[] createTimeFields,
                           String[] updateByFields,
                           String[] updateTimeFields) {
        this.authenticationService = authenticationService;
        this.createByFields = createByFields;
        this.createTimeFields = createTimeFields;
        this.updateByFields = updateByFields;
        this.updateTimeFields = updateTimeFields;
    }

    /**
     * 创建者字段
     */
    private final String[] createByFields;

    /**
     * 创建时间字段
     */
    private final String[] createTimeFields;

    /**
     * 修改者字段
     */
    private final String[] updateByFields;

    /**
     * 修改时间字段
     */
    private final String[] updateTimeFields;

    /**
     * 雪花Id
     */
    private final SnowFlake snowFlake = new SnowFlake(1,
                                                      1);

    /**
     * 身份验证服务
     */
    private final IAuthenticationService authenticationService;

    /**
     * 获取操作者的用户标识
     *
     * @return 操作者标识
     */
    private Object getOperatorOperatorKey() {
        return authenticationService.getOperator()
                                    .getKey();
    }

    /**
     * 获取日期值
     *
     * @param field 字段
     * @param date  日期
     * @return 值
     */
    private Object getDateValue(Field field,
                                Date date)
            throws
            ApplicationException {
        Class<?> fieldType = field.getType();
        if (fieldType.equals(Date.class)) return new Date();
        else if (fieldType.equals(Long.class)) return new Date().getTime();
        else if (fieldType.equals(String.class)) {
            return DateExtension.format(field,
                                        date);
        } else
            throw new ApplicationException(DbContextStrings.getUnsupportedDateType(fieldType.getTypeName()));
    }

    /**
     * 设置主键
     *
     * @param entity 实体
     */
    private <T> void setPrimaryKey(T entity)
            throws
            ApplicationException {
        Class<?> classz = entity.getClass();

        while (true) {
            for (Field field : classz.getDeclaredFields()) {
                ColumnSetting columnSettingAttribute = AnnotationUtils.findAnnotation(field,
                                                                                      ColumnSetting.class);
                if (columnSettingAttribute != null && columnSettingAttribute.primaryKey()) {
                    field.setAccessible(true);
                    Class<?> fieldType = field.getType();
                    if (fieldType.equals(Long.class)) {
                        try {
                            field.set(entity,
                                      newLongId());
                        } catch (IllegalAccessException ex) {
                            throw new ApplicationException(DbContextStrings.getEntityInitializationFailed(),
                                                           ex);
                        }
                    } else if (fieldType.equals(String.class)) {
                        try {
                            field.set(entity,
                                      columnSettingAttribute.length() == 36
                                      ? newStringId()
                                      : newStringId("O"));
                        } catch (Throwable ex) {
                            throw new ApplicationException(DbContextStrings.getEntityInitializationFailed(),
                                                           ex);
                        }
                    } else
                        throw new ApplicationException(DbContextStrings.getUnsupportedDataType4PrimaryKey(fieldType.getTypeName()));
                }
            }

            //获取继承类
            Class<?> superClazz = classz.getSuperclass();
            if (superClazz == null || superClazz.equals(Object.class)) break;
            else classz = superClazz;
        }
    }

    /**
     * 设置操作人信息
     *
     * @param entity          实体
     * @param setUserIdFields 需要写入用户Id的字段
     * @param setDateFields   需要写入时间的字段
     * @param <T>             实体类型
     */
    private <T> void setOperatorInfo(T entity,
                                     String[] setUserIdFields,
                                     String[] setDateFields)
            throws
            ApplicationException {
        Class<?> classz = entity.getClass();

        while (true) {
            for (Field field : classz.getDeclaredFields()) {
                if (Arrays.stream(setUserIdFields)
                          .anyMatch(name -> field.getName()
                                                 .equalsIgnoreCase(name))) {
                    field.setAccessible(true);
                    try {
                        field.set(entity,
                                  getOperatorOperatorKey());
                    } catch (Exception ignore) {

                    }
                } else if (Arrays.stream(setDateFields)
                                 .anyMatch(name -> field.getName()
                                                        .equalsIgnoreCase(name))) {
                    field.setAccessible(true);
                    try {
                        field.set(entity,
                                  getDateValue(field,
                                               new Date()));
                    } catch (IllegalAccessException ex) {
                        throw new ApplicationException(DbContextStrings.getSetupOperatorTimeFailed(),
                                                       ex);
                    }
                }
            }

            //获取继承类
            Class<?> superClazz = classz.getSuperclass();
            if (superClazz == null || superClazz.equals(Object.class)) break;
            else classz = superClazz;
        }
    }

    public String newStringId(String format) {
        return snowFlake.nextId2String(format);
    }

    public String newStringId() {
        return newStringId("D");
    }

    public Long newLongId() {
        return snowFlake.nextId();
    }

    public <T> T initialization(T entity)
            throws
            ApplicationException {
        setPrimaryKey(entity);
        setOperatorInfo(entity,
                        createByFields,
                        createTimeFields);
        return entity;
    }

    public <T> Collection<T> initialization(Collection<T> entities)
            throws
            ApplicationException {
        for (T entity : entities) {
            setPrimaryKey(entity);
            setOperatorInfo(entity,
                            createByFields,
                            createTimeFields);
        }
        return entities;
    }

    public <T> T modify(T entity)
            throws
            ApplicationException {
        setOperatorInfo(entity,
                        updateByFields,
                        updateTimeFields);
        return entity;
    }

    public <T> Collection<T> modify(Collection<T> entities)
            throws
            ApplicationException {
        for (T entity : entities) {
            setOperatorInfo(entity,
                            updateByFields,
                            updateTimeFields);
        }
        return entities;
    }
}
