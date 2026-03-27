package com.example.BusManagementSystem.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BusManagementSystem.model.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
   
}
