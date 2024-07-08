package com.ite.adminservice.service;

import org.springframework.http.ResponseEntity;

public interface BillService {
    ResponseEntity<?> getBillById(String orderId) throws Exception;

    ResponseEntity<?> getBill() throws Exception;

    ResponseEntity<?> searchBillss(String orderId, String email, int page, int size)throws Exception;

    ResponseEntity<?> getBillRefundById(String orderId)throws Exception;

    ResponseEntity<?> getBillRefund()throws Exception;

    ResponseEntity<?> searchBillRefund(String orderId, String email, int page, int size)throws Exception;
}
