package com.example.BusManagementSystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BusManagementSystem.dto.RequestDTOs.RouteRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.RouteResponseDTO;
import com.example.BusManagementSystem.exception.ResourceNotFoundException;
import com.example.BusManagementSystem.model.Route;
import com.example.BusManagementSystem.repository.RouteRepository;
import com.example.BusManagementSystem.service.RouteService;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public RouteResponseDTO createRoute(RouteRequestDTO requestDTO) {

        Route route = new Route();
        route.setRouteName(requestDTO.getRouteName());
        route.setOrigin(requestDTO.getOrigin());
        route.setDestination(requestDTO.getDestination());
        route.setDistanceKm(requestDTO.getDistanceKm());
        route.setEstimatedDuration(requestDTO.getEstimatedDuration());
        route.setStatus(requestDTO.getStatus());

        Route savedRoute = routeRepository.save(route);
        return mapToResponseDTO(savedRoute);
    }

    @Override
    public RouteResponseDTO getRouteById(Long id) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));

        return mapToResponseDTO(route);
    }

    @Override
    public List<RouteResponseDTO> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RouteResponseDTO updateRoute(Long id, RouteRequestDTO requestDTO) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));

        route.setRouteName(requestDTO.getRouteName());
        route.setOrigin(requestDTO.getOrigin());
        route.setDestination(requestDTO.getDestination());
        route.setDistanceKm(requestDTO.getDistanceKm());
        route.setEstimatedDuration(requestDTO.getEstimatedDuration());
        route.setStatus(requestDTO.getStatus());

        Route updatedRoute = routeRepository.save(route);
        return mapToResponseDTO(updatedRoute);
    }

    @Override
    public void deleteRoute(Long id) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));

        routeRepository.delete(route);
    }

    private RouteResponseDTO mapToResponseDTO(Route route) {
        RouteResponseDTO dto = new RouteResponseDTO();
        dto.setId(route.getId());
        dto.setRouteName(route.getRouteName());
        dto.setOrigin(route.getOrigin());
        dto.setDestination(route.getDestination());
        dto.setDistanceKm(route.getDistanceKm());
        dto.setEstimatedDuration(route.getEstimatedDuration());
        dto.setStatus(route.getStatus());
        return dto;
    }
}