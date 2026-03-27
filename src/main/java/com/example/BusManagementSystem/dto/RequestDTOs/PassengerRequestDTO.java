package com.example.BusManagementSystem.dto.RequestDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String idNumber;
}
