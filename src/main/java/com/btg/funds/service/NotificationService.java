package com.btg.funds.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void sendNotification(String message) {
        // Lógica para enviar notificación por email o SMS
        System.out.println("Notificación: " + message);
    }
}
