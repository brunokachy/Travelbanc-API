/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.enums;

public enum UnitType {
    PERCENT("Percent"),
    AMOUNT("Amount");

    private final String unitType;

    private UnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitType() {
        return unitType;
    }
}
