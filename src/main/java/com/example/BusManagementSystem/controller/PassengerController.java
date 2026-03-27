package com.example.BusManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusManagementSystem.dto.RequestDTOs.PassengerRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.PassengerResponseDTO;
import com.example.BusManagementSystem.service.PassengerService;

@RestController
@RequestMapping("/api/passengers")
@PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerResponseDTO> createPassenger(@RequestBody PassengerRequestDTO requestDTO) {
        return new ResponseEntity<>(passengerService.createPassenger(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> getPassengerById(@PathVariable Long id) {
        return new ResponseEntity<>(passengerService.getPassengerById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PassengerResponseDTO>> getAllPassengers() {
        return new ResponseEntity<>(passengerService.getAllPassengers(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> updatePassenger(
            @PathVariable Long id,
            @RequestBody PassengerRequestDTO requestDTO) {
        return new ResponseEntity<>(passengerService.updatePassenger(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return new ResponseEntity<>("Passenger deleted successfully", HttpStatus.OK);
    }
}
