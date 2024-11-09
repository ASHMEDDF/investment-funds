package com.btg.funds.controller;

import com.btg.funds.model.Fund;
import com.btg.funds.model.Transaction;
import com.btg.funds.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fondos")
public class FundController {

    @Autowired
    private FundService fundService;

    @PostMapping("/suscribir")
    public ResponseEntity<String> subscribeFund(@RequestBody Fund fund) {
        String newFund = fundService.subscribeFund(fund);
        return ResponseEntity.ok(newFund);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<String> createNewFund(@RequestBody Fund fund) {
        String newFund = fundService.createFund(fund);
        return ResponseEntity.ok(newFund);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fund> getFundById(@PathVariable Integer id) {
        Optional<Fund> Fund = fundService.getFundById(id);
        return Fund.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Fund>> getHistory() {
        List<Fund> history = fundService.getAllFunds();
        return ResponseEntity.ok(history);
    }


    @PostMapping("/cancelar/{fundId}")
    public ResponseEntity<String> unsubscribeFund(@PathVariable Integer fundId) {
        String response = fundService.unsubscribeFund(fundId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Transaction>> getTransactionHistory() {
        List<Transaction> history = fundService.getTransactionHistory();
        return ResponseEntity.ok(history);
    }
}
