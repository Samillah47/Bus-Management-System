package com.example.BusManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BusManagementSystem.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByPassengerId(Long passengerId);
    List<Ticket> findByScheduleId(Long scheduleId);
}
