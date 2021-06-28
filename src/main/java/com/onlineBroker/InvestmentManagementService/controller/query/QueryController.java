package com.onlineBroker.InvestmentManagementService.controller.query;

import com.onlineBroker.InvestmentManagementService.dto.responseDTO.WatchlistResponseDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.StockInvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.service.UserServiceImpl;
import com.onlineBroker.InvestmentManagementService.domain.InvestmentTransformationServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
public class QueryController {

    private final InvestmentTransformationServiceImpl investmentTransformationServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public QueryController(InvestmentTransformationServiceImpl investmentTransformationServiceImpl, UserServiceImpl userServiceImpl){
        this.investmentTransformationServiceImpl = investmentTransformationServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping(value = "/investmentService/users/{userId}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public UserEntity getUser(@PathVariable String userId) {
        investmentTransformationServiceImpl.initializeUser(userId);
        return investmentTransformationServiceImpl.getUserEntity();
    }

    @GetMapping(value = "/investmentService/users/{userId}/investments/{stockSymbol}")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public StockInvestmentEntity getInvestmentOfUser(@PathVariable String userId, @PathVariable String stockSymbol) {
        if(!userServiceImpl.isUserExisting(userId)) {
            investmentTransformationServiceImpl.initializeUser(userId);
            investmentTransformationServiceImpl.initializeSpecificInvestment(stockSymbol);
            return investmentTransformationServiceImpl.getSingleStockInvestmentEntity();
        }
        else{
                return null;
            }
    }

    @GetMapping(value = "/investmentService/users/{userId}/watchlist")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public WatchlistResponseDTO getWatchlist(@PathVariable String userId) {
            return userServiceImpl.findWatchlist(userId);
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
/*
    @PostMapping(value = "/investmentService/users/{userId}/depotBalance")
    @CrossOrigin(origins = {"http://localhost:3000"}) //for local development
    public void updateDepotBalance(@RequestBody DepotBalanceDTO depotBalanceDTO) {
        if(!userService.isUserExisting(depotBalanceDTO.getUserId())) {
            investmentTransformationService.initializeUser(depotBalanceDTO.getUserId());
            userService.setNewDepotBalanceOfUserEntity(depotBalanceDTO);
        }
    }
*/
}