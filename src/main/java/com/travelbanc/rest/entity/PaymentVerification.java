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
public class PaymentVerification {
    
    private String flwRef;
    
    private String secret;
    
    private Double amount;
    
    private Integer paymententity;
    
    private String paymentRef;
    
    private String paymentCode;

    public String getFlwRef() {
        return flwRef;
    }

    public void setFlwRef(String flwRef) {
        this.flwRef = flwRef;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getPaymententity() {
        return paymententity;
    }

    public void setPaymententity(Integer paymententity) {
        this.paymententity = paymententity;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }
    
    
    
}
