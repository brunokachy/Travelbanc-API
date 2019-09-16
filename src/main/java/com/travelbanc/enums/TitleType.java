/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.enums;

public enum TitleType {
    MR("Mr"),
    MISS("Miss"),
    MASTER("Master"),
    MRS("Mrs");

    private final String titleType;

    private TitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getTitleType() {
        return titleType;
    }
}
