package com.btg.funds.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String transactionId;
    private Integer fundId;
    private String fundName;
    private double amount;
    private String type; // "APERTURA" o "CANCELACION"
    private LocalDateTime timestamp;

}
