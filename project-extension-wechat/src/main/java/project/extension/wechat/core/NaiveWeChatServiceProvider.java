package project.extension.wechat.core;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import project.extension.ioc.IOCExtension;
import project.extension.standard.exception.ModuleException;
import project.extension.wechat.config.WeChatBaseConfig;
import project.extension.wechat.config.MpConfig;
import project.extension.wechat.config.PayConfig;
import project.extension.wechat.core.mp.servlet.WeChatMpEndpointServlet;
import project.extension.wechat.core.mp.servlet.WeChatOAuth2Servlet;
import project.extension.wechat.core.mp.standard.IWeChatMpService;
import project.extension.wechat.core.mp.normal.WeChatMpService;
import project.extension.wechat.core.pay.servlet.WeChatPayNotifyServlet;
import project.extension.wechat.core.pay.standard.IWeChatPayService;
import project.extension.wechat.core.pay.normal.WeChatPayService;
import project.extension.wechat.globalization.Strings;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信服务构造器实现类
 *
 * @author LCTR
 * @date 2023-03-15
 */
@SuppressWarnings("SpringDependsOnUnresolvedBeanInspection")
@Configuration
@EnableConfigurationProperties({WeChatBaseConfig.class})
@DependsOn("iocExtension")
public class NaiveWeChatServiceProvider
        implements INaiveWeChatServiceProvider {
    public NaiveWeChatServiceProvider(WeChatBaseConfig weChatBaseConfig) {
        this.weChatBaseConfig = weChatBaseConfig;
        this.loadAllWeChatMpService();
        this.loadAllWeChatPayService();
    }

    private final WeChatBaseConfig weChatBaseConfig;

    private final Map<String, IWeChatMpService> mpServiceMap = new HashMap<>();

    private final Map<String, IWeChatPayService> payServiceMap = new HashMap<>();

    private DefaultListableBeanFactory beanFactory;

    /**
     * 容器工厂
     */
    private DefaultListableBeanFactory getBeanFactory() {
        if (beanFactory == null)
            beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) IOCExtension.applicationContext).getBeanFactory();
        return beanFactory;
    }

    /**
     * 加载并注册微信公众号服务
     *
     * @param mp                              公众号名称
     * @param endpointServletRegistrationBean 微信终端服务注册类
     * @param oAuth2ServletRegistrationBean   微信网页授权服务注册类
     */
    private void loadAndRegisterMpService(String mp,
                                          @Nullable
                                                  ServletRegistrationBean<WeChatMpEndpointServlet> endpointServletRegistrationBean,
                                          @Nullable
                                                  ServletRegistrationBean<WeChatOAuth2Servlet> oAuth2ServletRegistrationBean) {
        MpConfig mpConfig = weChatBaseConfig.getMpConfig(mp);
        if (!mpConfig.isEnable())
            return;

        IWeChatMpService weChatMpService;
        try {
            weChatMpService = new WeChatMpService(weChatBaseConfig,
                                                  mpConfig);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getCreateWeChatMpServiceFailed(mpConfig.getName()),
                                      ex);
        }

        mpServiceMap.put(mpConfig.getName(),
                         weChatMpService);

        //是否为默认的公众号
        boolean _default = mpConfig.getName()
                                   .equals(defaultMp());

        //注册
        if (_default)
            getBeanFactory().registerSingleton(
                    getMpServiceBeanName(null),
                    weChatMpService);

        getBeanFactory().registerSingleton(
                getMpServiceBeanName(mpConfig.getName()),
                weChatMpService);

        if (weChatBaseConfig.isEnableMpEndpointServlet() && mpConfig.isEnableMpEndpointServlet()) {
            WeChatMpEndpointServlet.setup(mpConfig,
                                          weChatMpService);
//            endpointServletRegistrationBean.addUrlMappings(mpConfig.getMpEndpointUrl());
        }

        if (weChatBaseConfig.isEnableOAuth2Servlet() && mpConfig.isEnableOAuth2Servlet()) {
            WeChatOAuth2Servlet.setup(mpConfig,
                                      weChatMpService);
//            oAuth2ServletRegistrationBean.addUrlMappings(mpConfig.getOAuthBaseUrl(),
//                                                         mpConfig.getOAuthUserInfoUrl());
        }
    }

    /**
     * 加载并注册微信支付服务
     *
     * @param pay                              商户号名称
     * @param payNotifyServletRegistrationBean 微信收付通通知服务注册类
     */
    private void loadAndRegisterPayService(String pay,
                                           @Nullable
                                                   ServletRegistrationBean<WeChatPayNotifyServlet> payNotifyServletRegistrationBean) {
        PayConfig payConfig = weChatBaseConfig.getPayConfig(pay);
        if (!payConfig.isEnable())
            return;

        IWeChatPayService weChatPayService;
        try {
            weChatPayService = new WeChatPayService(payConfig);
        } catch (Exception ex) {
            throw new ModuleException(Strings.getCreateWeChatMpServiceFailed(payConfig.getName()),
                                      ex);
        }

        payServiceMap.put(payConfig.getName(),
                          weChatPayService);

        //是否为默认的商户号
        boolean _default = payConfig.getName()
                                    .equals(defaultMp());

        //注册
        if (_default)
            getBeanFactory().registerSingleton(
                    getPayServiceBeanName(null),
                    weChatPayService);

        getBeanFactory().registerSingleton(
                getPayServiceBeanName(payConfig.getName()),
                weChatPayService);

        if (weChatBaseConfig.isEnablePayNotifyServlet() && payConfig.isEnablePayNotifyServlet()) {
            WeChatPayNotifyServlet.setup(payConfig,
                                         weChatPayService);
//            payNotifyServletRegistrationBean.addUrlMappings(payConfig.getPayNotifyUrl(),
//                                                            payConfig.getPayNotifyV3Url(),
//                                                            payConfig.getPayScoreNotifyV3Url(),
//                                                            payConfig.getRefundNotifyUrl(),
//                                                            payConfig.getRefundNotifyV3Url());
        }
    }

    /**
     * 获取微信公众号服务在Spring IOC容器中的名称
     *
     * @param mp 公众号名称
     */
    public static String getMpServiceBeanName(
            @Nullable
                    String mp) {
        return mp == null
               ? INaiveWeChatServiceProvider.DEFAULT_MP_SERVICE_IOC_NAME
               : String.format("%s%s",
                               INaiveWeChatServiceProvider.MP_SERVICE_IOC_PREFIX,
                               mp);
    }

    /**
     * 获取微信支付服务在Spring IOC容器中的名称
     *
     * @param mp 公众号名称
     */
    public static String getPayServiceBeanName(
            @Nullable
                    String mp) {
        return mp == null
               ? INaiveWeChatServiceProvider.DEFAULT_PAY_SERVICE_IOC_NAME
               : String.format("%s%s",
                               INaiveWeChatServiceProvider.PAY_SERVICE_IOC_PREFIX,
                               mp);
    }

    /**
     * 获取服务在Spring IOC容器中的名称
     *
     * @param servletType 服务类型
     */
    public static String getServletBeanName(
            Class<?> servletType) {
        return String.format("%s%s",
                             INaiveWeChatServiceProvider.SERVLET_IOC_PREFIX,
                             servletType.getSimpleName());
    }

    @Override
    public String defaultMp() {
        return this.weChatBaseConfig.getMp();
    }

    @Override
    public boolean isMpExists(String mp) {
        return this.weChatBaseConfig.getMultiMp()
                                    .containsKey(mp);
    }

    @Override
    public boolean isMpEnable(String mp) {
        return this.weChatBaseConfig.getMultiMp()
                                    .get(mp)
                                    .isEnable();
    }

    @Override
    public List<String> allMp(boolean enabledOnly) {
        return this.weChatBaseConfig.getAllMp(enabledOnly);
    }

    @Override
    public Map<String, IWeChatMpService> loadAllWeChatMpService() {
        if (mpServiceMap.size() == 0) {
//            //创建服务
//            ServletRegistrationBean<WeChatMpEndpointServlet> endpointServletRegistrationBean = null;
//            if (baseConfig.isEnableMpEndpointServlet()) {
//                endpointServletRegistrationBean = new ServletRegistrationBean<>();
//                endpointServletRegistrationBean.setServlet(new WeChatMpEndpointServlet());
//            }
//            ServletRegistrationBean<WeChatOAuth2Servlet> oAuth2ServletRegistrationBean = null;
//            if (baseConfig.isEnableOAuth2Servlet()) {
//                oAuth2ServletRegistrationBean = new ServletRegistrationBean<>();
//                oAuth2ServletRegistrationBean.setServlet(new WeChatOAuth2Servlet(baseConfig));
//            }

            for (String mp : allMp(true)) {
                loadAndRegisterMpService(mp,
                                         null,
                                         null);
            }

//            //注册服务
//            if (baseConfig.isEnableMpEndpointServlet())
//                getBeanFactory().registerSingleton(getServletBeanName(WeChatMpEndpointServlet.class),
//                                                   endpointServletRegistrationBean);
//            if (baseConfig.isEnableOAuth2Servlet())
//                getBeanFactory().registerSingleton(getServletBeanName(WeChatOAuth2Servlet.class),
//                                                   oAuth2ServletRegistrationBean);
        }

        return mpServiceMap;
    }

    @Override
    public IWeChatMpService getDefaultWeChatMpService() {
        return getWeChatMpService(defaultMp());
    }

    @Override
    public IWeChatMpService getWeChatMpService(String mp) {
        if (!isMpExists(mp))
            throw new ModuleException(Strings.getConfigMpUndefined(mp));

        if (!isMpEnable(mp))
            throw new ModuleException(Strings.getConfigMpNotActive(mp));

        return loadAllWeChatMpService().get(mp);
    }


    @Override
    public String defaultPay() {
        return this.weChatBaseConfig.getPay();
    }

    @Override
    public boolean isPayExists(String pay) {
        return this.weChatBaseConfig.getMultiPay()
                                    .containsKey(pay);
    }

    @Override
    public boolean isPayEnable(String pay) {
        return this.weChatBaseConfig.getMultiPay()
                                    .get(pay)
                                    .isEnable();
    }

    @Override
    public List<String> allPay(boolean enabledOnly) {
        return this.weChatBaseConfig.getAllPay(enabledOnly);
    }

    @Override
    public Map<String, IWeChatPayService> loadAllWeChatPayService() {
        if (payServiceMap.size() == 0) {
//            //创建服务
//            ServletRegistrationBean<WeChatPayNotifyServlet> payNotifyServletRegistrationBean = null;
//            if (baseConfig.isEnablePayNotifyServlet()) {
//                payNotifyServletRegistrationBean = new ServletRegistrationBean<>();
//                payNotifyServletRegistrationBean.setServlet(new WeChatPayNotifyServlet(baseConfig));
//            }

            for (String pay : allPay(true)) {
                loadAndRegisterPayService(pay,
                                          null);
            }

//            //注册服务
//            if (baseConfig.isEnablePayNotifyServlet())
//                getBeanFactory().registerSingleton(getServletBeanName(WeChatPayNotifyServlet.class),
//                                                   payNotifyServletRegistrationBean);
        }

        return payServiceMap;
    }

    @Override
    public IWeChatPayService getDefaultWeChatPayService() {
        return getWeChatPayService(defaultPay());
    }

    @Override
    public IWeChatPayService getWeChatPayService(String pay) {
        if (!isPayExists(pay))
            throw new ModuleException(Strings.getConfigPayUndefined(pay));

        if (!isPayEnable(pay))
            throw new ModuleException(Strings.getConfigPayNotActive(pay));

        return loadAllWeChatPayService().get(pay);
    }
}
