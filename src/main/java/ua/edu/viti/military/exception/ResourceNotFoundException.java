package ua.edu.viti.military.exception;

public class ResourceNotFoundException extends BaseException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " з ID " + id + " не знайдено");
    }
}
