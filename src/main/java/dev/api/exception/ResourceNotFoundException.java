package dev.api.exception;

import org.springframework.stereotype.Component;

public class ResourceNotFoundException extends Exception {
    
    public ResourceNotFoundException(){}

    public ResourceNotFoundException(String error){

        super(error);
    }

    
}
