package project.extension.redis.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class TestDTO {
    public TestDTO() {

    }

    public static TestDTO getInstance() {
        TestDTO data = new TestDTO();
        data.setString("123456");
        data.setInteger(123456);
        data.setDecimal(new BigDecimal("1234.56"));
        data.setDate(new Date());
        data.setStrings(new String[]{"123456",
                                     "789456",
                                     "0"});
        data.setList(Arrays.asList("123456",
                                   "789456",
                                   "0"));
        return data;
    }

    /**
     * 字符串
     */
    private String string;

    /**
     * 数字
     */
    private Integer integer;

    /**
     * 浮点数
     */
    private BigDecimal decimal;

    /**
     * 日期
     */
    private Date date;

    /**
     * 数组
     */
    private String[] strings;

    /**
     * 集合
     */
    private List<String> list;
}
