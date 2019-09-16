/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author Bruno
 */
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent>{

  //  private com.travelbanc.utils.EmailService emailService;
//    @Autowired
//    public void setEmailService(com.travelbanc.utils.EmailService emailService) {
//        this.emailService = emailService;
//    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent e) {
       // System.out.println("IN EMAIL TEST");
     //  testMail();
    }
    private void testMail(){
//        Map<String, Object> test = new HashMap<>();
//        test.put("name", "Kenneth");
//        emailService.sendHtmlEmail("justin.nwanya@travelbeta.com", "As you Know it", "email-test", test);
        
    }
}
