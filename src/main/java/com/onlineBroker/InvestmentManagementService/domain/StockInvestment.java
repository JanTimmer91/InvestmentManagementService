package com.onlineBroker.InvestmentManagementService.domain;

import lombok.Data;

@Data
public class StockInvestment {
    private String symbol;
    private double priceBoughtIn;
    private int amount;
}
