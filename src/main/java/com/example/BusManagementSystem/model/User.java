package com.example.BusManagementSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User; // MUST IMPORT THIS

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails, OAuth2User { // Implements both

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    // --- OAUTH2 USER SECTION ---

    @Transient // CRITICAL: Tells Hibernate NOT to save this in the DB
    private Map<String, Object> attributes;

    @Override // REQUIRED by OAuth2User interface
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override // REQUIRED by OAuth2User interface
    public String getName() {
        return username; // or return email;
    }

    // --- USER DETAILS SECTION ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return enabled; }
    
    // Setter for attributes (used by CustomOAuth2UserService)
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}