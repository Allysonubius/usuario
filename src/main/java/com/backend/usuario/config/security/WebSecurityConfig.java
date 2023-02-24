package com.backend.usuario.config.security;

import com.backend.usuario.config.auth.JWTAuthenticationFilter;
import com.backend.usuario.config.auth.JWTAuthorizationFilter;
import com.backend.usuario.service.impl.UserDetalheServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.backend.usuario.constants.SecurityConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetalheServiceImpl userDetalheServiceImpl;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public WebSecurityConfig(UserDetalheServiceImpl userDetalheServiceImpl, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userDetalheServiceImpl = userDetalheServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    protected void configure(HttpSecurity httpSecurity)throws Exception{
        // Disable CSRF (cross site request forgery)
        httpSecurity.csrf().disable();
        // Entry points
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.GET, CREATE_USER_URL).permitAll()
                .anyRequest().authenticated();
        // JWT Auth
        httpSecurity.addFilter(new JWTAuthorizationFilter(authenticationManager()));
        // JWT Filter
        httpSecurity.addFilter(new JWTAuthenticationFilter(authenticationManager()));
        // If a user try to access a resource without having enough permissions
        httpSecurity.exceptionHandling().accessDeniedPage("/api/login");
        // this disable session creation on Spring Security
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    @Override
    public void configure(WebSecurity webSecurityConfig)throws Exception{
        // Allow swagger to be accessed without authentication
        webSecurityConfig.ignoring()
                .antMatchers("/swagger-ui/***")
                .antMatchers("/v2/api-docs")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/configuration/**")
                .antMatchers("/webjars/**");
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder)throws Exception{
        authenticationManagerBuilder.userDetailsService(userDetalheServiceImpl).passwordEncoder(bCryptPasswordEncoder);
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
