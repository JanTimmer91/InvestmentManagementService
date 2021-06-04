package com.onlineBroker.InvestmentManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepotBalanceDTO {
    private String userId;
    private Double amount;
    private boolean shouldIncrease;
}
