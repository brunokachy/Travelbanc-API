/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.rest.entity;

/**
 *
 * @author Bruno
 */
public class BookingResponse {
    
    private boolean successful;
    private String message;
    private String referenceNumber;
    private String bookingNumber;
    private String ticketLimitDate;
    private Integer paidAmount;
    private String paymentReference;
    private String paymentType;
    private String confirmed;
    private String bookingExpireTime;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getTicketLimitDate() {
        return ticketLimitDate;
    }

    public void setTicketLimitDate(String ticketLimitDate) {
        this.ticketLimitDate = ticketLimitDate;
    }

    public Integer getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Integer paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getBookingExpireTime() {
        return bookingExpireTime;
    }

    public void setBookingExpireTime(String bookingExpireTime) {
        this.bookingExpireTime = bookingExpireTime;
    }
    
    
    
}
