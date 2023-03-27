package com.backend.usuario.config.swagger2;

import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.*;

import static com.backend.usuario.constants.SecurityConstants.HEADER_STRING;
import static com.backend.usuario.constants.SecurityConstants.TOKEN_PREFIX;

/**
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    private final ResponseMessage m200 = simpleMessage(200,"Chamada realizada com sucesso");
    private final ResponseMessage m201 = simpleMessage(201,"Recurso criado");
    private final ResponseMessage m204 = simpleMessage(204,"Atualização ok");
    private final ResponseMessage m401 = simpleMessage(401,"Autorização é requerida");
    private final ResponseMessage m403 = simpleMessage(403,"Não e autorizado");
    private final ResponseMessage m404 = simpleMessage(404,"Objeto não encontrado");
    private final ResponseMessage m422 = simpleMessage(422,"Erro de validação");
    private final ResponseMessage m500 = simpleMessage(500,"Erro inesperado");

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
                .build()
                .globalResponseMessage(RequestMethod.POST,Arrays.asList(m201,m403,m422,m500,m200))
                .globalResponseMessage(RequestMethod.GET,Arrays.asList(m403,m404,m500))
                .globalResponseMessage(RequestMethod.PUT,Arrays.asList(m404,m204,m422,m500))
                .globalResponseMessage(RequestMethod.DELETE,Arrays.asList(m403,m404,m200));
    }

    private ApiKey apiKey(){
        return new ApiKey(HEADER_STRING, HttpHeaders.AUTHORIZATION.concat(TOKEN_PREFIX), In.HEADER.name());
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
   private ResponseMessage simpleMessage(int code, String msg){
        return new ResponseMessageBuilder().code(code).message(msg).build();
   }

}
