package com.example.fullstack.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(Management->Management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                authorizeHttpRequests(Authorize->Authorize

                        .requestMatchers("/api/**").authenticated().anyRequest().permitAll()).
                addFilterBefore((Filter) new JwtTokenValidator(), BasicAuthenticationFilter.class).
                csrf(Csrf->Csrf.disable()).
                cors(cors->cors.configurationSource(corsConfigurationSource())).
                httpBasic(HttpBasic->HttpBasic.disable()).
                formLogin(FormLogin->FormLogin.disable()).
                logout(Logout->Logout.disable());
        return http.build();

    }


    private CorsConfigurationSource corsConfigurationSource() {
    return new CorsConfigurationSource(){

        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
           CorsConfiguration configuration=new CorsConfiguration();
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:5454"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST"));
            configuration.setAllowCredentials(true);
            //configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return configuration;
        }
    };

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




}
