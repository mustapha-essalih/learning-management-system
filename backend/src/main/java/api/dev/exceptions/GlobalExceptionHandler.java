package api.dev.exceptions;


import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

 
@ControllerAdvice
public class GlobalExceptionHandler { 

    
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<?> userNotFoundException(BadCredentialsException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

   
    
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> runTimeException(RuntimeException e) {
       
        if (e instanceof AccessDeniedException) 
        {
            return ResponseEntity.status(403).body("you don't have permission to access this resource");
        }
       
        
        System.out.println(e.getLocalizedMessage());
        System.out.println("RuntimeException");
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> dataAccessException(DataIntegrityViolationException e){
 
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFoundException e) 
    {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(value = DisabledException.class)
    public ResponseEntity<?> DisabledUser(DisabledException e) {
        return ResponseEntity.status(403).body("should active your email, check your email");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(400).body(errorMap);
    }

    // AuthenticationException
    // should pass correct exception to the method and the correct status code
}
