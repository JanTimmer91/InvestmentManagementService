package com.onlineBroker.InvestmentManagementService.api;

import com.onlineBroker.InvestmentManagementService.dto.DepotBalanceDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.StockInvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.service.UserService;
import com.onlineBroker.InvestmentManagementService.business.InvestmentManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class Controller {

    private final InvestmentManagementService investmentManagementService;
    private final UserService userService;

    public Controller(InvestmentManagementService investmentManagementService, UserService userService){
        this.investmentManagementService = investmentManagementService;
        this.userService = userService;
    }

    @PostMapping(value = "/investmentService/users/{userId}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void createEmptyInvestmentForNewUser(@PathVariable String userId) {
        if(!userService.isUserExisting(userId)) {
            userService.saveUserEntity(userService.createNewUserEntity(userId));
        }else{
            System.out.println("Can't create user!");
        }
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
        if(!userService.isUserExisting(userId)) {
            investmentManagementService.initializeUser(userId);
            investmentManagementService.initializeSpecificInvestment(stockSymbol);
            return investmentManagementService.getSingleStockInvestmentEntity();
        }
        else{
                return null;
            }
    }

    @PostMapping(value = "/investmentService/users/{userId}/depotBalance")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void updateDepotBalance(@RequestBody DepotBalanceDTO depotBalanceDTO) {
        if(!userService.isUserExisting(depotBalanceDTO.getUserId())) {
            investmentManagementService.initializeUser(depotBalanceDTO.getUserId());
            investmentManagementService.calculateNewDepotBalanceOfUserEntity(depotBalanceDTO);
        }
    }

    @PostMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void addWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        userService.createWatchlistItem(userId, stockSymbol);
    }

    @DeleteMapping(value = "/investmentService/users/{userId}/watchlist/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void removeWatchlistItem(@PathVariable String userId, @PathVariable String stockSymbol) {
        userService.removeWatchlistItem(userId, stockSymbol);
    }

    @GetMapping(value = "/investmentService/users/{userId}/watchlist")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public ArrayList<String> getWatchlist(@PathVariable String userId) {
            return userService.findWatchlist(userId);
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