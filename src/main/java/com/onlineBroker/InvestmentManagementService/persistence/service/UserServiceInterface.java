package com.onlineBroker.InvestmentManagementService.persistence.service;

import com.onlineBroker.InvestmentManagementService.dto.WatchlistDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.TreeMap;

public interface UserServiceInterface {

    boolean isUserExisting(String userId);

    UserEntity createNewUserEntity(String userId);

    UserEntity findUserEntity(String userId);

    UserEntity saveUserEntity(UserEntity userEntity);

    void addWatchlistItem(String userId, String stockSymbol);

    void removeWatchlistItem(String userId, String stockSymbol);

    WatchlistDTO findWatchlist(String userId);
}
