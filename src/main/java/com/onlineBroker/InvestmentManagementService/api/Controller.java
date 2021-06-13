package com.onlineBroker.InvestmentManagementService.api;

import com.onlineBroker.InvestmentManagementService.dto.DepotBalanceDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.StockInvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.service.InvestmentManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class Controller {

    private final InvestmentManagementService investmentManagementService;

    public Controller(InvestmentManagementService investmentManagementService){
        this.investmentManagementService = investmentManagementService;
    }

    @PostMapping(value = "/investmentService/users/{userId}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void createEmptyInvestmentForNewUser(@PathVariable String userId) {
        investmentManagementService.createNewUserEntity(userId);
    }

    @GetMapping(value = "/investmentService/users/{userId}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public UserEntity getUser(@PathVariable String userId) {
        investmentManagementService.initializeUser(userId);
        return investmentManagementService.getUserEntity();
    }

    @GetMapping(value = "/investmentService/users/{userId}/investments/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public StockInvestmentEntity getInvestmentOfUser(@PathVariable String userId, @PathVariable String stockSymbol) {
        investmentManagementService.initializeUser(userId);
        investmentManagementService.initializeSpecificInvestment(stockSymbol);
        return investmentManagementService.getSingleStockInvestmentEntity();
    }

    @PostMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void addWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        investmentManagementService.addWatchlistItem(userId, stockSymbol);
    }

    @PostMapping(value = "/investmentService/users/{userId}/depotBalance")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void updateDepotBalance(@RequestBody DepotBalanceDTO depotBalanceDTO) {
        investmentManagementService.calculateNewDepotBalanceOfUserEntity(depotBalanceDTO);
    }

    @DeleteMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void removeWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        investmentManagementService.removeWatchlistItem(userId, stockSymbol);
    }

    @GetMapping(value = "/investmentService/users/{userId}/watchlist")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public ArrayList<String> getWatchlist(@PathVariable String userId) {
        return investmentManagementService.findWatchlist(userId);
    }

    /*
    @PostMapping(value = "/investmentService/priceAlert")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void addPriceAlert(@RequestBody String stockSymbol, Double price) {
        //InvestmentManagementService.addPriceAlert(stockSymbol, price);
    }

    @DeleteMapping(value = "/investmentService/priceAlert")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void deletePriceAlert(@RequestBody String stockSymbol, Double price) {
        //InvestmentManagementService.deletePriceAlert(stockSymbol, price);
    }
     */
}