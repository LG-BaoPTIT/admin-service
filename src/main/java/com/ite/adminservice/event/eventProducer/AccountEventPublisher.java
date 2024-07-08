package com.ite.adminservice.event.eventProducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.adminservice.config.SystemLogger;
import com.ite.adminservice.constants.LogStepConstant;
import com.ite.adminservice.payload.dto.AdminDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemLogger logger;

    public void createAdminEvent(AdminDTO adminDTO){
        try{
            logger.log(Thread.currentThread().getName(), "Publish event create admin account", LogStepConstant.BEGIN_PUBLISH_EVENT,adminDTO.toString());
            String json = objectMapper.writeValueAsString(adminDTO);
            rabbitTemplate.convertAndSend("adminAccount-exchange","createAdminAccount.routing.key",json);
            logger.log(Thread.currentThread().getName(), "Publish event create admin account", LogStepConstant.END_PUBLISH_EVENT,"Publish event successful");
        }catch (Exception e){
            throw new RuntimeException(e) ;
        }
    }

    public void updateAdminAccountEvent(AdminDTO adminDTO){
        try{
            logger.log(Thread.currentThread().getName(), "Publish event update admin account", LogStepConstant.BEGIN_PUBLISH_EVENT,adminDTO.getAdminId());
            String json = objectMapper.writeValueAsString(adminDTO);
            rabbitTemplate.convertAndSend("adminAccount-exchange","updateAdminAccount.routing.key",json);
            logger.log(Thread.currentThread().getName(), "Publish event update admin account", LogStepConstant.END_PUBLISH_EVENT,"Publish event successful");
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
