package project.extension.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 获取进程标识
 *
 * @author LCTR
 * @date 2022-12-14
 */
public class ProcessIdConverter
        extends ClassicConverter {
    public static String getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMXBean.getName()
                            .split("@")[0];
    }

    @Override
    public String convert(final ILoggingEvent event) {
        // for every logging event return processId from mx bean
        // (or better alternative)
        return getProcessID();
    }
}
