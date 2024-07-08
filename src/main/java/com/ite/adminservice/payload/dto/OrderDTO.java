package com.ite.adminservice.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ite.adminservice.payload.data.DeliveryMode;
import com.ite.adminservice.payload.data.PaymentMethod;
import com.ite.adminservice.payload.data.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String id;
    private String receiverName;
    private String receiverPhone;
    private String userId;
    private long totalAmount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Asia/Ho_Chi_Minh")
    private Instant createdAt;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Asia/Ho_Chi_Minh")
    private Instant canceledAt;

    private OrderStatusDTO orderStatus;
    private PaymentStatusDTO paymentStatus;
    private String address;
    private TransactionInfo transactionInfo;
    private DeliveryMode deliveryMode;
    private PaymentMethod paymentMethod;

}
