package com.caas.spring.boot.starter.bigcommerce.app;

import com.caas.spring.boot.starter.bigcommerce.app.controllers.AuthController;
import com.caas.spring.boot.starter.bigcommerce.app.controllers.LoadController;
import com.caas.spring.boot.starter.bigcommerce.app.controllers.UninstallController;
import com.caas.spring.boot.starter.bigcommerce.app.integrations.BigCommerceClient;
import com.caas.spring.boot.starter.bigcommerce.app.integrations.InMemoryAuthTokenRepository;
import com.caas.spring.boot.starter.bigcommerce.app.integrations.http.DefaultHttpClientFactory;
import com.caas.spring.boot.starter.bigcommerce.app.security.DefaultApiCredentialProvider;
import com.caas.spring.boot.starter.bigcommerce.app.security.DefaultSignedPayloadReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableConfigurationProperties(BigCommerceApplicationConfiguration.class)
public class BigCommerceApplicationAutoConfiguration {

    @Bean
    public LoadController registerLoadController(RequestMappingHandlerMapping handlerMapping, SignedPayloadReader signedPayloadReader, BigCommerceApplicationConfiguration configuration) throws NoSuchMethodException {
        LoadController loadController = new LoadController(signedPayloadReader, configuration);

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/load")
                        .methods(RequestMethod.GET)
                        .produces(MediaType.TEXT_HTML_VALUE)
                        .build(),
                loadController,
                loadController.getClass().getMethod("load", String.class));

        return loadController;
    }

    @Bean
    @ConditionalOnMissingBean(RequestMappingHandlerMapping.class)
    RequestMappingHandlerMapping handlerMapping() {
        throw new InvalidConfigurationException("missing request handler mapping");
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    ObjectMapper objectMapper() {
        throw new InvalidConfigurationException("missing object mapper");
    }

    @Bean
    public AuthController registerAuthController(RequestMappingHandlerMapping handlerMapping, AuthorizationFlowController authorizationFlowController, BigCommerceApplicationConfiguration bigCommerceAppConfiguration, AuthTokenRepository authTokenRepository) throws NoSuchMethodException {
        AuthController authController = new AuthController(authorizationFlowController, bigCommerceAppConfiguration, authTokenRepository);

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/auth")
                        .methods(RequestMethod.GET)
                        .produces(MediaType.TEXT_HTML_VALUE)
                        .build(),
                authController,
                authController.getClass().getMethod("authenticate", String.class, String.class, String.class));

        return authController;
    }

    @Bean
    public UninstallController uninstallController(RequestMappingHandlerMapping handlerMapping, SignedPayloadReader signedPayloadReader, AuthTokenRepository authTokenRepository) throws NoSuchMethodException {
        UninstallController uninstallController = new UninstallController(signedPayloadReader, authTokenRepository);

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/uninstall")
                        .methods(RequestMethod.GET)
                        .produces(MediaType.TEXT_HTML_VALUE)
                        .build(),
                uninstallController,
                uninstallController.getClass().getMethod("uninstall", String.class));

        return uninstallController;
    }

    @Bean
    @ConditionalOnMissingBean(value = { AuthorizationFlowController.class })
    public AuthorizationFlowController authorizationFlowController(BigCommerceApplicationConfiguration configuration, ObjectMapper objectMapper, HttpClientFactory httpClientFactory) {
        return new BigCommerceClient(configuration, objectMapper, httpClientFactory);
    }

    @Bean
    @ConditionalOnMissingBean(value = { HttpClientFactory.class })
    public HttpClientFactory httpClientFactory() {
        return new DefaultHttpClientFactory();
    }

    @Bean
    @ConditionalOnMissingBean(value = { AuthTokenRepository.class })
    public AuthTokenRepository authStorage() {
        return new InMemoryAuthTokenRepository();
    }

    @Bean
    @ConditionalOnMissingBean(value = { SignedPayloadReader.class })
    public SignedPayloadReader signedPayloadVerifier(BigCommerceApplicationConfiguration configuration, ObjectMapper objectMapper) {
        return new DefaultSignedPayloadReader(configuration, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(value = { ApiCredentialProvider.class })
    public ApiCredentialProvider apiCredentialProvider(BigCommerceApplicationConfiguration configuration, AuthTokenRepository authTokenRepository) {
        return new DefaultApiCredentialProvider(configuration, authTokenRepository);
    }

}
