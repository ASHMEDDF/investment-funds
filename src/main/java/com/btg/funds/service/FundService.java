package com.btg.funds.service;

import com.btg.funds.model.Fund;
import com.btg.funds.model.Transaction;
import com.btg.funds.repository.FundRepository;
import com.btg.funds.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FundService {

    private static final double INITIAL_BALANCE = 500_000;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private NotificationService notificationService;

    private double userBalance = INITIAL_BALANCE;

    public Optional<Fund> getFundById(Integer id) {
        return fundRepository.findById(id);
    }

    public void cancelFund(Integer id) {
        fundRepository.deleteById(id);
    }

    public List<Fund> getAllFunds() {
        return fundRepository.findAll();
    }

    public String subscribeFund(Fund fund) {
        if (userBalance < fund.getMinAmount()) {
            return "No tiene saldo disponible para vincularse al fondo " + fund.getName();
        }

        userBalance -= fund.getMinAmount();
        Transaction transaction = createTransaction(fund, "APERTURA");
        transactionRepository.save(transaction);
        notificationService.sendNotification("Suscripción exitosa al fondo: " + fund.getName());
        return "Suscripción realizada exitosamente al fondo " + fund.getName();
    }

    public String unsubscribeFund(Integer fundId) {
        Optional<Fund> fundOptional = fundRepository.findById(fundId);
        if (fundOptional.isEmpty()) {
            return "Fondo no encontrado";
        }

        Fund fund = fundOptional.get();
        userBalance += fund.getMinAmount();
        Transaction transaction = createTransaction(fund, "CANCELACION");
        transactionRepository.save(transaction);
        notificationService.sendNotification("Cancelación exitosa del fondo: " + fund.getName());
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
