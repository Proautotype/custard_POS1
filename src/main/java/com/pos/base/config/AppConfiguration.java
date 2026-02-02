package com.pos.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app")
public record AppConfiguration(String name, String currency , List<String> productCategories) { }
