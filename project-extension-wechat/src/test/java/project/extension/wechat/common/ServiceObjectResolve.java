package project.extension.wechat.common;

import org.junit.jupiter.api.Assertions;
import project.extension.ioc.IOCExtension;
import project.extension.wechat.config.WeChatBaseConfig;
import project.extension.wechat.config.MpConfig;
import project.extension.wechat.config.PayConfig;
import project.extension.wechat.core.INaiveWeChatService;
import project.extension.wechat.core.mp.standard.IWeChatMpService;
import project.extension.wechat.core.pay.standard.IWeChatPayService;

/**
 * 获取对象帮助类
 * <p>必须先执行injection方法</p>
 *
 * @author LCTR
 * @date 2023-03-17
 */
public class ServiceObjectResolve {
    public static WeChatBaseConfig weChatBaseConfig;

    public static IWeChatMpService masterWeChatMPService;

    public static IWeChatPayService masterWeChatPayService;

    public static INaiveWeChatService naiveWeChatServiceProvider;

    /**
     * 注入
     */
    public static void injection() {
        weChatBaseConfig = IOCExtension.applicationContext.getBean(WeChatBaseConfig.class);

        Assertions.assertNotNull(weChatBaseConfig,
                                 "未获取到BaseConfig");

        System.out.printf("\r\n%s已注入\r\n",
                          WeChatBaseConfig.class.getName());


//        naiveWeChatServiceProvider = IOCExtension.applicationContext.getBean(INaiveWeChatServiceProvider.class);
//
//        Assertions.assertNotNull(naiveWeChatServiceProvider,
//                                 "未获取到INaiveWeChatServiceProvider");
//
//        System.out.printf("\r\n%s已注入\r\n",
//                          INaiveWeChatServiceProvider.class.getName());

        masterWeChatMPService = IOCExtension.applicationContext.getBean(INaiveWeChatService.DEFAULT_MP_SERVICE_IOC_NAME,
                                                                        IWeChatMpService.class);

        Assertions.assertNotNull(masterWeChatMPService,
                                 "未获取到默认的IWeChatMpService");

        System.out.printf("\r\n默认的%s已注入\r\n",
                          IWeChatMpService.class.getName());


//        masterWeChatPayService = IOCExtension.applicationContext.getBean(INaiveWeChatServiceProvider.DEFAULT_PAY_SERVICE_IOC_NAME,
//                                                                         IWeChatPayService.class);
//
//        Assertions.assertNotNull(masterWeChatPayService,
//                                 "未获取到默认的IWeChatPayService");
//
//        System.out.printf("\r\n默认的%s已注入\r\n",
//                          IWeChatPayService.class.getName());


        naiveWeChatServiceProvider = IOCExtension.applicationContext.getBean(INaiveWeChatService.class);

        Assertions.assertNotNull(naiveWeChatServiceProvider,
                                 "未获取到INaiveWeChatServiceProvider");

        System.out.printf("\r\n%s已注入\r\n",
                          INaiveWeChatService.class.getName());
    }

    /**
     * 获取公众号配置
     *
     * @param mp 公众号名称
     */
    public static MpConfig getMpConfig(String mp) {
        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isMpExists(mp),
                              String.format("%s公众号不存在",
                                            mp));

        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isMpEnable(mp),
                              String.format("%s公众号未启用",
                                            mp));

        return weChatBaseConfig.getMpConfig(mp);
    }

    /**
     * 获取微信公众号服务对象对象
     *
     * @param mp 公众号名称
     */
    public static IWeChatMpService getMPService(String mp) {
        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isMpExists(mp),
                              String.format("%s公众号不存在",
                                            mp));

        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isMpEnable(mp),
                              String.format("%s公众号未启用",
                                            mp));

        if (mp.equals(weChatBaseConfig.getMp()))
            return masterWeChatMPService;
        else {
            IWeChatMpService weChatMPService = ServiceObjectResolve.naiveWeChatServiceProvider.getWeChatMpService(mp);

            Assertions.assertNotNull(weChatMPService,
                                     String.format("未获取到%s公众号的IWeChatMPService",
                                                   mp));

            System.out.printf("\r\n已获取%s公众号的IWeChatMPService\r\n",
                              mp);

            return weChatMPService;
        }
    }

    /**
     * 获取商户号配置
     *
     * @param pay 商户号名称
     */
    public static PayConfig getPayConfig(String pay) {
        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isPayExists(pay),
                              String.format("%s商户号不存在",
                                            pay));

        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isPayEnable(pay),
                              String.format("%s商户号未启用",
                                            pay));

        return weChatBaseConfig.getPayConfig(pay);
    }

    /**
     * 获取微信支付服务对象对象
     *
     * @param pay 商户号名称
     */
    public static IWeChatPayService getPayService(String pay) {
        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isPayExists(pay),
                              String.format("%s商户号不存在",
                                            pay));

        Assertions.assertTrue(ServiceObjectResolve.naiveWeChatServiceProvider.isPayEnable(pay),
                              String.format("%s商户号未启用",
                                            pay));

        if (pay.equals(weChatBaseConfig.getMp()))
            return masterWeChatPayService;
        else {
            IWeChatPayService weChatPayService = ServiceObjectResolve.naiveWeChatServiceProvider.getWeChatPayService(pay);

            Assertions.assertNotNull(weChatPayService,
                                     String.format("未获取到%s商户号的IWeChatPayService",
                                                   pay));

            System.out.printf("\r\n已获取%s商户号的IWeChatPayService\r\n",
                              pay);

            return weChatPayService;
        }
    }
}
