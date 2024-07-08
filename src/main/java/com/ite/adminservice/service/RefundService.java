package com.ite.adminservice.service;

import com.ite.adminservice.payload.request.RefundRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface RefundService {
    ResponseEntity<?> refundOrder(HttpServletRequest request, RefundRequest refundRequest);
}
