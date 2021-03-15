package com.e2x.bigcommerce.app.starter;

import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration;
import com.e2x.bigcommerce.app.starter.controllers.AuthController;
import com.e2x.bigcommerce.app.starter.controllers.LoadController;
import com.e2x.bigcommerce.app.starter.controllers.UninstallController;
import com.e2x.bigcommerce.app.starter.integrations.BigCommerceClient;
import com.e2x.bigcommerce.app.starter.integrations.InMemoryAuthTokenRepository;
import com.e2x.bigcommerce.app.starter.integrations.http.DefaultHttpClientFactory;
import com.e2x.bigcommerce.app.starter.security.DefaultApiCredentialProvider;
import com.e2x.bigcommerce.app.starter.security.DefaultSignedPayloadReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
@EnableConfigurationProperties(value = { BigCommerceAppConfiguration.class })
public class BigCommerceAppAutoConfiguration {

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        // add properties here
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public LoadController registerLoadController(RequestMappingHandlerMapping handlerMapping, SignedPayloadReader signedPayloadReader, BigCommerceAppConfiguration configuration) throws NoSuchMethodException {
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
    public AuthController registerAuthController(RequestMappingHandlerMapping handlerMapping, AuthorizationFlowController authorizationFlowController, BigCommerceAppConfiguration bigCommerceAppConfiguration, AuthTokenRepository authTokenRepository) throws NoSuchMethodException {
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
    public AuthorizationFlowController authorizationFlowController(BigCommerceAppConfiguration configuration, ObjectMapper objectMapper, HttpClientFactory httpClientFactory) {
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
    public SignedPayloadReader signedPayloadVerifier(BigCommerceAppConfiguration configuration, ObjectMapper objectMapper) {
        return new DefaultSignedPayloadReader(configuration, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(value = { ApiCredentialProvider.class })
    public ApiCredentialProvider apiCredentialProvider(BigCommerceAppConfiguration configuration, AuthTokenRepository authTokenRepository) {
        return new DefaultApiCredentialProvider(configuration, authTokenRepository);
    }

}
