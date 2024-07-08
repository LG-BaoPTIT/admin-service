package com.ite.adminservice.payload.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundBody {
    private String access_code;
    private String merchant_id;
    private String transaction_number;
    private String merchant_refund_transaction_reference;
    private String txn_amount;
    private String trans_time;
    private String refund_type;

    @Override
    public String toString() {
        return "{\"access_code\":\"" + access_code + "\"," +
                "\"merchant_id\":\"" + merchant_id + "\"," +
                "\"transaction_number\":\"" + transaction_number + "\"," +
                "\"merchant_refund_transaction_reference\":\"" + merchant_refund_transaction_reference + "\"," +
                "\"txn_amount\":\"" + txn_amount + "\"," +
                "\"trans_time\":\"" + trans_time + "\"," +
                "\"refund_type\":\"" + refund_type + "\"}";
    }
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("{");
//        sb.append("\"access_code\":\"").append(access_code).append("\",");
//        sb.append("\"merchant_id\":\"").append(merchant_id).append("\",");
//        sb.append("\"transaction_number\":\"").append(transaction_number).append("\",");
//        sb.append("\"trans_time\":\"").append(trans_time).append("\",");
//        sb.append("\"merchant_refund_transaction_reference\":\"").append(merchant_refund_transaction_reference).append("\",");
//        sb.append("\"txn_amount\":\"").append("1000000.00").append("\",");
//        sb.append("\"refund_type\":\"").append(refund_type).append("\"");
//        sb.append("}");
//        return sb.toString();
//    }
}
