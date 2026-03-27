package com.example.BusManagementSystem.service;

import java.util.List;

import com.example.BusManagementSystem.dto.RequestDTOs.TicketRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.TicketResponseDTO;

public interface TicketService {
    TicketResponseDTO bookTicket(TicketRequestDTO requestDTO);
    TicketResponseDTO getTicketById(Long id);
    List<TicketResponseDTO> getAllTickets();
    List<TicketResponseDTO> getTicketsByPassengerId(Long passengerId);
    List<TicketResponseDTO> getTicketsByScheduleId(Long scheduleId);
    TicketResponseDTO updateTicket(Long id, TicketRequestDTO requestDTO);
    void cancelTicket(Long id);
}
