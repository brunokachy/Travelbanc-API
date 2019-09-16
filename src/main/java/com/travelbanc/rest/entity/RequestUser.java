/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.rest.entity;

import com.travelbanc.enums.GenericRecordStatus;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;

public class RequestUser {
    
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private GenericRecordStatus status;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateCreated;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdated;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastLogin;
   
   
    
    
    
}
