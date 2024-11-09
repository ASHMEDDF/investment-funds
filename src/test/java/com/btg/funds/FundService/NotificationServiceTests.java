package com.btg.funds.FundService;

import com.btg.funds.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;


public class NotificationServiceTests {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendNotification_successful() throws MessagingException {
        // Arrange
        String recipientEmail = "test@example.com";
        String messageContent = "Este es un mensaje de prueba";

        MimeMessage sentMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(sentMessage);
        helper.setTo(recipientEmail);
        helper.setText(messageContent);
        helper.setFrom(recipientEmail);

        // Act
        notificationService.sendNotification(recipientEmail, messageContent);

        // Assert
        verify(javaMailSender, times(2)).createMimeMessage();
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));

    }

    @Test
    void testSendNotification_throwsRuntimeException() {
        // Arrange
        String recipientEmail = "test@example.com";
        String messageContent = "Este es un mensaje de prueba";

        doThrow(new RuntimeException("Error de envío")).when(javaMailSender).send(any(MimeMessage.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> notificationService.sendNotification(recipientEmail, messageContent));

        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

}
