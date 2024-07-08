package com.ite.adminservice.controller;

import com.ite.adminservice.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/bill")
public class BillController {
    @Autowired
    private BillService billService;

    @GetMapping("/billDetail")
    @PreAuthorize("hasAuthority('VIEW_PAYMENT_ORDER_DETAIL')")
    public ResponseEntity<?> getBillById(@RequestParam(name = "orderId") String orderId
    ) throws Exception {
        return billService.getBillById(orderId);
    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('VIEW_PAYMENT_ORDER_DETAIL')")
    public ResponseEntity<?> getBill() throws Exception {
        return billService.getBill();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SEARCH_PAYMENT_ORDER')")
    public ResponseEntity<?> searchBillss(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size) throws Exception {
        return  billService.searchBillss(orderId, email, page, size);
    }@GetMapping("/billRefund/billDetail")
    @PreAuthorize("hasAuthority('REFUND_ORDER')")
    public ResponseEntity<?> getBillRefundById(@RequestParam(name = "orderId") String orderId
    ) throws Exception {
        return billService.getBillRefundById(orderId);
    }
    @GetMapping("/billRefund")
    @PreAuthorize("hasAuthority('REFUND_ORDER')")
    public ResponseEntity<?> getBillRefund() throws Exception {
        return billService.getBillRefund();
    }

    @GetMapping("/billRefund/search")
    @PreAuthorize("hasAuthority('SEARCH_REFUND_ORDER')")
    public ResponseEntity<?> searchBillRefund(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int size) throws Exception {
        return  billService.searchBillRefund(orderId,email,page,size);
    }

}
