package project.extension.number;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 浮点型数据拓展方法
 *
 * @author LCTR
 * @date 2022-05-16
 */
public class DecimalExtension {
    /**
     * 保留小数点后指定位数
     *
     * @param value     数据
     * @param precision 小数点后位数
     * @return 数据
     */
    public static BigDecimal round(double value,
                                   int precision) {
        return new BigDecimal(value).setScale(precision,
                                              RoundingMode.HALF_UP);
    }
}
