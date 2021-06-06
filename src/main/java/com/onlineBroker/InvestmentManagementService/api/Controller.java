package com.onlineBroker.InvestmentManagementService.api;

import com.onlineBroker.InvestmentManagementService.dto.DepotBalanceDTO;
import com.onlineBroker.InvestmentManagementService.dto.OrderDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.InvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.repository.InvestmentRepository;
import com.onlineBroker.InvestmentManagementService.service.InvestmentManagementService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return investmentManagementService.getUser(userId);
    }

    @GetMapping(value = "/investmentService/users/{userId}/investments/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public InvestmentEntity getInvestmentOfUser(@PathVariable String userId, @PathVariable String stockSymbol) {
        return investmentManagementService.getInvestmentOfUser(userId, stockSymbol);
    }

    @PostMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void addWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        investmentManagementService.addWatchlistItem(userId, stockSymbol);
    }

    @PostMapping(value = "/investmentService/users/{userId}/depotBalance")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void updateDepotBalance(@RequestBody DepotBalanceDTO depotBalanceDTO) {
        investmentManagementService.updateDepotBalanceOfUserEntity(depotBalanceDTO);
    }

    @DeleteMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void removeWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        investmentManagementService.removeWatchlistItem(userId, stockSymbol);
    }

    @GetMapping(value = "/investmentService/{userId}/watchlist")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public ArrayList<String> getWatchlist(@PathVariable String userId) {
        return investmentManagementService.getWatchlist(userId);
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