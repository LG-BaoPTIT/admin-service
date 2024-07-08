package com.ite.adminservice.controller;

import com.ite.adminservice.constants.MessageConstant;
import com.ite.adminservice.payload.request.*;
import com.ite.adminservice.service.AccountService;
import com.ite.adminservice.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/approveAccount")
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT_STATUS')")
    ResponseEntity<?> approveAccount(@RequestBody ApproveAccountRequest request){
        try{
            return accountService.approveAccount(request);
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT_STATUS')")
    ResponseEntity<?> updateAccountStatus(@RequestBody UpdateStatusAccountRequest request){
        try{
            return accountService.updateStatusAccount(request);
        }catch (Exception e){
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT_INFO')")
    ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        try{
            return accountService.changePassword(changePasswordRequest);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forgetPassword")
    ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        try{
            return accountService.forgetPassword(forgetPasswordRequest.getEmail());
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resetPassword")
    ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest){
        try{

            return accountService.resetPassword(resetPasswordRequest);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateAccountInfo")
    @PreAuthorize("hasAuthority('UPDATE_ACCOUNT_INFO')")
    ResponseEntity<?> updateAccountInfo(@RequestBody UpdateAccountInfoRequest updateAccountInfoRequest){
        try {
            return accountService.updateAccountInfo(updateAccountInfoRequest);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PostMapping("/updateOtherAccountInfo")
    @PreAuthorize("hasAuthority('UPDATE_ANOTHER_ACCOUNT_INFO')")
    ResponseEntity<?> updateOtherAccount(@RequestBody UpdateOtherAccountRequest request){
        try{
            return accountService.updateOtherAccount(request);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.getResponseEntity(MessageConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
