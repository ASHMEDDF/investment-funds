package com.btg.funds.service;

import com.btg.funds.exceptions.FundAlreadyExistsException;
import com.btg.funds.exceptions.FundNotFoundException;
import com.btg.funds.exceptions.InsufficientFundsException;
import com.btg.funds.model.Fund;
import com.btg.funds.model.Transaction;
import com.btg.funds.repository.FundRepository;
import com.btg.funds.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class FundService {

    private static final double INITIAL_BALANCE = 500_000;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private NotificationService notificationService;

    private double userBalance = INITIAL_BALANCE;

    private final String emailToCatch = "ashmeddiazg@gmail.com";

    public Optional<Fund> getFundById(Integer id) {
        return fundRepository.findById(id);
    }

    public String createFund(Fund fund) {

        Optional<Fund> existingFund = fundRepository.findByFundIdAndName(fund.getFundId(), fund.getName());

        if (existingFund.isPresent()) {
            throw new FundAlreadyExistsException("El fondo con el mismo ID y nombre ya existe.");
        }
        fundRepository.save(fund);

        return "Nuevo fondo creado exitosamente con nombre: " + fund.getName();
    }

    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    public String subscribeFund(Fund fund) {
        // Verificar si el fondo existe
        Optional<Fund> existingFund = fundRepository.findByFundIdAndName(fund.getFundId(), fund.getName());
        if (existingFund.isEmpty()) {
            String message = "El fondo con ID " + fund.getFundId() + " y nombre " + fund.getName() + " no existe.";
            notificationService.sendNotification(emailToCatch, message);
            throw new FundNotFoundException(message);  // Lanza una excepción personalizada si no existe
        }

        // Validación de saldo
        if (userBalance < fund.getMinAmount()) {
            String message = "No tiene saldo disponible para vincularse al fondo " + fund.getName();
            notificationService.sendNotification(emailToCatch, message);
            throw new InsufficientFundsException(message);
        }

        // Reducir el saldo y registrar la transacción
        userBalance -= fund.getMinAmount();
        Transaction transaction = createTransaction(fund, "APERTURA");
        transactionRepository.save(transaction);

        notificationService.sendNotification(emailToCatch, "Suscripción realizada exitosamente al fondo " + fund.getName());

        return "Suscripción realizada exitosamente al fondo " + fund.getName();
    }

    public String unsubscribeFund(Integer fundId) {
        Fund fund = fundRepository.findById(fundId)
                .orElseThrow(() -> new FundNotFoundException("Fondo con ID " + fundId + " no encontrado"));

        userBalance += fund.getMinAmount();
        Transaction transaction = createTransaction(fund, "CANCELACION");
        transactionRepository.save(transaction);
        notificationService.sendNotification(emailToCatch, "Cancelación exitosa del fondo: " + fund.getName());
        return "Se ha cancelado la suscripción al fondo " + fund.getName();
    }

    public List<Transaction> getTransactionHistory() {
        return transactionRepository.findAll();
    }

    private Transaction createTransaction(Fund fund, String type) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(String.valueOf(UUID.randomUUID()));
        transaction.setFundId(fund.getFundId());
        transaction.setFundName(fund.getName());
        transaction.setAmount(fund.getMinAmount());
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        return transaction;
    }
}
