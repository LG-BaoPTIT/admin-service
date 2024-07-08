package com.ite.adminservice.controller;

import com.ite.adminservice.entities.Status;
import com.ite.adminservice.payload.dto.StatusDTO;
import com.ite.adminservice.repositories.StatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/test")
public class TestController {
    @Autowired
    StatusRepository statusRepository;

    @Autowired
    ModelMapper modelMapper;
//    @GetMapping("/LOGIN")
//    @PreAuthorize("hasAuthority('LOGIN')")
//    public String test1(){
//        return "LOGIN";
//    }
//
//    @GetMapping("/UPDATE_ACCOUNT_STATUS")
//    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT_STATUS')")
//    public String test2(){
//        return "UPDATE_ACCOUNT_STATUS";
//    }
//
//    @GetMapping("/UPDATE_ACCOUNT_INFO")
//    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT_INFO')")
//    public String test3(){
//        return "UPDATE_ACCOUNT_INFO";
//    }
//
    @PostMapping("/add")
    public String addStatus(@RequestBody String str){
//        Status status =modelMapper.map(statusDTO, Status.class);
//        statusRepository.save(status);
        System.out.println(str);
        return "ok";
    }


}
