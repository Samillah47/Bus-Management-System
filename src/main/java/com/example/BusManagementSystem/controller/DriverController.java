package com.example.BusManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusManagementSystem.dto.RequestDTOs.DriverRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.DriverResponseDTO;
import com.example.BusManagementSystem.service.DriverService;

@RestController
@RequestMapping("/api/drivers")
@PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    public ResponseEntity<DriverResponseDTO> createDriver(@RequestBody DriverRequestDTO requestDTO) {
        return new ResponseEntity<>(driverService.createDriver(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getDriverById(@PathVariable Long id) {
        return new ResponseEntity<>(driverService.getDriverById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<DriverResponseDTO>> getAllDrivers() {
        return new ResponseEntity<>(driverService.getAllDrivers(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> updateDriver(
            @PathVariable Long id,
            @RequestBody DriverRequestDTO requestDTO) {
        return new ResponseEntity<>(driverService.updateDriver(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
        return new ResponseEntity<>("Driver deleted successfully", HttpStatus.OK);
    }
}
