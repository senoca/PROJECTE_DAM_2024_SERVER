package app.model;

/**
 *
 * Classe usada per les excepcions causades al model
 */
public class ModelException extends RuntimeException {
    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }
    
    
}
