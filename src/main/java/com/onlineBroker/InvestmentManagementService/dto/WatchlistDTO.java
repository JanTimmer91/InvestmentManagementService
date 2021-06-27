package com.onlineBroker.InvestmentManagementService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistDTO {
    private ArrayList<String> watchlist;
}
