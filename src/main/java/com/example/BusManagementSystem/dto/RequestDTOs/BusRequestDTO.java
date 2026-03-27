package com.example.BusManagementSystem.dto.RequestDTOs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusRequestDTO {
    private String busNumber;
    private String busType;
    private int capacity;
    private String status;
    private int manufactureYear;
}
