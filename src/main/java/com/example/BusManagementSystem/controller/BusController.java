package com.example.BusManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusManagementSystem.dto.RequestDTOs.BusRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.BusResponseDTO;
import com.example.BusManagementSystem.service.BusService;

@RestController
@RequestMapping("/api/buses")
@PreAuthorize("hasRole('ADMIN')")
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping
    public ResponseEntity<BusResponseDTO> createBus(@RequestBody BusRequestDTO requestDTO) {
        return new ResponseEntity<>(busService.createBus(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusResponseDTO> getBusById(@PathVariable Long id) {
        return new ResponseEntity<>(busService.getBusById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BusResponseDTO>> getAllBuses() {
        return new ResponseEntity<>(busService.getAllBuses(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusResponseDTO> updateBus(
            @PathVariable Long id,
            @RequestBody BusRequestDTO requestDTO) {
        return new ResponseEntity<>(busService.updateBus(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return new ResponseEntity<>("Bus deleted successfully", HttpStatus.OK);
    }
}
