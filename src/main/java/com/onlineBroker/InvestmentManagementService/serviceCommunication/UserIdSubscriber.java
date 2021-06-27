package com.onlineBroker.InvestmentManagementService.serviceCommunication;

import com.onlineBroker.InvestmentManagementService.config.MessagingConfig;
import com.onlineBroker.InvestmentManagementService.domain.InvestmentTransformationServiceImpl;
import com.onlineBroker.InvestmentManagementService.persistence.service.UserServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdSubscriber {

    @Autowired
    InvestmentTransformationServiceImpl investmentTransformationServiceImpl;

    @Autowired
    UserServiceImpl userServiceImpl;

    @RabbitListener(queues = MessagingConfig.INVESTMENTSERVICE_USER_QUEUE)
    public void consumeNewUserIdFromQueue(String userId) {
        System.out.println("User " +userId +" received from exchange "
                + MessagingConfig.USERSERVICE_USER_EXCHANGE +" and queue " +MessagingConfig.INVESTMENTSERVICE_USER_QUEUE +"...");
        userServiceImpl.createNewUserEntity(userId);
    }
}
