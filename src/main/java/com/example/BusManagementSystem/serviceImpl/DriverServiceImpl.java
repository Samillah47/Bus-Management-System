package com.example.BusManagementSystem.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BusManagementSystem.dto.RequestDTOs.DriverRequestDTO;
import com.example.BusManagementSystem.dto.ResponseDTOs.DriverResponseDTO;
import com.example.BusManagementSystem.exception.DuplicateResourceException;
import com.example.BusManagementSystem.exception.ResourceNotFoundException;
import com.example.BusManagementSystem.model.Driver;
import com.example.BusManagementSystem.repository.DriverRepository;
import com.example.BusManagementSystem.service.DriverService;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public DriverResponseDTO createDriver(DriverRequestDTO requestDTO) {

        // Check if license number already exists
        if (driverRepository.existsByLicenseNumber(requestDTO.getLicenseNumber())) {
            throw new DuplicateResourceException(
                    "Driver", "licenseNumber", requestDTO.getLicenseNumber()
            );
        }

        Driver driver = new Driver();
        driver.setFirstName(requestDTO.getFirstName());
        driver.setLastName(requestDTO.getLastName());
        driver.setLicenseNumber(requestDTO.getLicenseNumber());
        driver.setPhoneNumber(requestDTO.getPhoneNumber());
        driver.setEmail(requestDTO.getEmail());
        driver.setStatus(requestDTO.getStatus());

        Driver savedDriver = driverRepository.save(driver);
        return mapToResponseDTO(savedDriver);
    }

    @Override
    public DriverResponseDTO getDriverById(Long id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", "id", id));

        return mapToResponseDTO(driver);
    }

    @Override
    public List<DriverResponseDTO> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponseDTO updateDriver(Long id, DriverRequestDTO requestDTO) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", "id", id));

        // Check license number belongs to a different driver
        if (driverRepository.existsByLicenseNumber(requestDTO.getLicenseNumber()) &&
                !driver.getLicenseNumber().equals(requestDTO.getLicenseNumber())) {
            throw new DuplicateResourceException(
                    "Driver", "licenseNumber", requestDTO.getLicenseNumber()
            );
        }

        driver.setFirstName(requestDTO.getFirstName());
        driver.setLastName(requestDTO.getLastName());
        driver.setLicenseNumber(requestDTO.getLicenseNumber());
        driver.setPhoneNumber(requestDTO.getPhoneNumber());
        driver.setEmail(requestDTO.getEmail());
        driver.setStatus(requestDTO.getStatus());

        Driver updatedDriver = driverRepository.save(driver);
        return mapToResponseDTO(updatedDriver);
    }

    @Override
    public void deleteDriver(Long id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver", "id", id));

        driverRepository.delete(driver);
    }

    private DriverResponseDTO mapToResponseDTO(Driver driver) {
        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setId(driver.getId());
        dto.setFirstName(driver.getFirstName());
        dto.setLastName(driver.getLastName());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setPhoneNumber(driver.getPhoneNumber());
        dto.setEmail(driver.getEmail());
        dto.setStatus(driver.getStatus());
        return dto;
    }
}
