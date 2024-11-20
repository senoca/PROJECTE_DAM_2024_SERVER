package app.model;

/**
 *
 * Classe usada per les excepcions causades al model
 */
public class ModelException extends RuntimeException {

    /**
     * Constructor
     * @param message
     */
    public ModelException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message
     * @param cause
     */
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor
     * @param cause
     */
    public ModelException(Throwable cause) {
        super(cause);
    }
    
    
}
