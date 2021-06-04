package com.onlineBroker.InvestmentManagementService.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class UserEntity {

    @Id
    private String userId;

    //key: stockSymbol, value: InvestmentEntity
    private TreeMap<String, InvestmentEntity> stockInvestments;

    //Alternativ
    //private TreeMap<String, LinkedList<InvestmentEntity>> stockInvestments;


    private double depotBalance;
    private double realizedProfitLossOfUserEntity;


    private ArrayList<String> watchlist;
/*
    //private TreeMap<symbol, ArrayList<price>> priceAlerts;
    private TreeMap<String, ArrayList<Double>> priceAlerts;
*/
}
