package com.onlineBroker.InvestmentManagementService.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

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
