package com.onlineBroker.InvestmentManagementService.service;

import com.onlineBroker.InvestmentManagementService.dto.DepotBalanceDTO;
import com.onlineBroker.InvestmentManagementService.dto.OrderDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.StockInvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.ShareEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.repository.InvestmentRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Getter
@Setter
@Service
public class InvestmentManagementService {

    @Autowired
    private InvestmentRepository repository;

    UserEntity userEntity;

    StockInvestmentEntity singleStockInvestmentEntity;
    OrderDTO orderDTO;

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

    public void createNewUserEntity(String userId) {
        if(!this.isUserExisting(userId)) {
            userEntity = new UserEntity(
                    userId,
                    new TreeMap<>(),
                    0.0,
                    0.0,
                    new ArrayList<>()
            );
            System.out.println("New user created!");
            repository.save(userEntity);
        }else{
            System.out.println("Can't create user, user already exists!");
        }
    }

    public void handleOrder(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;

        initializeUser(orderDTO.getUserId());
        initializeSpecificInvestment(orderDTO.getStockSymbol());

        if(orderDTO.getType().equals("OPEN")){
            System.out.println("Order is of type OPEN. Begin open flow...");
           beginFlowOnBuyOrder();
       } else{
            System.out.println("Order is of type CLOSE. Begin close flow...");
            beginFlowOnSellOrder();
       }
    }
    public void initializeUser(String userId) {
        userEntity = findUserEntity(userId);
    }
    public void initializeSpecificInvestment(String stockSymbol) {
        singleStockInvestmentEntity = findSingleStockInvestmentFromUserEntity(stockSymbol);
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

    public StockInvestmentEntity findSingleStockInvestmentFromUserEntity(String stockSymbol){
        System.out.println("find single user investment by stock symbol: " +stockSymbol);
        if(userEntity.getStockInvestments().containsKey(stockSymbol)){
            return userEntity.getStockInvestments().get(stockSymbol);
        }else{
            System.out.println("No investment found for symbol: "  +stockSymbol);
            if(orderDTO != null && orderDTO.getOrderId().equals("OPEN")){
                createUserInvestment();
            }
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

    void beginFlowOnBuyOrder(){
        decreaseDepotBalanceOfUserEntity();
        increaseSharesInPossession();
        calculateNewAveragePriceOfSingleInvestment();
        increaseSingleInvestmentValueOnOpen();
        saveSingleInvestment();
        saveUser();
    }

    void beginFlowOnSellOrder() {
        //do for each share unit
        for (int i = 0; i < orderDTO.getUnits(); i++) {
            calculateNewProfitLossOfShareEntity();
            increaseDepotBalanceOfUserEntity();
            calculateNewProfitLossOfInvestment();
            calculateNewProfitLossOfUserEntity();
            decreaseSingleInvestmentValueOnClose();
            markShareAsClosed();
            moveShareToClosedSharesList();
            decreaseSharesInPossession();
        }
        calculateNewAveragePriceOfSingleInvestment();
        saveSingleInvestment();
        saveUser();
    }

    void increaseSharesInPossession() {
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

    void calculateNewAveragePriceOfSingleInvestment(){
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

    void increaseSingleInvestmentValueOnOpen() {
        System.out.println("Increase value of investment for stock symbol: " +orderDTO.getStockSymbol());
        singleStockInvestmentEntity.setValueOfInvestment(singleStockInvestmentEntity.getValueOfInvestment() + (this.orderDTO.getUnits() * this.orderDTO.getPrice()));
    }

    private void decreaseSingleInvestmentValueOnClose() {
        System.out.println("Decrease value of investment for stock symbol: " +orderDTO.getStockSymbol());
        singleStockInvestmentEntity.setValueOfInvestment(singleStockInvestmentEntity.getValueOfInvestment() - singleStockInvestmentEntity.getSharesInPossession().get(0).getOpenPrice());
    }

    public void saveSingleInvestment() {
        System.out.println("Save new investment for stock symbol: " +orderDTO.getStockSymbol());
        userEntity.getStockInvestments().put(orderDTO.getStockSymbol(), singleStockInvestmentEntity);
    }

    public void saveUser() {
        repository.save(userEntity);
    }
        void decreaseDepotBalanceOfUserEntity(){
        userEntity.setDepotBalance(userEntity.getDepotBalance()-orderDTO.getPrice() * orderDTO.getUnits());
    }

    private void increaseDepotBalanceOfUserEntity() {
        System.out.println("Update depot balance of user: " +orderDTO.getUserId());
        userEntity.setDepotBalance(userEntity.getDepotBalance()
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getOpenPrice()
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

    private void markShareAsClosed() {
        System.out.println("Update ShareEntity with close order data...");
        singleStockInvestmentEntity.getSharesInPossession().get(0).setClosePrice(orderDTO.getPrice());
        singleStockInvestmentEntity.getSharesInPossession().get(0).setDateClosed(orderDTO.getDate());
        singleStockInvestmentEntity.getSharesInPossession().get(0).setStatus("CLOSED");
    }

    private void moveShareToClosedSharesList() {
        System.out.println("Mark share as closed/sold by moving it to List sharesSold...");
        singleStockInvestmentEntity.getSharesSold().add(singleStockInvestmentEntity.getSharesInPossession().get(0));
    }

    private void decreaseSharesInPossession(){
        singleStockInvestmentEntity.getSharesInPossession().remove(0);
    }

    private void calculateNewProfitLossOfShareEntity() {
        double profitLossOfShare = orderDTO.getPrice()
                - singleStockInvestmentEntity.getSharesInPossession().get(0).getOpenPrice();

        System.out.println("Calculate profit/loss of sold share...");
        singleStockInvestmentEntity.getSharesInPossession()
                .get(0)
                .setProfitLossOfShare(profitLossOfShare);
    }

    private void calculateNewProfitLossOfInvestment() {
        double oldRealizedProfitLossOfInvestment = singleStockInvestmentEntity.getRealizedProfitLossOfInvestment();

        System.out.println("Calculate new profit/loss of investment in Stock: " +orderDTO.getStockSymbol());
        singleStockInvestmentEntity.setRealizedProfitLossOfInvestment(oldRealizedProfitLossOfInvestment
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

    private void calculateNewProfitLossOfUserEntity() {
        System.out.println("Update realized profit/loss of user: " +orderDTO.getUserId());
        userEntity.setRealizedProfitLossOfUserEntity(userEntity.getRealizedProfitLossOfUserEntity()
                + singleStockInvestmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

    public void addWatchlistItem(String userId, String stockSymbol) {
        if(this.isUserExisting(userId)) {
            userEntity = this.findUserEntity(userId);
            userEntity.getWatchlist().add(stockSymbol);
            saveUser();
            System.out.println("Stock symbol " +stockSymbol +" added to watchlist for user " +userId);
        }
    }

    public void removeWatchlistItem(String userId, String stockSymbol) {
        userEntity = this.findUserEntity(userId);
        userEntity.getWatchlist().remove(stockSymbol);
        saveUser();
        System.out.println("stockSymbol " +stockSymbol +" removed from watchlist for user " +userId);

    }

    public ArrayList<String> findWatchlist(String userId) {
        System.out.println("Watchlist retrieved for user " +userId);
        userEntity = this.findUserEntity(userId);
        return userEntity.getWatchlist();
    }

    public void calculateNewDepotBalanceOfUserEntity(DepotBalanceDTO depotBalanceDTO){
        userEntity = this.findUserEntity(depotBalanceDTO.getUserId());
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
        saveUser();
    }

/*
    public static void addPriceAlert(String stockSymbol, Double price) {
    }

    public static void deletePriceAlert(String stockSymbol, Double price) {
    }
*/
}
