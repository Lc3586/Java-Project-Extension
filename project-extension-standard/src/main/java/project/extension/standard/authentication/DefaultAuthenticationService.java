package project.extension.standard.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 默认身份验证服务
 *
 * @author LCTR
 * @date 2023-03-24
 */
@Component
public class DefaultAuthenticationService
        implements IAuthenticationService {
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public Authentication getAuthentication() {
        return null;
    }

    @Override
    public AuthenticationInfo getOperator() {
        return null;
    }

    @Override
    public Optional<AuthenticationInfo> tryGetOperator() {
        return Optional.empty();
    }

    @Override
    public boolean isAuthorized(String... permission) {
        return true;
    }
}
