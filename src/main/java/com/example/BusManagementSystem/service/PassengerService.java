package com.example.BusManagementSystem.service;

import java.util.List;

import com.example.BusManagementSystem.dto.RequestDTOs.PassengerRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.PassengerResponseDTO;

public interface PassengerService {
    PassengerResponseDTO createPassenger(PassengerRequestDTO requestDTO);
    PassengerResponseDTO getPassengerById(Long id);
    List<PassengerResponseDTO> getAllPassengers();
    PassengerResponseDTO updatePassenger(Long id, PassengerRequestDTO requestDTO);
    void deletePassenger(Long id);
}
