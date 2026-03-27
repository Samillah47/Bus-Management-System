package com.example.BusManagementSystem.dto.RequestDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequestDTO {
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String phoneNumber;
    private String email;
    private String status;
}
