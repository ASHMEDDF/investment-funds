package com.btg.funds.repository;

import com.btg.funds.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FundRepository extends MongoRepository<Fund, Integer> {

    Optional<Fund> findByFundIdAndName(Integer fundId, String name);
}
