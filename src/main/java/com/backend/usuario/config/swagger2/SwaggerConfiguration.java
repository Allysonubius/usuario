package com.backend.usuario.config.swagger2;

import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.*;
import static com.backend.usuario.constants.SecurityConstants.HEADER_STRING;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(Arrays.asList(new ApiKey(HEADER_STRING,HttpHeaders.AUTHORIZATION,In.HEADER.name())))
                .securityContexts(Arrays.asList(securityContext()))
                .genericModelSubstitutes(Optional.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.backend.usuario.controller"))
                .paths(PathSelectors.any())
                .build();
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
        return SecurityContext.builder().securityReferences(this.defaultAuth()).forPaths(PathSelectors.ant("/api/**")).build();
    }
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("ADMIN", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(HEADER_STRING, authorizationScopes));
    }

}
