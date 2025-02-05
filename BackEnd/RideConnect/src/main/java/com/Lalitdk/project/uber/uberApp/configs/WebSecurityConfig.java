package com.Lalitdk.project.uber.uberApp.configs;

import com.Lalitdk.project.uber.uberApp.Security.JwtAuthFilter;
import com.Lalitdk.project.uber.uberApp.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private static final String[] PUBLIC_ROUTES = {"/auth/**", "/home.html"};

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        
        httpSecurity
        .sessionManagement(sessionConfig -> sessionConfig
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(csrfCongif -> csrfCongif.disable())
        .authorizeHttpRequests(auth -> auth
        .requestMatchers(PUBLIC_ROUTES).permitAll()
        .anyRequest(). authenticated()

        )
        // .formLogin(form -> form
        //     .defaultSuccessUrl("/auth/login")  // Redirect after successful login

        //     .permitAll()
        // )
        
        
        .oauth2Login(oauth2Config-> oauth2Config 
                        .failureUrl("/login?error=ture")
                         .successHandler(oAuth2SuccessHandler))
    
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
