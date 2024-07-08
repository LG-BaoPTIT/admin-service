package com.ite.adminservice.service;

import com.ite.adminservice.payload.request.*;
import org.springframework.http.ResponseEntity;

public interface AccountService {

    ResponseEntity<?> approveAccount(ApproveAccountRequest request);

    ResponseEntity<?> updateStatusAccount(UpdateStatusAccountRequest request);

    ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseEntity<?> forgetPassword(String email);

    ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest);

    ResponseEntity<?> updateOtherAccount(UpdateOtherAccountRequest request);

    ResponseEntity<?> updateAccountInfo(UpdateAccountInfoRequest updateAccountInfoRequest);
}
