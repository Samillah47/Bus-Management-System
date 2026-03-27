package com.example.BusManagementSystem.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BusManagementSystem.dto.RequestDTOs.*;
import com.example.BusManagementSystem.dto.ResponseDTOs.*;
import com.example.BusManagementSystem.exception.BadRequestException;
import com.example.BusManagementSystem.exception.ResourceNotFoundException;
import com.example.BusManagementSystem.model.*;
import com.example.BusManagementSystem.repository.*;
import com.example.BusManagementSystem.service.*;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PassengrRepository passengerRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public TicketResponseDTO bookTicket(TicketRequestDTO requestDTO) {

        Passenger passenger = passengerRepository.findById(requestDTO.getPassengerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Passenger", "id", requestDTO.getPassengerId()
                ));

        Schedule schedule = scheduleRepository.findById(requestDTO.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Schedule", "id", requestDTO.getScheduleId()
                ));

        if (schedule.getStatus().equalsIgnoreCase("Cancelled")) {
            throw new BadRequestException(
                    "Cannot book ticket. Schedule with id: "
                    + requestDTO.getScheduleId() + " is Cancelled"
            );
        }

        if (schedule.getAvailableSeats() <= 0) {
            throw new BadRequestException(
                    "No available seats for schedule with id: "
                    + requestDTO.getScheduleId()
            );
        }

        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setSchedule(schedule);
        ticket.setSeatNumber(requestDTO.getSeatNumber());
        ticket.setPrice(requestDTO.getPrice());
        ticket.setBookingDateTime(LocalDateTime.now());
        ticket.setStatus("Confirmed");

        schedule.setAvailableSeats(schedule.getAvailableSeats() - 1);
        scheduleRepository.save(schedule);

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToResponseDTO(savedTicket);
    }

    @Override
    public TicketResponseDTO getTicketById(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        return mapToResponseDTO(ticket);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDTO> getTicketsByPassengerId(Long passengerId) {

        if (!passengerRepository.existsById(passengerId)) {
            throw new ResourceNotFoundException("Passenger", "id", passengerId);
        }

        return ticketRepository.findByPassengerId(passengerId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketResponseDTO> getTicketsByScheduleId(Long scheduleId) {

        if (!scheduleRepository.existsById(scheduleId)) {
            throw new ResourceNotFoundException("Schedule", "id", scheduleId);
        }

        return ticketRepository.findByScheduleId(scheduleId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO requestDTO) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        if (ticket.getStatus().equalsIgnoreCase("Cancelled")) {
            throw new BadRequestException(
                    "Cannot update ticket with id: " + id + ". Ticket is already Cancelled"
            );
        }

        ticket.setSeatNumber(requestDTO.getSeatNumber());
        ticket.setPrice(requestDTO.getPrice());
        ticket.setStatus(requestDTO.getStatus());

        Ticket updatedTicket = ticketRepository.save(ticket);
        return mapToResponseDTO(updatedTicket);
    }

    @Override
    public void cancelTicket(Long id) {

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", id));

        if (ticket.getStatus().equalsIgnoreCase("Cancelled")) {
            throw new BadRequestException(
                    "Ticket with id: " + id + " is already Cancelled"
            );
        }

        ticket.setStatus("Cancelled");

        Schedule schedule = ticket.getSchedule();
        schedule.setAvailableSeats(schedule.getAvailableSeats() + 1);
        scheduleRepository.save(schedule);

        ticketRepository.save(ticket);
    }

    private TicketResponseDTO mapToResponseDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setPassengerId(ticket.getPassenger().getId());
        dto.setPassengerName(ticket.getPassenger().getFirstName()
                + " " + ticket.getPassenger().getLastName());
        dto.setScheduleId(ticket.getSchedule().getId());
        dto.setRouteName(ticket.getSchedule().getRoute().getRouteName());
        dto.setOrigin(ticket.getSchedule().getRoute().getOrigin());
        dto.setDestination(ticket.getSchedule().getRoute().getDestination());
        dto.setBusNumber(ticket.getSchedule().getBus().getBusNumber());
        dto.setSeatNumber(ticket.getSeatNumber());
        dto.setPrice(ticket.getPrice());
        dto.setBookingDateTime(ticket.getBookingDateTime());
        dto.setStatus(ticket.getStatus());
        return dto;
    }
}