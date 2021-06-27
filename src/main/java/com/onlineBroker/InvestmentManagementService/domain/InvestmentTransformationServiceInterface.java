package com.onlineBroker.InvestmentManagementService.domain;

import com.onlineBroker.InvestmentManagementService.persistence.entity.StockInvestmentEntity;
import com.onlineBroker.InvestmentManagementService.persistence.entity.UserEntity;

public interface InvestmentTransformationServiceInterface {

    void initializeUser(String userId);

    void initializeSpecificInvestment(String stockSymbol);

    StockInvestmentEntity findSingleStockInvestmentFromUserEntity(String stockSymbol);

    void createUserInvestment();

    void beginTransformationFlowOnBuyOrder();

    void beginTransformationFlowOnSellOrder();

    void increaseSharesInPossession();

    void calculateNewAveragePriceOfSingleInvestment();

    void increaseSingleInvestmentValueOnOpen();

    void decreaseSingleInvestmentValueOnClose();

    void saveSingleInvestment();

    void markShareAsClosed();

    void moveShareToClosedSharesList();

    void decreaseSharesInPossession();

    void calculateNewProfitLossOfShareEntity();

    void calculateNewProfitLossOfInvestment();

    void calculateNewProfitLossOfAllInvestments();

    UserEntity getUserEntity();

    StockInvestmentEntity getSingleStockInvestmentEntity();

}
