package com.example.BusManagementSystem.controller;

import com.example.BusManagementSystem.model.Role;
import com.example.BusManagementSystem.model.User;
import com.example.BusManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_shouldReturn200_whenUserHasAdminRole() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.PASSENGER);

        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUser_shouldReturn403_whenUserHasUserRole() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.PASSENGER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_shouldReturn200_whenUserHasAdminRole() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.PASSENGER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
}
