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

    public Loan(Media loanedMedia, User user) {
        this.loanedMedia = loanedMedia;
        this.user = user;
        setDateStartLoan(null);
        setDateEndLoan(null);
    }    

    public Loan(Media loanedMedia, User user, Date dateStartLoan, Date dateEndLoan) {
        this.loanedMedia = loanedMedia;
        this.user = user;
        this.dateStartLoan = dateStartLoan;
        this.dateEndLoan = dateEndLoan;
    }
    
    public static int getMaxLengthLoanInDays() {
        return maxLengthLoanInDays;
    }
    
    public Media getLoanedMedia() {
        return loanedMedia;
    }

    public void setLoanedMedia(Media loanedMedia) {
        if (loanedMedia == null) throw new ModelException("Error in setLoanedMedia: loanedMedia cannot be null");
        this.loanedMedia = loanedMedia;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) throw new ModelException("Error in setUser: user cannot be null");
        this.user = user;
    }

    public boolean isIsReturned() {
        return isReturned;
    }

    public void setIsReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    public Date getDateStartLoan() {
        return dateStartLoan;
    }

    public void setDateStartLoan(Date dateStartLoan) {
        if (dateStartLoan == null) {
            long currentTimeMillis = System.currentTimeMillis();
            dateStartLoan = new Date(currentTimeMillis);;
        }
        this.dateStartLoan = dateStartLoan;
    }

    public Date getDateEndLoan() {
        return dateEndLoan;
    }

    public void setDateEndLoan(Date dateEndLoan) {
        if (dateEndLoan == null) {
            long currentTimeMillis = System.currentTimeMillis();
            long millisInADay = 24 * 60 * 60 * 1000L;
            dateEndLoan = new Date(currentTimeMillis + (millisInADay * maxLengthLoanInDays));
        }
        this.dateEndLoan = dateEndLoan;
    }
}
