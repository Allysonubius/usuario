package com.backend.usuario.config.swagger2;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 */
public class SwaggerUiWebMvcConfigurer implements WebMvcConfigurer {
    private final String baseUrl;
    public SwaggerUiWebMvcConfigurer(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseURLSwaggerUI = StringUtils.trimTrailingCharacter(this.baseUrl , '/');
        registry
                .addResourceHandler(baseURLSwaggerUI + "/swagger-ui/***")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
