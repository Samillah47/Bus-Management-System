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

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // ── Extract info based on provider ──────────────────────────────────
        String email = oAuth2User.getAttribute("email");
        String name  = provider.equals("github")
                ? oAuth2User.getAttribute("login")
                : oAuth2User.getAttribute("name");

        // GitHub private email fallback
        if (email == null) {
            email = oAuth2User.getAttribute("id") + "@github.com";
        }

        final String finalEmail = email;
        final String finalName  = name;

        // ── Find or create user ─────────────────────────────────────────────
        User user = userRepository.findByEmail(finalEmail)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(finalEmail);
                    newUser.setUsername(generateUsername(finalName));
                    newUser.setPassword(UUID.randomUUID().toString());
                    newUser.setRole(Role.PASSENGER);
                    newUser.setEnabled(true);
                    log.info("New OAuth2 user registered: {}", finalEmail);
                    return userRepository.save(newUser);
                });

        // ── Attach OAuth2 attributes to user ────────────────────────────────
        user.setAttributes(oAuth2User.getAttributes());

        return user;
    }

    private String generateUsername(String name) {
        if (name == null) name = "user";
        String base     = name.replaceAll("\\s+", "").toLowerCase();
        String username = base;
        int    count    = 1;
        while (userRepository.existsByUsername(username)) {
            username = base + count++;
        }
        return username;
    }
}