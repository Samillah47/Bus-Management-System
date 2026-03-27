package com.example.BusManagementSystem.security;

import com.example.BusManagementSystem.model.Role;
import com.example.BusManagementSystem.model.User;
import com.example.BusManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            String provider = userRequest.getClientRegistration().getRegistrationId();

            String email = oAuth2User.getAttribute("email");
            String name  = provider.equals("github")
                    ? oAuth2User.getAttribute("login")
                    : oAuth2User.getAttribute("name");

            if (email == null) {
                email = oAuth2User.getAttribute("id") + "@github.com";
            }

            final String finalEmail = email;
            final String finalName  = name;

            User user = userRepository.findByEmail(finalEmail)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(finalEmail);
                        newUser.setUsername(generateUsername(finalName));
                        newUser.setPassword(UUID.randomUUID().toString());
                        newUser.setRole(Role.PASSENGER);
                        newUser.setEnabled(true);
                        log.info("Creating new OAuth2 user in DB: {}", finalEmail);
                        return userRepository.save(newUser);
                    });

            user.setAttributes(oAuth2User.getAttributes());
            return user;

        } catch (Exception e) {
            log.error("CRITICAL ERROR in CustomOAuth2UserService: ", e);
            throw new OAuth2AuthenticationException("Failed to process OAuth2 user: " + e.getMessage());
        }
    }

    private String generateUsername(String name) {
        if (name == null) name = "user";
        String base = name.replaceAll("\\s+", "").toLowerCase();
        String username = base;
        int count = 1;
        // Note: This loop can be dangerous if not careful, but for testing it is fine
        while (userRepository.existsByUsername(username)) {
            username = base + count++;
        }
        return username;
    }
}