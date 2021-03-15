package com.e2x.bigcommerce.app.starter.controllers;

import com.e2x.bigcommerce.app.starter.AuthTokenRepository;
import com.e2x.bigcommerce.app.starter.AuthorizationFlowController;
import com.e2x.bigcommerce.app.starter.BigCommerceAppException;
import com.e2x.bigcommerce.app.starter.configuration.BigCommerceAppConfiguration;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@Slf4j
public class AuthController {

    private final AuthorizationFlowController authorizationFlowController;
    private final AuthTokenRepository authTokenRepository;
    private final BigCommerceAppConfiguration configuration;

    public AuthController(AuthorizationFlowController authorizationFlowController, BigCommerceAppConfiguration configuration, AuthTokenRepository authTokenRepository) {
        this.authorizationFlowController = authorizationFlowController;
        this.authTokenRepository = authTokenRepository;
        this.configuration = configuration;
    }

    public String authenticate(@RequestParam String code, @RequestParam String scope, @RequestParam String context) {
        val storeHash = context.substring(context.indexOf('/') + 1);
        val scopes = Arrays.asList(scope.split(" "));

        validateRequest(scopes);

        var authResponse = authorizationFlowController.authenticate(code, storeHash, scopes);
        authTokenRepository.save(storeHash, authResponse);

        return configuration.getAuthenticatedHtml();
    }

    private void validateRequest(List<String> scopes) {
        if (!scopes.containsAll(configuration.getRequiredScopes())) {
            var missingScopes = configuration.getRequiredScopes().stream()
                    .filter(s -> !scopes.contains(s))
                    .collect(Collectors.toSet());

            throw new BigCommerceAppException("invalid authentication request due to missing required scopes: " + missingScopes.toString());
        }
    }
}
