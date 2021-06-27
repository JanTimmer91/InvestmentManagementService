package com.onlineBroker.InvestmentManagementService.controller.command;

import com.onlineBroker.InvestmentManagementService.dto.WatchlistDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.StockInvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.service.UserServiceImpl;
import com.onlineBroker.InvestmentManagementService.domain.InvestmentTransformationServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommandController {

    private final InvestmentTransformationServiceImpl investmentTransformationServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public CommandController(InvestmentTransformationServiceImpl investmentTransformationServiceImpl, UserServiceImpl userServiceImpl){
        this.investmentTransformationServiceImpl = investmentTransformationServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping(value = "/investmentService/users/{userId}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void createEmptyInvestmentForNewUser(@PathVariable String userId) {
        if(!userServiceImpl.isUserExisting(userId)) {
            userServiceImpl.createNewUserEntity(userId);
        }else{
            System.out.println("Can't create user!");
        }
    }

    @PostMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void addWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        userServiceImpl.addWatchlistItem(userId, stockSymbol);
    }

    @DeleteMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void removeWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        userServiceImpl.removeWatchlistItem(userId, stockSymbol);
    }
}