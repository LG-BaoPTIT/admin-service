package com.ite.adminservice.controller;

import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.payload.request.RegisterAdminRequest;
import com.ite.adminservice.payload.request.SearchAccountRequest;
import com.ite.adminservice.service.AdminService;
import com.ite.adminservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<?> signupClient(@RequestBody RegisterAdminRequest request){
        try{
            return adminService.signup(request);
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/listAdmins")
    @PreAuthorize("hasAuthority('UPDATE_ANOTHER_ACCOUNT_INFO')")
    public ResponseEntity<?> getListAdmins(){
        try{
            return adminService.getListAdmins();
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/search")
    @PreAuthorize("hasAuthority('UPDATE_ANOTHER_ACCOUNT_INFO')")
    public ResponseEntity<?> search(@RequestBody SearchAccountRequest request){
        try{
            return adminService.searchAdminsByStatusNameOrPhone(request);
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/myAccountDetail")
    public ResponseEntity<?> getMyAccountDetail(){
        try{
            return adminService.getMyAccountDetail();
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adminDetail")
    @PreAuthorize("hasAuthority('UPDATE_ANOTHER_ACCOUNT_INFO')")
    public ResponseEntity<?> getAdminDetail(@RequestParam("id") String id){
        try{
            return adminService.getAdminDetail(id);
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
