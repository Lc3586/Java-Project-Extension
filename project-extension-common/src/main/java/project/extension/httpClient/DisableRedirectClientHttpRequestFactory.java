package project.extension.httpClient;

import org.apache.http.client.config.RequestConfig;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;

/**
 * 禁用重定向
 */
public class DisableRedirectClientHttpRequestFactory
        extends HttpComponentsClientHttpRequestFactory {
    @Override
    protected RequestConfig mergeRequestConfig(
            @Nullable
                    RequestConfig clientConfig) {
        RequestConfig superConfig = super.mergeRequestConfig(clientConfig);

        RequestConfig.Builder builder = RequestConfig.copy(superConfig);
        builder.setRedirectsEnabled(false);
        return builder.build();
    }
}
