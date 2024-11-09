package com.btg.funds.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fundsdb")
public class Fund {

    @Id
    private Integer fundId;
    private String name;
    private double minAmount;
    private String category;
}
