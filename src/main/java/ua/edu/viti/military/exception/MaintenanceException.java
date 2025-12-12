package ua.edu.viti.military.exception;

/**
 * Exception thrown when maintenance-related issues occur.
 */
public class MaintenanceException extends RuntimeException {

    public MaintenanceException(String message) {
        super(message);
    }
}
