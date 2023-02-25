package com.backend.usuario.config.swagger2;

import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .tags(new Tag("Application", "Operations about users."))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.backend.usuario.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiKey apiKey(){
        return new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name());
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Usuario Auth")
                .description("Sistema de autenticação JWT")
                .version("1.0.0")
                .license("MIT")
                .contact(new Contact("allyson","www.site.com..br","allyson@email.com"))
                .build();
    }
    private SecurityContext securityContext(){
        return SecurityContext.builder()
                .securityReferences(this.defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }
}
