package app.model;

import java.io.Serializable;

/**
 * Enum que descriu el tipus d'usuari: pot ser USER, ADMIN o WORKER
 * @author Sergio
 */
public enum UserType implements Serializable {
    USER,
    ADMIN,
    WORKER
}
