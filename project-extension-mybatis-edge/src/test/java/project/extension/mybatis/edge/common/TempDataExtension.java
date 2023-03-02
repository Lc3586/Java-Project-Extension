package project.extension.mybatis.edge.common;

import org.junit.jupiter.api.Assertions;
import org.junit.platform.commons.util.ClassLoaderUtils;
import project.extension.collections.CollectionsExtension;
import project.extension.mybatis.edge.annotations.ColumnSetting;
import project.extension.mybatis.edge.core.provider.standard.INaiveSql;
import project.extension.mybatis.edge.configure.TestDataSourceConfigure;
import project.extension.mybatis.edge.extention.EntityExtension;
import project.extension.standard.exception.BusinessException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.*;

/**
 * 临时数据扩展方法
 *
 * @author LCTR
 * @date 2023-01-11
 */
public class TempDataExtension {
    /**
     * 各数据源的临时数据
     */
    private static final Map<String, Map<Class, Map<Object, Object>>> tempData = new HashMap<>();

    /**
     * 当前开启事务的线程
     */
    private static final Map<Long, String> threadTransactionMap = new HashMap<>();

    /**
     * 用于测试的字符值
     */
    private static final char[] charsValue = new char[]
            {
                    'a',
                    'b',
                    'c',
                    'd',
                    'e',
                    'f',
                    'g',
                    'h',
                    'i',
                    'j',
                    'k',
                    'l',
                    'm',
                    'n',
                    'o',
                    'p',
                    'q',
                    'r',
                    's',
                    't',
                    'u',
                    'v',
                    'w',
                    'x',
                    'y',
                    'z',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9',
                    '0',
                    '!',
                    '@',
                    '$',
                    '%',
                    '^',
                    '&',
                    '*',
                    '(',
                    ')',
                    '~',
                    '`',
                    '·',
                    '！',
                    '￥',
                    '…',
                    '（',
                    '）',
                    '_',
                    '+',
                    '-',
                    '=',
                    '。',
                    '.',
                    '?',
                    '？',
                    '¿',
                    '；',
                    '’',
                    '\'',
                    '[',
                    ']',
                    '【',
                    '】',
                    '\\',
                    '|',
                    '/',
                    ',',
                    '，',
                    ':',
                    '：',
                    '{',
                    '}',
                    '▁',
                    '▂',
                    '▃',
                    '▄',
                    '▅',
                    '▆',
                    '▇',
                    '█',
                    '№',
                    '↑',
                    '→',
                    '↘',
                    '↓',
                    '↙',
                    '←',
                    '㊣',
                    '∮',
                    '♂',
                    '♀',
                    '∞',
                    'ㄨ',
                    '╬',
                    '╭',
                    '╮',
                    '╰',
                    '╱',
                    '╲',
                    '＠',
                    'ξ',
                    'ζ',
                    'ω',
                    '々',
                    '√',
                    '①',
                    '¤',
                    '★',
                    '☆',
                    '■',
                    '▓',
                    '☉',
                    '⊙',
                    '●',
                    '〇',
                    '◎',
                    '๑',
                    '♬',
                    '✿',
                    '☂',
                    '☃',
                    '☄',
                    '☒',
                    '☢',
                    '☺',
                    '☻',
                    '♣',
                    '♠',
                    '♦',
                    'ஐ',
                    'ﻬ',
                    '☎',
                    '☏',
                    '♨',
                    '㊝',
                    '♈'};

    /**
     * 获取数据
     *
     * @param name  数据源名称
     * @param type  实体类型
     * @param first 获取首条数据
     * @param key   数据标识
     * @param <T>   实体类型
     * @return 数据
     */
    private static <T> T getData(String name,
                                 Class<T> type,
                                 Boolean first,
                                 Object key) {
        Assertions.assertTrue(tempData.containsKey(name),
                              String.format("%s数据源下没有数据",
                                            name));

        Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);
        Assertions.assertTrue(dataOfType.containsKey(type),
                              String.format("%s数据源下没有%s类型的数据",
                                            name,
                                            type.getName()));

        Map<Object, Object> dataOfKey = dataOfType.get(type);

        Object data;

        if (first)
            data = CollectionsExtension.firstValue(dataOfKey);
        else
            data = dataOfKey.get(key);

        Assertions.assertNotNull(data,
                                 String.format("%s数据源下没有%s类型的key为%s的数据",
                                               name,
                                               type.getName(),
                                               key));

        return (T) data;
    }

    /**
     * 生成测试数据
     *
     * @param type 数据类型
     * @param <T>  数据类型
     * @return 自动填充了数据的对象
     */
    public static <T> T generateData(Class<T> type) {
        try {
            T data = type.newInstance();

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                ColumnSetting columnSetting = field.getAnnotation(ColumnSetting.class);

                if (columnSetting != null
                        && (columnSetting.isPrimaryKey()
                        || columnSetting.isIdentity()
                        || columnSetting.isIgnore()))
                    continue;

                if (Character.class.equals(field.getType())
                        || char.class.equals(field.getType())) {
                    if (columnSetting == null)
                        field.set(data,
                                  '6');
                    else
                        field.set(data,
                                  charsValue[new Random().nextInt(charsValue.length - 1)]);
                } else if (String.class.equals(field.getType())) {
                    if (columnSetting == null)
                        field.set(data,
                                  "6");
                    else {
                        int length = columnSetting.length();
                        if (length == 36) {
                            field.set(data,
                                      UUID.randomUUID()
                                          .toString());
                            continue;
                        }

                        if (length <= 0)
                            length = 100000;

                        char[] chars = new char[length];
                        Random random = new Random();
                        for (int i = 0; i < length; i++) {
                            chars[i] = charsValue[random.nextInt(charsValue.length - 1)];
                        }
                        field.set(data,
                                  new String(chars));
                    }
                } else if (UUID.class.equals(field.getType())) {
                    field.set(data,
                              UUID.randomUUID());
                } else if (Boolean.class.equals(field.getType())
                        || boolean.class.equals(field.getType())) {
                    field.set(data,
                              new Random().nextBoolean());
                } else if (Byte.class.equals(field.getType())
                        || byte.class.equals(field.getType())) {
                    field.set(data,
                              new Random().nextBoolean()
                              ? Byte.MAX_VALUE
                              : Byte.MIN_VALUE);
                } else if (Short.class.equals(field.getType())
                        || short.class.equals(field.getType())) {
                    field.set(data,
                              new Random().nextBoolean()
                              ? Short.MAX_VALUE
                              : Short.MIN_VALUE);
                } else if (Integer.class.equals(field.getType())
                        || int.class.equals(field.getType())) {
                    field.set(data,
                              new Random().nextBoolean()
                              ? Integer.MAX_VALUE
                              : Integer.MIN_VALUE);
                } else if (Long.class.equals(field.getType())
                        || long.class.equals(field.getType())) {
                    field.set(data,
                              new Random().nextBoolean()
                              ? Long.MAX_VALUE
                              : Long.MIN_VALUE);
                } else if (Float.class.equals(field.getType())
                        || float.class.equals(field.getType())) {
                    if (columnSetting == null || columnSetting.precision() == 0)
                        field.set(data,
                                  new Random().nextBoolean()
                                  ? Float.MAX_VALUE
                                  : Float.MIN_VALUE);
                    else {
                        int precision = columnSetting.precision();
                        int scale = columnSetting.scale();
                        char[] integer = new char[precision - scale];
                        Arrays.fill(integer,
                                    '9');
                        char[] decimal = new char[scale];
                        Arrays.fill(decimal,
                                    '9');
                        field.set(data,
                                  new Float(String.format("%s.%s",
                                                          new String(integer),
                                                          new String(decimal))));
                    }
                } else if (Double.class.equals(field.getType())
                        || double.class.equals(field.getType())) {
                    if (columnSetting == null || columnSetting.precision() == 0)
                        field.set(data,
                                  new Random().nextBoolean()
                                  ? Double.MAX_VALUE
                                  : Double.MIN_VALUE);
                    else {
                        int precision = columnSetting.precision();
                        int scale = columnSetting.scale();
                        char[] integer = new char[precision - scale];
                        Arrays.fill(integer,
                                    '9');
                        char[] decimal = new char[scale];
                        Arrays.fill(decimal,
                                    '9');
                        field.set(data,
                                  new Double(String.format("%s.%s",
                                                           new String(integer),
                                                           new String(decimal))));
                    }
                } else if (BigDecimal.class.equals(field.getType())) {
                    if (columnSetting == null || columnSetting.precision() == 0)
                        field.set(data,
                                  new BigDecimal(new Random().nextBoolean()
                                                 ? Long.MAX_VALUE
                                                 : Long.MIN_VALUE));
                    else {
                        int precision = columnSetting.precision();
                        int scale = columnSetting.scale();
                        char[] integer = new char[precision - scale];
                        Arrays.fill(integer,
                                    '9');
                        char[] decimal = new char[scale];
                        Arrays.fill(decimal,
                                    '9');
                        field.set(data,
                                  new BigDecimal(String.format("%s.%s",
                                                               new String(integer),
                                                               new String(decimal))));
                    }
                } else if (java.sql.Date.class.equals(field.getType())) {
                    field.set(data,
                              new java.sql.Date(new java.sql.Timestamp(System.currentTimeMillis()).getTime()));
                } else if (Time.class.equals(field.getType())) {
                    field.set(data,
                              new Time(new java.sql.Timestamp(System.currentTimeMillis()).getTime()));
                } else if (Date.class.equals(field.getType())) {
                    field.set(data,
                              new Date(System.currentTimeMillis()));
                } else if (byte[].class.equals(field.getType())) {
                    //data:image/gif;base64,
                    String base64 = readBase64FromFile("file/gif_base64");
                    byte[] bytes = base64.getBytes(StandardCharsets.UTF_8);
                    field.set(data,
                              bytes);
                }
            }

            return new EntityExtension(null).initialization(data);
        } catch (Exception ex) {
            BusinessException businessException = new BusinessException(String.format("生成%s类型的数据失败",
                                                                                      type.getName()),
                                                                        ex);
            Assertions.fail(businessException.getMessage(),
                            businessException);
            throw businessException;
        }
    }

    /**
     * 从文件中读取base64编码数据
     *
     * @param filePath 文件路径
     * @return base64编码数据
     */
    public static String readBase64FromFile(String filePath) {
        try {
            //读取文件Base64编码数据
            Path faceBase64Path = Paths.get(Objects.requireNonNull(ClassLoaderUtils.getDefaultClassLoader()
                                                                                   .getResource(filePath))
                                                   .toURI());
            return String.join("",
                               Files.readAllLines(faceBase64Path,
                                                  StandardCharsets.UTF_8));
        } catch (Exception ex) {
            BusinessException businessException = new BusinessException(String.format("读取%s文件的base64数据失败失败",
                                                                                      filePath),
                                                                        ex);
            Assertions.fail(businessException.getMessage(),
                            businessException);
            throw businessException;
        }
    }

    /**
     * 添加数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     * @param data 数据
     */
    public static void putData(String name,
                               Class type,
                               Object key,
                               Object data) {
        if (!tempData.containsKey(name))
            tempData.put(name,
                         new HashMap<>());

        Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);
        if (!dataOfType.containsKey(type))
            dataOfType.put(type,
                           new HashMap<>());

        dataOfType.get(type)
                  .put(key,
                       data);
    }

    /**
     * 获取第一条数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     */
    public static <T> T getFirstData(String name,
                                     Class<T> type) {
        return getData(name,
                       type,
                       true,
                       null);
    }

    /**
     * 获取数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     */
    public static <T> T getData(String name,
                                Class<T> type,
                                Object key) {
        return getData(name,
                       type,
                       false,
                       key);
    }

    /**
     * 移除数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     */
    public static void removeData(String name,
                                  Class type,
                                  Object key) {
        if (!tempData.containsKey(name))
            return;

        Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);
        if (!dataOfType.containsKey(type))
            return;

        dataOfType.get(type)
                  .remove(key);
    }

    /**
     * 清理数据
     *
     * @param name 数据源名称
     * @param type 实体类型
     * @param key  数据标识
     */
    public static void cleanData(String name,
                                 Class type,
                                 Object key) {
        Object data = getData(name,
                              type,
                              false,
                              key);

        INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

        int rowsDelete = naiveSql.delete(type,
                                         data)
                                 .executeAffrows();

        Assertions.assertNotEquals(-1,
                                   rowsDelete,
                                   "清理数据失败");

        removeData(name,
                   type,
                   key);

        System.out.printf("\r\n已清理数据，key：%s\r\n",
                          key);
    }

    /**
     * 清理全部数据
     */
    public static void clearUp() {
        for (String name : TestDataSourceConfigure.getMultiTestDataSourceName()) {
            if (!tempData.containsKey(name))
                return;

            Map<Class, Map<Object, Object>> dataOfType = tempData.get(name);

            if (dataOfType.size() == 0)
                return;

            INaiveSql naiveSql = OrmObjectResolve.getOrm(TestDataSourceConfigure.getTestDataSource(name));

            for (Class type : dataOfType.keySet()) {
                Map<Object, Object> dataOfKey = dataOfType.get(type);

                if (dataOfKey.size() == 0)
                    return;

                List<Object> data = new ArrayList<>(dataOfKey.values());

                int rowsDelete = naiveSql.batchDelete(type,
                                                      data)
                                         .executeAffrows();

                Assertions.assertNotEquals(-1,
                                           rowsDelete,
                                           "清理数据失败");

                for (Object key : dataOfKey.keySet()) {
                    System.out.printf("\r\n已清理数据，key：%s\r\n",
                                      key);
                }
            }
        }
    }

    /**
     * 新增事务
     *
     * @param threadId 线程标识
     * @param name     数据源名称
     */
    public static void putThreadTransaction(Long threadId,
                                            String name) {
        threadTransactionMap.put(threadId,
                                 name);
    }

    /**
     * 获取事务对应的数据源名称
     *
     * @param threadId 线程标识
     * @return 数据源名称
     */
    public static String getThreadTransaction(Long threadId) {
        return threadTransactionMap.get(threadId);
    }
}
