package com.example.BusManagementSystem.service;

import java.util.List;

import com.example.BusManagementSystem.dto.RequestDTOs.*;
import com.example.BusManagementSystem.dto.ResponseDTOs.*;

public interface BusService {
    BusResponseDTO createBus(BusRequestDTO requestDTO);
    BusResponseDTO getBusById(Long id);
    List<BusResponseDTO> getAllBuses();
    BusResponseDTO updateBus(Long id, BusRequestDTO requestDTO);
    void deleteBus(Long id);
}
