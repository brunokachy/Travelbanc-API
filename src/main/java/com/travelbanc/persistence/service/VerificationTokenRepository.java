package com.travelbanc.persistence.service;

import com.travelbanc.persistence.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

public VerificationToken findByToken(String token);   
}
