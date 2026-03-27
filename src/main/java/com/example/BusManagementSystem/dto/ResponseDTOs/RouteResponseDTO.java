package com.example.BusManagementSystem.dto.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponseDTO {
    private Long id;
    private String routeName;
    private String origin;
    private String destination;
    private double distanceKm;
    private String estimatedDuration;
    private String status;
}
