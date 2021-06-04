package com.onlineBroker.InvestmentManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private String orderId;
    private String userId;
    private String type;
    private String stockSymbol;
    private double price;
    private int units;
    private Date date;
}
