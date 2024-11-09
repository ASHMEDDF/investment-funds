package com.btg.funds.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class NotificationService {

    JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(String email, String messageEmail) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setSubject("Informacion Proceso de Fondos");
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setText(messageEmail);
            helper.setFrom(email);
            javaMailSender.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
