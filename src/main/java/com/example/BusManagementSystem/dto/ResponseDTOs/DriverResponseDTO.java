package com.example.BusManagementSystem.dto.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String phoneNumber;
    private String email;
    private String status;
}
