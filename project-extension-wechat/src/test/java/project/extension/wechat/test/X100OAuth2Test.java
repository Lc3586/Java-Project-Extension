package project.extension.wechat.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.extension.wechat.common.ServiceObjectResolve;
import project.extension.wechat.configure.TestMPConfigure;
import project.extension.wechat.core.mp.standard.IWeChatMPService;

import java.util.List;

/**
 * 100.用户授权测试
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
    }

    /**
     * 测试基础授权功能
     *
     * @param name 名称
     */
    @ParameterizedTest
    @MethodSource("project.extension.wechat.configure.TestMPConfigure#getMultiTestMPName")
    @DisplayName("100.测试基础授权功能")
    @Order(100)
    public void _100(String name) {
        String mp = TestMPConfigure.getTestMP(name);

        IWeChatMPService mpService = ServiceObjectResolve.getMPService(mp);

        mpService.
    }
}