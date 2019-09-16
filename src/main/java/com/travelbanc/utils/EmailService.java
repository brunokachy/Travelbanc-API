/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.travelbanc.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.travelbanc.ProjectConstant;
import com.travelbanc.persistence.entity.PortalUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author Bruno
 */
@Service
public class EmailService {

    private final static String EMAIL_TEMPLATE_BASE_DIR = "mail/";

    private JavaMailSender mailSender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void sendPlainEmail(String to, String subject, String text) {
        String[] recipients = {to};
        sendPlainEmail(recipients, subject, text);
    }

    public void sendPlainEmail(String[] recipients, String subject, String text) {
        sendEmail(recipients, subject, text, false, "");
    }

    @Async
    public void sendHtmlEmail(String to, String subject, String templateFileName, Map<String, Object> emailVariables, String emailSender) {
        String[] recipients = {to};
        sendHtmlEmail(recipients, subject, templateFileName, emailVariables, emailSender);
    }

    public void sendHtmlEmail(String[] recipients, String subject, String templateFileName, Map<String, Object> emailVariables, String emailSender) {
        Context context = new Context();
        emailVariables.forEach(context::setVariable);
        String emailBody = templateEngine.process(EMAIL_TEMPLATE_BASE_DIR + templateFileName, context);
        sendEmail(recipients, subject, emailBody, true, emailSender);
    }

    private void sendEmail(String[] recipients, String subject, String content, boolean isHTML, String emailSender) {
        try {
            MimeMessage mailMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);
            helper.setTo(recipients);
            helper.setSubject(subject);
            helper.setFrom(new InternetAddress(emailSender));
            helper.setText(content, isHTML);

            /* DataSource aAttachment =  new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
            helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));
            memorynotfound-logo.png is in resource directory
            <img src="cid:logo.png" alt="https://memorynotfound.com" style="display: block;" />
            helper.addAttachment("report.pdf",aAttachment); */
            mailSender.send(mailMessage);
            System.out.println("Email sent>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            // log.info("Email sent: Subject: {}  Recipient: {}",subject, Arrays.toString(recipients));
        } catch (MessagingException | MailException ex) {
            ex.printStackTrace();
        }
    }

   
}
