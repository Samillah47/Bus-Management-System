package com.example.BusManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BusManagementSystem.model.Bus;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    boolean existsByBusNumber(String busNumber);
}
