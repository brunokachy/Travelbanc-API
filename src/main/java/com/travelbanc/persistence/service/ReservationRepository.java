/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.persistence.service;

import com.travelbanc.persistence.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bruno
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public Reservation findByReferenceNumber(String referenceNumber);

    List<Reservation> findTop12ByOwnerEmailOrderByDateCreatedDesc(String email);

}
