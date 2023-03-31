package project.extension.wechat.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import project.extension.wechat.core.NaiveWeChatServiceProvider;
import project.extension.wechat.core.mp.servlet.WeChatMpEndpointServlet;
import project.extension.wechat.core.mp.servlet.WeChatOAuth2Servlet;
import project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 服务配置
 *
 * @author LCTR
 * @date 2023-03-24
 */
@Component
@DependsOn("naiveWeChatServiceProvider")
public class ServletConfig
        implements ServletContextInitializer {
    public ServletConfig(WeChatBaseConfig weChatBaseConfig) {
        this.weChatBaseConfig = weChatBaseConfig;
    }

    private final WeChatBaseConfig weChatBaseConfig;

    @Override
    public void onStartup(ServletContext servletContext) {
        if (weChatBaseConfig.isEnableMpEndpointServlet()) {
            ServletRegistration servletRegistration = servletContext.addServlet(NaiveWeChatServiceProvider.getServletBeanName(WeChatMpEndpointServlet.class),
                                                                                WeChatMpEndpointServlet.class);
            for (String urlPattern : WeChatMpEndpointServlet.getAllUrlPattern()) {
                servletRegistration.addMapping(urlPattern);
            }
        }

        if (weChatBaseConfig.isEnableOAuth2Servlet()) {
            ServletRegistration servletRegistration = servletContext.addServlet(NaiveWeChatServiceProvider.getServletBeanName(WeChatOAuth2Servlet.class),
                                                                                WeChatOAuth2Servlet.class);
            for (String urlPattern : WeChatOAuth2Servlet.getAllUrlPattern()) {
                servletRegistration.addMapping(urlPattern);
            }
        }

        if (weChatBaseConfig.isEnablePayNotifyServlet()) {
            ServletRegistration servletRegistration = servletContext.addServlet(NaiveWeChatServiceProvider.getServletBeanName(WeChatPayNotifyServlet.class),
                                                                                WeChatPayNotifyServlet.class);
            for (String urlPattern : WeChatPayNotifyServlet.getAllUrlPattern()) {
                servletRegistration.addMapping(urlPattern);
            }
        }
    }
}
