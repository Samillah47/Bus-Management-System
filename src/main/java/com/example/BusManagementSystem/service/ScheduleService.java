package com.example.BusManagementSystem.service;

import java.util.List;

import com.example.BusManagementSystem.dto.RequestDTOs.ScheduleRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.ScheduleResponseDTO;

public interface ScheduleService {
    ScheduleResponseDTO createSchedule(ScheduleRequestDTO requestDTO);
    ScheduleResponseDTO getScheduleById(Long id);
    List<ScheduleResponseDTO> getAllSchedules();
    List<ScheduleResponseDTO> getSchedulesByBusId(Long busId);
    List<ScheduleResponseDTO> getSchedulesByDriverId(Long driverId);
    List<ScheduleResponseDTO> getSchedulesByRouteId(Long routeId);
    ScheduleResponseDTO updateSchedule(Long id, ScheduleRequestDTO requestDTO);
    void deleteSchedule(Long id);
}
