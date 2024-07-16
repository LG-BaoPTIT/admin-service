package com.ite.adminservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.support.GenericWebApplicationContext;


@Configuration
@EnableRabbit
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class RabbitMQConfig {
    @Autowired
    private GenericWebApplicationContext context;


    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitVirtualHost;
//    @Value("${spring.rabbitmq.virtual-host-job}")
//    private String rabbitVirtualHostJob;

    private CachingConnectionFactory getCachingConnectionFactoryCommon(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.rabbitHost,this.rabbitPort);
        connectionFactory.setUsername(this.rabbitUsername);
        connectionFactory.setPassword(this.rabbitPassword);
        return connectionFactory;
    }

    @Primary
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        return new RabbitTemplate(connectionFactory);
    }

    @Primary
    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = this.getCachingConnectionFactoryCommon();
        connectionFactory.setVirtualHost(this.rabbitVirtualHost);
        return connectionFactory;
    }

    @Primary
    @Bean("rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(ConnectionFactory connectionFactory){
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setDefaultRequeueRejected(false);
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}