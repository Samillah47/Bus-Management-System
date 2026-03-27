package com.example.BusManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BusManagementSystem.model.Passenger;

@Repository
public interface PassengrRepository extends JpaRepository<Passenger, Long> {
    
    boolean existsByEmail(String email);
    boolean existsByIdNumber(String idNumber);
}
