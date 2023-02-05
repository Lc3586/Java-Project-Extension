package project.extension.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 应用容器拓展方法
 *
 * @author LCTR
 * @date 2022-05-27
 */
@Configuration
public class IOCExtension {
    /**
     * 应用上下文
     */
    public static ApplicationContext applicationContext;

    @Autowired
    public void init(ApplicationContext _applicationContext) {
        applicationContext = _applicationContext;
    }
}
