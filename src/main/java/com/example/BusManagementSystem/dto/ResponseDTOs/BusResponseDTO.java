package com.example.BusManagementSystem.dto.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusResponseDTO {
    private Long id;
    private String busNumber;
    private String busType;
    private int capacity;
    private String status;
    private int manufactureYear;
}
