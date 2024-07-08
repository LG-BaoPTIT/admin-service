package com.ite.adminservice.event.eventProducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.adminservice.config.SystemLogger;
import com.ite.adminservice.constants.LogStepConstant;
import com.ite.adminservice.payload.dto.RoleGroupDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoleGroupEventPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemLogger logger;

    public void createRoleGroup(RoleGroupDTO roleGroupDTO){
        try{
            logger.log(Thread.currentThread().getName(), "Publish event create role group", LogStepConstant.BEGIN_PROCESS,roleGroupDTO.toString());
            String data = objectMapper.writeValueAsString(roleGroupDTO);
            rabbitTemplate.convertAndSend("roleGroup-exchange","addRoleGroup.routing.key",data);
            logger.log(Thread.currentThread().getName(), "Publish event create role group", LogStepConstant.END_PROCESS,data);

        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "Publish event create role group", LogStepConstant.END_PROCESS,"ERROR: "+ e.getMessage());
            throw new RuntimeException(e) ;
        }

    }

    public void updateRoleGroupEvent(RoleGroupDTO roleGroupDTO){
        try {
            logger.log(Thread.currentThread().getName(), "Publish event update new role group", LogStepConstant.BEGIN_PROCESS,roleGroupDTO.getName());

            String json = objectMapper.writeValueAsString(roleGroupDTO);
            rabbitTemplate.convertAndSend("roleGroup-exchange","updaterole.routing.key",json);

            logger.log(Thread.currentThread().getName(), "Publish event update new role group", LogStepConstant.END_PROCESS,roleGroupDTO.getName());
        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "Publish event update new role group", LogStepConstant.END_PROCESS,"ERROR: " + e.getMessage());
        }
    }public void deleteRoleGroupEvent(String roleGroupId){
        try {
            logger.log(Thread.currentThread().getName(), "Publish event update new role group", LogStepConstant.END_PROCESS,roleGroupId);
//            String json = objectMapper.writeValueAsString(roleGroupId);
            rabbitTemplate.convertAndSend("roleGroup-exchange","deleteRole.routing.key",roleGroupId);


        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "Publish event update new role group", LogStepConstant.END_PROCESS,"ERROR: " + e.getMessage());
        }
    }

}
