package com.example.BusManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BusManagementSystem.dto.RequestDTOs.ScheduleRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.ScheduleResponseDTO;
import com.example.BusManagementSystem.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@PreAuthorize("hasAnyRole('ADMIN', 'DRIVER')")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@RequestBody ScheduleRequestDTO requestDTO) {
        return new ResponseEntity<>(scheduleService.createSchedule(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Long id) {
        return new ResponseEntity<>(scheduleService.getScheduleById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> getAllSchedules() {
        return new ResponseEntity<>(scheduleService.getAllSchedules(), HttpStatus.OK);
    }

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByBusId(@PathVariable Long busId) {
        return new ResponseEntity<>(scheduleService.getSchedulesByBusId(busId), HttpStatus.OK);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByDriverId(@PathVariable Long driverId) {
        return new ResponseEntity<>(scheduleService.getSchedulesByDriverId(driverId), HttpStatus.OK);
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<ScheduleResponseDTO>> getSchedulesByRouteId(@PathVariable Long routeId) {
        return new ResponseEntity<>(scheduleService.getSchedulesByRouteId(routeId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDTO requestDTO) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, requestDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>("Schedule deleted successfully", HttpStatus.OK);
    }
}
