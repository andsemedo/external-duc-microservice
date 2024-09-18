package com.andsemedodev.externalducmicroservice.security;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class BasicAuthConfig {
    @Value("${auth.username}")
    private String username;
    @Value("${auth.password}")
    private String password;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private static String[] AUTH_WHITELIST = {
            "/v3/api-docs/**", "/api-docs/**", "/error",
            "/api/swagger-ui/**", "/api/swagger-ui.html", "/api/docs.html"
    };

    public BasicAuthConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(expressionInterceptUrlRegistry ->
                        expressionInterceptUrlRegistry.requestMatchers(AUTH_WHITELIST).permitAll()
                                .anyRequest().authenticated())
                .httpBasic(httpSecurityHttpBasicConfigurer ->
                        httpSecurityHttpBasicConfigurer.authenticationEntryPoint(restAuthenticationEntryPoint))
                .csrf(AbstractHttpConfigurer::disable);

//        http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .inMemoryAuthentication()
                .withUser(username)
                .password(passwordEncoder.encode(password))
                .authorities("ROLE_USER");
        return authenticationManagerBuilder.build();
    }


}