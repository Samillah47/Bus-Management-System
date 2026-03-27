package com.example.BusManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusManagementSystem.dto.RequestDTOs.TicketRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.TicketResponseDTO;
import com.example.BusManagementSystem.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
@PreAuthorize("hasAnyRole('ADMIN', 'PASSENGER')")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> bookTicket(@RequestBody TicketRequestDTO requestDTO) {
        return new ResponseEntity<>(ticketService.bookTicket(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> getTicketById(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.getTicketById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        return new ResponseEntity<>(ticketService.getAllTickets(), HttpStatus.OK);
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByPassengerId(@PathVariable Long passengerId) {
        return new ResponseEntity<>(ticketService.getTicketsByPassengerId(passengerId), HttpStatus.OK);
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByScheduleId(@PathVariable Long scheduleId) {
        return new ResponseEntity<>(ticketService.getTicketsByScheduleId(scheduleId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @PathVariable Long id,
            @RequestBody TicketRequestDTO requestDTO) {
        return new ResponseEntity<>(ticketService.updateTicket(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelTicket(@PathVariable Long id) {
        ticketService.cancelTicket(id);
        return new ResponseEntity<>("Ticket cancelled successfully", HttpStatus.OK);
    }
}
