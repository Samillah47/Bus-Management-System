package com.example.BusManagementSystem.exception;
import lombok.Getter;

@Getter
public class DuplicateResourceException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public DuplicateResourceException(String resourceName,
                                      String fieldName,
                                      Object fieldValue) {
        super(String.format("%s already exists with %s : '%s'",
                resourceName, fieldName, fieldValue));

        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
