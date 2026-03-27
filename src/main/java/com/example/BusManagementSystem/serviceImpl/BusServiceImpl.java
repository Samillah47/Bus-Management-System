package com.example.BusManagementSystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BusManagementSystem.dto.RequestDTOs.BusRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.BusResponseDTO;
import com.example.BusManagementSystem.exception.DuplicateResourceException;
import com.example.BusManagementSystem.exception.ResourceNotFoundException;
import com.example.BusManagementSystem.model.Bus;
import com.example.BusManagementSystem.repository.BusRepository;
import com.example.BusManagementSystem.service.BusService;

@Service
public class BusServiceImpl implements BusService {

    @Autowired
    private BusRepository busRepository;

    @Override
    public BusResponseDTO createBus(BusRequestDTO requestDTO) {

        // Check if bus number already exists
        if (busRepository.existsByBusNumber(requestDTO.getBusNumber())) {
            throw new DuplicateResourceException(
                    "Bus",           // resource name
                    "busNumber",     // field name
                    requestDTO.getBusNumber() // field value
            );
        }

        Bus bus = new Bus();
        bus.setBusNumber(requestDTO.getBusNumber());
        bus.setBusType(requestDTO.getBusType());
        bus.setCapacity(requestDTO.getCapacity());
        bus.setStatus(requestDTO.getStatus());
        bus.setManufactureYear(requestDTO.getManufactureYear());

        Bus savedBus = busRepository.save(bus);
        return mapToResponseDTO(savedBus);
    }

    @Override
    public BusResponseDTO getBusById(Long id) {

        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bus",   // resource name
                        "id",    // field name
                        id       // field value
                ));

        return mapToResponseDTO(bus);
    }

    @Override
    public List<BusResponseDTO> getAllBuses() {
        List<Bus> buses = busRepository.findAll();
        return buses.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BusResponseDTO updateBus(Long id, BusRequestDTO requestDTO) {

        // Check bus exists
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus", "id", id));

        // Check if new bus number belongs to a DIFFERENT bus
        if (busRepository.existsByBusNumber(requestDTO.getBusNumber()) &&
                !bus.getBusNumber().equals(requestDTO.getBusNumber())) {
            throw new DuplicateResourceException(
                    "Bus", "busNumber", requestDTO.getBusNumber()
            );
        }

        bus.setBusNumber(requestDTO.getBusNumber());
        bus.setBusType(requestDTO.getBusType());
        bus.setCapacity(requestDTO.getCapacity());
        bus.setStatus(requestDTO.getStatus());
        bus.setManufactureYear(requestDTO.getManufactureYear());

        Bus updatedBus = busRepository.save(bus);
        return mapToResponseDTO(updatedBus);
    }

    @Override
    public void deleteBus(Long id) {

        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus", "id", id));

        busRepository.delete(bus);
    }

    private BusResponseDTO mapToResponseDTO(Bus bus) {
        BusResponseDTO dto = new BusResponseDTO();
        dto.setId(bus.getId());
        dto.setBusNumber(bus.getBusNumber());
        dto.setBusType(bus.getBusType());
        dto.setCapacity(bus.getCapacity());
        dto.setStatus(bus.getStatus());
        dto.setManufactureYear(bus.getManufactureYear());
        return dto;
    }
}