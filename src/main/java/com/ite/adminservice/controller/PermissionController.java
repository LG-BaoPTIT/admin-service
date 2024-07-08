package com.ite.adminservice.controller;

import com.ite.adminservice.entities.Permission;
import com.ite.adminservice.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSION')")
    public ResponseEntity<?> getAllPermissions() {
        return permissionService.getAllPermissions();
    }
}
