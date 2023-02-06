package project.extension.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 应用容器拓展方法
 *
 * <p>需要先添加 @DependsOn("IOCExtension")注解</p>
 *
 * @author LCTR
 * @date 2022-05-27
 */
@Component("iocExtension")
public class IOCExtension {
    public IOCExtension(ApplicationContext _applicationContext) {
        applicationContext = _applicationContext;
    }

    /**
     * 应用上下文
     */
    public static ApplicationContext applicationContext;
}
