package api.dev.exceptions;

 
public class ResourceNotFoundException extends Exception {
    
    public ResourceNotFoundException(){}

    public ResourceNotFoundException(String error){

        super(error);
    }

    
}
