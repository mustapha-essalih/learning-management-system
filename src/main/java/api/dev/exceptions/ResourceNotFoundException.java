package api.dev.exceptions;

import api.dev.utils.ApiResponse;

public class ResourceNotFoundException extends Exception {
    
    public ResourceNotFoundException(){}

    public ResourceNotFoundException(String error){

        super(error);
    }

    public ResourceNotFoundException(ApiResponse error){

        super(error.getMessage());
    }

    
}
