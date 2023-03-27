package project.extension.wechat.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.extension.httpClient.HttpClientUtils;
import project.extension.string.StringExtension;
import project.extension.wechat.common.ServiceObjectResolve;
import project.extension.wechat.config.MpConfig;
import project.extension.wechat.configure.TestMpConfigure;
import project.extension.wechat.core.mp.servlet.WeChatOAuth2Servlet;
import project.extension.wechat.core.mp.standard.IWeChatMpService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 100.网页授权测试
 *
 * @author LCTR
 * @date 2023-03-17
 */
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class X100OAuth2Test {
    /**
     * 注入
     */
    @BeforeEach
    public void injection() {
        ServiceObjectResolve.injection();
        WeChatOAuth2Servlet.testMode = true;
    }

    /**
     * 测试基础授权功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.wechat.configure.TestMpConfigure#getMultiTestMpName")
    @DisplayName("100.测试基础授权功能")
    @Order(100)
    public void _100(String name)
            throws
            Throwable {
        String mp = TestMpConfigure.getTestMp(name);

        MpConfig mpConfig = ServiceObjectResolve.getMpConfig(mp);

        IWeChatMpService mpService = ServiceObjectResolve.getMPService(mp);

        //跳转地址
        String url = mpService.getBaseOAuth2Url();

        System.out.printf("基础授权地址：%s%n",
                          url);

        Assertions.assertTrue(url.endsWith(mpConfig.getOAuthBaseUrl()));

        //state参数
        String state = UUID.randomUUID()
                           .toString();
        Map<String, Object> params = new HashMap<>();
        params.put(WeChatOAuth2Servlet.STATE_PARAMETER,
                   state);

        System.out.printf("%s：%s%n",
                          WeChatOAuth2Servlet.STATE_PARAMETER,
                          state);

        //发起请求
        ResponseEntity<String> apiResponseEntity = HttpClientUtils.doGet(url,
                                                                         params,
                                                                         null,
                                                                         false);

        Assertions.assertEquals(HttpStatus.FOUND,
                                apiResponseEntity.getStatusCode());

        String api_redirect_uri = apiResponseEntity.getHeaders()
                                                   .getLocation()
                                                   .toString();

        System.out.printf("基础授权回调地址：%s%n",
                          api_redirect_uri);

        String redirect_uri_parameter = StringExtension.getQueryParameter(api_redirect_uri,
                                                                          WeChatOAuth2Servlet.REDIRECT_URI_PARAMETER);

        Assertions.assertNotNull(redirect_uri_parameter,
                                 String.format("基础授权回调地址的%s参数为空",
                                               WeChatOAuth2Servlet.REDIRECT_URI_PARAMETER));

        redirect_uri_parameter = URLDecoder.decode(redirect_uri_parameter,
                                                   StandardCharsets.UTF_8.name());

        System.out.printf("基础授权回调地址的%s参数：%s%n",
                          WeChatOAuth2Servlet.REDIRECT_URI_PARAMETER,
                          redirect_uri_parameter);

        String state_parameter = StringExtension.getQueryParameter(api_redirect_uri,
                                                                   WeChatOAuth2Servlet.STATE_PARAMETER);

        Assertions.assertNotNull(state_parameter,
                                 String.format("基础授权回调地址的%s参数为空",
                                               WeChatOAuth2Servlet.STATE_PARAMETER));

        System.out.printf("基础授权回调地址的%s参数：%s%n",
                          WeChatOAuth2Servlet.STATE_PARAMETER,
                          state_parameter);

        params.clear();
        params.put(WeChatOAuth2Servlet.STATE_PARAMETER,
                   state_parameter);

        ResponseEntity<String> returnResponseEntity = HttpClientUtils.doGet(redirect_uri_parameter,
                                                                            params,
                                                                            null,
                                                                            false);

        Assertions.assertEquals(HttpStatus.FOUND,
                                returnResponseEntity.getStatusCode());

        String return_redirect_uri = returnResponseEntity.getHeaders()
                                                         .getLocation()
                                                         .toString();

        state_parameter = StringExtension.getQueryParameter(return_redirect_uri,
                                                            WeChatOAuth2Servlet.STATE_PARAMETER);

        Assertions.assertNotNull(state_parameter,
                                 String.format("%s参数为空",
                                               WeChatOAuth2Servlet.STATE_PARAMETER));

        Assertions.assertEquals(state,
                                state_parameter,
                                String.format("%s参数有误",
                                              WeChatOAuth2Servlet.STATE_PARAMETER));

        System.out.printf("%s参数：%s%n",
                          WeChatOAuth2Servlet.STATE_PARAMETER,
                          state_parameter);
    }

    /**
     * 测试身份信息授权功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.wechat.configure.TestMpConfigure#getMultiTestMpName")
    @DisplayName("110.测试身份信息授权功能")
    @Order(110)
    public void _110(String name)
            throws
            Throwable {
        String mp = TestMpConfigure.getTestMp(name);

        MpConfig mpConfig = ServiceObjectResolve.getMpConfig(mp);

        IWeChatMpService mpService = ServiceObjectResolve.getMPService(mp);

        //跳转地址
        String url = mpService.getUserInfoOAuth2Url();

        System.out.printf("身份信息授权地址：%s%n",
                          url);

        Assertions.assertTrue(url.endsWith(mpConfig.getOAuthUserInfoUrl()));

        //state参数
        String state = UUID.randomUUID()
                           .toString();
        Map<String, Object> params = new HashMap<>();
        params.put(WeChatOAuth2Servlet.STATE_PARAMETER,
                   state);

        System.out.printf("%s：%s%n",
                          WeChatOAuth2Servlet.STATE_PARAMETER,
                          state);

        //发起请求
        ResponseEntity<String> apiResponseEntity = HttpClientUtils.doGet(url,
                                                                         params,
                                                                         null,
                                                                         false);

        Assertions.assertEquals(HttpStatus.FOUND,
                                apiResponseEntity.getStatusCode());

        String api_redirect_uri = apiResponseEntity.getHeaders()
                                                   .getLocation()
                                                   .toString();

        System.out.printf("身份信息授权回调地址：%s%n",
                          api_redirect_uri);

        String redirect_uri_parameter = StringExtension.getQueryParameter(api_redirect_uri,
                                                                          WeChatOAuth2Servlet.REDIRECT_URI_PARAMETER);

        Assertions.assertNotNull(redirect_uri_parameter,
                                 String.format("身份信息授权回调地址的%s参数为空",
                                               WeChatOAuth2Servlet.REDIRECT_URI_PARAMETER));

        redirect_uri_parameter = URLDecoder.decode(redirect_uri_parameter,
                                                   StandardCharsets.UTF_8.name());

        System.out.printf("身份信息授权回调地址的%s参数：%s%n",
                          WeChatOAuth2Servlet.REDIRECT_URI_PARAMETER,
                          redirect_uri_parameter);

        String state_parameter = StringExtension.getQueryParameter(api_redirect_uri,
                                                                   WeChatOAuth2Servlet.STATE_PARAMETER);

        Assertions.assertNotNull(state_parameter,
                                 String.format("身份信息授权回调地址的%s参数为空",
                                               WeChatOAuth2Servlet.STATE_PARAMETER));

        System.out.printf("身份信息授权回调地址的%s参数：%s%n",
                          WeChatOAuth2Servlet.STATE_PARAMETER,
                          state_parameter);

        params.clear();
        params.put(WeChatOAuth2Servlet.STATE_PARAMETER,
                   state_parameter);

        ResponseEntity<String> returnResponseEntity = HttpClientUtils.doGet(redirect_uri_parameter,
                                                                            params,
                                                                            null,
                                                                            false);

        Assertions.assertEquals(HttpStatus.FOUND,
                                returnResponseEntity.getStatusCode());

        String return_redirect_uri = returnResponseEntity.getHeaders()
                                                         .getLocation()
                                                         .toString();

        state_parameter = StringExtension.getQueryParameter(return_redirect_uri,
                                                            WeChatOAuth2Servlet.STATE_PARAMETER);

        Assertions.assertNotNull(state_parameter,
                                 String.format("%s参数为空",
                                               WeChatOAuth2Servlet.STATE_PARAMETER));

        Assertions.assertEquals(state,
                                state_parameter,
                                String.format("%s参数有误",
                                              WeChatOAuth2Servlet.STATE_PARAMETER));

        System.out.printf("%s参数：%s%n",
                          WeChatOAuth2Servlet.STATE_PARAMETER,
                          state_parameter);
    }
}