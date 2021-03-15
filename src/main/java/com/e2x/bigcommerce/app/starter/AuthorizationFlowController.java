package com.e2x.bigcommerce.app.starter;

import com.e2x.bigcommerce.app.starter.model.AuthToken;

import java.util.List;

public interface AuthorizationFlowController {

    AuthToken authenticate(String code, String storeHash, List<String> requiredScopes);

}
