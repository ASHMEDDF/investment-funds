package com.btg.funds.FundService;

import com.btg.funds.exceptions.FundNotFoundException;
import com.btg.funds.exceptions.InsufficientFundsException;
import com.btg.funds.model.Fund;
import com.btg.funds.model.Transaction;
import com.btg.funds.repository.FundRepository;
import com.btg.funds.repository.TransactionRepository;
import com.btg.funds.service.FundService;
import com.btg.funds.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;


public class FundsServiceTests {

    @Mock
    private FundRepository fundRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FundService fundService;

    private Fund testFund;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testFund = new Fund();
        testFund.setFundId(1);
        testFund.setName("FPV_BTG_PACTUAL_RECAUDADORA");
        testFund.setMinAmount(75000);
        testFund.setCategory("FPV");
    }

    @Test
    void testSubscribeFund_successfulSubscription() {
        // Simulamos que el fondo existe
        when(fundRepository.findByFundIdAndName(testFund.getFundId(), testFund.getName())).thenReturn(Optional.of(testFund));

        String result = fundService.subscribeFund(testFund);

        assertEquals("Suscripción realizada exitosamente al fondo FPV_BTG_PACTUAL_RECAUDADORA", result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(notificationService, times(1)).sendNotification(anyString(), contains("Suscripción realizada exitosamente"));
    }

    @Test
    void testSubscribeFund_fundNotFound() {
        // Simulamos que el fondo no existe
        when(fundRepository.findByFundIdAndName(testFund.getFundId(), testFund.getName())).thenReturn(Optional.empty());

        FundNotFoundException exception = assertThrows(FundNotFoundException.class, () -> fundService.subscribeFund(testFund));

        assertEquals("El fondo con ID 1 y nombre FPV_BTG_PACTUAL_RECAUDADORA no existe.", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(notificationService, times(1)).sendNotification(anyString(), contains("El fondo con ID 1"));
    }

    @Test
    void testSubscribeFund_insufficientFunds() {
        testFund.setMinAmount(600000); // Simulamos un fondo con un monto mínimo superior al balance inicial

        // Simulamos que el fondo existe
        when(fundRepository.findByFundIdAndName(testFund.getFundId(), testFund.getName())).thenReturn(Optional.of(testFund));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> fundService.subscribeFund(testFund));

        assertTrue(exception.getMessage().contains("No tiene saldo disponible"));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(notificationService, times(1)).sendNotification(anyString(), contains("No tiene saldo disponible"));
    }

    @Test
    void testUnsubscribeFund_successfulUnsubscription() {
        // Simulamos que el fondo existe usando findByFundIdAndName
        when(fundRepository.findById(testFund.getFundId()))
                .thenReturn(Optional.of(testFund));

        String result = fundService.unsubscribeFund(testFund.getFundId());

        assertEquals("Se ha cancelado la suscripción al fondo FPV_BTG_PACTUAL_RECAUDADORA", result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(notificationService, times(1)).sendNotification(anyString(), contains("Cancelación exitosa"));
    }

    @Test
    void testUnsubscribeFund_fundNotFound() {
        // Simulamos que el fondo no existe
        when(fundRepository.findByFundIdAndName(testFund.getFundId(), testFund.getName())).thenReturn(Optional.empty());

        FundNotFoundException exception = assertThrows(FundNotFoundException.class, () -> fundService.unsubscribeFund(testFund.getFundId()));

        assertEquals("Fondo con ID 1 no encontrado", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetFundById_found() {
        // Simulamos que el fondo existe
        when(fundRepository.findById(testFund.getFundId())).thenReturn(Optional.of(testFund));

        Optional<Fund> foundFund = fundService.getFundById(testFund.getFundId());

        assertTrue(foundFund.isPresent());
        assertEquals("FPV_BTG_PACTUAL_RECAUDADORA", foundFund.get().getName());
        verify(fundRepository, times(1)).findById(testFund.getFundId());
    }

    @Test
    void testGetFundById_notFound() {
        // Simulamos que el fondo no existe
        when(fundRepository.findById(testFund.getFundId())).thenReturn(Optional.empty());

        Optional<Fund> foundFund = fundService.getFundById(testFund.getFundId());

        assertFalse(foundFund.isPresent());
        verify(fundRepository, times(1)).findById(testFund.getFundId());
    }
}