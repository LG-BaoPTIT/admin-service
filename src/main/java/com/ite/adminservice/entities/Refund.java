package com.ite.adminservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "refunds")
public class Refund {
    @Id
    private String id;

    private String orderId;

    private String refundAmount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="Asia/Ho_Chi_Minh")
    private Instant createdDate;

    private String adminId;

    private RefundType refundType;

    private String reason;

    private RefundStatus refundStatus;

}
