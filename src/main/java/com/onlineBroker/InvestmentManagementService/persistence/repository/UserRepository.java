package com.onlineBroker.InvestmentManagementService.persistence.repository;

import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<UserEntity, Integer> {
    UserEntity findByUserId(String userId);
}