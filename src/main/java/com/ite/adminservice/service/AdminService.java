package com.ite.adminservice.service;

import com.ite.adminservice.payload.request.RegisterAdminRequest;
import com.ite.adminservice.payload.request.SearchAccountRequest;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<?> signup(RegisterAdminRequest request);
    ResponseEntity<?> getListAdmins();

    ResponseEntity<?> getAdminDetail(String id);

    ResponseEntity<?> getMyAccountDetail();

    ResponseEntity<?> searchAdminsByStatusNameOrPhone(SearchAccountRequest searchAccountRequest);

}
