/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.model;

import java.time.LocalDate;

/**
 *
 * @author Sergio
 */
public class Loan {
    
    private static int maxLengthLoanInDays = 30;
    
    private Media loanedMedia;
    private User user;
    private boolean isReturned = false;
    private LocalDate date_start_loan;
    private LocalDate date_end_loan;

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

    public LocalDate getDate_start_loan() {
        return date_start_loan;
    }

    public void setDate_start_loan(LocalDate date_start_loan) {
        this.date_start_loan = date_start_loan;
    }

    public LocalDate getDate_end_loan() {
        return date_end_loan;
    }

    public void setDate_end_loan(LocalDate date_end_loan) {
        this.date_end_loan = date_end_loan;
    }
}
