package com.example.BusManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusManagementSystem.dto.RequestDTOs.RouteRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.RouteResponseDTO;
import com.example.BusManagementSystem.service.RouteService;

@RestController
@RequestMapping("/api/routes")
@PreAuthorize("hasRole('ADMIN')")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping
    public ResponseEntity<RouteResponseDTO> createRoute(@RequestBody RouteRequestDTO requestDTO) {
        return new ResponseEntity<>(routeService.createRoute(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> getRouteById(@PathVariable Long id) {
        return new ResponseEntity<>(routeService.getRouteById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RouteResponseDTO>> getAllRoutes() {
        return new ResponseEntity<>(routeService.getAllRoutes(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponseDTO> updateRoute(
            @PathVariable Long id,
            @RequestBody RouteRequestDTO requestDTO) {
        return new ResponseEntity<>(routeService.updateRoute(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return new ResponseEntity<>("Route deleted successfully", HttpStatus.OK);
    }
}
