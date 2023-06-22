package com.backend.usuario.config.security;

import com.backend.usuario.config.auth.JWTAuthenticationFilter;
import com.backend.usuario.config.auth.JWTAuthorizationFilter;
import com.backend.usuario.service.impl.UserDetalheServiceImpl;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@NoArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetalheServiceImpl userDetalheServiceImpl;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    /**
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().ignoringAntMatchers("/api/**");
        // Enable CSRF protection for "/api/**" endpoint.
        // httpSecurity.csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/**"))

        httpSecurity.authorizeRequests()//
                .antMatchers(HttpMethod.POST, "/api/login-user").permitAll()
                .antMatchers(HttpMethod.POST, "/api/save-user").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/delete-user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers("/swagger-ui/***").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/configuration/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                // Disallow everything else..
                .anyRequest().authenticated();

        httpSecurity.headers().cacheControl();
        // JWT Auth
        httpSecurity.addFilter(new JWTAuthorizationFilter(authenticationManager()));
        // JWT Filter
        httpSecurity.addFilter(new JWTAuthenticationFilter(authenticationManager()));

    }
    /**
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /**
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetalheServiceImpl).passwordEncoder(bCryptPasswordEncoder);
    }

    /**
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // Configuração para todas as rotas
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}
