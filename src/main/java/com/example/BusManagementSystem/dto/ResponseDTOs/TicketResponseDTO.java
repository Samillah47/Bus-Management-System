package com.example.BusManagementSystem.dto.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private Long passengerId;
    private String passengerName;
    private Long scheduleId;
    private String routeName;
    private String origin;
    private String destination;
    private String busNumber;
    private String seatNumber;
    private double price;
    private LocalDateTime bookingDateTime;
    private String status;
}
