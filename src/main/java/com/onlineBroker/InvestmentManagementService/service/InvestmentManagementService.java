package com.onlineBroker.InvestmentManagementService.service;

import com.onlineBroker.InvestmentManagementService.dto.DepotBalanceDTO;
import com.onlineBroker.InvestmentManagementService.dto.OrderDTO;
import com.onlineBroker.InvestmentManagementService.persistence.entity.InvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.ShareEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;
import com.onlineBroker.InvestmentManagementService.persistence.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvestmentManagementService {

    @Autowired
    private InvestmentRepository repository;

    static UserEntity userEntity;
    InvestmentEntity investmentEntity;
    OrderDTO orderDTO;

    public boolean checkIfUserExists(String userId){
        if(repository.findByUserId(userId) == null){
            System.out.println("User with userId " +userId +" doesn't exist");
            return false;
        }else{
            System.out.println("User with userId " +userId +" exists");
            return true;
        }
    }

    public void createNewUserEntity(String userId) {
        if(!this.checkIfUserExists(userId)) {
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
        System.out.println(orderDTO.getDate());
        getUserEntityByUserIdFromOrder();
        getUserInvestmentFromUserEntityByStockSymbol();

        if(orderDTO.getType().equals("OPEN")){
            System.out.println("Order is of type OPEN. Begin open flow...");
           beginOpenFlow();
       } else{
            System.out.println("Order is of type CLOSE. Begin close flow...");
            beginCloseFlow();
       }
    }

    public void getUserEntityByUserIdFromOrder(){
        System.out.println("Get user entity...");
        try {
            userEntity = repository.findByUserId(orderDTO.getUserId());
        }catch(NullPointerException e){
            System.out.println("User with userId " + orderDTO.getUserId() + " hasn't been found");
        }
    }

    public void getUserInvestmentFromUserEntityByStockSymbol(){
        System.out.println("Get user investment by stock symbol: " +orderDTO.getStockSymbol());
        if(userEntity.getStockInvestments().containsKey(orderDTO.getStockSymbol())){
            investmentEntity = userEntity.getStockInvestments().get(orderDTO.getStockSymbol());
        }else{
            System.out.println("No investment found. Creating new investment for stock symbol: "  +orderDTO.getStockSymbol());
            createUserInvestment();
        }
    }

    public void createUserInvestment() {
        investmentEntity = new InvestmentEntity(
                UUID.randomUUID().toString(),
                new LinkedList<>(),
                new ArrayList<>(),
                0.0,
                0.0,
                0.0
        );
    }

    void beginOpenFlow(){
        decreaseDepotBalanceOfUserEntity();
        increaseSharesInPossession();
        updateAveragePriceOfInvestment();
        increaseInvestmentValueOnOpen();
        saveInvestment();
        saveUser();
    }

    void beginCloseFlow() {
        for (int i = 0; i < orderDTO.getUnits(); i++) {
            updateProfitLossOfShareEntity();
            increaseDepotBalanceOfUserEntity();
            updateProfitLossOfInvestment();
            updateProfitLossOfUserEntity();
            decreaseInvestmentValueOnClose();
            markShareAsClosed();
        }
        updateAveragePriceOfInvestment();
        saveInvestment();
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
            investmentEntity.getSharesInPossession().add(shareEntity);
        }
    }

    void updateAveragePriceOfInvestment(){
        System.out.println("Update average price of investment for stock symbol: " +orderDTO.getStockSymbol());

        double AmountOfSharesInTotal = investmentEntity.getSharesInPossession().size();
        if(AmountOfSharesInTotal == 0) {
            investmentEntity.setAveragePriceOfInvestment(0.0);
        }else {
            double sum = 0;
            for(int i = 0; i < investmentEntity.getSharesInPossession().size(); i++){
                sum += investmentEntity.getSharesInPossession().get(i).getOpenPrice();
            }
            investmentEntity.setAveragePriceOfInvestment(sum/AmountOfSharesInTotal);
        }
    }

    void increaseInvestmentValueOnOpen() {
        System.out.println("Increase value of investment for stock symbol: " +orderDTO.getStockSymbol());
        investmentEntity.setValueOfInvestment(investmentEntity.getValueOfInvestment() + (this.orderDTO.getUnits() * this.orderDTO.getPrice()));
    }

    public void saveInvestment() {
        System.out.println("Save new investment for stock symbol: " +orderDTO.getStockSymbol());
        userEntity.getStockInvestments().put(orderDTO.getStockSymbol(), investmentEntity);
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
                + investmentEntity.getSharesInPossession().get(0).getOpenPrice()
                + investmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

    private void markShareAsClosed() {
        System.out.println("Update ShareEntity with close order data...");
        investmentEntity.getSharesInPossession().get(0).setClosePrice(orderDTO.getPrice());
        investmentEntity.getSharesInPossession().get(0).setDateClosed(orderDTO.getDate());
        investmentEntity.getSharesInPossession().get(0).setStatus("CLOSED");

        System.out.println("Mark share as sold by moving it to List sharesSold...");
        investmentEntity.getSharesSold().add(investmentEntity.getSharesInPossession().get(0));
        investmentEntity.getSharesInPossession().remove(0);
    }

    private void updateProfitLossOfShareEntity() {
        double profitLossOfShare = orderDTO.getPrice()
                - investmentEntity.getSharesInPossession().get(0).getOpenPrice();

        System.out.println("Calculate profit/loss of sold share...");
        investmentEntity.getSharesInPossession()
                .get(0)
                .setProfitLossOfShare(profitLossOfShare);
    }

    private void updateProfitLossOfInvestment() {
        double oldRealizedProfitLossOfInvestment = investmentEntity.getRealizedProfitLossOfInvestment();

        System.out.println("Calculate new profit/loss of investment in Stock: " +orderDTO.getStockSymbol());
        investmentEntity.setRealizedProfitLossOfInvestment(oldRealizedProfitLossOfInvestment
                + investmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

    private void updateProfitLossOfUserEntity() {
        System.out.println("Update realized profit/loss of user: " +orderDTO.getUserId());
        userEntity.setRealizedProfitLossOfUserEntity(userEntity.getRealizedProfitLossOfUserEntity()
                + investmentEntity.getSharesInPossession().get(0).getProfitLossOfShare());
    }

    private void decreaseInvestmentValueOnClose() {
        System.out.println("Decrease value of investment for stock symbol: " +orderDTO.getStockSymbol());
        investmentEntity.setValueOfInvestment(investmentEntity.getValueOfInvestment() - investmentEntity.getSharesInPossession().get(0).getOpenPrice());
    }

    public UserEntity getUser(String userId) {
            return repository.findByUserId(userId);
    }

    public InvestmentEntity getInvestmentOfUser(String userId, String stockSymbol) {
        return repository.findByUserId(userId).getStockInvestments().get(stockSymbol);
    }

    public void addWatchlistItem(String userId, String stockSymbol) {
        if(this.checkIfUserExists(userId)) {
            userEntity = this.getUser(userId);
            userEntity.getWatchlist().add(stockSymbol);
            System.out.println(userEntity.getUserId());
            saveUser();
            System.out.println("Stock symbol " +stockSymbol +" added to watchlist for user " +userId);
        }
    }

    public void removeWatchlistItem(String userId, String stockSymbol) {
        userEntity = this.getUser(userId);
        userEntity.getWatchlist().remove(stockSymbol);
        saveUser();
        System.out.println("stockSymbol " +stockSymbol +" removed from watchlist for user " +userId);

    }

    public ArrayList<String> getWatchlist(String userId) {
        System.out.println("Watchlist retrieved for user " +userId);
        userEntity = this.getUser(userId);
        return userEntity.getWatchlist();
    }

    public void updateDepotBalanceOfUserEntity(DepotBalanceDTO depotBalanceDTO){
        userEntity = this.getUser(depotBalanceDTO.getUserId());
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
