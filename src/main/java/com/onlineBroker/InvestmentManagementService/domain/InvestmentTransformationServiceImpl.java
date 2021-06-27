package com.onlineBroker.InvestmentManagementService.domain;

import com.onlineBroker.InvestmentManagementService.dto.OrderDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.StockInvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.ShareEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.service.UserServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

@Getter
@Setter
@Service
public class InvestmentTransformationServiceImpl implements InvestmentTransformationServiceInterface {

    @Autowired
    private UserServiceImpl userServiceImpl;

    UserEntity userEntity;
    StockInvestmentEntity singleStockInvestmentEntity;
    OrderDTO orderDTO;

    public void handleIncomingOrder(OrderDTO orderDTO) {

        //initialize required data in service
        this.setOrderDTO(orderDTO);
        this.initializeUser(orderDTO.getUserId());
        this.initializeSpecificInvestment(orderDTO.getStockSymbol());


        if(orderDTO.getType().equals("OPEN")){
            System.out.println("Order is of type OPEN. Begin open flow...");
            this.beginTransformationFlowOnBuyOrder();
        } else{
            System.out.println("Order is of type CLOSE. Begin close flow...");
            this.beginTransformationFlowOnSellOrder();
        }
    }

    public void initializeUser(String userId) {
        userEntity = userServiceImpl.findUserEntity(userId);
    }

    public void initializeSpecificInvestment(String stockSymbol) {
        singleStockInvestmentEntity = findSingleStockInvestmentFromUserEntity(stockSymbol);
        if(singleStockInvestmentEntity == null && orderDTO != null && orderDTO.getOrderId().equals("OPEN")){
            createUserInvestment();
        }
    }

    public StockInvestmentEntity findSingleStockInvestmentFromUserEntity(String stockSymbol){
        System.out.println("find single user investment by stock symbol: " +stockSymbol);
        if(userEntity.getStockInvestments().containsKey(stockSymbol)){
            return userEntity.getStockInvestments().get(stockSymbol);
        }else{
            System.out.println("No investment found for symbol: "  +stockSymbol);
            return null;
        }
    }
    public void createUserInvestment() {
        singleStockInvestmentEntity = new StockInvestmentEntity(
                UUID.randomUUID().toString(),
                new LinkedList<>(),
                new ArrayList<>(),
                0.0,
                0.0,
                0.0
        );
    }

    public void beginTransformationFlowOnBuyOrder(){
        //decreaseDepotBalanceOfUserEntity();
        increaseSharesInPossession();
        calculateNewAveragePriceOfSingleInvestment();
        increaseSingleInvestmentValueOnOpen();
        saveSingleInvestment();
        userServiceImpl.saveUserEntity(userEntity);
    }

    public void beginTransformationFlowOnSellOrder() {
        //do for each share unit
        for (int i = 0; i < orderDTO.getUnits(); i++) {
            calculateNewProfitLossOfShareEntity();
            //increaseDepotBalanceOfUserEntity();
            calculateNewProfitLossOfInvestment();
            calculateNewProfitLossOfAllInvestments();
            decreaseSingleInvestmentValueOnClose();
            markShareAsClosed();
            moveShareToClosedSharesList();
            decreaseSharesInPossession();
        }
        calculateNewAveragePriceOfSingleInvestment();
        saveSingleInvestment();
        userServiceImpl.saveUserEntity(userEntity);
    }

    public void increaseSharesInPossession() {
        System.out.println("Increase shares in possession...");
        System.out.println("Amount of shares to add: " + orderDTO.getUnits());
        for(int i = 0; i < orderDTO.getUnits(); i++) {
            ShareEntity shareEntity = new ShareEntity(
                    orderDTO.getOrderId(),
                    null,
                    orderDTO.getDate(),
                    null,
                    orderDTO.getPrice(),
                    null,
                    "OPEN",
                    null
            );
            singleStockInvestmentEntity.getSharesInPossession().add(shareEntity);
        }
    }

    public void calculateNewAveragePriceOfSingleInvestment(){
        System.out.println("Update average price of investment for stock symbol: " +orderDTO.getStockSymbol());

        double AmountOfSharesInTotal = singleStockInvestmentEntity.getSharesInPossession().size();
        if(AmountOfSharesInTotal == 0) {
            singleStockInvestmentEntity.setAveragePriceOfInvestment(0.0);
        }else {
            double sum = 0;
            for(int i = 0; i < singleStockInvestmentEntity.getSharesInPossession().size(); i++){
                sum += singleStockInvestmentEntity.getSharesInPossession().get(i).getOpenPrice();
            }
            singleStockInvestmentEntity.setAveragePriceOfInvestment(sum/AmountOfSharesInTotal);
        }
    }

    public void increaseSingleInvestmentValueOnOpen() {
        System.out.println("Increase value of investment for stock symbol: " +orderDTO.getStockSymbol());
        singleStockInvestmentEntity.setValueOfInvestment(singleStockInvestmentEntity.getValueOfInvestment() + (this.orderDTO.getUnits() * this.orderDTO.getPrice()));
    }

    public void decreaseSingleInvestmentValueOnClose() {
        System.out.println("Decrease value of investment for stock symbol: " +orderDTO.getStockSymbol());
        singleStockInvestmentEntity.setValueOfInvestment(singleStockInvestmentEntity.getValueOfInvestment() - singleStockInvestmentEntity.getSharesInPossession().get(0).getOpenPrice());
    }

    public void saveSingleInvestment() {
        System.out.println("Save new investment for stock symbol: " +orderDTO.getStockSymbol());
        userEntity.getStockInvestments().put(orderDTO.getStockSymbol(), singleStockInvestmentEntity);
    }


    public void markShareAsClosed() {
        System.out.println("Update ShareEntity with close order data...");
        singleStockInvestmentEntity.getSharesInPossession().get(0).setClosePrice(orderDTO.getPrice());
        singleStockInvestmentEntity.getSharesInPossession().get(0).setDateClosed(orderDTO.getDate());
        singleStockInvestmentEntity.getSharesInPossession().get(0).setStatus("CLOSED");
    }

    public void moveShareToClosedSharesList() {
        System.out.println("Mark share as closed/sold by moving it to List sharesSold...");
        singleStockInvestmentEntity.getSharesSold().add(singleStockInvestmentEntity.getSharesInPossession().get(0));
    }

    public void decreaseSharesInPossession(){
        singleStockInvestmentEntity.getSharesInPossession().remove(0);
    }

    public void calculateNewProfitLossOfShareEntity() {
        double profitLossOfShare = orderDTO.getPrice()
                - singleStockInvestmentEntity.getSharesInPossession().get(0).getOpenPrice();

        System.out.println("Calculate profit/loss of sold share...");
        singleStockInvestmentEntity.getSharesInPossession()
                .get(0)
                .setProfitLossOfShare(profitLossOfShare);
    }

    public void calculateNewProfitLossOfInvestment() {
        double oldRealizedProfitLossOfInvestment = singleStockInvestmentEntity.getRealizedProfitLossOfInvestment();

        System.out.println("Calculate new profit/loss of investment in Stock: " +orderDTO.getStockSymbol());
        singleStockInvestmentEntity.setRealizedProfitLossOfInvestment(oldRealizedProfitLossOfInvestment
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

    public void calculateNewProfitLossOfAllInvestments() {
        System.out.println("Update realized profit/loss of user: " +orderDTO.getUserId());
        userEntity.setRealizedProfitLossOfUserEntity(userEntity.getRealizedProfitLossOfUserEntity()
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

/*
    public void calculateNewDepotBalanceOfUserEntity(DepotBalanceDTO depotBalanceDTO){
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
        userService.saveUserEntity(userEntity);
    }

    public void decreaseDepotBalanceOfUserEntity(){
        userEntity.setDepotBalance(userEntity.getDepotBalance()-orderDTO.getPrice() * orderDTO.getUnits());
    }

    public void increaseDepotBalanceOfUserEntity() {
        System.out.println("Update depot balance of user: " +orderDTO.getUserId());
        userEntity.setDepotBalance(userEntity.getDepotBalance()
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getOpenPrice()
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }
*/
}