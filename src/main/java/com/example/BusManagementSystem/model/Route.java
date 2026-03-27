package com.example.BusManagementSystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;

    @Column(name = "estimated_duration")
    private String estimatedDuration; // e.g "2h 30m"

    @Column(name = "status")
    private String status; // e.g., Active, Inactive

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private List<Schedule> schedules;
}
