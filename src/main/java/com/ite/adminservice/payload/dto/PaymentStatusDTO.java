package com.ite.adminservice.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusDTO {
    private String id;

    private String code;

    private String description;
}
