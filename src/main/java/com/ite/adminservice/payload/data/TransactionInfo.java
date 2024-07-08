package com.ite.adminservice.payload.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionInfo {
    private String session_id;
    private String transaction_number;
    private String error_code;
    private String status;
    private String request_amount;
    private String settlement_amount;
    private String currency;
    private String trans_time;
    private String order_reference;
    private String order_info;
    private String merchant_id;
    private String issuer_code;
    private String issuer_transaction_reference;
    private String issuer_customer_name;
    private String issuer_customer_mobile;
    private String issuer_customer_account;
//    private String risk_infomation;
//    private String tokenization;
}
