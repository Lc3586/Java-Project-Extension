package project.extension.standard.authentication;

import org.springframework.security.core.Authentication;

import java.util.Optional;

/**
 * 身份验证服务
 *
 * @author LCTR
 * @date 2022-12-08
 */
public interface IAuthenticationService {
    /**
     * 是否已通过身份验证
     */
    boolean isAuthenticated();

    /**
     * 获取身份验证信息
     */
    Authentication getAuthentication();

    /**
     * 获取操作者
     */
    AuthenticationInfo getOperator();

    /**
     * 尝试获取操作者
     */
    Optional<AuthenticationInfo> tryGetOperator();

    /**
     * 是否拥有授权
     *
     * @param permission 权限标识符
     */
    boolean isAuthorized(String... permission);
}
