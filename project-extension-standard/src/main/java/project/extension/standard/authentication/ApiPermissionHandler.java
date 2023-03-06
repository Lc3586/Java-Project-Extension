package project.extension.standard.authentication;

import org.springframework.stereotype.Service;

/**
 * 接口权限验证处理类
 *
 * @author LCTR
 * @date 2023-03-06
 */
@Service("aph")
public class ApiPermissionHandler {
    public ApiPermissionHandler(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private final IAuthenticationService authenticationService;

    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = "admin";

    /**
     * 验证用户的权限
     *
     * @param permission 权限标识符
     * @return true：运行访问，false：禁止访问
     */
    public boolean isPass(String permission) {
        if (!authenticationService.isAuthenticated())
            return false;

        return authenticationService.isAuthorized(permission);
    }
}
