package com.ite.adminservice.payload.dto;

import com.ite.adminservice.entities.EPermission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    private String permissionId;
    private EPermission name;
    private String description;
}
