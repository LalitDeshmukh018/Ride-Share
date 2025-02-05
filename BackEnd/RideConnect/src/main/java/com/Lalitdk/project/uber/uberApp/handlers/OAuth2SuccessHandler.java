package com.Lalitdk.project.uber.uberApp.handlers;

import java.io.IOException;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import com.Lalitdk.project.uber.uberApp.Security.JWTService;
import com.Lalitdk.project.uber.uberApp.entities.User;
import com.Lalitdk.project.uber.uberApp.entities.enums.Role;
import com.Lalitdk.project.uber.uberApp.services.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
            private final UserService userService;
            private final JWTService jwtService;
              @Value("${deploy.env}")
             private String deployEnv;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authToken.getPrincipal();

        String email = (oAuth2User.getAttribute("email"));
//here we check wheter the user exists or not, if not exists then we just create the user and save to the user repo
        User user = userService.getUserByEmail(email);
        if(user==null){
            User newUser = User.builder()
                            .name(oAuth2User.getAttribute("name"))
                            .email(email)
                            .roles(Set.of(Role.RIDER)) 
                            .build();

            user = userService.save(newUser);
        }
        
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

          Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);
        String frontEndUrl = "http://localhost:8080/home.html?token=" + accessToken;
        response.sendRedirect(frontEndUrl);
        
        
    }
}
