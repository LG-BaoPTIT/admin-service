package com.ite.adminservice.event.eventProducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.adminservice.config.SystemLogger;
import com.ite.adminservice.constants.LogStepConstant;
import com.ite.adminservice.event.messages.ApproveAccountNotification;
import com.ite.adminservice.event.messages.ResetPasswordMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEventPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemLogger logger;

    public void publishAccountApprovalEventWithQRCode(ApproveAccountNotification approveAccountNotification) {
        try {
            logger.log(Thread.currentThread().getName(), "Publish event send notification approve admin account", LogStepConstant.BEGIN_PROCESS,approveAccountNotification.getEmail());
            String json = objectMapper.writeValueAsString(approveAccountNotification);
            rabbitTemplate.convertAndSend("email-exchange", "approveAccountNotice.routing.key", json);
            logger.log(Thread.currentThread().getName(), "Publish event send notification approve admin account", LogStepConstant.END_PROCESS,"Publishing event successful: " + approveAccountNotification.getEmail());

        } catch (Exception e) {
            logger.log(Thread.currentThread().getName(), "Publish event send notification approve admin account", LogStepConstant.END_PROCESS,"Publishing event failed: " + e.getMessage());
        }
    }

    public void sendMailResetPasswordEvent(ResetPasswordMessage message){
        try {
            logger.log(Thread.currentThread().getName(), "Publish event send email reset password", LogStepConstant.BEGIN_PROCESS,message.getEmail());

            String json = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend("email-exchange","resetPassword.routing.key",json);

            logger.log(Thread.currentThread().getName(), "Publish event send email reset password", LogStepConstant.END_PROCESS,message.getEmail());
        }catch (Exception e){
            logger.log(Thread.currentThread().getName(), "Publish event send email reset password", LogStepConstant.END_PROCESS,"ERROR: " + e.getMessage());
        }
    }
}
