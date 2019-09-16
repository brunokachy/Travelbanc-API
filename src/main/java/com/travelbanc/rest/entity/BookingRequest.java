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
public class BookingRequest {
    private Booking reservation;
    
    private BookingResponse bookingResponse;

    public Booking getReservation() {
        return reservation;
    }

    public void setReservation(Booking reservation) {
        this.reservation = reservation;
    }

    public BookingResponse getBookingResponse() {
        return bookingResponse;
    }

    public void setBookingResponse(BookingResponse bookingResponse) {
        this.bookingResponse = bookingResponse;
    }
    
    
    
}
