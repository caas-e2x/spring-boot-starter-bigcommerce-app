# BigCommerce Spring Boot Starter Module

A Spring Boot Starter module for BigCommerce third-party applications 

![Build](https://github.com/caas-e2x-org/spring-boot-starter-bigcommerce-app/actions/workflows/gradle.yml/badge.svg)

## Reference

- [BigCommerce App Guide](https://developer.bigcommerce.com/api-docs/apps/guide/intro)
- [Spring Boot](https://spring.io/projects/spring-boot)

## How To Use

- gradle
```groovy
dependencies {
    implementation 'caas.e2x.bigcommerce:spring-boot-starter-bigcommerce-app:[VERSION]'
}
```

- maven

```xml
<!-- https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-cognitoidp -->
<dependency>
    <groupId>caas.e2x.bigcommerce</groupId>
    <artifactId>spring-boot-starter-bigcommerce-app</artifactId>
    <version>[VERSION]</version>
</dependency>
```

By defaults this module will register all necessary endpoints required for a Single-Click BigCommerce App (see [Single-Click OAuth](https://developer.bigcommerce.com/api-docs/apps/guide/auth))

Upon Spring Boot start-up, the following endpoints will be automatically registered:

- /auth
- /load
- /uninstall

## Configuration

This section below allows you to configure this module according to the settings used during your app registration via BigCommerce (see [BigCommerce App Portal](https://devtools.bigcommerce.com/my/apps)).

```yaml
caas:
  bigcommerce:
    url: 'https://login.bigcommerce.com/oauth2/token'       // (default) url to BigCommerce oauth token

    required-scopes:                                        // list of BigCommerce scopes required for this application
      - scope-1
      - scope-2

    redirect-uri: 'https:/a/redirect/uri'                   // callback redirect-uri (declared during app commerce app registration)

    store-credentials:                                      // store credentials used to fetch oauth token on 'installation' of this app 
      store-id:
        client-id: a-client-id
        client-secret: a-client-secret

    http-client:
      read-timeout: 10000
      connect-timeout: 1000

    html:                                                   // html resources returned on load and install of this app (this html will be loaded in BigCommerce iFrame)
      authenticated-html: /path/to/html
      loaded-html: /path/to/html
```

## Overrides

When importing this module, various interface implementations can be substituted during application start (see BigCommerceApplicationAutoConfiguration).

A default out-of-the-box implementation are provided for each interfaces. These can be used unless specific requirements requires an override.

> **Note**: 
> 
> When the app using this starter module is 'installed' through BigCommerce app administration, a BigCommerce token will be automatically retrieved through the OAuth callback flow. It is **important** to implement your own TokenRepository to securely store this token.
> 
> see TokenRepository<T> interface for more details.



