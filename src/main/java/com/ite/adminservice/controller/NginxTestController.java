package com.ite.adminservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/testadmin")
public class NginxTestController {

    @GetMapping("/hihi")
    public ResponseEntity<?> hihi (HttpServletRequest request){
        String userId = request.getHeader("X-User-Id");

        // Xử lý yêu cầu dựa trên giá trị của userId
        // ...

        return ResponseEntity.ok("ok admin:" + userId);

    }
}
