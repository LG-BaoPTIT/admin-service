package com.ite.adminservice.controller;

import com.ite.adminservice.payload.request.RefundRequest;
import com.ite.adminservice.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/refund")
public class RefundController {

    @Autowired
    private RefundService refundService;

    @PostMapping
//    @PreAuthorize("hasAuthority('REFUND_ORDER')")
    public ResponseEntity<?> refundOrder(@RequestBody RefundRequest refundRequest, HttpServletRequest request){
        return refundService.refundOrder(request, refundRequest );
    }
}
