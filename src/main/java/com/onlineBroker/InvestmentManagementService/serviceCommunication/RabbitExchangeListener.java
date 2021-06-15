package com.onlineBroker.InvestmentManagementService.serviceCommunication;

import com.onlineBroker.InvestmentManagementService.config.MessagingConfig;
import com.onlineBroker.InvestmentManagementService.dto.OrderDTO;
import com.onlineBroker.InvestmentManagementService.business.InvestmentManagementService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitExchangeListener {

    @Autowired
    InvestmentManagementService investmentManagementService;

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeNewOrderFromQueue(OrderDTO orderDTO) {
        System.out.println("Order " +orderDTO.getOrderId() +" received from exchange "
                + MessagingConfig.EXCHANGE +" and queue " +MessagingConfig.QUEUE +"...");
        investmentManagementService.handleIncomingOrder(orderDTO);
    }
}
