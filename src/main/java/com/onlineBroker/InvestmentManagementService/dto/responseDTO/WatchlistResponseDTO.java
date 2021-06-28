package com.onlineBroker.InvestmentManagementService.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistResponseDTO {
    private ArrayList<String> watchlist;
}
