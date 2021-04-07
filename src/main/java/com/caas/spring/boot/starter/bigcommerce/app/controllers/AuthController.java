package com.caas.spring.boot.starter.bigcommerce.app.controllers;

import com.caas.spring.boot.starter.bigcommerce.app.TokenRepository;
import com.caas.spring.boot.starter.bigcommerce.app.TokenService;
import com.caas.spring.boot.starter.bigcommerce.app.BigCommerceAppException;
import com.caas.spring.boot.starter.bigcommerce.app.AuthToken;
import com.caas.spring.boot.starter.bigcommerce.app.configuration.BigCommerceApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for authorization endpoints used in BigCommerce Single-Click OAuth Flow
 *
 * @see <a href="https://developer.bigcommerce.com/api-docs/apps/guide/auth">BigCommerce Single-Click OAuth Flow</a>
 *
 */
@Slf4j
public class AuthController {

    private final TokenService<AuthToken> tokenService;
    private final BigCommerceApplicationConfiguration configuration;
    private final TokenRepository<AuthToken> tokenRepository;

    /**
     * AuthController
     * @param tokenService      service used to retrieve an AuthToken for the given code, scopes and store hash
     * @param configuration     configuration for this app
     * @param tokenRepository   token repository
     */
    public AuthController(TokenService<AuthToken> tokenService, BigCommerceApplicationConfiguration configuration, TokenRepository<AuthToken> tokenRepository) {
        this.tokenService = tokenService;
        this.configuration = configuration;
        this.tokenRepository = tokenRepository;
    }

    /**
     * Auth Callback handler from BigCommerce with authorized scopes from admin user.
     * @param code      auth code
     * @param scope     scopes granted
     * @param context   string containing BigCommerce store hash
     * @return html
     */
    public String authenticate(@RequestParam String code, @RequestParam String scope, @RequestParam String context) {
        val storeHash = context.substring(context.indexOf('/') + 1);
        val scopes = Arrays.asList(scope.split(" "));

        validateRequest(scopes);

        var authResponse = tokenService.fetchFor(code, storeHash, scopes);
        tokenRepository.save(storeHash, authResponse);

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
