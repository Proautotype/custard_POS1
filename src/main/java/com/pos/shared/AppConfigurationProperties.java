package com.pos.shared;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppConfigurationProperties(String currencySymbol) {
}
