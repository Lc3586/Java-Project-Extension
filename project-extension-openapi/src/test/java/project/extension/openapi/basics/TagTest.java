package project.extension.openapi.basics;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * 注解标签测试
 *
 * @author LCTR
 * @date 2022-03-23
 */
public class TagTest {
    /**
     * 测试主标签
     */
    @Test
    public void mainTag()
            throws
            Throwable {
        System.out.println("Test mainTag.");
        Class<?> clazz = Class.forName("project.extension.openapi.example.ExtensionModel.List");

        Class<?> c = Class.forName("project.extension.openapi.extention.SchemaExtension");
        Method m = c.getDeclaredMethod("getMainTag",
                                       Class.forName("java.lang.Class"));
        m.setAccessible(true);

        String[] mainTag = (String[]) m.invoke(null,
                                               clazz);
        Assert.assertArrayEquals(new String[]{"List"},
                                 mainTag);
    }
}
