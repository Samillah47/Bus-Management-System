package com.example.BusManagementSystem.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "buses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_number", nullable = false, unique = true)
    private String busNumber;

    @Column(name = "bus_type", nullable = false)
    private String busType; // e.g., Standard, Express, Luxury

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "status", nullable = false)
    private String status; // e.g., Active, Maintenance, Retired

    @Column(name = "manufacture_year")
    private int manufactureYear;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL)
    private List<Schedule> schedules;
}
