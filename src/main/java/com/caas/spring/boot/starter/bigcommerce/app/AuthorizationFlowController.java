package com.caas.spring.boot.starter.bigcommerce.app;

import com.caas.spring.boot.starter.bigcommerce.app.model.AuthToken;

import java.util.List;

public interface AuthorizationFlowController {

    AuthToken authenticate(String code, String storeHash, List<String> requiredScopes);

}
