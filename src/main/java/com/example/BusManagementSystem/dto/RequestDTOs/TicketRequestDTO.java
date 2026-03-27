package com.example.BusManagementSystem.dto.RequestDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {
    private Long passengerId;
    private Long scheduleId;
    private String seatNumber;
    private double price;
    private String status;
}
