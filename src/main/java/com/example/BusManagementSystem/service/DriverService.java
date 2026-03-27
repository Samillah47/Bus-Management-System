package com.example.BusManagementSystem.service;

import java.util.List;

import com.example.BusManagementSystem.dto.RequestDTOs.DriverRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.DriverResponseDTO;

public interface DriverService {
    DriverResponseDTO createDriver(DriverRequestDTO requestDTO);
    DriverResponseDTO getDriverById(Long id);
    List<DriverResponseDTO> getAllDrivers();
    DriverResponseDTO updateDriver(Long id, DriverRequestDTO requestDTO);
    void deleteDriver(Long id);
}
