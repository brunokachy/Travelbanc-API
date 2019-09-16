/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.enums;

public enum ReservationType {

    FLIGHT("Flight"),
    HOTEL("Hotel");

    private final String reservationType;

    private ReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

    public String getReservationType() {
        return reservationType;
    }
}
