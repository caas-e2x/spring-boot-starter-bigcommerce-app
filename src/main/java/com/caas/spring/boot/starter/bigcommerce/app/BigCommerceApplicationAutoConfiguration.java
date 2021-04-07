package com.caas.spring.boot.starter.bigcommerce.app;

import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration;
import com.caas.spring.boot.starter.bigcommerce.app.controllers.AuthController;
import com.caas.spring.boot.starter.bigcommerce.app.controllers.LoadController;
import com.caas.spring.boot.starter.bigcommerce.app.controllers.UninstallController;
import com.caas.spring.boot.starter.bigcommerce.app.integrations.BigCommerceAuthorizationFlowClient;
import com.caas.spring.boot.starter.bigcommerce.app.integrations.InMemoryAuthTokenRepository;
import com.caas.spring.boot.starter.bigcommerce.app.integrations.http.DefaultHttpClientFactory;
import com.caas.spring.boot.starter.bigcommerce.app.security.DefaultSignedPayloadReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Slf4j
@Configuration
@EnableConfigurationProperties(BigCommerceApplicationConfiguration.class)
public class BigCommerceApplicationAutoConfiguration {

    private static final String LOAD = "load";

    @Bean
    public LoadController registerLoadController(RequestMappingHandlerMapping handlerMapping, SignedPayloadReader signedPayloadReader, BigCommerceApplicationConfiguration configuration) throws NoSuchMethodException {
        LoadController loadController = new LoadController(signedPayloadReader, configuration);

        handlerMapping.registerMapping(
                RequestMappingInfo.paths(String.format("/%s", LOAD))
                        .methods(RequestMethod.GET)
                        .produces(MediaType.TEXT_HTML_VALUE)
                        .build(),
                loadController,
                loadController.getClass().getMethod(LOAD, String.class));

        return loadController;
    }

    @Bean
    @ConditionalOnMissingBean(RequestMappingHandlerMapping.class)
    public RequestMappingHandlerMapping handlerMapping() {
        throw new RuntimeException("Spring RequestMappingHandlerMapping must be present on the application context but is missing...");
    }

    @Bean
    public AuthController registerAuthController(RequestMappingHandlerMapping handlerMapping, TokenService<AuthToken> tokenService, BigCommerceApplicationConfiguration bigCommerceAppConfiguration, TokenRepository<AuthToken> tokenRepository) throws NoSuchMethodException {
        AuthController authController = new AuthController(tokenService, bigCommerceAppConfiguration, tokenRepository);

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
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper() {
        log.warn("using BigCommerce Spring Boot starter app default object mapper as none supplied in project.");

        return new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .propertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)
                .build();
    }

    @Bean
    public UninstallController uninstallController(RequestMappingHandlerMapping handlerMapping, SignedPayloadReader signedPayloadReader, TokenRepository<AuthToken> tokenRepository) throws NoSuchMethodException {
        UninstallController uninstallController = new UninstallController(signedPayloadReader, tokenRepository);

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
    @ConditionalOnMissingBean(value = { TokenService.class })
    public TokenService<AuthToken> authorizationFlowController(BigCommerceApplicationConfiguration configuration, ObjectMapper objectMapper, HttpClientFactory httpClientFactory) {
        return new BigCommerceAuthorizationFlowClient(configuration, objectMapper, httpClientFactory);
    }

    @Bean
    @ConditionalOnMissingBean(value = { HttpClientFactory.class })
    public HttpClientFactory httpClientFactory() {
        return new DefaultHttpClientFactory();
    }

    @Bean
    @ConditionalOnMissingBean(value = { TokenRepository.class })
    public TokenRepository<AuthToken> authStorage() {
        return new InMemoryAuthTokenRepository();
    }

    @Bean
    @ConditionalOnMissingBean(value = { SignedPayloadReader.class })
    public SignedPayloadReader signedPayloadVerifier(BigCommerceApplicationConfiguration configuration, ObjectMapper objectMapper) {
        return new DefaultSignedPayloadReader(configuration, objectMapper);
    }

}
