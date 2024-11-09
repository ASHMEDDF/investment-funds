package com.btg.funds.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class NotificationService {

    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

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
            helper.setFrom(sender);
            javaMailSender.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        // Lógica para enviar notificación por email o SMS
        System.out.println("Notificación: " + message);
    }
}
