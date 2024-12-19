/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Classe que emmagatzema els préstecs
 * @author Sergio
 */
public class Loan implements Serializable {
    
    private static int maxLengthLoanInDays = 30;
    
    private Media loanedMedia;
    private User user;
    private boolean isReturned = false;
    private Date dateStartLoan;
    private Date dateEndLoan;

    /**
 * Constructor de la clase {@code Loan} que crea un nuevo préstamo con el medio y el usuario especificados.
 * Inicializa las fechas de inicio y fin del préstamo como {@code null}.
 *
 * @param loanedMedia el medio prestado, representado por un objeto de la clase {@code Media}.
 * @param user el usuario que realiza el préstamo, representado por un objeto de la clase {@code User}.
 */
    public Loan(Media loanedMedia, User user) {
        this.loanedMedia = loanedMedia;
        this.user = user;
        setDateStartLoan(null);
        setDateEndLoan(null);
    }    

    /**
 * Constructor de la clase {@code Loan} que crea un nuevo préstamo con el medio, usuario 
 * y las fechas de inicio y fin especificadas.
 *
 * @param loanedMedia el medio prestado, representado por un objeto de la clase {@code Media}.
 * @param user el usuario que realiza el préstamo, representado por un objeto de la clase {@code User}.
 * @param dateStartLoan la fecha de inicio del préstamo, representada por un objeto de la clase {@code Date}.
 * @param dateEndLoan la fecha de fin del préstamo, representada por un objeto de la clase {@code Date}.
 */
    public Loan(Media loanedMedia, User user, Date dateStartLoan, Date dateEndLoan) {
        this.loanedMedia = loanedMedia;
        this.user = user;
        this.dateStartLoan = dateStartLoan;
        this.dateEndLoan = dateEndLoan;
    }
    
    /**
 * Obtiene el valor máximo permitido para la duración de un préstamo en días.
 *
 * @return el número máximo de días que un préstamo puede durar.
 */
    public static int getMaxLengthLoanInDays() {
        return maxLengthLoanInDays;
    }
    
    /**
    * Obtiene el medio que está siendo prestado.
    *
    * @return el objeto {@code Media} que representa el medio prestado.
    */
    public Media getLoanedMedia() {
        return loanedMedia;
    }

    /**
    * Establece el medio que será prestado.
    * 
    * @param loanedMedia el objeto {@code Media} que representa el medio a ser prestado.
    * @throws ModelException si {@code loanedMedia} es {@code null}.
    */
    public void setLoanedMedia(Media loanedMedia) {
        if (loanedMedia == null) throw new ModelException("Error in setLoanedMedia: loanedMedia cannot be null");
        this.loanedMedia = loanedMedia;
    }

    /**
    * Retorna l'usuari associat amb l'objecte actual.
    * @return l'usuari associat amb aquest objecte.
    */
    public User getUser() {
        return user;
    }

    /**
     * Establece el usuario que realiza el préstamo.
     * 
     * @param user el objeto {@code User} que representa al usuario que realiza el préstamo.
     * @throws ModelException si {@code user} es {@code null}.
     */
    public void setUser(User user) {
        if (user == null) throw new ModelException("Error in setUser: user cannot be null");
        this.user = user;
    }

    /**
    * Indica si el préstamo ha sido devuelto.
    * 
    * @return {@code true} si el préstamo ha sido devuelto, {@code false} en caso contrario.
    */
    public boolean isIsReturned() {
        return isReturned;
    }

    /**
    * Establece el estado de devolución del préstamo.
    * 
    * @param isReturned el valor {@code true} si el préstamo ha sido devuelto, {@code false} en caso contrario.
    */
    public void setIsReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    /**
    * Obtiene la fecha de inicio del préstamo.
    * 
    * @return la fecha de inicio del préstamo, representada por un objeto {@code Date}.
    */
    public Date getDateStartLoan() {
        return dateStartLoan;
    }

    /**
     * Establece la fecha de inicio del préstamo.
     * Si el parámetro {@code dateStartLoan} es {@code null}, se establece la fecha actual.
     * 
     * @param dateStartLoan la fecha de inicio del préstamo, representada por un objeto {@code Date}.
     *                      Si es {@code null}, se utiliza la fecha actual.
     */
    public void setDateStartLoan(Date dateStartLoan) {
        if (dateStartLoan == null) {
            long currentTimeMillis = System.currentTimeMillis();
            dateStartLoan = new Date(currentTimeMillis);;
        }
        this.dateStartLoan = dateStartLoan;
    }

    	/**
	 * Obtiene la fecha de fin del préstamo.
	 * 
	 * @return la fecha de fin del préstamo, representada por un objeto {@code Date}.
	 */
    public Date getDateEndLoan() {
        return dateEndLoan;
    }

    /**
     * Establece la fecha de fin del préstamo.
     * Si el parámetro {@code dateEndLoan} es {@code null}, se establece la fecha de fin 
     * como la fecha actual más el número máximo de días permitidos para el préstamo.
     * 
     * @param dateEndLoan la fecha de fin del préstamo, representada por un objeto {@code Date}.
     *                    Si es {@code null}, se calcula automáticamente.
     */
    public void setDateEndLoan(Date dateEndLoan) {
        if (dateEndLoan == null) {
            long currentTimeMillis = System.currentTimeMillis();
            long millisInADay = 24 * 60 * 60 * 1000L;
            dateEndLoan = new Date(currentTimeMillis + (millisInADay * maxLengthLoanInDays));
        }
        this.dateEndLoan = dateEndLoan;
    }
}
