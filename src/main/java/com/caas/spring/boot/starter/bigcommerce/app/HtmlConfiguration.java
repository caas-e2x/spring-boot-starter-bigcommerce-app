package com.caas.spring.boot.starter.bigcommerce.app;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HtmlConfiguration {
    private String authenticatedHtml = "authenticated-default.html";
    private String loadedHtml = "loaded-default.html";
}
