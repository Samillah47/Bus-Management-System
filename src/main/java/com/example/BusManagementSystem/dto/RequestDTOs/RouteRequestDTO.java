package com.example.BusManagementSystem.dto.RequestDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequestDTO {
    private String routeName;
    private String origin;
    private String destination;
    private double distanceKm;
    private String estimatedDuration;
    private String status;
}
