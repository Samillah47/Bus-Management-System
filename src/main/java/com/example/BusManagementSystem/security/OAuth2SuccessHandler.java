package com.example.BusManagementSystem.security;

import com.example.BusManagementSystem.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        User user  = (User) authentication.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(user);

        log.info("OAuth2 login success for: {}", user.getEmail());

        // Return JWT as JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(
            "{"                                          +
            "\"token\":\""    + jwt              + "\"," +
            "\"username\":\"" + user.getUsername()+ "\"," +
            "\"email\":\""    + user.getEmail()  + "\"," +
            "\"role\":\""     + user.getRole()   + "\""  +
            "}"
        );
    }
}