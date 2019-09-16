/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.rest.entity;

import java.util.List;

/**
 *
 * @author Bruno
 */
public class Booking {

    private String reservationType;

    private String title;

    private String description;

    private Integer amount;

    private ReservationOwner reservationOwner;

    private List<ReservationOwner> travellers;
    
    private String checkinDate;
    
    private String checkoutDate;
    
    private String hotelLocation;
    
    private String ticketLimitTime;

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ReservationOwner getReservationOwner() {
        return reservationOwner;
    }

    public void setReservationOwner(ReservationOwner reservationOwner) {
        this.reservationOwner = reservationOwner;
    }

    public List<ReservationOwner> getTravellers() {
        return travellers;
    }

    public void setTravellers(List<ReservationOwner> travellers) {
        this.travellers = travellers;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }

    public String getTicketLimitTime() {
        return ticketLimitTime;
    }

    public void setTicketLimitTime(String ticketLimitTime) {
        this.ticketLimitTime = ticketLimitTime;
    }
    
    

}
