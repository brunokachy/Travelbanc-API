package com.travelbanc.persistence.service;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.travelbanc.persistence.entity.PortalUser;

public interface PortalUserRepository extends JpaRepository<PortalUser, Long> {

    public PortalUser findByEmail(String email);
    
    public PortalUser findByPhoneNumber(String phoneNumber);

    @Override
    public List<PortalUser> findAll();
    


   
    

}
