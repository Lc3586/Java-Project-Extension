package project.extension.date;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import project.extension.number.DecimalExtension;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期拓展方法
 *
 * @author LCTR
 * @date 2022-04-01
 */
public class DateExtension {
    /**
     * 日期格式化
     *
     * @param field 字段
     * @param date  日期
     * @return 格式化字符串
     */
    public static String format(Field field,
                                Date date) {
        String format = null;
        JsonFormat jsonFormat = AnnotationUtils.findAnnotation(field,
                                                               JsonFormat.class);
        if (jsonFormat != null && StringUtils.hasText(jsonFormat.pattern())) format = jsonFormat.pattern();
        else {
            JSONField jsonField = AnnotationUtils.findAnnotation(field,
                                                                 JSONField.class);
            if (jsonField != null && StringUtils.hasText(jsonField.format())) format = jsonField.format();
        }
        if (!StringUtils.hasText(format)) format = "yyyy-MM-dd HH:mm:ss";
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 获取时间轴对应的毫秒数
     *
     * @param time 时间轴 00:00:00或者00:00:00.0010000
     * @return 毫秒数
     */
    public static Long getTimeMilliseconds(String time) {
        String[] times = time.split("[:.]");
        //小时数转毫秒数
        long milliseconds = Long.parseLong(times[0]) * 60 * 60 * 1000;
        //分钟数转毫秒数
        milliseconds += Long.parseLong(times[1]) * 60 * 1000;
        //秒数转毫秒数
        milliseconds += Long.parseLong(times[2]) * 60 * 1000;
        //毫秒数
        if (times.length > 3)
            milliseconds += Long.parseLong(times[3]);

        return milliseconds;
    }

    /**
     * 获取时间说明
     *
     * @param time      毫秒总数
     * @param precision 精度
     */
    public static String getTime(long time,
                                 int precision) {
        if (time <= 0) return "0 ms";

        String[] formats = new String[]{"毫秒",
                                        "秒",
                                        "分钟",
                                        "小时",
                                        "天",
                                        "星期"};

        for (int i = 0; i < formats.length; i++) {
            int unit = i == 0
                       ? 1000
                       : 60;

            double value;
            if (i == 0)
                value = time / 1000d;
            else
                value = time / 1000d / Math.pow(60,
                                                i + 1);

            if (value < unit)
                return String.format("%s %s",
                                     DecimalExtension.round(value,
                                                            precision),
                                     formats[i]);
        }

        return String.format("%s %s",
                             DecimalExtension.round(time / 1000d / Math.pow(60,
                                                                            formats.length),
                                                    precision),
                             formats[formats.length - 1]);
    }
}
