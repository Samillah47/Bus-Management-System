package com.example.BusManagementSystem.security;

import com.example.BusManagementSystem.model.User;
import com.fasterxml.jackson.databind.ObjectMapper; // Use this for safe JSON
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper; // Spring will automatically inject this

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        try {
            User user = (User) authentication.getPrincipal();
            String jwt = jwtTokenProvider.generateToken(user);

            log.info("OAuth2 login successful for user: {}", user.getEmail());

            // Create a Map instead of a manual String to ensure valid JSON
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", jwt);
            responseData.put("username", user.getUsername());
            responseData.put("email", user.getEmail());
            responseData.put("role", user.getRole());

            // Set response headers
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);

            // Write the Map as JSON using ObjectMapper
            response.getWriter().write(objectMapper.writeValueAsString(responseData));

        } catch (Exception e) {
            log.error("Error in OAuth2SuccessHandler: ", e);
            // If something goes wrong, send a clear error message to the browser
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Internal Server Error during OAuth2 success logic\"}");
        }
    }
}