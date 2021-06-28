package com.onlineBroker.InvestmentManagementService.persistence.service;

import com.onlineBroker.InvestmentManagementService.dto.responseDTO.WatchlistResponseDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.TreeMap;

@Service
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    private UserRepository repository;

    public boolean isUserExisting(String userId){
        System.out.println("Check if user exists...");

        if(repository.findByUserId(userId) == null){
            System.out.println("User with userId " +userId +" doesn't exist!");
            return false;
        }else{
            System.out.println("User with userId " +userId +" exists!");
            return true;
        }
    }

    public UserEntity createNewUserEntity(String userId) {
        UserEntity userEntity = new UserEntity(
                userId,
                new TreeMap<>(),
                0.0,
                new ArrayList<>()
        );
        System.out.println("New user created!");
        return saveUserEntity(userEntity);
    }

    public UserEntity findUserEntity(String userId){
        System.out.println("Find user entity in repository...");
        try {
            return repository.findByUserId(userId);
        }catch(NullPointerException e){
            System.out.println("User with userId " + userId + " hasn't been found");
            return null;
        }
    }

    public UserEntity saveUserEntity(UserEntity userEntity) {
        if(userEntity != null) {
            return repository.save(userEntity);
        }
        return null;
    }

    /*
    public void setNewDepotBalanceOfUserEntity(DepotBalanceDTO depotBalanceDTO){
        if((depotBalanceDTO.isShouldIncrease())) {
            userEntity.setDepotBalance(userEntity.getDepotBalance() + (depotBalanceDTO.getAmount()));
            System.out.println("Depot balance increased for userId " +depotBalanceDTO.getUserId());
        }else{
            if(userEntity.getDepotBalance() - depotBalanceDTO.getAmount() < 0){
                System.out.println("Depot balance can't be lower than 0");
            }else {
                userEntity.setDepotBalance(userEntity.getDepotBalance() - depotBalanceDTO.getAmount());
                System.out.println("Depot balance decreased for userId " +depotBalanceDTO.getUserId());
            }
        }
        saveUserEntity(userEntity);
    }
*/
    public void addWatchlistItem(String userId, String stockSymbol) {
        UserEntity userEntity = repository.findByUserId(userId);
        userEntity.getWatchlist().add(stockSymbol);
        this.saveUserEntity(userEntity);
        System.out.println("Stock symbol " +stockSymbol +" added to watchlist for user " +userEntity.getUserId());
    }

    public void removeWatchlistItem(String userId, String stockSymbol) {
        UserEntity userEntity = repository.findByUserId(userId);
        userEntity.getWatchlist().remove(stockSymbol);
        this.saveUserEntity(userEntity);
        System.out.println("stockSymbol " +stockSymbol +" removed from watchlist for user " +userEntity.getUserId());

    }

    public WatchlistResponseDTO findWatchlist(String userId) {
        System.out.println("Watchlist retrieved for user " +userId);
        WatchlistResponseDTO watchlistDTO = new WatchlistResponseDTO(repository.findByUserId(userId).getWatchlist());
        return watchlistDTO;
    }
}
