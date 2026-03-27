package com.example.BusManagementSystem.service;

import java.util.List;

import com.example.BusManagementSystem.dto.RequestDTOs.RouteRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.RouteResponseDTO;

public interface RouteService {
    RouteResponseDTO createRoute(RouteRequestDTO requestDTO);
    RouteResponseDTO getRouteById(Long id);
    List<RouteResponseDTO> getAllRoutes();
    RouteResponseDTO updateRoute(Long id, RouteRequestDTO requestDTO);
    void deleteRoute(Long id);
}
