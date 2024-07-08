package com.ite.adminservice.service;


import com.ite.adminservice.entities.Permission;
import com.ite.adminservice.payload.dto.PermissionDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PermissionService {

    Permission getPermissionById(String permissionId);

    ResponseEntity<?> getAllPermissions();
}
