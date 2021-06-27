package com.onlineBroker.InvestmentManagementService.serviceCommunication;

import com.onlineBroker.InvestmentManagementService.config.MessagingConfig;
import com.onlineBroker.InvestmentManagementService.dto.OrderDTO;
import com.onlineBroker.InvestmentManagementService.domain.InvestmentTransformationServiceImpl;
import com.onlineBroker.InvestmentManagementService.persistence.service.UserServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderSubscriber {

    @Autowired
    InvestmentTransformationServiceImpl investmentTransformationServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @RabbitListener(queues = MessagingConfig.INVESTMENTSERVICE_ORDER_QUEUE)
    public void consumeNewOrderFromQueue(OrderDTO orderDTO) {
        System.out.println("Order " +orderDTO.getOrderId() +" received from exchange "
                + MessagingConfig.ORDERSERVICE_ORDER_EXCHANGE +" and queue " +MessagingConfig.INVESTMENTSERVICE_ORDER_QUEUE +"...");
        investmentTransformationServiceImpl.handleIncomingOrder(orderDTO);
    }
}
