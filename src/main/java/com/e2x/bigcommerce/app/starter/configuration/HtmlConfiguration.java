package com.e2x.bigcommerce.app.starter.configuration;

import lombok.*;

@Getter
@Setter
public class HtmlConfiguration {
    private String authenticatedHtml = "authenticated-default.html";
    private String loadedHtml = "loaded-default.html";
}
