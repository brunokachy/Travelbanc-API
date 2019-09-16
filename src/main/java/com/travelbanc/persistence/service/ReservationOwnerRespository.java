/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.persistence.service;

import com.travelbanc.persistence.entity.ReservationOwner;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bruno
 */
public interface ReservationOwnerRespository extends JpaRepository<ReservationOwner, Long> {

    public ReservationOwner findFirstByEmail(String email);
}
