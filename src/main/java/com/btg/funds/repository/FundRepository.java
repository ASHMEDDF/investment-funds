package com.btg.funds.repository;

import com.btg.funds.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundRepository extends MongoRepository<Fund, Integer> {
}
