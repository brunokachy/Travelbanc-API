/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.enums;

public enum GenericRecordStatus {
    ACTIVE("Active"),
    DELETED("Deleted"),
    DEACTIVATED("Deactivated"),
    PENDING("Pending");

    private final String genericRecordStatus;

    private GenericRecordStatus(String genericRecordStatus) {
        this.genericRecordStatus = genericRecordStatus;
    }

    public String getGenericRecordStatus() {
        return genericRecordStatus;
    }
}
