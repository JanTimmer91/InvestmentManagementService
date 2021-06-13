package com.onlineBroker.InvestmentManagementService.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

/*
This class represents a single share of a stock.
A user can be in possession of multiple shares per stock.
 */

@Data
@AllArgsConstructor
public class ShareEntity {
    String orderId_OpenOrder;
    String orderId_CloseOrder;
    Date dateOpened;
    Date dateClosed;
    Double openPrice;
    Double closePrice;
    String status;
    Double profitLossOfShare;
}
