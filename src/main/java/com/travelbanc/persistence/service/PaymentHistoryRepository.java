/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.persistence.service;

import com.travelbanc.persistence.entity.PaymentHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Bruno
 */
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

  

    @Override
    public List<PaymentHistory> findAll();
    
    

}
